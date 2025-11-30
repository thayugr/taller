package taller1.grupo.vueadmin.config.security;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.dto.JwtUserDto;
import taller1.grupo.vueadmin.system.entity.dto.UserDto;
import taller1.grupo.vueadmin.system.service.SysRoleService;
import taller1.grupo.vueadmin.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService userService;

    private final SysRoleService roleService;

    /**
     * @Description: Obtener información de usuario por nombre de usuario
     * @Param: [s]
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean searchDb = true;
        JwtUserDto jwtUserDto = null;
        if (searchDb) {
            UserDto user;
            try {
                user = userService.loadByName(username);
            } catch (BadRequestException e) {
                throw new UsernameNotFoundException("Nombre de usuario o contraseña incorrectos", e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("Nombre de usuario o contraseña incorrectos");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("La cuenta no está activada！");
                }
                jwtUserDto = new JwtUserDto(
                        user,
                        roleService.getRolesByUser(user.getId()));
            }
        }
        return jwtUserDto;
    }
}
