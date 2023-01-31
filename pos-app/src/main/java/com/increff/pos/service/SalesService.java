package com.increff.pos.service;

import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class SalesService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public List<SalesData> generateSalesDataList(SalesForm salesForm) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        return null;
    }
}
