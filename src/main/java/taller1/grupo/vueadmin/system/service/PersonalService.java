package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import taller1.grupo.vueadmin.system.entity.dto.PersonaUpsertDTO;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.vo.PersonaVO;

public interface PersonalService {
  PersonaVO getByUserId(Long idusuario);
  PersonaVO create(PersonaUpsertDTO dto);
  PersonaVO update(Long idusuario, PersonaUpsertDTO dto);
  void delete(Long idusuario);
  IPage<PersonaUpsertDTO> queryPersonalTable(QueryDto queryDto);
}

