package de.ba.bub.studisu.common.integration.soa;

import org.junit.Test;
import org.opensaml.xml.security.x509.X509Credential;

import static org.junit.Assert.assertNotNull;


/**
 * copied from TestSamlEnv
 * @author APOK OPDT
 * vereinfacht durch Christian Kunzmann
 */
public class X509CredentialFactoryTest {

    @Test
    public void testSamlEnv() throws Exception {
    	
    	System.getProperties().setProperty(X509CredentialFactory.SAML_CERTIFICATE, CertificateData.CERTIFICATE);
        System.getProperties().setProperty(X509CredentialFactory.SAML_KEY, CertificateData.KEY);
        
        
        
        X509Credential credential = new X509CredentialFactory().createX509Credential();
        assertNotNull(credential);
        
        System.getProperties().remove(X509CredentialFactory.SAML_CERTIFICATE);
        System.getProperties().remove(X509CredentialFactory.SAML_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSamlEnv_null() {
    	new X509CredentialFactory().createX509Credential();
    }
}
