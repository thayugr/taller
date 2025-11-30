package taller1.grupo.vueadmin.system.controller;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.RoleMenuDto;
import taller1.grupo.vueadmin.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("sys")
public class SysRoleMenuController extends ResultUtil {

    private final SysRoleMenuService roleMenuService;

    /**
     * @Description: Obtenga el menú correspondiente por ID de personaje
     * @Param: [roleId]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Obtener menú de personajes")
    @GetMapping("/role/menu/list")
    public ResponseEntity<Object> getMenuByRoleId(Long roleId) {
        try {
            return success(true, roleMenuService.getMenuByRoleId(roleId));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Menú de rol autorizado
     * @Param: [roleMenuDto]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Log("Menú de rol autorizado")
    @PostMapping("/role/menu/edit")
    public ResponseEntity<Object> editMenuRoleByRoleId(@RequestBody RoleMenuDto roleMenuDto) {
        try {
            roleMenuService.editMenuRoleByRoleId(roleMenuDto);
            return success(true, "Autorizacion exitosa");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
