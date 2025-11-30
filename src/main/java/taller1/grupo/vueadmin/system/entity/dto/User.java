package taller1.grupo.vueadmin.system.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data

@TableName(value = "user", schema = "academico")
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
