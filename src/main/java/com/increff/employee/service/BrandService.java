package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.BrandUtil;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.increff.employee.util.BrandUtil.normalize;

@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo b) throws ApiException {
        BrandUtil.normalize(b);
        if (StringUtil.isEmpty(b.getBrand())) {
            throw new ApiException("Brand name cannot be empty!");
        }
        if (StringUtil.isEmpty(b.getCategory())) {
            throw new ApiException("Brand category cannot be empty!");
        }
        if (!checkValidity(b)) {
            throw new ApiException("Brand+category combination must be unique!");
        }
        brandDao.insert(b);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<BrandPojo> getAll(){
        return brandDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, BrandPojo b) throws ApiException {
        normalize(b);
        BrandPojo bx = getCheck(id);
        bx.setBrand(b.getBrand());
        bx.setCategory(b.getCategory());
    }

    @Transactional
    public void delete(int id) throws ApiException{
        BrandPojo b = getCheck(id);
        brandDao.delete(id);
    }

    @Transactional
    public BrandPojo getCheck(int id) throws ApiException{
        BrandPojo b = brandDao.select_id(id);
        if(b == null){
            throw new ApiException("Brand with given ID does not exist");
        }
        return b;
    }

    @Transactional
    public BrandPojo getBrandCategory(String brand, String category) throws ApiException {
        brand = brand.toLowerCase().trim();
        category = category.toLowerCase().trim();
        if (StringUtil.isEmpty(brand)){
            throw new ApiException("Brand name cannot be empty!");
        }
        if (StringUtil.isEmpty(category)){
            throw new ApiException("Brand category cannot be empty!");
        }
        return brandDao.selectBrandCategory(brand, category);
    }

    @Transactional
    private boolean checkValidity(BrandPojo b){
        BrandPojo pojoByBrand = brandDao.select_brand(b.getBrand());
        BrandPojo pojoByCategory = brandDao.select_category(b.getCategory());
        if(pojoByBrand != null && pojoByCategory != null) {
            String existingBrandCategory = pojoByBrand.getBrand() + pojoByCategory.getCategory();
            String newBrandCategory = b.getBrand() + b.getCategory();
            if (existingBrandCategory.equals(newBrandCategory)) {
                return false;
            }
        }
        return true;
    }

}
