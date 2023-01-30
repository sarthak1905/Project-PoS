package com.increff.invoice.controller;

import com.increff.invoice.dto.InvoiceDto;
import com.increff.invoice.models.InvoiceForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping(path = "/api")
public class InvoiceController {

    @Autowired
    private InvoiceDto invoiceDto;

    @ApiOperation(value = "Returns the base64 encoded string")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getInvoice(@RequestBody InvoiceForm invoiceForm) throws Exception {
        return invoiceDto.getInvoice(invoiceForm);
    }
}
