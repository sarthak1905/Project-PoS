package com.increff.invoice.service;

import com.increff.invoice.models.InvoiceForm;
import com.increff.invoice.models.OrderItemData;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;

@Component
@Service
public class InvoiceService {

    @Value("${path.xml}")
    private String xmlFilePath;
    @Value("${path.xsl}")
    private String xslFilePath;

    public void generateInvoice(InvoiceForm invoiceForm) {
        createXML(invoiceForm);
        createPDF();
    }

    private void createXML(InvoiceForm invoiceForm) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("invoice");
            document.appendChild(root);

            Element order_id = document.createElement("order_id");
            order_id.appendChild(document.createTextNode(invoiceForm.getOrderId().toString()));
            root.appendChild(order_id);

            Element order_date = document.createElement("order_date");
            order_date.appendChild(document.createTextNode(invoiceForm.getOrderDate()));
            root.appendChild(order_date);

            Element invoice_date = document.createElement("invoice_date");
            invoice_date.appendChild(document.createTextNode(invoiceForm.getInvoiceDate()));
            root.appendChild(invoice_date);

            // order item element
            for (OrderItemData o : invoiceForm.getOrderItemData()){
                DecimalFormat df = new DecimalFormat("#.##");
                Element order_item = document.createElement("order_item");

                root.appendChild(order_item);

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(o.getId().toString()));
                order_item.appendChild(id);

                Element ProductId = document.createElement("product_name");
                ProductId.appendChild(document.createTextNode(o.getProductName()));
                order_item.appendChild(ProductId);

                Element quantity = document.createElement("quantity");
                quantity.appendChild(document.createTextNode(o.getQuantity().toString()));
                order_item.appendChild(quantity);

                Element sellingPrice = document.createElement("selling_price");
                sellingPrice.appendChild(document.createTextNode(Double.valueOf(df.format(o.getSellingPrice())).toString()));
                order_item.appendChild(sellingPrice);

                Element amt = document.createElement("amt");
                amt.appendChild(document.createTextNode(Double.valueOf(df.format(o.getSellingPrice() * o.getQuantity())).toString()));
                order_item.appendChild(amt);
            }

            Element amount = document.createElement("amount");
            amount.appendChild(document.createTextNode(invoiceForm.getOrderTotal().toString()));
            root.appendChild(amount);
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            System.out.println(xmlFilePath);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            transformer.transform(domSource, streamResult);


        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public void createPDF() {
        try {
            File xmlfile = new File(xmlFilePath);
            File xsltfile = new File(xslFilePath);
            File pdfDir = new File("./Test");
            pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, "invoice.pdf");
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
            // Setup output
            OutputStream out = Files.newOutputStream(pdfFile.toPath());
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

}
