package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandDao brandDao;

    private final String brandName = "brand";
    private final String categoryName = "category";

    @Test(expected = ApiException.class)
    public void testNullAdd() throws ApiException {
        brandService.add(null);
    }

    @Test(expected = ApiException.class)
    public void testRepeatedBrandCategoryAdd() throws ApiException {
        BrandPojo brandPojo = createNewBrandPojo(brandName, categoryName);
        brandService.add(brandPojo);
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandPojo brandPojo = createNewBrandPojo("brand2", "category2");
        brandService.add(brandPojo);
        List<BrandPojo> brandPojoList = brandService.getAll();
        if(brandPojoList.size() != 2){
            fail();
        }
        BrandPojo brandPojo1 = brandPojoList.get(0);
        assertBrandPojoEquals(brandPojo1);
        BrandPojo brandPojo2 = brandPojoList.get(1);
        assertEquals("brand2", brandPojo2.getBrand());
        assertEquals("category2", brandPojo2.getCategory());
    }

    @Test
    public void testGet() throws ApiException {
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        if(brandPojoList.size() == 0){
            fail();
        }
        BrandPojo brandPojo = brandPojoList.get(0);
        if(brandPojo == null){
            fail();
        }
        int id = brandPojo.getId();
        BrandPojo retrievedBrandPojo = brandService.get(id);
        assertEquals(brandPojo.getBrand(), retrievedBrandPojo.getBrand());
        assertEquals(brandPojo.getCategory(), retrievedBrandPojo.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testInvalidIdGet() throws ApiException {
        int id = 999;
        BrandPojo retrievedBrandPojo = brandService.get(id);
    }

    @Test
    public void testUpdate() throws ApiException {
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        if(brandPojoList.size() == 0){
            fail();
        }
        BrandPojo brandPojo = brandPojoList.get(0);
        if(brandPojo == null){
            fail();
        }
        String newBrand = "newBrand";
        String newCategory = "newCategory";
        brandPojo.setBrand(newBrand);
        brandPojo.setCategory(newCategory);
        brandService.update(brandPojo.getId(), brandPojo);
        BrandPojo retrievedBrandPojo = brandDao.selectId(brandPojo.getId());
        assertEquals(newBrand, retrievedBrandPojo.getBrand());
        assertEquals(newCategory, retrievedBrandPojo.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testNullUpdate() throws ApiException {
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        if(brandPojoList.size() == 0){
            fail();
        }
        BrandPojo brandPojo = brandPojoList.get(0);
        if(brandPojo == null){
            fail();
        }
        brandPojo = null;
        brandService.update(null, brandPojo);
    }

    @Test
    public void testGetByBrandCategory(){
        BrandPojo brandPojo = brandService.getByBrandCategory(brandName, categoryName);
        if(brandPojo == null){
            fail();
        }
        assertBrandPojoEquals(brandPojo);
    }

    @Before
    public void initBrand() throws ApiException {
        BrandPojo brandPojo = createNewBrandPojo(brandName, categoryName);
        brandService.add(brandPojo);
    }

    private BrandPojo createNewBrandPojo(String brand, String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    private void assertBrandPojoEquals(BrandPojo brandPojo){
        assertEquals(brandName, brandPojo.getBrand());
        assertEquals(categoryName, brandPojo.getCategory());
    }
}
