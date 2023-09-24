package abc.userservice.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
    @Id
    private String userId;

    @NotBlank(message = "user.name must be present")
    private String name;

    @Email(message = "user.email must be valid email id")
    private String email;

   // @Length(max = 10, min = 10, message = "phone should be 10 digits long ")
    private String phone;
}
