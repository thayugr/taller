package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.vo.PersonaVO;

@Mapper
public interface PersonalExtMapper {

  // Trae una persona combinada (personal + datos) por idusuario
  PersonaVO selectPersonaByUserId(@Param("idusuario") Long idusuario);

  // Paginación de personas (JOIN personal + datos) con filtro "blurry"
  IPage<PersonaVO> pagePersonas(Page<?> page,
                                @Param("blurry") String blurry);
}

