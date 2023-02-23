package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
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

public class InventoryDtoTest extends AbstractUnitTest {

    private int productId;
    private static final String productName = "name";
    private static final String productBarcode = "barcode";
    private static final String productBrand = "brand";
    private static final String productCategory = "category";
    private static final Double productMrp = 99.99;
    private static final Integer quantity = 100;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;

    @Test
    public void testGet() throws ApiException {
        List<InventoryData> inventoryDataList = inventoryDto.getAll();
        if(inventoryDataList.size() != 1){
            fail();
        }
        InventoryData inventoryData = inventoryDataList.get(0);
        InventoryData inventoryData1 = inventoryDto.get(inventoryData.getId());
        assertEquals(inventoryData.getQuantity(), inventoryData1.getQuantity());
        assertEquals(inventoryData.getBarcode(), inventoryData1.getBarcode());
        assertEquals(inventoryData.getId(), inventoryData1.getId());
    }

    @Test
    public void testGetAll() throws ApiException {
        ProductPojo productPojo = createTemplateProductPojo();
        Integer quantity2 = 200;
        String barcode2 = productPojo.getBarcode();
        int productId2 = productPojo.getId();
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productId2, quantity2);
        inventoryDao.insert(inventoryPojo);

        List<InventoryData> inventoryDataList = inventoryDto.getAll();
        if(inventoryDataList.size() != 2){
            fail();
        }
        InventoryData inventoryData1 = inventoryDataList.get(0);
        assertInventoryDataEquals(inventoryData1);
        InventoryData inventoryData2 = inventoryDataList.get(1);
        assertEquals(productId2, inventoryData2.getId());
        assertEquals(quantity2, inventoryData2.getQuantity());
        assertEquals(barcode2, inventoryData2.getBarcode());
    }

    @Test
    public void testUpdate() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        if(inventoryPojoList.size() != 1){
            fail();
        }
        InventoryPojo inventoryPojo = inventoryPojoList.get(0);
        Integer quantity2 = 200;
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setQuantity(quantity2);
        inventoryForm.setBarcode(productBarcode);
        inventoryDto.update(productId, inventoryForm);
        InventoryPojo inventoryPojo1 = inventoryDao.selectId(productId);
        assertEquals(quantity2, inventoryPojo1.getQuantity());
    }

    @Test
    public void testUpdateByBarcode() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        if(inventoryPojoList.size() != 1){
            fail();
        }
        InventoryPojo inventoryPojo = inventoryPojoList.get(0);
        Integer quantity2 = 200;
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setQuantity(quantity2);
        inventoryForm.setBarcode(productBarcode);
        inventoryDto.updateByBarcode(productBarcode, inventoryForm);
        InventoryPojo inventoryPojo1 = inventoryDao.selectId(productId);
        assertEquals(quantity2, inventoryPojo1.getQuantity());
    }

    @Before
    public void initInventory(){
        ProductPojo productPojo = createNewProductPojo(productName, productBarcode, productBrand, productCategory, productMrp);
        productDao.insert(productPojo);
        productPojo = productDao.selectBarcode(productBarcode);
        productId = productPojo.getId();
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productId, quantity);
        inventoryDao.insert(inventoryPojo);
    }

    private ProductPojo createTemplateProductPojo(){
        String name2 = "name2";
        String category2 = "category2";
        String brand2 = "brand2";
        String barcode2 = "barcode2";
        Double mrp2 = 199.99;

        ProductPojo productPojo = createNewProductPojo(name2, barcode2, brand2, category2, mrp2);
        productDao.insert(productPojo);
        productPojo = productDao.selectBarcode(barcode2);
        return productPojo;
    }

    private ProductPojo createNewProductPojo(String productName, String productBarcode, String productBrand, String productCategory, Double productMrp) {
        BrandPojo brandPojo = TestHelper.createBrand(productBrand, productCategory);
        brandDao.insert(brandPojo);
        brandPojo = brandDao.selectBrandCategory(productBrand, productCategory);
        int brandId = brandPojo.getId();
        return TestHelper.createProduct(productName, productBarcode, brandId, productMrp);
    }

    private void assertInventoryDataEquals(InventoryData inventoryData) {
        assertEquals(productId, inventoryData.getId());
        assertEquals(quantity, inventoryData.getQuantity());
        assertEquals(productBarcode, inventoryData.getBarcode());
    }

}
