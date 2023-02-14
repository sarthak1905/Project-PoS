package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class BrandDtoTest extends AbstractUnitTest {

    private BrandForm brandForm;
    private static final String brand = "brand";
    private static final String category = "category";

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private BrandDao brandDao;

    @Test
    public void testGet() throws ApiException {
        List<BrandData> brandDataList = brandDto.getAll();
        if(brandDataList.size() != 1){
            fail();
        }
        BrandData brandData = brandDataList.get(0);
        int brandId = brandData.getId();
        BrandData retrivedBrandData = brandDto.get(brandId);
        assertBrandDataEquals(retrivedBrandData);
    }

    @Test
    public void testGetAll() throws ApiException {
        String brand2 = "brand2";
        String category2 = "category2";
        BrandForm brandForm = createNewBrandForm(brand2, category2);
        brandDto.add(brandForm);
        List<BrandData> brandDataList = brandDto.getAll();
        if(brandDataList.size() != 2){
            fail();
        }
        BrandData brandData1 = brandDataList.get(0);
        assertBrandDataEquals(brandData1);
        BrandData brandData2 = brandDataList.get(1);
        assertEquals(brand2, brandData2.getBrand());
        assertEquals(category2, brandData2.getCategory());
    }

    @Test
    public void testNormalizeAdd() throws ApiException {
        String brandNormalize = "BrAnD1";
        String categoryNormalize = "CaTegOrY1";
        BrandForm brandForm = createNewBrandForm(brandNormalize, categoryNormalize);
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandDao.selectBrandCategory("brand1", "category1");
        if (brandPojo == null){
            fail();
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        if(brandPojoList.size() == 0){
            fail();
        }
        BrandPojo brandPojo = brandPojoList.get(0);
        String newBrand = "newbrand";
        String newCategory = "newcategory";
        BrandForm brandForm1 = createNewBrandForm(newBrand, newCategory);
        brandDto.update(brandPojo.getId(), brandForm1);
        BrandPojo brandPojo1 = brandDao.selectId(brandPojo.getId());
        assertEquals(newBrand, brandPojo1.getBrand());
        assertEquals(newCategory, brandPojo1.getCategory());
    }

    @Before
    public void initBrandForm() throws ApiException {
        brandForm = createNewBrandForm(brand, category);
        brandDto.add(brandForm);
    }

    private BrandForm createNewBrandForm(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    private void assertBrandDataEquals(BrandData brandData){
        assertEquals(brand, brandData.getBrand());
        assertEquals(category, brandData.getCategory());
    }

}
