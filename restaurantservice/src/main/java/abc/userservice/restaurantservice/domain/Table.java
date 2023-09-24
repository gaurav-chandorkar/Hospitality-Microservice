package abc.userservice.restaurantservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

//@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {

    @Id
    private String tableId;

    private Integer chairCapacity;

    private boolean isAvailable;

    private String bookingDate;
}
