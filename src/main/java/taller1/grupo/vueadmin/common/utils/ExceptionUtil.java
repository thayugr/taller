package taller1.grupo.vueadmin.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @program: tarija
 * @description: Obtenga información detallada sobre errores
 * @author: richard sivila
 * @create: 2024
 **/
public class ExceptionUtil {

    public static String getStackTraceInfo(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);// Envíe la información de la pila de errores a printWriter
            pw.flush();
            sw.flush();

            return sw.toString();
        } catch (Exception ex) {

            return "Se produjo un error";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

    }
}
