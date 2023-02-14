package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ProductDtoTest extends AbstractUnitTest {

    private static final String productName = "name";
    private static final String productBarcode = "barcode";
    private static final String productBrand = "brand";
    private static final String productCategory = "category";
    private static final Double productMrp = 99.99;

    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void testGet() throws ApiException {
        List<ProductData> productDataList = productDto.getAll();
        if(productDataList.size() != 1){
            fail();
        }
        ProductData productData = productDataList.get(0);
        int productId = productData.getId();
        ProductData productData1 = productDto.get(productId);
        assertProductDataEquals(productData1);
    }

    @Test
    public void testGetAll() throws ApiException {
        String name2 = "name2";
        String category2 = "category2";
        String brand2 = "brand2";
        String barcode2 = "barcode2";
        Double mrp2 = 199.99;
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand2);
        brandPojo.setCategory(category2);
        brandDao.insert(brandPojo);
        ProductForm productForm = TestHelper.createNewProductForm(name2, barcode2, brand2, category2, mrp2);
        productDto.add(productForm);
        List<ProductData> productDataList = productDto.getAll();
        if(productDataList.size() != 2){
            fail();
        }
        ProductData productData1 = productDataList.get(0);
        assertProductDataEquals(productData1);
        ProductData productData2 = productDataList.get(1);
        assertEquals(name2, productData2.getName());
        assertEquals(barcode2, productData2.getBarcode());
        assertEquals(brand2, productData2.getBrand());
        assertEquals(category2, productData2.getCategory());
        assertEquals(mrp2, productData2.getMrp());
    }

    @Test
    public void testUpdate() throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        String name2 = "name2";
        String category2 = "category2";
        String brand2 = "brand2";
        Double mrp2 = 199.99;
        BrandPojo brandPojo = TestHelper.createBrand(brand2, category2);
        brandDao.insert(brandPojo);
        BrandPojo brandPojo1 = brandDao.selectBrandCategory(brand2, category2);
        Integer brandId = brandPojo1.getId();
        ProductForm productForm = TestHelper.createNewProductForm(name2, productBarcode, brand2, category2, mrp2);
        productDto.update(productPojo.getId(), productForm);
        ProductPojo productPojo1 = productDao.selectId(productPojo.getId());
        assertEquals(name2, productPojo1.getName());
        assertEquals(productBarcode, productPojo1.getBarcode());
        assertEquals(mrp2, productPojo1.getMrp());
        assertEquals(brandId, productPojo1.getBrandCategory());
    }

    @Test
    public void testInventoryCreation(){
        List<ProductPojo> productPojoList = productDao.selectAll();
        if(productPojoList.size() == 0){
            fail();
        }
        ProductPojo productPojo = productPojoList.get(0);
        int productId = productPojo.getId();
        InventoryPojo inventoryPojo = inventoryDao.selectId(productId);
        if(inventoryPojo == null){
            fail();
        }
        if(inventoryPojo.getQuantity() != 0){
            fail();
        }
    }

    @Before
    public void initProductForm() throws ApiException {
        ProductForm productForm = TestHelper.createNewProductForm(productName, productBarcode,
                productBrand, productCategory, productMrp);
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(productBrand);
        brandPojo.setCategory(productCategory);
        brandDao.insert(brandPojo);
        productDto.add(productForm);
    }

    private void assertProductDataEquals(ProductData productData){
        assertEquals(productName, productData.getName());
        assertEquals(productCategory, productData.getCategory());
        assertEquals(productBrand, productData.getBrand());
        assertEquals(productMrp, productData.getMrp());
        assertEquals(productBarcode, productData.getBarcode());
    }

}
