package com.increff.invoice.service;

import com.increff.invoice.models.InvoiceForm;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Service
public class InvoiceService {

    public void generatePDFFromJavaObject(InvoiceForm invoiceForm, String fileName) throws Exception {
        ByteArrayOutputStream xmlSource = getXMLSource(invoiceForm);
        StreamSource streamSource = new StreamSource(new ByteArrayInputStream(xmlSource.toByteArray()));
        generatePDF(streamSource, fileName);
    }

    private ByteArrayOutputStream getXMLSource(InvoiceForm invoiceForm) {
        JAXBContext context;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            context = JAXBContext.newInstance(InvoiceForm.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(invoiceForm, outStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return outStream;
    }

    private void generatePDF(StreamSource streamSource, String fileName)
            throws FOPException, TransformerException, IOException {
        File xsltFile = new File("../templates/template.xsl");

        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance();

        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output

        try (OutputStream out = Files.newOutputStream(Paths.get(fileName))) {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();

            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(streamSource, res);
        }
    }
}
