package taller1.grupo.vueadmin.system.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.dto.PersonaUpsertDTO;
import taller1.grupo.vueadmin.system.entity.vo.PersonaVO;
import taller1.grupo.vueadmin.system.service.PersonalService;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import org.springframework.http.ResponseEntity; 

@RestController
@RequestMapping("/sys")
///api/sys/personal/table
public class PersonalController {

  @Resource private PersonalService personalService;

  /*
   * @Log("Consultar lista de clientes dto")
    @GetMapping("/clientes/table")
    public ResponseEntity<Object> queryClientesTable(QueryDto queryDto) {
        try {
            return success(true, clientesService.queryClientesTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
   */

  // Página de Personas (JOIN personal + datos)
  @GetMapping("/personal/table")
  public ResponseEntity<Object> queryPersonalTable(QueryDto queryDto) {
        try {
            return taller1.grupo.vueadmin.common.utils.ResultUtil.success(true, personalService.queryPersonalTable(queryDto));
        } catch (BadRequestException e) {
            return taller1.grupo.vueadmin.common.utils.ResultUtil.fail(false, e.getMsg());
        }
    }

  @GetMapping("/{idusuario}")
  public PersonaVO get(@PathVariable Long idusuario) {
    return personalService.getByUserId(idusuario);
  }

  @PostMapping
  public PersonaVO create(@Valid @RequestBody PersonaUpsertDTO dto) {
    return personalService.create(dto);
  }

  @PutMapping("/{idusuario}")
  public PersonaVO update(@PathVariable Long idusuario,
                          @Valid @RequestBody PersonaUpsertDTO dto) {
    dto.setIdusuario(idusuario);
    return personalService.update(idusuario, dto);
  }

  @DeleteMapping("/{idusuario}")
  public void delete(@PathVariable Long idusuario) {
    personalService.delete(idusuario);
  }
}

