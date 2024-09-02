package com.user.UserService.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;


@Entity
@Table
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private LocalTime timeOfRegistration;
    private Boolean enabled;

   // @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
   // @JsonIgnore
    private Country country;
    @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "region_id")
   // @JsonIgnore
    private Region region;
//   public void setCountry(String c){
//          country.setName(c);
//       }

//    @PrePersist
//    protected void onCreate() {
//        if (timeOfRegistration == null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            timeOfRegistration = (LocalDate.now().format(formatter)); // Store the current date in dd-MM-yyyy format
//        }
//    }
//    public void setCountry(Long id,String name){
//         country.setId(id);
//         country.setName(name);
//    }
}

