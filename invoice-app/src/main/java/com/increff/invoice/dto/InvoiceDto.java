package com.increff.invoice.dto;

import com.increff.invoice.models.InvoiceForm;
import com.increff.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class InvoiceDto {

    @Autowired
    private InvoiceService invoiceService;

    public ResponseEntity<byte[]> getInvoice(InvoiceForm invoiceForm) throws Exception {
        invoiceService.generateInvoice(invoiceForm);
        String _filename = "./Test/invoice_"+invoiceForm.getOrderId() +".pdf";
        Path pdfPath = Paths.get("./Test/invoice.pdf");

        byte[] contents = Base64.getEncoder().encode(Files.readAllBytes(pdfPath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }
}