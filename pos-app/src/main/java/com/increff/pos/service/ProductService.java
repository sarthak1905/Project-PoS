package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public void add(ProductPojo productPojo) throws ApiException {
        ValidationUtil.checkPojo(productPojo);
        if (isValidBarcode(productPojo.getBarcode())) {
            productDao.insert(productPojo);
            return;
        }
        throw new ApiException("Barcode already exists!");
    }

    public ProductPojo get(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        return getCheck(id);
    }

    public List<ProductPojo> getAll(){
        return productDao.selectAll();
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException {
        ValidationUtil.checkPojo(productPojo);
        ProductPojo existingProductPojo = getCheck(id);
        if(existingProductPojo.getBarcode().equals(productPojo.getBarcode())){
            existingProductPojo.setBrandCategory(productPojo.getBrandCategory());
            existingProductPojo.setName(productPojo.getName());
            existingProductPojo.setMrp(productPojo.getMrp());
            return;
        }
        throw new ApiException("Barcode must be same while updating!");
    }

    public String getBarcodeFromProductId(Integer id) throws ApiException {
        ProductPojo productPojo = getCheck(id);
        return productPojo.getBarcode();
    }

    public Integer getProductIdFromBarcode(String barcode) throws ApiException {
        ProductPojo existingProductPojo = productDao.selectBarcode(barcode);
        if (existingProductPojo == null){
            throw new ApiException("Product with given barcode" + barcode + " does not exist!");
        }
        return existingProductPojo.getId();
    }

    public Boolean isValidBarcode(String barcode){
        ProductPojo existingProductPojo = productDao.selectBarcode(barcode);
        return existingProductPojo == null;
    }

    public String getProductNameFromProductId(Integer productId) throws ApiException {
        ProductPojo productPojo = getCheck(productId);
        return productPojo.getName();
    }

    private ProductPojo getCheck(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        ProductPojo productPojo = productDao.selectId(id);
        if(productPojo == null){
            throw new ApiException("Product with given ID does not exist!");
        }
        return productPojo;
    }

}
