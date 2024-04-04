package helloworlds.MongoDB.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("customer")
public class Customer {
    @Id
    public String id;
    public String firstName;
    public String lastName;



}