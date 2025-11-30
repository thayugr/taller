package taller1.grupo.vueadmin.logs.ascept;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.RequestHolder;
import taller1.grupo.vueadmin.common.utils.SecurityUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.common.utils.ThrowsUtil;
import taller1.grupo.vueadmin.system.entity.SysLog;
import taller1.grupo.vueadmin.system.service.SysLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    private final SysLogService sysLogService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * @Description:Configurar punto de entrada
     * @Param: []
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Pointcut("@annotation(taller1.grupo.vueadmin.logs.annotation.Log)")
    public void logPointcut() {

    }

    /**
     * @Description: Para configurar notificaciones envolventes, utilice el método
     *               logPointcut() Punto de entrada para el registro
     * @Param: [joinPoint]
     * @return: java.lang.Object
     * @Author: richard sivila
     * @Date: 2024
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        SysLog sysLog = new SysLog("1", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        sysLogService.save(getUsername(), StringUtil.getIp(request), joinPoint, sysLog);
        return result;
    }

    /**
     * @Description: Configurar notificación de excepción
     * @Param: [joinPoint, e]
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    @Transactional(rollbackFor = Exception.class)
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog sysLog = new SysLog("2", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        sysLog.setExceptionDetail(ThrowsUtil.getStackTrace(e));
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        sysLogService.save(getUsername(), StringUtil.getIp(request), (ProceedingJoinPoint) joinPoint, sysLog);
    }

    /**
     * @Description: Obtenga el nombre de usuario de inicio de sesión actual
     * @Param: []
     * @return: java.lang.String
     * @Author: starao
     * @Date: 2021/11/27
     */
    public String getUsername() {
        try {
            return SecurityUtil.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
