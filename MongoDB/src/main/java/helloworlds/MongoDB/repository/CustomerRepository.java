package helloworlds.MongoDB.repository;

import helloworlds.MongoDB.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@EnableMongoRepositories
public interface CustomerRepository extends MongoRepository<Customer,String> {
    Customer findCustomerById(String id);
}
