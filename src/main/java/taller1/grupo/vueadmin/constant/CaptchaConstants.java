package taller1.grupo.vueadmin.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: tarija
 * @description: captcha
 * @author: richard sivila
 * @create: 2024
 **/
@Getter
@Setter
public class CaptchaConstants {

    /**
     * Configuración del código de verificación (aritmética)
     */
    public static String codeType = "arithmetic";
    /**
     * Minutos del período de validez del código de verificación.
     */
    //public static Long expiration = 2L;
    public static Long expiration = 15L;
    /**
     * Longitud del contenido del código de verificación
     */
    public static int length = 2;
    /**
     * Ancho del código de verificación
     */
    public static int width = 111;
    /**
     * Altura del código de verificación
     */
    public static int height = 28;
    /**
     * Fuente del código de verificación
     */
    public static String fontName;
    /**
     * tamaño de fuente
     */
    public static int fontSize = 25;
}
