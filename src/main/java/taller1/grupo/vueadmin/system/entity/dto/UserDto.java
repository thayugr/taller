package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Setter
@Getter
public class UserDto {

    private Long id;

    private String username;

    private String nickName;

    private String email;

    private String password;

    private List<String> roles;

    private Boolean enabled;

    private List<String> roleIds;

    private String uuid;

    private String code;
}
