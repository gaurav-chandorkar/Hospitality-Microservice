package abc.userservice.bookingservice.domain.restaurant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Restaurant {

    @Id
    private String restaurantId;

    @NotBlank(message = "restaurant.name must not be blank")
    private String name;

    @NotBlank(message = "restaurant.city must not be blank")
    private String city;

    private Integer stars;

    List<Table> tables;

}
