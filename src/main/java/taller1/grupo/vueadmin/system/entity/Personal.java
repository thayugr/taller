package taller1.grupo.vueadmin.system.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taller1.grupo.vueadmin.common.utils.BaseEntity;
import java.time.LocalDate;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@TableName(value = "personal", schema = "academico")
public class Personal extends BaseEntity {
    /*
     * * idusuario integer NOT NULL,
    * *nombre character varying(100) COLLATE pg_catalog."default" NOT NULL,
    *ap character varying(100) COLLATE pg_catalog."default",
    *am character varying(100) COLLATE pg_catalog."default",
    estado smallint,
    fnac date,
    ecivil character varying(30) COLLATE pg_catalog."default",
    genero character varying(10) COLLATE pg_catalog."default",
    dir character varying(200) COLLATE pg_catalog."default",
    telf character varying(30) COLLATE pg_catalog."default",
    tipo character varying(30) COLLATE pg_catalog."default",
    foto character varying(255) COLLATE pg_catalog."default",
    create_time timestamp(6) without time zone,
    update_time timestamp(6) without time zone,
     */
    @TableId(value = "idusuario", type = IdType.AUTO)
    private Long idusuario;
    @TableField("nombre")
    private String nombre;
    @TableField("ap")
    private String ap;
    @TableField("am")
    private String am;
    @TableField("estado")
    private Integer estado;
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
