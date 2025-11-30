package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import taller1.grupo.vueadmin.system.entity.SysUser;
import taller1.grupo.vueadmin.system.entity.dto.UserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @program: tairja
 * @description: this is a interface
 * @author: richard sivila
 * @create: 2024
 **/
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * @Description: Obtener objeto de usuario por nombre de usuario
     * @Param: [username]
     * @return: taller1.grupo.vueadmin.system.entity.dto.UserDto
     * @Author: starao
     * @Date: 2024
     */
    UserDto loadByName(@Param("username") String username);

    /**
     * @Description: Consultar lista de usuarios
     * @Param: [blurry]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.dto.UserDto>
     * @Author: starao
     * @Date: 2021/12/4
     */
    IPage<UserDto> queryUserTable(Page<?> page, @Param("blurry") String blurry);
}
