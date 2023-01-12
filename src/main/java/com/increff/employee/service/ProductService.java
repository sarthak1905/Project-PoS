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
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        ProductUtil.normalize(p);
        if (StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty!");
        }
        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty!");
        }
/*        if (!checkValidity(p)) {
            throw new ApiException("Brand-category combination does not exist");
        }*/
        dao.insert(p);
    }


    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductPojo> getAll(){
        return dao.selectAll();
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
        dao.delete(id);
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException{
        ProductPojo p = dao.selectId(id);
        if(p == null){
            throw new ApiException("Product with given ID does not exist");
        }
        return p;
    }

/*    @Transactional
    private boolean checkValidity(ProductPojo p){
        ProductPojo pojoByBrand = dao.select_brand(p.getBrand());
        ProductPojo pojoByCategory = dao.select_category(p.getCategory());
        if(pojoByBrand != null && pojoByCategory != null) {
            String existingBrandCategory = pojoByBrand.getBrand() + pojoByCategory.getCategory();
            String newBrandCategory = p.getBrand() + p.getCategory();
            if (existingBrandCategory.equals(newBrandCategory)) {
                return false;
            }
        }
        return true;
    }*/

}
