package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.ProductUtil;
import com.increff.employee.util.StringUtil;
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
        ProductUtil.normalize(p);
        if (StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty!");
        }
        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty!");
        }
        if (!checkValidity(p)) {
            throw new ApiException("Barcode already exists!");
        }
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
        bx.setBarcode(p.getBarcode());
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
    private boolean checkValidity(ProductPojo p){
        ProductPojo px = productDao.selectBarcode(p.getBarcode());
        return px == null;
    }

}
