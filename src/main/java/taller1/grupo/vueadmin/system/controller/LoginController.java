package taller1.grupo.vueadmin.system.controller;

import taller1.grupo.vueadmin.common.constant.SecurityConstants;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.JwtUtil;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.config.config.CacheConfig;
import taller1.grupo.vueadmin.config.security.JwtUser;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.SysRole;
import taller1.grupo.vueadmin.system.entity.SysUser;
import taller1.grupo.vueadmin.system.entity.dto.UserDto;
import taller1.grupo.vueadmin.system.service.SysRoleService;
import taller1.grupo.vueadmin.system.service.SysUserService;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.constant.CaptchaConstants;

import java.util.*;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2021-11-27 14:08
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController extends ResultUtil {

    private final SysUserService userService;

    private final SysRoleService roleService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CacheConfig cacheConfig;

    @Log("Inicio de sesión de usuario")
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        try {
            // Verificar código de verificación
            if (StringUtil.isBlank(userDto.getCode()) || !checkCode(userDto.getUuid(), userDto.getCode())) {
                return fail(false, "Error del código de verificación");
            }
            // Compruebe si el usuario existe según el nombre de usuario
            SysUser user = userService.findByName(userDto.getUsername());
            System.out.println("contrasena:" + user.getPassword());
            if (user == null) {
                return fail(false, "Nombre de usuario o contraseña incorrectos");
            }
            // Determinar si la contraseña es correcta
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDto.getUsername(), userDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // Establecer información de autenticación en el contexto SpringSecurity
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Obtener rol de usuario actual
            List<String> roles = getRolesByUserId(user.getId());
            // Generartoken
            String token = JwtUtil.generateToken(user.getUsername(), roles, false);
            // Generarrefresh_token
            String refreshToken = JwtUtil.getRefreshToken(user.getUsername());

            // Información del Usuario
            userDto.setEmail(user.getEmail());
            userDto.setNickName(user.getNickName());
            userDto.setRoles(roles);
            // Constraseña oculta
            userDto.setPassword("******");

            return success(true, new JwtUser(token, refreshToken, userDto));
        } catch (BadRequestException e) {
            e.printStackTrace();
            return fail(false, e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return fail(false, e.getMessage());
        }
    }

    /**
     * @Description: registro de usuario
     * @Param: [userDto]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Registro de Usuario")
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.editUser(userDto);
            return success(true, "Registro Exitoso");
        } catch (BadRequestException e) {
            e.printStackTrace();
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: AprobarrefreshTokenConseguirNuevo token
     * @Param: [request]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @PutMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        try {
            // Obtenido a través del encabezado de la solicitudrefreshToken
            String refreshToken = request.getHeader(SecurityConstants.TOKEN_HEADER);
            // Si refresh Token Existir
            if (StringUtil.isNotBlank(refreshToken)) {
                // Retire el encabezado y obtenga el original. refreshToken
                refreshToken = refreshToken.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");
                // Analizar refrescoToken y obtener el nombre de usuario
                Claims claims = JwtUtil.getRefreshTokenBody(refreshToken);
                System.out.println(claims.getSubject());
                // Si el refrescoToken sigue siendo válido
                if (claims.get("exp", Long.class) > 0) {
                    // Obtener información de usuario actual
                    SysUser user = userService.findByName(claims.getSubject());
                    // Obtener rol de usuario actual
                    List<String> roles = getRolesByUserId(user.getId());
                    // reAdquirir token
                    String token = JwtUtil.generateToken(user.getUsername(), roles, false);
                    return success(true, token);
                }
            }
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
        return fail(false, "Por favor inicia sesión nuevamente");
    }

    /**
     * @Description: Obtener código de verificación
     * @Param: []
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @GetMapping("/code")
    public ResponseEntity<Object> getVerifyCode() {
        try {
            // Obtener el resultado de la operación.
            Captcha captcha = new ArithmeticCaptcha(CaptchaConstants.width, CaptchaConstants.height);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            // Cuando el tipo captcha es aritmético y la longitud es >= 2, el resultado de
            // captcha.text() puede ser de tipo coma flotante.
            String captchaValue = captcha.text();
            if (captchaValue.contains(".")) {
                captchaValue = captchaValue.split("\\.")[0];
            }
            // Información del código de verificación de caché durante 1 minuto
            cacheConfig.put(uuid, captchaValue, 1);
            // Información del código de verificación
            Map<String, Object> imgResult = new HashMap<String, Object>(2) {
                {
                    put("img", captcha.toBase64());
                    put("uuid", uuid);
                }
            };
            return ResponseEntity.ok(imgResult);
        } catch (BadRequestException e) {
            e.printStackTrace();
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Obtener la lista de roles de usuario actual
     * @Param: [userId]
     * @return: java.util.List<java.lang.String>
     * @Author: starao
     * @Date: 2024
     */
    private List<String> getRolesByUserId(Long userId) {
        // Obtener el objeto de rol de usuario actual
        List<SysRole> sysRoles = roleService.getRoleByUserId(userId);
        // Role
        List<String> roles = new ArrayList<>();
        sysRoles.forEach(sysRole -> {
            roles.add(sysRole.getRoleCode());
        });
        return roles;
    }

    /**
     * @Description: Verificación del código de verificación.
     * @Param: [uuid, code]
     * @return: boolean
     * @Author: starao
     * @Date: 2024
     */
    private boolean checkCode(String uuid, String code) {
        boolean b = false;
        if (cacheConfig.get(uuid) != null && cacheConfig.get(uuid).equals(code)) {
            b = true;
            cacheConfig.invalidate(uuid);
        }
        return b;
    }
}
