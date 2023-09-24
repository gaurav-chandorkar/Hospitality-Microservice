package abc.userservice.bookingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class Booking {

    @Id
    private String bookingId;

    private String restaurantId;

    private String tableId;

    private String userId;

    private String bookingDate;
}
