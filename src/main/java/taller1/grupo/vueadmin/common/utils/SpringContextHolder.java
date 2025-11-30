package taller1.grupo.vueadmin.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: tarija
 * @description: this is a class
 * @author: starao
 * @create: 2024
 **/
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    private static final List<CallBack> CALL_BACKS = new ArrayList<>();

    private static boolean addCallback = true;

    /**
     * @Description: Obtenga el Bean del contexto de la aplicación y conviértalo
     *               automáticamente al tipo de objeto asignado
     * @Param: [requiredType]
     * @return: T
     * @Author: starao
     * @Date: 2021/11/27
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * @Description: controlarApplicationContext
     * @Param: []
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContextPropiedad no inyectada，" +
                    "Regístrese en la clase de inicio SpringBoot SpringContextHolder.");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextHolder.applicationContext != null) {
            log.info("SpringContextHolder en ApplicationContextCubierto，" +
                    "Original ApplicationContext Para：" + SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
        if (addCallback) {
            for (CallBack callBack : SpringContextHolder.CALL_BACKS) {
                callBack.executor();
            }
        }
        SpringContextHolder.addCallback = false;
    }

    @Override
    public void destroy() {
        log.info("Claro SpringContextHolder de ApplicationContext:" + applicationContext);
        applicationContext = null;
    }
}
