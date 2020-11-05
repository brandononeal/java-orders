package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Order;

public interface OrderServices
{
    Order findOrderById(long ordnum);

    Order save(Order order);

    void deleteAllOrders();

    void delete(long ordnum);
}
