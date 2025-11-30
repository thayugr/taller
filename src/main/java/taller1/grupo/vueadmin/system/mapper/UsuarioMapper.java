package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import taller1.grupo.vueadmin.system.entity.Personal;
import taller1.grupo.vueadmin.system.entity.dto.QueryUsuarioDto; 
import taller1.grupo.vueadmin.system.entity.SysUser; // Importar SysUser

@Repository
public interface UsuarioMapper extends BaseMapper<Personal> {

// Método de consulta principal con paginación y filtros
@Select({
"<script>",
"SELECT p.idusuario, d.cedula, p.nombre, p.ap, p.am, p.estado, to_date(p.fnac::text, 'YYYY-MM-DD') AS fnac, p.ecivil, p.genero, p.dir, p.telf, p.tipo, p.foto ",
"FROM academico.personal p",
"LEFT JOIN academico.datos d ON p.idusuario = d.cod",
// REEMPLAZA "WHERE 1=1" Y LOS ESPACIOS CON LA ETIQUETA <where>
"<where>",
// Filtro de Búsqueda (blurry) - <where> se encarga del primer AND si se activa
"<if test='dto.blurry != null and dto.blurry != \"\"'>",
" (p.nombre ILIKE '%' || #{dto.blurry} || '%' ",
" OR p.ap ILIKE '%' || #{dto.blurry} || '%' ",
" OR p.am ILIKE '%' || #{dto.blurry} || '%')",
"</if>",
// Filtro por Estado (status) - Debe llevar AND si no es la primera condición
"<if test='dto.status != null'>",
"  AND p.estado = #{dto.status}",
"</if>",
 
// Filtro por Tipo de Personal (type) - Debe llevar AND si no es la primera condición
"<if test='dto.type != null and dto.type != \"\"'>",
 "  AND p.tipo = #{dto.type}",
"</if>",
 
 "</where>", // CIERRA EL BLOQUE WHERE

 "ORDER BY p.idusuario DESC",
"</script>"
 })
IPage<Personal> queryUsuarioTable(Page<?> page, @Param("dto") QueryUsuarioDto queryDto);

// Método auxiliar para obtener usuarios de sys_user que AÚN NO tienen registro en personal
@Select({
"SELECT id, username FROM public.sys_user ",
"WHERE id NOT IN (SELECT idusuario FROM academico.personal)"
 })
// Se retorna List<SysUser> o un DTO simple con id y username
java.util.List<SysUser> findUnassignedSysUsers(); 
}