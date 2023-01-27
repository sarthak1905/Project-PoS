package com.increff.invoice.dto;

import com.increff.invoice.models.InvoiceForm;
import com.increff.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

@Component
public class InvoiceDto {

    @Autowired
    private InvoiceService invoiceService;

    public String getInvoice(InvoiceForm invoiceForm) throws Exception {
        String basePath = new File("").getAbsolutePath();
        String fileName = basePath + "invoice.pdf";

        invoiceService.generatePDFFromJavaObject(invoiceForm, fileName);
        File file = new File(fileName);
        byte[] contents = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(contents);
    }
}