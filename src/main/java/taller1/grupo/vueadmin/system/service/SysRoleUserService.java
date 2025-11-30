package taller1.grupo.vueadmin.system.service;

import taller1.grupo.vueadmin.system.entity.SysRoleUser;

import java.util.List;

/**
 * @program: ems-admin-boot
 * @description: this is a interface
 * @author: richard sivila
 * @create: 2024
 **/
public interface SysRoleUserService {

    /**
     * @Description: Consultar usuarios vinculados por ID de rol
     * @Param: [roleId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRoleUser>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<SysRoleUser> getRoleUserByRoleId(Long roleId);

    /**
     * @Description:Modificar rol de usuario
     * @Param: [userId, roleIds]
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    void edit(Long userId, List<String> roleIds);

    /**
     * @Description: Consultar el rol de usuario actual
     * @Param: [userId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRoleUser>
     * @Author: starao
     * @Date: 2021/11/27
     */
    List<SysRoleUser> getRoleUserByUserId(Long userId);

    /**
     * @Description: Eliminar el enlace entre usuario y rol
     * @Param: [userId]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void deleteByUserId(Long userId);
}
