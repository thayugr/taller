package taller1.grupo.vueadmin.system.controller;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.SecurityUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.SysMenu;
import taller1.grupo.vueadmin.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class SysMenuController extends ResultUtil {

    private final SysMenuService menuService;

    /**
     * @Description: Obtener árbol de menú
     * @Param: []
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Log("Obtener árbol de menú")
    @GetMapping("/menu/tree")
    public ResponseEntity<Object> getMenuTree() {
        try {
            List<String> roles = SecurityUtil.getCurrentRoles();
            return success(true, menuService.getMenuTree(roles));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description:Obtener lista de permisos
     * @Param: []
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2022/10/6
     */
    @Log(value = "Obtener lista de permisos")
    @GetMapping("/menu/permission")
    public ResponseEntity<Object> getPermission() {
        try {
            return success(true, menuService.getPermission());
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Consultar todos los menús de permisos del usuario actual.
     * @Param: []
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @GetMapping("/menu/all")
    public ResponseEntity<Object> queryAllMenus() {
        try {
            List<String> roles = SecurityUtil.getCurrentRoles();
            return success(true, menuService.queryAllMenus(roles));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description:Obtener lista de menú
     * @Param: [blurry]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Obtener lista de menú")
    @GetMapping("/menu/table")
    public ResponseEntity<Object> getMenuTable(String blurry) {
        try {
            return success(true, menuService.getMenuTable(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Editar menú
     * @Param: [sysMenu]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Editar menu")
    @PostMapping("/menu/edit")
    public ResponseEntity<Object> editMenu(@RequestBody SysMenu sysMenu) {
        try {
            String str = StringUtil.getEditType(sysMenu.getId());
            menuService.editMenu(sysMenu);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Eliminar Menu
     * @Param: [id]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2021/11/27
     */
    @Log("eliminar menu")
    @DeleteMapping("/menu/del")
    public ResponseEntity<Object> delMenu(Long id) {
        try {
            menuService.delMenu(id);
            return success(true, "Eliminar Exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log(value = "Obtener árbol desplegable del menú")
    @GetMapping("/menu/select")
    public ResponseEntity<Object> getMenuTreeSelect() {
        try {
            return success(true, menuService.getMenuTreeSelect());
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
