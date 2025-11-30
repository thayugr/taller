package taller1.grupo.vueadmin.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
public class ResultUtil {

    /**
     * @Description: resultado exitoso
     * @Param: [code, data]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2024
     */
    public static ResponseEntity<Object> success(boolean code, Object data) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("success", code);
        map.put("data", data);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * @Description: Resultado del fracaso
     * @Param: [code, msg]
     * @return: org.springframework.http.ResponseEntity<java.lang.Object>
     * @Author: starao
     * @Date: 2024
     */
    public static ResponseEntity<Object> fail(boolean code, String msg) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("success", code);
        map.put("msg", msg);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
