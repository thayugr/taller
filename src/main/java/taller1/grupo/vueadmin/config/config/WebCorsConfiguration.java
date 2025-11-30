package taller1.grupo.vueadmin.config.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Component
public class WebCorsConfiguration implements WebMvcConfigurer {

    /**
     * @Description: Establezca la ruta de acceso predeterminada (implementación
     *               separada de front-end y back-end, este método no es necesario)
     * @Param: [registry]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/dist/css/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/dist/fonts/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/dist/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/dist/js/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/dist/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * @Description: Establezca la página predeterminada (implementación separada de
     *               front-end y back-end, este método no es necesario)
     * @Param: [registry]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    /**
     * @Description: Configurar para evitar dominios cruzados
     * @Param: []
     * @return: org.springframework.web.filter.CorsFilter
     * @Author: starao
     * @Date: 2024
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        // Configurar propiedades de encabezado
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", "X-Total-Count", "Link",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
