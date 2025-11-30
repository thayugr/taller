package taller1.grupo.vueadmin.config.security;

import taller1.grupo.vueadmin.system.entity.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Getter
@Setter
public class JwtUser {

    private UserDto userDto;

    private String token;

    private String refreshToken;

    /**
     * @Description: Personalice el objeto de usuario devuelto después de iniciar
     *               sesión
     * @Param: [token, userDto]
     * @return:
     * @Author: richard sivila
     * @Date: 2024
     */
    public JwtUser(String token, String refreshToken, UserDto userDto) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userDto = userDto;
    }
}
