package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import taller1.grupo.vueadmin.common.utils.BaseEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value="datos",schema="academico")
public class Datos extends BaseEntity {
@TableId (value="cod",type=IdType.INPUT)
private Long cod;
private Long cedula;
}
