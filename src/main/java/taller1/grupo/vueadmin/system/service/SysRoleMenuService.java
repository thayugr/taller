package taller1.grupo.vueadmin.system.service;

import taller1.grupo.vueadmin.system.entity.SysRoleMenu;
import taller1.grupo.vueadmin.system.entity.dto.RoleMenuDto;

import java.util.List;

/**
 * @program: tarija
 * @description: this is a interface
 * @author: richard sivila
 * @create: 2024
 **/
public interface SysRoleMenuService {

    /**
     * @Description: Obtenga el menú correspondiente por ID de personaje
     * @Param: [roleId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRoleMenu>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<SysRoleMenu> getMenuByRoleId(Long roleId);

    /**
     * @Description: Menú de rol autorizado
     * @Param: [roleMenuDto]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void editMenuRoleByRoleId(RoleMenuDto roleMenuDto);

    /**
     * @Description: Eliminar menús vinculados a caracteres
     * @Param: [roleId]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void deleteByRoleId(Long roleId);
}
