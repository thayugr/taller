package taller1.grupo.vueadmin.common.utils;

import taller1.grupo.vueadmin.common.constant.SecurityConstants;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: tarija
 * @description: captcha
 * @author: richard sivila
 * @create: 2024
 **/

@Slf4j
public class JwtUtil {

    private static final byte[] secretKey = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);

    private static final byte[] refreshKey = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_REFRESH_KEY);

    private JwtUtil() {
        throw new IllegalStateException("Deshabilitar la creación del objeto actual");
    }

    /**
     * @Description: Generado en función del usuario y el rol del usuario. token
     * @Param: [userName, roles, isRemember]
     * @return: java.lang.String
     * @Author: richard sivila
     * @Date: 2024
     */
    public static String generateToken(String userName, List<String> roles, Boolean isRemember) {
        try {
            // tiempo de vencimiento
            long expirationTime = isRemember ? SecurityConstants.TOKEN_EXPIRATION_REMEMBER_TIME
                    : SecurityConstants.TOKEN_EXPIRATION_TIME;

            // generar token
            return Jwts.builder()
                    // Generar información de visa
                    .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                    .signWith(Keys.hmacShaKeyFor(secretKey))
                    // todos
                    .setSubject(userName)
                    // Role
                    .claim(SecurityConstants.TOKEN_ROLE_CLAIM, roles)
                    // JWTcuerpo principal
                    .setIssuer(SecurityConstants.TOKEN_ISSUER)
                    // tiempo de emision
                    .setIssuedAt(new Date())
                    .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                    // Establecer hora valida
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                    .compact();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * @Description: Generar refresh_token
     * @Param: [userName]
     * @return: java.lang.String
     * @Author: starao
     * @Date: 2024
     */
    public static String getRefreshToken(String userName) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(refreshKey))
                .setSubject(userName)
                .setExpiration(
                        new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION_REMEMBER_TIME * 1000))
                .compact();
    }

    /**
     * @Description: Verificar si el token es válido
     * @Param: [token]
     * @return: boolean
     * @Author: starao
     * @Date: 2024
     */
    public static boolean verifyToken(String token) {
        try {
            getTokenBody(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("token Vencio : {} failed : {}", token, e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("token No Compatible : {} failed : {}", token, e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("token Formato Incorrecto : {} failed : {}", token, e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (SignatureException e) {
            log.warn("token Firma no valida : {} failed : {}", token, e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("token No puede estar vacio : {} failed : {}", token, e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * @Description:Obtener información de autenticación de usuario basada en token
     * @Param: [token]
     * @return: org.springframework.security.core.Authentication
     * @Author: starao
     * @Date: 2024
     */
    public static Authentication getAuthentication(String token) {
        Claims claims;
        try {
            claims = getTokenBody(token);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            claims = e.getClaims();
        }
        // Obtener rol de usuario
        List<String> roles = (List<String>) claims.get(SecurityConstants.TOKEN_ROLE_CLAIM);
        List<SimpleGrantedAuthority> authorities = CollectionUtils.isEmpty(roles)
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        // Obtener nombre de usuario
        User principal = new User(claims.getSubject(), "******", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * @Description: Obtener información del usuario del token
     * @Param: [token]
     * @return: io.jsonwebtoken.Claims
     * @Author: starao
     * @Date: 2021/11/27
     */
    private static Claims getTokenBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @Description: Obtener información del usuario de refresco Token
     * @Param: [refreshToken]
     * @return: io.jsonwebtoken.Claims
     * @Author: richard sivila
     * @Date: 2024
     */
    public static Claims getRefreshTokenBody(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
    }
}
