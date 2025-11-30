package taller1.grupo.vueadmin.system.service.impl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import taller1.grupo.vueadmin.system.entity.*;
import taller1.grupo.vueadmin.system.entity.dto.ClientesDto;
import taller1.grupo.vueadmin.system.entity.dto.PersonaUpsertDTO;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.vo.PersonaVO;
import taller1.grupo.vueadmin.system.mapper.*;
import taller1.grupo.vueadmin.system.service.PersonalService;

@Service
public class PersonalServiceImpl implements PersonalService {

  @Resource private PersonalMapper personalMapper;
  @Resource private DatosMapper datosMapper;
  @Resource private PersonalExtMapper personalExtMapper;
  @Resource private SysUserMapper sysUserMapper; // existente en tu proyecto

  @Override 
  public IPage<PersonaUpsertDTO> queryPersonalTable(QueryDto queryDto) {
       Page<PersonaUpsertDTO> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return personalMapper.queryPersonalTable(page, queryDto.getBlurry());
  }
  @Override
  public PersonaVO getByUserId(Long idusuario) {
    return personalExtMapper.selectPersonaByUserId(idusuario);
  }

  @Override
  @Transactional
  public PersonaVO create(PersonaUpsertDTO dto) {
    if (dto.getIdusuario() == null) {
      throw new IllegalArgumentException("idusuario es obligatorio");
    }
    if (sysUserMapper.selectById(dto.getIdusuario()) == null) {
      throw new IllegalArgumentException("Usuario no existe: " + dto.getIdusuario());
    }
    if (personalMapper.selectById(dto.getIdusuario()) != null) {
      throw new IllegalStateException("La persona ya existe para idusuario=" + dto.getIdusuario());
    }

    Personal p = toPersonal(dto);
    personalMapper.insert(p);

    if (dto.getCedula() != null) {
      Datos d = new Datos(dto.getIdusuario(), dto.getCedula());
      datosMapper.insert(d);
    }
    return getByUserId(dto.getIdusuario());
  }

  @Override
  @Transactional
  public PersonaVO update(Long idusuario, PersonaUpsertDTO dto) {
    Personal p = personalMapper.selectById(idusuario);
    if (p == null) throw new IllegalArgumentException("Persona no encontrada");

    apply(p, dto);
    personalMapper.updateById(p);

    if (dto.getCedula() != null) {
      Datos existente = datosMapper.selectById(idusuario);
      if (existente == null) {
        datosMapper.insert(new Datos(idusuario, dto.getCedula()));
      } else {
        existente.setCedula(dto.getCedula());
        datosMapper.updateById(existente);
      }
    }
    return getByUserId(idusuario);
  }

  @Override
  @Transactional
  public void delete(Long idusuario) {
    datosMapper.deleteById(idusuario);
    personalMapper.deleteById(idusuario);
  }

  

  private Personal toPersonal(PersonaUpsertDTO dto) {
    return Personal.builder()
      .idusuario(dto.getIdusuario())
      .nombre(dto.getNombre())
      .ap(dto.getAp())
      .am(dto.getAm())
      .estado(dto.getEstado())
      .fnac(dto.getFnac())
      .ecivil(dto.getEcivil())
      .genero(dto.getGenero())
      .dir(dto.getDir())
      .telf(dto.getTelf())
      .tipo(dto.getTipo())
      .foto(dto.getFoto())
      .build();
  }

  private void apply(Personal p, PersonaUpsertDTO dto) {
    if (dto.getNombre()!=null) p.setNombre(dto.getNombre());
    if (dto.getAp()!=null)     p.setAp(dto.getAp());
    if (dto.getAm()!=null)     p.setAm(dto.getAm());
    if (dto.getEstado()!=null) p.setEstado(dto.getEstado());
    if (dto.getFnac()!=null)   p.setFnac(dto.getFnac());
    if (dto.getEcivil()!=null) p.setEcivil(dto.getEcivil());
    if (dto.getGenero()!=null) p.setGenero(dto.getGenero());
    if (dto.getDir()!=null)    p.setDir(dto.getDir());
    if (dto.getTelf()!=null)   p.setTelf(dto.getTelf());
    if (dto.getTipo()!=null)   p.setTipo(dto.getTipo());
    if (dto.getFoto()!=null)   p.setFoto(dto.getFoto());
  }
}
