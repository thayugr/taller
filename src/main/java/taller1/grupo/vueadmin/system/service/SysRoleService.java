package taller1.grupo.vueadmin.system.service;

import com.alibaba.fastjson.JSONArray;
import taller1.grupo.vueadmin.system.entity.SysRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @program: ems-admin-boot
 * @description: this is a interface
 * @author: starao
 * @create: 2021-11-27 14:26
 **/
public interface SysRoleService {

    /**
     * @Description: Obtener lista de roles
     * @Param: [blurry]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRole>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<SysRole> getRoleList(String blurry);

    /**
     * @Description: Editar rol
     * @Param: [role]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void editRole(SysRole role);

    /**
     * @Description: Eliminar rol
     * @Param: [id]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void delRole(Long id);

    /**
     * @Description: Lista de selección de roles
     * @Param: [userId]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: richard sivila
     * @Date: 2024
     */
    JSONArray getAllRoleForXm(Long userId);

    /**
     * @Description: Obtener el rol de usuario actual por ID de usuario
     * @Param: [userId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRole>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<SysRole> getRoleByUserId(Long userId);

    /**
     * @Description: Obtener la lista de funciones actuales del usuario
     * @Param: [id]
     * @return: java.util.List<org.springframework.security.core.GrantedAuthority>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<GrantedAuthority> getRolesByUser(Long id);
}
