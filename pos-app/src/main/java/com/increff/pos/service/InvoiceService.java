package com.increff.pos.service;

import com.increff.pos.dao.InvoiceDao;
import com.increff.pos.pojo.InvoicePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;
    
    public void add(InvoicePojo invoicePojo) throws ApiException {
        InvoicePojo existingPojo = invoiceDao.select_id(invoicePojo.getOrderId());
        if(existingPojo == null) {
            invoiceDao.insert(invoicePojo);
        }
    }
    
    public InvoicePojo get(int id) throws ApiException{
        return getCheck(id);
    }
    
    public List<InvoicePojo> getAll(){
        return invoiceDao.selectAll();
    }
    
    public InvoicePojo getCheck(Integer id) throws ApiException{
        InvoicePojo b = invoiceDao.select_id(id);
        if(b == null){
            throw new ApiException("Invoice with given ID does not exist");
        }
        return b;
    }
}
