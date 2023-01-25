package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        productDao.insert(p);
    }


    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductPojo> getAll(){
        return productDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {
        ProductUtil.normalize(p);
        ProductPojo bx = getCheck(id);
        bx.setName(p.getName());
        bx.setMrp(p.getMrp());
    }

    @Transactional
    public void delete(int id) throws ApiException{
        ProductPojo p = getCheck(id);
        productDao.delete(id);
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException{
        ProductPojo p = productDao.selectId(id);
        if(p == null){
            throw new ApiException("Product with given ID does not exist");
        }
        return p;
    }

    @Transactional
    public String getBarcodeFromProductId(int id){
        ProductPojo p = productDao.selectId(id);
        return p.getBarcode();
    }

    @Transactional
    public int getProductIdFromBarcode(String barcode) throws ApiException {
        ProductPojo px = productDao.selectBarcode(barcode);
        if (px == null){
            throw new ApiException("Product with given barcode" + barcode + " does not exist!");
        }
        return px.getId();
    }

    @Transactional
    public boolean isValidBarcode(String barcode){
        ProductPojo px = productDao.selectBarcode(barcode);
        if(px != null){
            return false;
        }
        return true;
    }


}
