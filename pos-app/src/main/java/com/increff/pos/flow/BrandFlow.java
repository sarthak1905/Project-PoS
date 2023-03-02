package com.increff.pos.flow;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandFlow {

    @Autowired
    private BrandService brandService;

    public void add(BrandPojo brandPojo) throws ApiException {
        ValidationUtil.checkPojo(brandPojo);
        if (!checkByBrandCategory(brandPojo.getBrand(), brandPojo.getCategory())) {
            throw new ApiException("Brand+category combination must be unique!");
        }
        brandService.add(brandPojo);
    }

    public BrandPojo get(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        return brandService.get(id);
    }

    public BrandPojo getByBrandCategory(String brand, String category) throws ApiException {
        if(checkByBrandCategory(brand, category)) {
            throw new ApiException("Brand with given brand + category combination does not exist!");
        }
        return brandService.getByBrandCategory(brand, category);
    }

    public List<BrandPojo> getAll() {
        return brandService.getAll();
    }

    public void update(Integer id, BrandPojo brandPojo) throws ApiException {
        ValidationUtil.checkPojo(brandPojo);
        ValidationUtil.checkId(id);
        if (!checkExistingById(brandPojo, id)) {
            throw new ApiException("Brand+category combination must be unique!");
        }
        brandService.update(id, brandPojo);
    }

    // <----------------------------------PRIVATE METHODS------------------------------------->

    private Boolean checkByBrandCategory(String brand, String category){
        BrandPojo existingBrandPojo = brandService.getByBrandCategory(brand, category);
        return existingBrandPojo == null;
    }

    private Boolean checkExistingById(BrandPojo brandPojo, Integer id){
        BrandPojo existingBrandPojo = brandService.getByBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (existingBrandPojo != null)
            return existingBrandPojo.getId() == id;
        return true;
    }

}
