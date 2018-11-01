package de.ba.bub.studisu.common.integration.soa;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * copied from TestSamlHandler
 * @author APOK OPDT
 * vereinfacht durch Christian Kunzmann
 */public class SamlHandlerTest extends CertificateData {

    @Test
    public void test() throws Exception {

        String request = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><S:Body><ns3:famkaStandorteErmitteln xmlns=\"http://services.dst.baintern.de/types/common/serviceinfo_v_1\" xmlns:ns2=\"http://services.dst.baintern.de/types/common_v_1\" xmlns:ns3=\"http://services.dst.baintern.de/wsdl/basis/systematiken/RegionenServiceMessages_V_2\" xmlns:ns4=\"http://services/dst.baintern.de/types/basis/systematiken/Servicedatentypen_v_1\"><ns3:plz>90425</ns3:plz><ns3:ort>Nuernberg</ns3:ort></ns3:famkaStandorteErmitteln></S:Body></S:Envelope>";
        SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(request.getBytes()));

        SOAPMessageContext context = mock(SOAPMessageContext.class);
        when(context.get(eq(MessageContext.MESSAGE_OUTBOUND_PROPERTY))).thenReturn(Boolean.TRUE);
        when(context.getMessage()).thenReturn(message);

        SamlHandler samlHandler = new SamlHandler(new X509CredentialFactory().createX509Credential());
        samlHandler.handleMessage(context);

        String content = dumpSOAPMessage(message);
        //System.out.println(content);

        assertTrue(content.contains("wsse:Security"));
        assertTrue(content.contains("ds:Signature"));

//        FileWriter writer = new FileWriter("saml-request.xml");
//        writer.write(content);
//        writer.close();
    }

    private String dumpSOAPMessage(SOAPMessage soapMessage) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StringWriter writer = new StringWriter();
        transformer.transform(soapMessage.getSOAPPart().getContent(), new StreamResult(writer));
        return writer.toString();
    }
}
