package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import taller1.grupo.vueadmin.constant.CaptchaConstants;
import taller1.grupo.vueadmin.constant.CommonConstants;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.SysRole;
import taller1.grupo.vueadmin.system.entity.SysRoleMenu;
import taller1.grupo.vueadmin.system.entity.dto.RoleMenuDto;
import taller1.grupo.vueadmin.system.mapper.SysRoleMapper;
import taller1.grupo.vueadmin.system.mapper.SysRoleMenuMapper;
import taller1.grupo.vueadmin.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    private final SysRoleMenuMapper roleMenuMapper;

    private final SysRoleMapper roleMapper;

    /**
     * @param roleId
     * @Description: Obtenga el menú correspondiente por ID de personaje
     * @Param: [roleId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRoleMenu>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<SysRoleMenu> getMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        return roleMenuMapper.selectList(wrapper);
    }

    /**
     * @param roleMenuDto
     * @Description: Menú de rol autorizado
     * @Param: [roleMenuDto]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    @Transactional
    public void editMenuRoleByRoleId(RoleMenuDto roleMenuDto) {
        Long roleId = roleMenuDto.getRoleId();
        // Obtener información de rol actual
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BadRequestException("El rol actual no existe.");
        }
        if (CommonConstants.ROLE_ADMIN.equals(role.getRoleCode())) {
            throw new BadRequestException(
                    "El superadministrador tiene todos los permisos y no se requiere autorización");
        }
        // 1 Primero borre todos los menús de caracteres anteriores
        deleteByRoleId(roleId);
        // 2Recorre todos los menús
        if (!CollectionUtils.isEmpty(roleMenuDto.getMenuIds())) {
            roleMenuDto.getMenuIds().forEach(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setMenuId(menuId);
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
            });
        }
    }

    /**
     * @param roleId
     * @Description: Eliminar menús vinculados a caracteres
     * @Param: [roleId]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
    }
}
