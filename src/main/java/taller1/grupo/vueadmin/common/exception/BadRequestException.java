package taller1.grupo.vueadmin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @program: tarija
 * @description: captcha
 * @author: richard sivila
 * @create: 2024
 **/

@Getter
public class BadRequestException extends RuntimeException {

    /**
     * Defina códigos de error unificados para facilitar el procesamiento front-end
     */
    private Integer status = BAD_REQUEST.value();

    private final String msg;

    public BadRequestException(String msg) {
        this.msg = msg;
    }

    public BadRequestException(HttpStatus status, String msg) {
        this.msg = msg;
        this.status = status.value();
    }
}
