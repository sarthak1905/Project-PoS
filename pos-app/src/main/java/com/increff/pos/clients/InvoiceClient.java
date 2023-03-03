package com.increff.pos.clients;

import com.increff.pos.model.InvoiceForm;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class InvoiceClient {

    @Value("${invoice.uri}")
    private String invoiceUrl;

    public byte[] generateInvoice(InvoiceForm invoiceForm){
        RestTemplate restTemplate = new RestTemplate();
        return Base64.getDecoder().decode(restTemplate.postForEntity(invoiceUrl, invoiceForm, byte[].class).getBody());
    }
}
