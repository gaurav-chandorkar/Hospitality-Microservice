package abc.userservice.restaurantservice.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    private String restaurantId;

    @NotBlank(message = "restaurant.name must not be blank")
    private String name;

    @NotBlank(message = "restaurant.city must not be blank")
    private String city;

  //  @Length(min = 1, max = 5, message = "restaurant.stars must be between 1 to 5")
    private Integer stars;

    //@DocumentReference
    List<Table> tables;

}
