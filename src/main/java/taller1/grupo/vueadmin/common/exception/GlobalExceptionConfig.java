package taller1.grupo.vueadmin.common.exception;

import taller1.grupo.vueadmin.common.utils.ExceptionUtil;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @program: tarija
 * @description: captcha
 * @author: richard sivila
 * @create: 2024
 **/

@ControllerAdvice
public class GlobalExceptionConfig extends ResultUtil {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        exception.printStackTrace();
        return fail(false, ExceptionUtil.getStackTraceInfo(exception));
    }
}
