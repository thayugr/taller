// Archivo: src/main/java/taller1/grupo/vueadmin/system/service/UsuarioService.java

package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Personal;
import taller1.grupo.vueadmin.system.entity.dto.QueryUsuarioDto;
import taller1.grupo.vueadmin.system.entity.dto.UsuarioDto;
import taller1.grupo.vueadmin.system.entity.SysUser;

import java.util.List;

public interface UsuarioService {

    IPage<Personal> queryUsuarioTable(QueryUsuarioDto queryDto);

    /**
     * Inserta o modifica los datos de Personal y Datos.
     * @param usuarioDto DTO con los datos para Upsert.
     */
    void editUsuario(UsuarioDto usuarioDto);
    
    /**
     * Obtiene la lista de usuarios del sistema (sys_user) que no tienen registro en academico.personal.
     * @return Lista de SysUser (id, username).
     */
    List<SysUser> getUnassignedSysUsers();
    
    /**
     * Obtiene un registro completo de Personal por ID.
     * @param idusuario ID del usuario.
     * @return UsuarioDto con todos los datos combinados.
     */
    UsuarioDto getUsuarioDetails(Long idusuario);
}