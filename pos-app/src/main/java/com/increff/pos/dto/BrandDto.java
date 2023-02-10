package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.BrandUtil;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        ValidationUtil.validateForms(brandForm);

        // TODO Rather than having normalize for each form in separate classes, have a single class Normalize, and have overloaded functions there.
        BrandUtil.normalize(brandForm);

        BrandPojo brandPojo = ConvertUtil.convertBrandFormToPojo(brandForm);
        brandService.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException{
        BrandPojo brandPojo = brandService.get(id);
        return ConvertUtil.convertBrandPojoToData(brandPojo);
    }

    // TODO remove if not required
    public BrandPojo getBrandCategory(String brand, String category) throws ApiException{
        brand = brand.toLowerCase().trim();
        category = category.toLowerCase().trim();
        if (StringUtil.isEmpty(brand)){
            throw new ApiException("Brand name cannot be empty!");
        }
        if (StringUtil.isEmpty(category)){
            throw new ApiException("Brand category cannot be empty!");
        }
        return brandService.getBrandCategory(brand, category);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandList) {
            brandDataList.add(ConvertUtil.convertBrandPojoToData(brandPojo));
        }
        return brandDataList;
    }

    public void update(int id, BrandForm brandForm) throws ApiException {
        checkInputValidity(brandForm);
        BrandUtil.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convertBrandFormToPojo(brandForm);
        brandService.update(id, brandPojo);
    }

    public void delete(int id) throws ApiException {
        brandService.delete(id);
    }

    private void checkInputValidity(BrandForm brandForm) {
        ValidationUtil.validateForms(brandForm);
    }

}
