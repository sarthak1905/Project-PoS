package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ProductServiceTest extends AbstractUnitTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;

    private final String brandName = "brand";
    private final String categoryName = "category";
    private static final String productName = "product";
    private static final double mrp = 99.99;
    private static final String barcode = "barcode";
    private Integer brandId;

    @Test(expected = ApiException.class)
    public void testNullAdd() throws ApiException {
        productService.add(null);
    }

    @Test(expected = ApiException.class)
    public void testRepeatedBarcodeAdd() throws ApiException {
        ProductPojo productPojo = createNewProductPojo(productName, barcode, mrp, brandId);
        productService.add(productPojo);
    }

    @Test
    public void testGetAll() throws ApiException {
        String productName2 = "productName2";
        String barcode2 = "barcode2";
        double mrp2 = 199.99;
        Integer brandId2 = 5;
        ProductPojo productPojo = createNewProductPojo(productName2, barcode2, mrp2, brandId2);
        productService.add(productPojo);
        List<ProductPojo> productPojoList = productService.getAll();
        if(productPojoList.size() != 2){
            fail();
        }
        ProductPojo productPojo1 = productPojoList.get(0);
        assertProductPojoEquals(productPojo1);
        ProductPojo productPojo2 = productPojoList.get(1);
        assertEquals(productName2, productPojo2.getName());
        assertEquals(barcode2, productPojo2.getBarcode());
        assertEquals(mrp2, productPojo2.getMrp());
        assertEquals(brandId2, productPojo2.getBrandCategory());
    }

    @Test
    public void testGet() throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if(productPojo == null){
            fail();
        }
        int id = productPojo.getId();
        ProductPojo retrievedProductPojo = productService.get(id);
        assertEquals(productPojo.getName(), retrievedProductPojo.getName());
        assertEquals(productPojo.getBarcode(), retrievedProductPojo.getBarcode());
        assertEquals(productPojo.getMrp(), retrievedProductPojo.getMrp());
        assertEquals(productPojo.getBrandCategory(), retrievedProductPojo.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void testInvalidIdGet() throws ApiException {
        int id = 999;
        ProductPojo retrievedProductPojo = productService.get(id);
    }

    @Test
    public void testUpdate() throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if(productPojo == null){
            fail();
        }
        String newName = "newName";
        String newBarcode = "newBarcode";
        double newMrp = 299.99;
        Integer newBrandId = 99;
        productPojo.setName(newName);
        productPojo.setBarcode(newBarcode);
        productPojo.setMrp(newMrp);
        productPojo.setBrandCategory(newBrandId);
        productService.update(productPojo.getId(), productPojo);
        ProductPojo retrievedProductPojo = productDao.selectId(productPojo.getId());
        assertEquals(newName, retrievedProductPojo.getName());
        assertEquals(newBarcode, retrievedProductPojo.getBarcode());
        assertEquals(newMrp, retrievedProductPojo.getMrp());
        assertEquals(newBrandId, retrievedProductPojo.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void testNullUpdate() throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if(productPojo == null){
            fail();
        }
        productService.update(productPojo.getId(), null);
    }

    @Test
    public void testGetProductIdFromBarcode() throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if(productPojo == null){
            fail();
        }
        String barcode = productPojo.getBarcode();
        int productId = productService.getProductIdFromBarcode(barcode);
        ProductPojo retrievedProductPojo = productDao.selectId(productId);
        assertEquals(productPojo.getName(), retrievedProductPojo.getName());
        assertEquals(productPojo.getBarcode(), retrievedProductPojo.getBarcode());
        assertEquals(productPojo.getMrp(), retrievedProductPojo.getMrp());
        assertEquals(productPojo.getBrandCategory(), retrievedProductPojo.getBrandCategory());
    }

    @Test
    public void testGetProductNameFromProductId() throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if (productPojoList.size() == 0) {
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if (productPojo == null) {
            fail();
        }
        int id = productPojo.getId();
        String name = productService.getProductNameFromProductId(id);
        assertEquals(name, productPojo.getName());
    }

    @Test
    public void testGetBarcodeFromProductId() {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if (productPojoList.size() == 0) {
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        if (productPojo == null) {
            fail();
        }
        String barcode = productService.getBarcodeFromProductId(productPojo.getId());
        assertEquals(barcode, productPojo.getBarcode());
    }

    @Before
    public void initBrand() {
        BrandPojo brandPojo = createNewBrandPojo(brandName, categoryName);
        brandDao.insert(brandPojo);
        brandId = brandPojo.getId();
        ProductPojo productPojo = createNewProductPojo(productName, barcode, mrp, brandId);
        productDao.insert(productPojo);
    }

    private ProductPojo createNewProductPojo(String name, String barcode, double mrp, int brandCategoryId){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setBarcode(barcode);
        productPojo.setMrp(mrp);
        productPojo.setBrandCategory(brandCategoryId);
        return productPojo;
    }

    private BrandPojo createNewBrandPojo(String brand, String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    private void assertProductPojoEquals(ProductPojo productPojo){
        assertEquals(productName, productPojo.getName());
        assertEquals(barcode, productPojo.getBarcode());
        assertEquals(mrp, productPojo.getMrp());
        assertEquals(brandId, productPojo.getBrandCategory());
    }
}
