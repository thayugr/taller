package taller1.grupo.vueadmin.config.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Configuration
@MapperScan("taller1.grupo.vueadmin.system.mapper")
public class MybatisPlusConfig {

    /**
     * @Description: Configurar la herramienta de paginación mybatis-plus
     * @Param: []
     * @return: com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
}
