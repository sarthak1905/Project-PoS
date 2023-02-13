package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
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
        if(brandPojo == null){
            throw new ApiException("BrandPojo is null!");
        }
        if (!brandCategoryCombinationExists(brandPojo)) {
            throw new ApiException("Brand+category combination must be unique!");
        }
        brandDao.insert(brandPojo);
    }
    
    public BrandPojo get(int id) throws ApiException{
        return getCheck(id);
    }
    
    public List<BrandPojo> getAll(){
        return brandDao.selectAll();
    }
    
    public void update(Integer id, BrandPojo brandPojo) throws ApiException {
        if(brandPojo == null){
            throw new ApiException("BrandPojo is null!");
        }
        if (!brandCategoryCombinationExists(brandPojo, id)){
            throw new ApiException("Brand+category combination must be unique!");
        }
        BrandPojo existingBrandPojo = getCheck(id);
        existingBrandPojo.setBrand(brandPojo.getBrand());
        existingBrandPojo.setCategory(brandPojo.getCategory());
    }
    
    public BrandPojo getCheck(Integer id) throws ApiException{
        BrandPojo brandPojo = brandDao.selectId(id);
        if(brandPojo == null){
            throw new ApiException("Brand with given ID does not exist");
        }
        return brandPojo;
    }
    
    public BrandPojo getByBrandCategory(String brand, String category){
        return brandDao.selectBrandCategory(brand, category);
    }
    
    private boolean brandCategoryCombinationExists(BrandPojo brandPojo, int id){
        BrandPojo existingBrandPojo = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (existingBrandPojo != null) {
            return existingBrandPojo.getId() == id;
        }
        return true;
    }

    private boolean brandCategoryCombinationExists(BrandPojo brandPojo){
        BrandPojo existingBrandPojo = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        return existingBrandPojo == null;
    }
    
}
