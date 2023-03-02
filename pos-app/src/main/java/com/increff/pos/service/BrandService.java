package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

    @Autowired
    private BrandDao brandDao;
    
    public void add(BrandPojo brandPojo) throws ApiException {
        ValidationUtil.checkPojo(brandPojo);
        brandDao.insert(brandPojo);
    }
    
    public BrandPojo get(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        return getCheck(id);
    }

    public List<BrandPojo> getAll(){
        return brandDao.selectAll();
    }
    
    public void update(Integer id, BrandPojo brandPojo) throws ApiException {
        ValidationUtil.checkId(id);
        ValidationUtil.checkPojo(brandPojo);
        BrandPojo existingBrandPojo = getCheck(id);
        existingBrandPojo.setBrand(brandPojo.getBrand());
        existingBrandPojo.setCategory(brandPojo.getCategory());
    }
    
    public BrandPojo getByBrandCategory(String brand, String category){
        return brandDao.selectBrandCategory(brand, category);
    }

    public BrandPojo getCheck(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        BrandPojo brandPojo = brandDao.selectId(id);
        if(brandPojo == null){
            throw new ApiException("Brand with given ID does not exist!");
        }
        return brandPojo;
    }

}
