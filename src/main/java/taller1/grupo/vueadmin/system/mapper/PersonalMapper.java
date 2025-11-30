package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import taller1.grupo.vueadmin.system.entity.Personal;
import taller1.grupo.vueadmin.system.entity.dto.PersonaUpsertDTO;

@Mapper
public interface PersonalMapper extends BaseMapper<Personal> {

    @Select("""
        SELECT
      p.idusuario,
      p.nombre,
      p.ap,
      p.am,
      p.estado,
      p.fnac,
      p.ecivil,
      p.genero,
      p.dir,
      p.telf,
      p.tipo,
      p.foto,
      d.cedula
    FROM academico.personal p
    LEFT JOIN academico.datos d ON d.cod = p.idusuario
        """)
    IPage<PersonaUpsertDTO> queryPersonalTable(Page<?> page, @Param("blurry") String blurry);
}



