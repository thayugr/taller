package taller1.grupo.vueadmin.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.SysRole;
import taller1.grupo.vueadmin.system.service.SysRoleService;

import lombok.RequiredArgsConstructor;

/**
 * @program: taller
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class SysRoleController extends ResultUtil {

    private final SysRoleService roleService;

    @Log("Obtener lista de roles")
    @GetMapping("/role/table")
    public ResponseEntity<Object> getRoleList(String blurry) {
        try {
            return success(true, roleService.getRoleList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Editar o inserto un rol")
    @PostMapping("/role/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> editRole(@RequestBody SysRole role) {
        try {
            String tag = StringUtil.getEditType(role.getId());
            roleService.editRole(role);
            return success(true, tag);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Eliminar rol
     * @Param: [id]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Log("Eliminar rol")
    @DeleteMapping("/role/del")
    public ResponseEntity<Object> delRole(Long id) {
        try {
            roleService.delRole(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description:Lista de selección de roles
     * @Param: [userId]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Obtener la lista del cuadro de selección de roles")
    @GetMapping("/role/select")
    public ResponseEntity<Object> getAllRoleForXm(Long userId) {
        try {
            return success(true, roleService.getAllRoleForXm(userId));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
