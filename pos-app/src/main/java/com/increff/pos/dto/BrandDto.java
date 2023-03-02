package com.increff.pos.dto;

import com.increff.pos.flow.BrandFlow;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandFlow brandFlow;

    public void add(BrandForm brandForm) throws ApiException {
        ValidationUtil.validateForms(brandForm);
        NormalizeUtil.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convertBrandFormToPojo(brandForm);
        brandFlow.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException{
        BrandPojo brandPojo = brandFlow.get(id);
        return ConvertUtil.convertBrandPojoToData(brandPojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandList = brandFlow.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandList) {
            brandDataList.add(ConvertUtil.convertBrandPojoToData(brandPojo));
        }
        return brandDataList;
    }

    public void update(int id, BrandForm brandForm) throws ApiException {
        ValidationUtil.validateForms(brandForm);
        NormalizeUtil.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convertBrandFormToPojo(brandForm);
        brandFlow.update(id, brandPojo);
    }

}
