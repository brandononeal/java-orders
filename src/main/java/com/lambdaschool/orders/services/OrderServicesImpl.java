package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.models.Payment;
import com.lambdaschool.orders.repositories.OrderRepository;
import com.lambdaschool.orders.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@Service(value = "orderServices")
public class OrderServicesImpl implements OrderServices
{
    @Autowired
    OrderRepository orderrepos;

    @Autowired
    PaymentRepository paymentrepos;

    @Override
    public Order findOrderById(long ordnum)
    {
        Order rtnOrd = orderrepos.findById(ordnum)
            .orElseThrow(() -> new EntityNotFoundException("Order " + ordnum + " Not Found"));
        return rtnOrd;
    }

    // Valid Data
    @Transactional
    @Override
    public Order save(Order order)
    {
        Order newOrder = new Order();

        if (order.getOrdnum() != 0)
        {
            findOrderById(order.getOrdnum());
            newOrder.setOrdnum(order.getOrdnum());
        }

        // single value fields
        newOrder.setOrdamount(order.getOrdamount());
        newOrder.setAdvanceamount(order.getAdvanceamount());
        newOrder.setOrderdescription(order.getOrderdescription());
        newOrder.setCustomer(order.getCustomer());

        // collections
        newOrder.getPayments().clear();
        for (Payment p : order.getPayments())
        {
            Payment newPayment = paymentrepos.findById(p.getPaymentid())
                .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid() + " Not Found"));
            newOrder.getPayments().add(newPayment);
        }

        return orderrepos.save(newOrder);
    }

    @Transactional
    @Override
    public void deleteAllOrders()
    {
        orderrepos.deleteAll();
    }

    @Transactional
    @Override
    public void delete(long ordnum)
    {
        if (orderrepos.findById(ordnum)
            .isPresent())
        {
            orderrepos.deleteById(ordnum);
        } else
        {
            throw new EntityNotFoundException("Order " + ordnum + " Not Found");
        }
    }
}
