package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductDao productDao;

    public void add(ProductPojo productPojo) throws ApiException {
        productDao.insert(productPojo);
    }

    public ProductPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    public List<ProductPojo> getAll(){
        return productDao.selectAll();
    }

    public void update(int id, ProductPojo productPojo) throws ApiException {
        ProductPojo existingProductPojo = getCheck(id);
        if(!existingProductPojo.getBarcode().equals(productPojo.getBarcode())){
            throw new ApiException("Barcode must be same while updating!");
        }
        existingProductPojo.setBrandCategory(productPojo.getBrandCategory());
        existingProductPojo.setName(productPojo.getName());
        existingProductPojo.setMrp(productPojo.getMrp());
    }

    public void delete(int id) throws ApiException{
        ProductPojo p = getCheck(id);
        productDao.delete(id);
    }

    public ProductPojo getCheck(int id) throws ApiException{
        ProductPojo p = productDao.selectId(id);
        if(p == null){
            throw new ApiException("Product with given ID does not exist");
        }
        return p;
    }

    public String getBarcodeFromProductId(int id){
        ProductPojo productPojo = productDao.selectId(id);
        return productPojo.getBarcode();
    }

    public int getProductIdFromBarcode(String barcode) throws ApiException {
        ProductPojo existingProductPojo = productDao.selectBarcode(barcode);
        if (existingProductPojo == null){
            throw new ApiException("Product with given barcode" + barcode + " does not exist!");
        }
        return existingProductPojo.getId();
    }

    public boolean isValidBarcode(String barcode){
        ProductPojo existingProductPojo = productDao.selectBarcode(barcode);
        return existingProductPojo == null;
    }

    public String getProductNameFromProductId(int productId) throws ApiException {
        ProductPojo productPojo = getCheck(productId);
        return productPojo.getName();
    }
}
