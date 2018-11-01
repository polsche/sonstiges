package de.ba.bub.studisu.common.integration.soa;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialContextSet;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.x509.X509Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class X509CredentialFactory {

    private static final Logger LOG = LoggerFactory.getLogger(X509CredentialFactory.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    static final String SAML_CERTIFICATE = "saml.certificate";

    static final String SAML_KEY = "saml.key";

    public X509Credential createX509Credential() {
        try {
            List<X509Certificate> certificates = readCertificateChain();
            PrivateKey privateKey = readPrivateKey();
            LOG.info("Certificate: {}, Key: {}/{}", certificates.get(0).getSubjectDN(), privateKey.getAlgorithm(), privateKey.getFormat());
            return new X509CredentialImpl(certificates, privateKey);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<X509Certificate> readCertificateChain() throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = factory.generateCertificates(readEnv(SAML_CERTIFICATE));

        List<X509Certificate> x509Certificates = new ArrayList<>(certificates.size());
        for (Certificate certificate : certificates) {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity();
            x509Certificates.add(x509Certificate);
        }
        return x509Certificates;
    }

    private PrivateKey readPrivateKey() throws IOException, GeneralSecurityException {
        PemReader pemReader = new PemReader(new InputStreamReader(readEnv(SAML_KEY)));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();

        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
        return KeyFactory.getInstance("RSA", "BC").generatePrivate(privKeySpec);
    }

    private InputStream readEnv(String key) {
        String value = System.getenv(key.toUpperCase().replace(".", "_"));
        if (value == null || value.isEmpty()) {
            value = System.getProperty(key);
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Property not set : " + key);
        }
        return new ByteArrayInputStream(value.getBytes());
    }

    private static class X509CredentialImpl implements X509Credential {

        private final List<X509Certificate> certificates;

        private final PrivateKey privateKey;

        X509CredentialImpl(List<X509Certificate> certificates, PrivateKey privateKey) {
            this.certificates = Collections.unmodifiableList(certificates);
            this.privateKey = privateKey;
        }

        @Override
        public X509Certificate getEntityCertificate() {
            return certificates.get(0);
        }

        @Override
        public Collection<X509Certificate> getEntityCertificateChain() {
            return new ArrayList<>(certificates);
        }

        @Override
        public Collection<X509CRL> getCRLs() {
            return Collections.emptyList();
        }

        @Override
        public String getEntityId() {
            return null;
        }

        @Override
        public UsageType getUsageType() {
            return null;
        }

        @Override
        public Collection<String> getKeyNames() {
            return Collections.emptyList();
        }

        @Override
        public PublicKey getPublicKey() {
            return certificates.get(0).getPublicKey();
        }

        @Override
        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        @Override
        public SecretKey getSecretKey() {
            return null;
        }

        @Override
        public CredentialContextSet getCredentalContextSet() {
            return null;
        }

        @Override
        public Class<? extends Credential> getCredentialType() {
            return null;
        }
    }
}