package de.ba.bub.studisu.common.integration.soa;

import org.junit.After;
import org.junit.Before;

public abstract class CertificateData {

/*
    openssl genrsa -out private.key 1024
    openssl req -new -x509 -key private.key -out publickey.cer -days 365
    openssl pkcs12 -export -out public_privatekey.pfx -inkey private.key -in publickey.cer
 */

    static final String CERTIFICATE =
            "-----BEGIN CERTIFICATE-----\n" +
                    "MIICTDCCAbWgAwIBAgIJAJRRxakwxyHKMA0GCSqGSIb3DQEBCwUAMD8xCzAJBgNV\n" +
                    "BAYTAkRFMQ0wCwYDVQQIDARBUE9LMRIwEAYDVQQHDAlOdXJlbWJlcmcxDTALBgNV\n" +
                    "BAoMBFBEQjEwHhcNMTgwMTE1MTU0OTU2WhcNMjgwMTEzMTU0OTU2WjA/MQswCQYD\n" +
                    "VQQGEwJERTENMAsGA1UECAwEQVBPSzESMBAGA1UEBwwJTnVyZW1iZXJnMQ0wCwYD\n" +
                    "VQQKDARQREIxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXKXNZzUFWHfYa\n" +
                    "VQUv61kuUV57O42Jesxr7fwURkhb0lC1sAJyYdHevrbcCfmWeA/tTzFtHWKwWrkJ\n" +
                    "TYNqretk2LrtdgRI+EPgncBNq16zPyESwJTzcNylu4w8kz9OnURw0r6VPOUglZ0h\n" +
                    "HbHf16ctSOHrrW3AB/skxmMGHoDXxQIDAQABo1AwTjAdBgNVHQ4EFgQU4Bico5oF\n" +
                    "UgC6N/m4oQikrbn8LDEwHwYDVR0jBBgwFoAU4Bico5oFUgC6N/m4oQikrbn8LDEw\n" +
                    "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOBgQBFaolOqwdvmjAnIhlBO2oe\n" +
                    "VWVMkDttStNDagM3zl+Olw4t3JGhIRQt0K51nmTtH2EUcKigH3LNjNAnPXRJd+pX\n" +
                    "JyUGsnLf0IZ2L9kwje8+TQJUrGWCvjBkmHqHuYnikl9jbF100v2yfAA5cuS/qKZG\n" +
                    "/6kssUuibt8Zg+l7gZoGOw==\n" +
                    "-----END CERTIFICATE-----";

    static final String KEY =
            "-----BEGIN RSA PRIVATE KEY-----\n" +
                    "MIICXAIBAAKBgQDXKXNZzUFWHfYaVQUv61kuUV57O42Jesxr7fwURkhb0lC1sAJy\n" +
                    "YdHevrbcCfmWeA/tTzFtHWKwWrkJTYNqretk2LrtdgRI+EPgncBNq16zPyESwJTz\n" +
                    "cNylu4w8kz9OnURw0r6VPOUglZ0hHbHf16ctSOHrrW3AB/skxmMGHoDXxQIDAQAB\n" +
                    "AoGAUIvt6NJUAT8aIGVvma7LJfHEIvH6XFsiJ81hrt1YvTcQi9OmNtezz2Up41nA\n" +
                    "m7m6jKY25Jg5l7d+yaLA7bDKC7iwhCz6iUtDuuQlyAz7ZviMZwU/+VuPmLE1y4ti\n" +
                    "piPfZQ35WWjHUkbkR1YqTZsAZKiO0FBt3uHstbievsqoMUkCQQD4KCCJ2kgqpAw9\n" +
                    "Ac1oZ5MM5uRH6QQPIGAu8erZE1e16j9UTSew8VfPbHbMIsWa6S73LlPKd4ASN0OG\n" +
                    "oJFhsrcHAkEA3fZbgTlRcW9uyfVnwN+rZJc4Uz49xUVGJWExY4rhsgQZWuYiQACb\n" +
                    "kdhIYDEhvIs+Wy57UybXDb91l8e4nBfb0wJAC810xrmwBv8oadplF5sdflaY8uad\n" +
                    "P87tZC4zWkG2QTuz6WGGCr9fysjA2bKprVV721vDtnR6jeM8/fEzGO90DwJAR8DW\n" +
                    "P+YJoAQH6p28lqs3uuLvwGlEirskO/05fBZvc57Dm5D9zXbk07b5Xq9DWrIgzFUw\n" +
                    "ZOcPalBUcWaNFVjZJQJBAJz8MfeDvTKwhEXZM8HJ8Nq49i47wc/IUMg62Xkp8zqK\n" +
                    "R/1PwS3Rn0sIjO/yHYTA8vuU6AOg+yA9IinS7zqVRe4=\n" +
                    "-----END RSA PRIVATE KEY-----";

    @Before
    public void setup() {
        System.getProperties().setProperty(X509CredentialFactory.SAML_CERTIFICATE, CERTIFICATE);
        System.getProperties().setProperty(X509CredentialFactory.SAML_KEY, KEY);
    }

    @After
    public void reset() {
        for (String key : new String[] {
                X509CredentialFactory.SAML_CERTIFICATE, X509CredentialFactory.SAML_KEY,
                "com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump",
                "com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump",
                "com.sun.xml.ws.transport.http.HttpAdapter.dump",
                "com.sun.xml.internal.ws.transport.http.HttpAdapter.dump",
                "com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold"
        }) {
            System.getProperties().remove(key);
        }
    }

}
