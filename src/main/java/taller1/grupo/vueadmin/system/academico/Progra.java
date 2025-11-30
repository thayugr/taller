package taller1.grupo.vueadmin.system.academico;
import java.io.Serializable;
import java.time.LocalDate;

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

@TableName(value = "progra", 
schema = "academico")
public class Progra extends 
BaseEntity implements Serializable {
 @TableId("codpart")
  private Integer codpart;
  @TableId("codp")
  private Integer codp;
  @TableId("codmat")
  private Integer codmat;
  @TableId("gestion")
  private Integer gestion;
  @TableField("login")
  private String login;
}
