package taller1.grupo.vueadmin.system.controller;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class SysLogController extends ResultUtil {

    private final SysLogService logService;

    /**
     * @Description: Lista de registro de consultas
     * @Param: [queryDto, logType]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: richard sivila
     * @Date: 2024
     */
    @GetMapping("/log/list")
    public ResponseEntity<Object> getLogList(QueryDto queryDto, String logType) {
        try {
            return success(true, logService.getLogList(queryDto, logType));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
