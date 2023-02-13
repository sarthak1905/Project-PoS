package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ProductDaoTest extends AbstractUnitTest {

    private static final String productName = "product";
    private static final double productMRP = 99.99;
    private static final String productBarcode = "barcode";
    private static final Integer brandId = 1;

    @Autowired
    private ProductDao productDao;

    @Test
    public void testInsert(){
        List<ProductPojo> productPojoList = productDao.selectAll();
        ProductPojo productPojo = null;
        try {
            productPojo = productPojoList.get(0);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail();
        }
        assertProductPojo(productPojo);
    }

    @Test
    public void testSelectId(){
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if(productPojo == null){
            fail();
        }
        int id = productPojo.getId();
        ProductPojo retrievedProductPojo = productDao.selectId(id);
        assertProductPojo(retrievedProductPojo);
    }

    @Test
    public void testSelectAll(){
        String productName2 = "product2";
        String productBarcode2 = "barcode2";
        double productMrp2 = 199.99;
        Integer brandId2 = 2;
        insertProduct(productName2, productBarcode2, brandId2, productMrp2);
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() != 2){
            fail();
        }
        ProductPojo retrievedProductPojo1 = productPojoList.get(0);
        assertProductPojo(retrievedProductPojo1);

        ProductPojo retrievedProductPojo2 = productPojoList.get(1);
        assertEquals(productName2, retrievedProductPojo2.getName());
        assertEquals(productBarcode2, retrievedProductPojo2.getBarcode());
        assertEquals(brandId2, retrievedProductPojo2.getBrandCategory());
        assertEquals(productMrp2,retrievedProductPojo2.getMrp());
    }

    @Test
    public void testSelectBarcode(){
        ProductPojo productPojo = productDao.selectBarcode(productBarcode);
        if(productPojo == null){
            fail();
        }
        assertProductPojo(productPojo);
    }

    @Before
    public void initProduct(){
        insertProduct(productName, productBarcode, brandId, productMRP);
    }

    private void insertProduct(String productName, String productBarcode, int brandCategoryId, double productMRP){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(productName);
        productPojo.setBarcode(productBarcode);
        productPojo.setMrp(productMRP);
        productPojo.setBrandCategory(brandCategoryId);
        productDao.insert(productPojo);
    }

    private void assertProductPojo(ProductPojo productPojo){
        assertEquals(productName, productPojo.getName());
        assertEquals(productBarcode, productPojo.getBarcode());
        assertEquals(brandId, productPojo.getBrandCategory());
        assertEquals(productMRP, productPojo.getMrp());
    }

}
