package abc.userservice.bookingservice.domain.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

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
