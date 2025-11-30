package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import taller1.grupo.vueadmin.system.entity.SysRoleUser;
import taller1.grupo.vueadmin.system.mapper.SysRoleUserMapper;
import taller1.grupo.vueadmin.system.service.SysRoleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Service
@RequiredArgsConstructor
public class SysRoleUserServiceImpl implements SysRoleUserService {

    private final SysRoleUserMapper roleUserMapper;

    /**
     * @param roleId
     * @Description: Consultar usuarios vinculados por ID de rol
     * @Param: [roleId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRoleUser>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<SysRoleUser> getRoleUserByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleUser::getRoleId, roleId);
        return roleUserMapper.selectList(wrapper);
    }

    /**
     * @param userId
     * @param roleIds
     * @Description: Modificar rol de usuario
     * @Param: [userId, roles]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    @Transactional
    public void edit(Long userId, List<String> roleIds) {
        // Primero borre todos los roles para este usuario
        LambdaQueryWrapper<SysRoleUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleUser::getUserId, userId);
        roleUserMapper.delete(wrapper);
        // Luego vincule al usuario al rol.
        roleIds.forEach(role -> {
            SysRoleUser roleUser = new SysRoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleId(Long.parseLong(role));
            roleUserMapper.insert(roleUser);
        });
    }

    /**
     * @param userId
     * @Description: Consultar el rol de usuario actual
     * @Param: [userId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRoleUser>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<SysRoleUser> getRoleUserByUserId(Long userId) {
        LambdaQueryWrapper<SysRoleUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleUser::getUserId, userId);
        return roleUserMapper.selectList(wrapper);
    }

    /**
     * @param userId
     * @Description: Eliminar el enlace entre usuario y rol
     * @Param: [id]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void deleteByUserId(Long userId) {
        LambdaQueryWrapper<SysRoleUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleUser::getUserId, userId);
        roleUserMapper.delete(wrapper);
    }
}
