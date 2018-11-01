package de.ba.bub.studisu.common.integration.soa;

import java.security.cert.CertificateEncodingException;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.utils.Base64;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.AuthenticationStatement;
import org.opensaml.saml1.core.Conditions;
import org.opensaml.saml1.core.ConfirmationMethod;
import org.opensaml.saml1.core.NameIdentifier;
import org.opensaml.saml1.core.Subject;
import org.opensaml.saml1.core.SubjectConfirmation;
import org.opensaml.saml1.core.impl.AssertionMarshaller;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.signature.X509Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.w3c.dom.Element;

//import de.arbeitsagentur.apok.kontakt.web.CustomHttpHeader;

/**
 * SamlHandler
 * Erstellt einen Security-Header mit SAML 1.1 Assertation mit Dummy-AuthStatement.
 * Wird die Klasse mit einem  X509Credential initialisiert, so wird die Assertation damit signiert.
 *
 * Bei Weiterleitung durch den  OAG wird nur geprueft ob eine Assertation vorhanden und mit einem bekannten Schluessel signiert ist.
 * Der OAG ersetzt dann diese Dummy-Assertation durch eine korrekte Assertation.
 *
 * D.h. ein direkter Aufruf (ohne OAG) mit dieser Assertaion funktioniert nicht !
 * Nur in Umgebungen (wie PINT) wo keine Signature erforderlich ist geht der Aufruf mit Assertation und ohne Signatur (saml.signature=false) durch.
 *
 */
public class SamlHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SamlHandler.class);

    private final XMLObjectBuilderFactory builderFactory;
    private final X509Credential x509Credential;

    public SamlHandler(X509Credential x509Credential) {
        this.x509Credential = x509Credential;
        try {
            DefaultBootstrap.bootstrap();
            builderFactory = Configuration.getBuilderFactory();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {
        // f..ck sonar
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

    	if (MDC.get("Authorization") != null) {
    		if (LOGGER.isDebugEnabled()) {
    			LOGGER.debug("SamlHandler.handleMessage returning early missing Authorization");
    		}
    		return true;
        }

        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (!outboundProperty) {
    		if (LOGGER.isDebugEnabled()) {
    			LOGGER.debug("SamlHandler.handleMessage returning early context is not outbound");
    		}
            return true;
        }

        try {
            addSamlHeader(context);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return true;
    }

    private void addSamlHeader(SOAPMessageContext context) throws SOAPException, CertificateEncodingException, MarshallingException, SignatureException {

        SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();

        SOAPElement securityHeader = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("After creating security Header: " + header.toString());
		}
        
        Assertion assertion = createSAMLAssertion();

        Signature signature = createSignature(assertion);
        AssertionMarshaller marshaller = new AssertionMarshaller();
        Element plaintextElement = marshaller.marshall(assertion, envelope);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("PlaintextElement: "+ plaintextElement.toString());
		}
        
        if (signature != null) {
            Signer.signObjects(Collections.singletonList(signature));
        }

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SamlHandler.addSamlHeader(...) plaintextElement="+plaintextElement);
		}
        
        securityHeader.appendChild(plaintextElement);
		
        if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Current SoapHeader: " + envelope.getHeader().toString());
		}
    }

    public Assertion createSAMLAssertion() {

        DateTime instant = new DateTime();
        DateTime notBefore = instant.minusHours(1);
        DateTime notAfter = instant.plusHours(1);
        String assertionId = UUID.randomUUID().toString();

        Assertion assertion = createXmlObject(Assertion.DEFAULT_ELEMENT_NAME);
        Conditions conditions = createXmlObject(Conditions.DEFAULT_ELEMENT_NAME);
        conditions.setNotBefore(notBefore);
        conditions.setNotOnOrAfter(notAfter);

        assertion.setIssuer("http://services.dst.baintern.de/wespe/standard");
        assertion.setIssueInstant(instant);
        assertion.setConditions(conditions);
        assertion.setID(assertionId);

        AuthenticationStatement authenticationStatement = createXmlObject(AuthenticationStatement.DEFAULT_ELEMENT_NAME);
        assertion.getAuthenticationStatements().add(authenticationStatement);

        Subject subject = createXmlObject(Subject.DEFAULT_ELEMENT_NAME);
        authenticationStatement.setSubject(subject);
        authenticationStatement.setAuthenticationInstant(notAfter);
        authenticationStatement.setAuthenticationMethod("urn:oasis:names:tc:SAML:1.0:am:password");

        NameIdentifier nameIdentifier = createXmlObject(NameIdentifier.DEFAULT_ELEMENT_NAME);
        subject.setNameIdentifier(nameIdentifier);
        nameIdentifier.setNameIdentifier("owsm_anonymous");
        nameIdentifier.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");

        SubjectConfirmation subjectConfirmation = createXmlObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
        subject.setSubjectConfirmation(subjectConfirmation);

        ConfirmationMethod confirmationMethod = createXmlObject(ConfirmationMethod.DEFAULT_ELEMENT_NAME);
        confirmationMethod.setConfirmationMethod("urn:oasis:names:tc:SAML:1.0:cm:sender-vouches");
        subjectConfirmation.setSubjectConfirmationData(confirmationMethod);
        
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("After createSAMLAssertion() call: " + assertion.toString());
		}
        return assertion;
    }


    public Signature createSignature(Assertion assertion) throws CertificateEncodingException  {

        if (x509Credential == null) {
    		if (LOGGER.isWarnEnabled()) {
    			LOGGER.warn("***** x509Credential isNull *****");
    		}
            return null;
        }

        Signature signature = createXmlObject(Signature.DEFAULT_ELEMENT_NAME);
        signature.setSigningCredential(x509Credential);
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        signature.setCanonicalizationAlgorithm(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

        KeyInfo keyInfo = createXmlObject(KeyInfo.DEFAULT_ELEMENT_NAME);
        X509Data data = createXmlObject(X509Data.DEFAULT_ELEMENT_NAME);
        X509Certificate cert = createXmlObject(X509Certificate.DEFAULT_ELEMENT_NAME);
        String value = Base64.encode(x509Credential.getEntityCertificate().getEncoded());
        cert.setValue(value);
        data.getX509Certificates().add(cert);
        keyInfo.getX509Datas().add(data);
        signature.setKeyInfo(keyInfo);

        assertion.setSignature(signature);
        return signature;
    }

    @SuppressWarnings("unchecked")
    private <T> T createXmlObject(QName elementName) {
        return (T) builderFactory.getBuilder(elementName).buildObject(elementName.getNamespaceURI(), elementName.getLocalPart(), elementName.getPrefix());
    }

    X509Credential getX509Credential() {
        return x509Credential;
    }
}