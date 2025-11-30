package taller1.grupo.vueadmin.config.filter;

import taller1.grupo.vueadmin.common.constant.SecurityConstants;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.JwtUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    /**
     * @Description: Filtrar solicitudes de usuarios
     * @Param: [request, response, filterChain]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("solicitar ruta:" + request.getRequestURI());
            // De request Entra token
            String token = this.getTokenFromHttpServletRequest(request);
            // Si el token no existe o lleva un token de actualización (la longitud es
            // inferior a 150, puede juzgar según el token de actualización que generó),
            // Liberelo directamente y la seguridad del sistema determinará si tiene
            // derechos de acceso.
            if (StringUtil.isBlank(token) || token.length() < 150) {
                filterChain.doFilter(request, response);
                return;
            }
            // Verificar si el token es válido
            if (JwtUtil.verifyToken(token)) {
                // Obtener información de certificación
                Authentication authentication = JwtUtil.getAuthentication(token);
                // Guarde la información de autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Este es el requisito para la configuración no separada del front-end y
                // back-end después de la implementación. Si se trata de una implementación con
                // front-end y back-end separados (es decir, usando nginx para la
                // implementación, puede eliminar este bloque de código). )
                if (request.getRequestURI().startsWith("/api/")) {
                    // Modifique la ruta de solicitud y elimine el prefijo "/api/"
                    String newRequestURI = request.getRequestURI().substring(4);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(newRequestURI);
                    dispatcher.forward(request, response);
                } else {
                    // solicitud de liberacion
                    filterChain.doFilter(request, response);
                }
            }
        } catch (BadRequestException e) {
            // token Los problemas se tratarán de manera uniforme con 401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    /**
     * @Description: Obtener token de http
     * @Param: [request]
     * @return: java.lang.String
     * @Author: richard sivila
     * @Date: 2024
     */
    private String getTokenFromHttpServletRequest(HttpServletRequest request) {
        // Obtener token mediante autorización
        String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
        }
        return null;
    }
}
