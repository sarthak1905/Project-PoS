package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.BrandUtil;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convertFormToPojo(brandForm);
        brandService.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException{
        BrandPojo brandPojo = brandService.get(id);
        return convertPojoToData(brandPojo);
    }

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
            brandDataList.add(convertPojoToData(brandPojo));
        }
        return brandDataList;
    }

    public void update(int id, BrandForm brandForm) throws ApiException {
        checkInputValidity(brandForm);
        BrandPojo brandPojo = convertFormToPojo(brandForm);
        brandService.update(id, brandPojo);
    }

    public void delete(int id) throws ApiException {
        brandService.delete(id);
    }

    private static BrandData convertPojoToData(BrandPojo brandPojo){
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    private static BrandPojo convertFormToPojo(BrandForm brandForm){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brandForm.getBrand());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }

    private void checkInputValidity(BrandForm brandForm) throws ApiException {
        BrandUtil.normalize(brandForm);
        if (StringUtil.isEmpty(brandForm.getBrand())) {
            throw new ApiException("Brand name cannot be empty!");
        }
        if (StringUtil.isEmpty(brandForm.getCategory())) {
            throw new ApiException("Brand category cannot be empty!");
        }
    }

}
