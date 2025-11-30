package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO2 {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    private String city;
    private String address;
}