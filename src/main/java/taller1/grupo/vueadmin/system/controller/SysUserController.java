package taller1.grupo.vueadmin.system.controller;

import com.alibaba.fastjson.JSONObject;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.SysUser;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.dto.UserDto;
import taller1.grupo.vueadmin.system.service.SysUserService;
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
@RequestMapping("/sys")
public class SysUserController extends ResultUtil {

    private final SysUserService userService;

    /**
     * @Description: Consultar lista de usuarios
     * @Param: [blurry]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Consultar lista de usuarios")
    @GetMapping("/user/table")
    public ResponseEntity<Object> queryUserTable(QueryDto queryDto) {
        try {
            return success(true, userService.queryUserTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Editar usuario
     * @Param: [userDto]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2024
     */
    @Log("Editar usuario")
    @PostMapping("/user/edit")
    public ResponseEntity<Object> editUser(@RequestBody UserDto userDto) {
        try {
            String str = StringUtil.getEditType(userDto.getId());
            /*
             * if (userDto.getEnabled() == null) {
             * userDto.setEnabled(false);
             * } else {
             * userDto.setEnabled(true);
             * }
             */
            userService.editUser(userDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Eliminar usuario
     * @Param: [id]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2024
     */
    @Log("Eliminar usuario")
    @DeleteMapping("/user/del")
    public ResponseEntity<Object> delUser(Long id) {
        try {
            userService.delUser(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, "Error al eliminar");
        }
    }

    /**
     * @Description: Modificar el estado del usuario
     * @Param: [sysUser]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log("Cambiar estado de usuario")
    @PutMapping("/user/enabled")
    public ResponseEntity<Object> enabledUser(@RequestBody SysUser sysUser) {
        String str = sysUser.isEnabled() ? "permitir" : "desactivar";
        try {
            userService.enabledUser(sysUser);
            return success(true, str + "exito");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * @Description: Cambiar contraseña de usuario
     * @Param: [jsonObject]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Log(value = "Cambiar contraseña de usuario")
    @PutMapping("/user/password")
    public ResponseEntity<Object> updatePassword(@RequestBody JSONObject jsonObject) {
        try {
            userService.updatePassword(jsonObject);
            return success(true, "Modificacion exitosa");
        } catch (BadRequestException e) {
            return fail(false, "La modificación falló");
        }
    }
}
