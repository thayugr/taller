package taller1.grupo.vueadmin.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
public class StringUtil extends StringUtils {

    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    private static final String UNKNOWN = "unknown";

    /**
     * @Description: Obtener dirección IP de solicitud
     * @Param: [request]
     * @return: java.lang.String
     * @Author: richard sivila
     * @Date: 2024
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String localhost = "127.0.0.1";
        // toma el ip
        ip = ip.split(",")[0];
        if (localhost.equals(ip)) {
            // Consigue la real ip
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * @Description: Determinar el tipo de edición
     * @Param: [id]
     * @return: java.lang.String
     * @Author: richard sivila
     * @Date: 2024
     */
    public static String getEditType(Long id) {
        if (id != null) {
            return "Editado con éxito";
        } else {
            return "Agregado Exitosamente";
        }
    }
}
