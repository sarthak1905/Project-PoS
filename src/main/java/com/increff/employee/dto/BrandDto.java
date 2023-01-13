package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm form) throws ApiException {
        BrandPojo b = convert(form);
        brandService.add(b);
    }

    public BrandData get(int id) throws ApiException{
        BrandPojo b = brandService.get(id);
        return convert(b);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<BrandData>();
        for (BrandPojo b : brandList) {
            brandDataList.add(convert(b));
        }
        return brandDataList;
    }

    public void update(int id, BrandForm form) throws ApiException {
        BrandPojo b = convert(form);
        brandService.update(id, b);
    }

    public void delete(int id) throws ApiException {
        brandService.delete(id);
    }

    private static BrandData convert(BrandPojo b){
        BrandData d = new BrandData();
        d.setBrand(b.getBrand());
        d.setCategory(b.getCategory());
        d.setId(b.getId());
        return d;
    }

    private static BrandPojo convert(BrandForm f){
        BrandPojo b = new BrandPojo();
        b.setBrand(f.getBrand());
        b.setCategory(f.getCategory());
        return b;
    }

}
