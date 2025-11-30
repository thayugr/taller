package taller1.grupo.vueadmin.config.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * @Description: autocompletar tiempo de creación
     * @Param: [metaObject]
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }

    /**
     * @Description: autocompletarupdateTime
     * @Param: [metaObject]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }
}
