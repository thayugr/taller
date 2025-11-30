package taller1.grupo.vueadmin.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.SysUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
public class SecurityUtil {

    /**
     * @Description: Obtener el usuario actualmente conectado
     * @Param: []
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @Author: starao
     * @Date: 2024
     */
    public static UserDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ;
        if (authentication == null) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "El estado de inicio de sesión actual expiró");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailsService userDetailsService = SpringContextHolder.getBean(UserDetailsService.class);
            return userDetailsService.loadUserByUsername(userDetails.getUsername());
        }
        throw new BadRequestException(HttpStatus.UNAUTHORIZED,
                "No se puede encontrar la información de inicio de sesión actual");
    }

    /**
     * @Description: Obtener nombre de usuario del sistema
     * @Param: []
     * @return: java.lang.String
     * @Author: starao
     * @Date: 2024
     */
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "El estado de inicio de sesión actual expiró");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * @Description: Obtener usuario del sistemaID
     * @Param: []
     * @return: java.lang.String
     * @Author: starao
     * @Date: 2021/11/27
     */
    public static String getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        if (userDetails != null) {
            return JSON.parseObject(JSONObject.toJSONString(userDetails)).getJSONObject("user").getString("id");
        } else {
            return null;
        }
    }

    /**
     * @Description: Consultar la lista de roles de usuario del sistema
     * @Param: []
     * @return: java.util.List<java.lang.String>
     * @Author: richard sivila
     * @Date: 2024
     */
    public static List<String> getCurrentRoles() {
        UserDetails userDetails = getCurrentUser();
        List<String> list = new ArrayList<>();
        if (userDetails != null) {
            userDetails.getAuthorities().forEach((item) -> {
                list.add(item.toString());
            });
        }
        return list;
    }
}
