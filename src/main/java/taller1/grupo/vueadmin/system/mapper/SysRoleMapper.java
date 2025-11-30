package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import taller1.grupo.vueadmin.system.entity.SysRole;
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
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * @Description: Obtener el rol de usuario actual por ID de usuario
     * @Param: [userId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRole>
     * @Author: richard sivila
     * @Date: 2021/11/29
     */
    List<SysRole> getRoleByUserId(@Param("userId") Long userId);
}
