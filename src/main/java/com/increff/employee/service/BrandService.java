package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

    @Autowired
    private BrandDao brandDao;
    
    public void add(BrandPojo b) throws ApiException {
        if (!existingBrandCategoryCombination(b)) {
            throw new ApiException("Brand+category combination must be unique!");
        }
        brandDao.insert(b);
    }
    
    public BrandPojo get(int id) throws ApiException{
        return getCheck(id);
    }
    
    public List<BrandPojo> getAll(){
        return brandDao.selectAll();
    }
    
    public void update(Integer id, BrandPojo brandPojo) throws ApiException {
        if (!existingBrandCategoryCombination(brandPojo)) {
            throw new ApiException("Brand+category combination must be unique!");
        }
        BrandPojo bx = getCheck(id);
        bx.setBrand(brandPojo.getBrand());
        bx.setCategory(brandPojo.getCategory());
    }
    
    public void delete(Integer id) throws ApiException{
        BrandPojo b = getCheck(id);
        brandDao.delete(id);
    }
    
    public BrandPojo getCheck(Integer id) throws ApiException{
        BrandPojo b = brandDao.select_id(id);
        if(b == null){
            throw new ApiException("Brand with given ID does not exist");
        }
        return b;
    }
    
    public BrandPojo getBrandCategory(String brand, String category){
        return brandDao.selectBrandCategory(brand, category);
    }
    
    private boolean existingBrandCategoryCombination(BrandPojo brandPojo){
        BrandPojo p = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (p == null){
            return true;
        }
        return false;
    }
    
}
