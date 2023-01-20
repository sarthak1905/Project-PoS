package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
// TODO can have @Transactional(rollbackFor = ApiException.class) at class level
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo b) throws ApiException {
        if (!isExists(b)) {
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
        if (!isExists(b)) {
            throw new ApiException("Brand+category combination must be unique!");
        }
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
    public BrandPojo getBrandCategory(String brand, String category){
        return brandDao.selectBrandCategory(brand, category);
    }

    @Transactional
    private boolean isExists(BrandPojo brandPojo){
        BrandPojo p = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (p == null){
            return true;
        }
        return false;
    }

    public void checkIfNull(BrandPojo b) throws ApiException {
        if (b == null) {
            throw new ApiException("Brand-category combination does not exist!");
        }
    }
}
