package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.EmployeePojo;
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
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo b) throws ApiException {
        normalize(b);
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

    @Transactional(rollbackOn = ApiException.class)
    public List<BrandPojo> getAll(){
        return dao.selectAll();
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
        dao.delete(id);
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
