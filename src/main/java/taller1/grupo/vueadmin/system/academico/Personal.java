package taller1.grupo.vueadmin.system.academico;

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

@TableName(value = "personal", schema = "academico")
public class Personal extends BaseEntity {

@TableId(value="idusuario",type=IdType.INPUT)
private Integer codp;
@TableField("nombre")
  private String nombre;

  @TableField("ap")
  private String ap;

  @TableField("am")
  private String am;

  @TableField("estado")
  private Byte estado;

  @TableField("fnac")
  private LocalDate fnac;

  @TableField("ecivil")
  private String ecivil;

  @TableField("genero")
  private String genero;

  @TableField("dir")
  private String dir;

  @TableField("telf")
  private String telf;

  @TableField("tipo")
  private String tipo;

  @TableField("foto")
  private String foto;
}
