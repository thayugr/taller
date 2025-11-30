package taller1.grupo.vueadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import taller1.grupo.vueadmin.common.utils.SpringContextHolder;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2021-11-27 11:51
 **/
/** Modificado **/
@EnableTransactionManagement
@SpringBootApplication
public class VueadminApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueadminApplication.class, args);
    }

    /**
     * @Description: Registrar objeto de contexto de primavera
     * @Param: []
     * @return: taller1.grupo.vueadmin.common.utils.SpringContextHolder
     * @Author: richard sivila
     * @Date: 2024
     */
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
