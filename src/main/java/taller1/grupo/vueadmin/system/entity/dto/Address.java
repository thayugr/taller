package taller1.grupo.vueadmin.system.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "address", schema = "academico")
public class Address {
    private Long id;
    private Long userId;
    private String city;
    private String address;
}
