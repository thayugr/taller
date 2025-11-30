package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import taller1.grupo.vueadmin.system.entity.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: tarija
 * @description: this is a interface
 * @author: richard sivila
 * @create: 2024
 **/
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * @Description: Obtener árbol de menú
     * @Param: [roles]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysMenu>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<SysMenu> getMenuTree(@Param("roles") List<String> roles);

    /**
     * @Description: Obtenido por rol url
     * @Param: [roles]
     * @return: java.util.List<java.lang.String>
     * @Author: richard sivila
     * @Date: 2024
     */
    List<String> getMenuUrlByRole(@Param("roles") List<String> roles);

    /**
     * @Description: Obtener lista de permisos
     * @Param: [roles]
     * @return: java.util.List<java.lang.String>
     * @Author: richard sivila
     * @Date: 2022/10/6
     */
    List<String> getPermission(@Param("roles") List<String> roles);
}
