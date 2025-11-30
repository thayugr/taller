package taller1.grupo.vueadmin.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @program: ems-admin-boot
 * @description: this is a class
 * @author: starao
 * @create: 2021-11-27 13:09
 **/
public class ThrowsUtil {

    /**
     * @Description: Obtener información de la pila de errores
     * @Param: [throwable]
     * @return: java.lang.String
     * @Author: richard sivila
     * @Date: 2024
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
