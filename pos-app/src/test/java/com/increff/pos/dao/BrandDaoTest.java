package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class BrandDaoTest extends AbstractUnitTest {

    @Autowired
    private BrandDao brandDao;

    private final String testBrand = "brand";
    private final String testCategory = "category";

    @Test
    public void testInsert(){
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        BrandPojo brandPojo = null;
        try {
            brandPojo = brandPojoList.get(0);
        }
        catch (Exception e){
            fail();
        }
        assertEquals(testBrand, brandPojo.getBrand());
        assertEquals(testCategory, brandPojo.getCategory());
    }

    @Test
    public void testSelectId(){
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        if(brandPojoList.size() == 0){
            fail();
        }
        BrandPojo brandPojo = brandPojoList.get(0);
        if(brandPojo == null){
            fail();
        }
        int id = brandPojo.getId();
        BrandPojo retrievedBrandPojo = brandDao.selectId(id);
        assertEquals(testBrand, retrievedBrandPojo.getBrand());
        assertEquals(testCategory, retrievedBrandPojo.getCategory());
    }

    @Test
    public void testSelectBrand(){
        BrandPojo brandPojo = brandDao.selectBrand(testBrand);
        if(brandPojo == null){
            fail();
        }
        assertEquals(testBrand, brandPojo.getBrand());
    }

    @Test
    public void testSelectCategory() {
        BrandPojo brandPojo = brandDao.selectCategory(testCategory);
        if (brandPojo == null) {
            fail();
        }
        assertEquals(testCategory, brandPojo.getCategory());
    }

    @Test
    public void testSelectAll(){
        String brand2 = "brand2";
        String category2 = "category2";
        insertBrand(brand2, category2);
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        if(brandPojoList.size() != 2){
            fail();
        }
        BrandPojo retrievedBrandPojo1 = brandPojoList.get(0);
        assertEquals(testBrand, retrievedBrandPojo1.getBrand());
        assertEquals(testCategory, retrievedBrandPojo1.getCategory());
        BrandPojo retrievedBrandPojo2 = brandPojoList.get(1);
        assertEquals("brand2", retrievedBrandPojo2.getBrand());
        assertEquals("category2", retrievedBrandPojo2.getCategory());
    }

    @Test
    public void testSelectBrandCategory() {
        BrandPojo brandPojo = brandDao.selectBrandCategory(testBrand, testCategory);
        if (brandPojo == null) {
            fail();
        }
        assertEquals(testBrand, brandPojo.getBrand());
        assertEquals(testCategory, brandPojo.getCategory());
    }

    @Before
    public void initBrand(){
        insertBrand(testBrand, testCategory);
    }

    private void insertBrand(String brand, String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        brandDao.insert(brandPojo);
    }
    
}
