package taller1.grupo.vueadmin.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
public class RequestHolder {

    /**
     * @Description: ConseguirhttpRequest
     * @Param: []
     * @return: javax.servlet.http.HttpServletRequest
     * @Author: starao
     * @Date: 2021/11/27
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }
}
