package com.ecom.services;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;

import java.util.Iterator;
import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;
   public List<ProductOrder> getOrderByUser(Integer userId);
   public ProductOrder updateOrderStatus(Integer id, String st);
    public List<ProductOrder> getAllOrder();

}
