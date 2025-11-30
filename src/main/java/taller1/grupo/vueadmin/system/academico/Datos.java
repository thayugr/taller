package taller1.grupo.vueadmin.system.academico;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taller1.grupo.vueadmin.common.utils.BaseEntity;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "datos", schema = "academico")
public class Datos extends BaseEntity {

@TableId(value="codp",type=IdType.INPUT)
private Integer codp;
@TableField("cedula")
  private String cedula;
}

