package taller1.grupo.vueadmin.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Asegurar todas las anotaciones

import lombok.RequiredArgsConstructor;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.QueryUsuarioDto; 
import taller1.grupo.vueadmin.system.service.UsuarioService;
import taller1.grupo.vueadmin.system.entity.dto.UsuarioDto;
import taller1.grupo.vueadmin.system.entity.SysUser;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class UsuarioController extends ResultUtil { 
    
    private final UsuarioService usuarioService;

    @Log("Consultar lista de Usuarios con filtros")
    @GetMapping("/usuario/table")
    public ResponseEntity<Object> queryUsuarioTable(QueryUsuarioDto queryDto) {
        try {
            return success(true, usuarioService.queryUsuarioTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
    
    @Log("Consultar detalles de un usuario para edición")
    @GetMapping("/usuario/details/{id}")
    public ResponseEntity<Object> getUsuarioDetails(@PathVariable("id") Long idusuario) {
        try {
            UsuarioDto details = usuarioService.getUsuarioDetails(idusuario);
            return success(true, details);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Consultar usuarios de sistema sin registro de personal")
    @GetMapping("/usuario/sys-users-unassigned")
    public ResponseEntity<Object> getUnassignedSysUsers() {
        try {
            List<SysUser> users = usuarioService.getUnassignedSysUsers();
            return success(true, users);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * POST /sys/usuario/edit -> Guardar o Actualizar (Upsert) los datos de Personal/Datos
     */
    @Log("Guardar/Actualizar datos de Personal y Datos")
    @PostMapping("/usuario/edit") // Se corrige la ruta para ser específica
    public ResponseEntity<Object> editUsuario(@RequestBody UsuarioDto usuarioDto) {
        try {
            // Usamos StringUtil.getEditType para saber si fue un Insert o Update, aunque la lógica está en el Service
            String tag = StringUtil.getEditType(usuarioDto.getIdusuario()); 
            usuarioService.editUsuario(usuarioDto);
            return success(true, tag); // Retorna el tag ("Modificado" o "Creado")
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}