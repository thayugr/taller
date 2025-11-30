package taller1.grupo.vueadmin.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import taller1.grupo.vueadmin.constant.CommonConstants;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.SecurityUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.system.entity.SysUser;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.dto.UserDto;
import taller1.grupo.vueadmin.system.mapper.SysUserMapper;
import taller1.grupo.vueadmin.system.service.SysRoleUserService;
import taller1.grupo.vueadmin.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import taller1.grupo.vueadmin.constant.CaptchaConstants;
import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;

    private final PasswordEncoder passwordEncoder;

    private final SysRoleUserService roleUserService;

    /**
     * @param userName
     * @Description: Consultar usuarios según el nombre de usuario
     * @Param: [userName]
     * @return: taller1.grupo.vueadmin.system.entity.SysUser
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Override
    public SysUser findByName(String userName) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, userName);
        return sysUserMapper.selectOne(wrapper);
    }

    /**
     * @param userDto
     * @Description: Editar usuario
     * @Param: [userDto]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void editUser(UserDto userDto) {
        checkUser(userDto);
        SysUser user = new SysUser();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setId(userDto.getId());
        user.setNickName(userDto.getNickName());
        if (userDto.getEnabled() != null) {
            user.setEnabled(userDto.getEnabled());
        }
        if (user.getId() != null) {
            sysUserMapper.updateById(user);
        } else {
            // Inicialice la contraseña del usuario. El valor predeterminado es 111111.
            // user.setEnabled((user.getEnabled().equals(false)) ? '0' : '1');
            user.setPassword(passwordEncoder.encode(CommonConstants.DEFAULT_PASSWORD));
            sysUserMapper.insert(user);
        }

        // Si tiene un rol, modifica el rol.
        if (!CollectionUtils.isEmpty(userDto.getRoleIds()) && user.getId() != null) {
            roleUserService.edit(user.getId(), userDto.getRoleIds());
        }
    }

    /**
     * @param queryDto
     * @Description: Consultar lista de usuarios
     * @Param: [blurry]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysUser>
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Override
    public IPage<UserDto> queryUserTable(QueryDto queryDto) {
        Page<UserDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return sysUserMapper.queryUserTable(page, queryDto.getBlurry());
    }

    /**
     * @param username
     * @Description: Consultar usuarios según el nombre de usuario
     * @Param: [username]
     * @return: taller1.grupo.vueadmin.system.entity.dto.UserDto
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public UserDto loadByName(String username) {
        return sysUserMapper.loadByName(username);
    }

    /**
     * @param id
     * @Description: Eliminar usuario
     * @Param: [id]
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Override
    @Transactional(rollbackFor = BadRequestException.class)
    public void delUser(Long id) {
        // Primero desvincule al usuario del rol
        roleUserService.deleteByUserId(id);
        // Eliminar el usuario nuevamente
        sysUserMapper.deleteById(id);
    }

    /**
     * @param sysUser
     * @Description: Modificar el estado del usuario
     * @Param: [sysUser]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void enabledUser(SysUser sysUser) {
        sysUserMapper.updateById(sysUser);
    }

    /**
     * @param jsonObject
     * @Description: Cambiar contraseña de usuario
     * @Param: [jsonObject]
     * @return: void
     * @Author: starao
     * @Date: 2022/10/6
     */
    @Override
    public void updatePassword(JSONObject jsonObject) {
        String password = jsonObject.getString("password");
        String newPassword = jsonObject.getString("newPassword");
        String confirmPassword = jsonObject.getString("confirmPassword");

        // Obtener el usuario actualmente conectado
        SysUser user = sysUserMapper.selectById(SecurityUtil.getCurrentUserId());
        String pwd = user.getPassword();
        // Verificar contraseña original
        if (!passwordEncoder.matches(password, pwd)) {
            throw new BadRequestException("La contraseña original es incorrecta.，Por favor vuelve a entrar");
        }
        // Verificar nueva contraseña y confirmar contraseña
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException(
                    "La nueva contraseña es diferente de la contraseña confirmada.，Por favor vuelve a entrar");
        }
        // Cambiar contraseña a nueva contraseña
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserMapper.updateById(user);
    }

    /**
     * @Description:Verificar nombre de usuario y apodo
     * @Param: [userDto]
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    private void checkUser(UserDto userDto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (userDto.getId() != null) {
            wrapper.ne(SysUser::getId, userDto.getId());
        }
        if (StringUtil.isNotBlank(userDto.getUsername()) && StringUtil.isNotBlank(userDto.getNickName())) {
            wrapper.and(w -> w.eq(SysUser::getUsername, userDto.getUsername()).or().eq(SysUser::getNickName,
                    userDto.getNickName()));
        }
        long count = sysUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("El nombre de usuario o apodo ya existe，Por favor vuelve a entrar");
        }
    }
}
