package helloworlds.MongoDB.controller;

import helloworlds.MongoDB.entities.Customer;
import helloworlds.MongoDB.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.FeatureDescriptor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomer() {
        try {
            List<Customer> customers = customerRepository.findAll();
            if (customers != null) {
                System.out.println(customers);
            } else {
                System.out.println("No Custgmers");
            }
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        try {
            customerRepository.save(customer);
            return ResponseEntity.ok("Customer Saved");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        try {
            Customer existingCustomer = customerRepository.findCustomerById(id);
            if (existingCustomer == null) {
                return ResponseEntity.notFound().build(); // Customer not found
            }

            // Update the existing customer with new values if provided
            if (customer.getFirstName() != null) {
                existingCustomer.setFirstName(customer.getFirstName());
            }
            if (customer.getLastName() != null) {
                existingCustomer.setLastName(customer.getLastName());
            }

            customerRepository.save(existingCustomer); // Save the updated customer
            return ResponseEntity.ok("Customer Updated");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update2/{id}")
    public ResponseEntity<?> update2Customer(@PathVariable String id, @RequestBody Customer updatedCustomer){
        try{
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (!optionalCustomer.isPresent()) {
                return ResponseEntity.notFound().build(); // Customer not found
            }

            Customer existingCustomer = optionalCustomer.get();

            // Copy non-null properties from updatedCustomer to existingCustomer
            BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));

            customerRepository.save(existingCustomer); // Save the updated customer
            return ResponseEntity.ok("Customer Updated");
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update3/{id}")
    public ResponseEntity<?> update3Customer(@PathVariable String id, @RequestBody Customer updatedCustomer){
        try{
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update();

            return ResponseEntity.ok("Customer Updated");
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id){
        try{
            customerRepository.deleteById(id);
            return ResponseEntity.ok("Cusotmer Deleted");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Stream.of(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> src.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}

