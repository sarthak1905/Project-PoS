package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.BrandUtil;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BrandService {

    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo b) throws ApiException {
        BrandUtil.normalize(b);
        if(StringUtil.isEmpty(b.getBrand())) {
            throw new ApiException("Brand name cannot be empty!");
        }
        if(StringUtil.isEmpty(b.getCategory())) {
            throw new ApiException("Brand category cannot be empty!");
        }
        dao.insert(b);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional
    public BrandPojo getCheck(int id) throws ApiException{
        BrandPojo b = dao.select(id);
        if(b == null){
            throw new ApiException("Brand with given ID does not exist");
        }
        return b;
    }

}
