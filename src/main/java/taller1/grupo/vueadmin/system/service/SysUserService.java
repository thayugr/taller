package taller1.grupo.vueadmin.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.SysUser;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.dto.UserDto;

/**
 * @program: tarija
 * @description: this is a interface
 * @author: richard sivila
 * @create: 2024
 **/
public interface SysUserService {

    /**
     * @Description: Consultar usuarios según el nombre de usuario
     * @Param: [userName]
     * @return: taller1.grupo.vueadmin.system.entity.SysUser
     * @Author: richard sivila
     * @Date: 2024
     */
    SysUser findByName(String userName);

    /**
     * @Description: Editar usuario
     * @Param: [userDto]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void editUser(UserDto userDto);

    /**
     * @Description: Consultar lista de usuarios
     * @Param: [queryDto]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysUser>
     * @Author: starao
     * @Date: 2024
     */
    IPage<UserDto> queryUserTable(QueryDto queryDto);

    /**
     * @Description: Consultar usuarios según el nombre de usuario
     * @Param: [username]
     * @return: taller1.grupo.vueadmin.system.entity.dto.UserDto
     * @Author: richard sivila
     * @Date: 2024
     */
    UserDto loadByName(String username);

    /**
     * @Description: Eliminar usuario
     * @Param: [id]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void delUser(Long id);

    /**
     * @Description: Modificar el estado del usuario
     * @Param: [sysUser]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void enabledUser(SysUser sysUser);

    /**
     * @Description: Cambiar contraseña de usuario
     * @Param: [jsonObject]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    void updatePassword(JSONObject jsonObject);
}
