package taller1.grupo.vueadmin.common.constant;

/**
 * @program: tarija
 * @description: captcha
 * @author: richard sivila
 * @create: 2024
 **/

public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("No se puede crear una clase constante estática");
    }

    /**
     * Acceso url
     */
    public static final String AUTH_LOGIN_URL = "/auth/login";

    /**
     * JWTClave de firma, usada512-bit Encryption key
     * Direccion https://www.allkeysgenerator.com/
     */
    public static final String JWT_SECRET_KEY = "B?E(H+MbQeShVmYq3t6w9z$C&F)J@NcRfUjWnZr4u7x!A%D*G-KaPdSgVkYp3s5v";

    /**
     * Actualizar clave de refresco_token
     */
    public static final String JWT_REFRESH_KEY = "q3t6w9z$C&F)H@McQfTjWnZr4u7x!A%D*G-KaNdRgUkXp2s5v8y/B?E(H+MbQeSh";

    /**
     * token Prefijo, utilizado en encabezados de solicitud Authorization
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Encabezado de solicitud
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * tokenTipo
     */
    public static final String TOKEN_TYPE = "JWT";

    /**
     * Declarar rol de usuario
     */
    public static final String TOKEN_ROLE_CLAIM = "role";

    /**
     * tokenID del Emisor
     */
    public static final String TOKEN_ISSUER = "security";

    /**
     * tokenCobertura
     */
    public static final String TOKEN_AUDIENCE = "security-all";

    /**
     * token Válido por 2 horas (cuando recordar es falso)
     */
    public static final Long TOKEN_EXPIRATION_TIME = 60 * 60 * 2L;

    /**
     * tokenEl período de validez es de 7 días (cuando recordar es TRUE)
     */
    public static final Long TOKEN_EXPIRATION_REMEMBER_TIME = 60 * 60 * 24 * 7L;
}
