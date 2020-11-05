package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.repositories.CustomerRepository;
import com.lambdaschool.orders.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "customerServices")
public class CustomerServicesImpl implements CustomerServices
{
    @Autowired
    CustomerRepository custrepos;

    @Autowired
    OrderRepository orderrepos;

    @Override
    public List<Customer> findAllCustomers()
    {
        List<Customer> list = new ArrayList<>();
        custrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Customer findCustomerById(long custcode)
    {
        Customer rtnCust = custrepos.findById(custcode)
            .orElseThrow(() -> new EntityNotFoundException("Customer " + custcode + " Not Found"));
        return rtnCust;
    }

    @Override
    public List<Customer> findCustomerLikeName(String subname)
    {
        List<Customer> rtnList = custrepos.findByCustnameContainingIgnoringCase(subname);
        return rtnList;
    }

    // Valid Data
    @Transactional
    @Override
    public Customer save(Customer customer)
    {
        Customer newCustomer = new Customer();

        if (customer.getCustcode() != 0)
        {
            findCustomerById(customer.getCustcode());
            newCustomer.setCustcode(customer.getCustcode());
        }

        // single value fields
        newCustomer.setCustname(customer.getCustname());
        newCustomer.setCustcity(customer.getCustcity());
        newCustomer.setWorkingarea(customer.getWorkingarea());
        newCustomer.setCustcountry(customer.getCustcountry());
        newCustomer.setGrade(customer.getGrade());
        newCustomer.setOpeningamt(customer.getOpeningamt());
        newCustomer.setReceiveamt(customer.getReceiveamt());
        newCustomer.setPaymentamt(customer.getPaymentamt());
        newCustomer.setOutstandingamt(customer.getOutstandingamt());
        newCustomer.setPhone(customer.getPhone());
        newCustomer.setAgent(customer.getAgent());

        // collections
        newCustomer.getOrders().clear();
        for (Order o : customer.getOrders())
        {
            Order newOrder = orderrepos.findById(o.getOrdnum())
                .orElseThrow(() -> new EntityNotFoundException("Order " + o.getOrdnum() + " Not Found"));
            newCustomer.getOrders().add(newOrder);
        }

        return custrepos.save(newCustomer);
    }

    @Transactional
    @Override
    public Customer update(
        Customer customer,
        long custcode)
    {
        Customer updateCustomer = findCustomerById(customer.getCustcode());

        // single value fields
        if (customer.getCustname() != null)
        {
            updateCustomer.setCustname(customer.getCustname());
        }
        if (customer.getCustcity() != null)
        {
            updateCustomer.setCustcity(customer.getCustcity());
        }
        if (customer.getWorkingarea() != null)
        {
            updateCustomer.setWorkingarea(customer.getWorkingarea());
        }
        if (customer.getCustcountry() != null)
        {
            updateCustomer.setCustcountry(customer.getCustcountry());
        }
        if (customer.getGrade() != null)
        {
            updateCustomer.setGrade(customer.getGrade());
        }
        if (customer.hasvalueforopeningamt)
        {
            updateCustomer.setOpeningamt(customer.getOpeningamt());
        }
        if (customer.hasvalueforreceiveamt)
        {
            updateCustomer.setReceiveamt(customer.getReceiveamt());
        }
        if (customer.hasvalueforpaymentamt)
        {
            updateCustomer.setPaymentamt(customer.getPaymentamt());
        }
        if (customer.hasvalueforoutstandingamt)
        {
            updateCustomer.setOutstandingamt(customer.getOutstandingamt());
        }
        if (customer.getPhone() != null)
        {
            updateCustomer.setPhone(customer.getPhone());
        }
        if (customer.getAgent() != null)
        {
            updateCustomer.setAgent(customer.getAgent());
        }

        // collections
        if (customer.getOrders().size() > 0)
        {
            updateCustomer.getOrders().clear();
            for (Order o : customer.getOrders())
            {
                Order newOrder = orderrepos.findById(o.getOrdnum())
                    .orElseThrow(() -> new EntityNotFoundException("Order " + o.getOrdnum() + " Not Found"));
                updateCustomer.getOrders().add(newOrder);
            }
        }

        return custrepos.save(updateCustomer);
    }

    @Transactional
    @Override
    public void deleteAllCustomers()
    {
        custrepos.deleteAll();
    }

    @Transactional
    @Override
    public void delete(long custcode)
    {
        if (custrepos.findById(custcode)
            .isPresent())
        {
            custrepos.deleteById(custcode);
        } else
        {
            throw new EntityNotFoundException("Customer " + custcode + " Not Found");
        }
    }
}
