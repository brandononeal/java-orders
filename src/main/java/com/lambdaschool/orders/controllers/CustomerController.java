package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController
{
    @Autowired
    CustomerServices customerServices;

    // http://localhost:2019/customers/orders
    @GetMapping(value = "/orders", produces = "application/json")
    public ResponseEntity<?> listAllCustomersAndOrders()
    {
        List<Customer> rtnList = customerServices.findAllCustomers();
        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/customers/customer/7
    // http://localhost:2019/customers/customer/77
    @GetMapping(value = "/customer/{custcode}", produces = "application/json")
    public ResponseEntity<?> listCustomerById(@PathVariable long custcode)
    {
        Customer rtnCust = customerServices.findCustomerById(custcode);
        return new ResponseEntity<>(rtnCust, HttpStatus.OK);
    }

    // http://localhost:2019/customers/namelike/mes
    // http://localhost:2019/customers/namelike/cin
    @GetMapping(value = "/namelike/{subname}", produces = "application/json")
    public ResponseEntity<?> listCustomersLikeName(@PathVariable String subname)
    {
        List<Customer> rtnList = customerServices.findCustomerLikeName(subname);
        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/customers/customer
    @PostMapping(value = "/customer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addNewCustomer(@Valid
                                            @RequestBody Customer newCustomer)
    {
        newCustomer.setCustcode(0);
        newCustomer = customerServices.save(newCustomer);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newCustomerURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{custcode}")
            .buildAndExpand(newCustomer.getCustcode())
            .toUri();
        responseHeaders.setLocation(newCustomerURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    // http://localhost:2019/customers/customer/19
    @PutMapping(value = "/customer/{custcode}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateFullCustomer(@PathVariable long custcode, @Valid @RequestBody Customer updateCustomer)
    {
        updateCustomer.setCustcode(custcode);
        updateCustomer = customerServices.save(updateCustomer);

        return new ResponseEntity<>(updateCustomer, HttpStatus.OK);
    }

    // http://localhost:2019/customers/customer/19
    @PatchMapping(value = "/customer/{custcode}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePartCustomer(@PathVariable long custcode, @RequestBody Customer updateCustomer)
    {
        updateCustomer = customerServices.update(updateCustomer, custcode);
        return new ResponseEntity<>(updateCustomer, HttpStatus.OK);
    }

    // http://localhost:2019/customers/customer/54
    @DeleteMapping(value = "/customer/{custcode}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable long custcode)
    {
        customerServices.delete(custcode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
