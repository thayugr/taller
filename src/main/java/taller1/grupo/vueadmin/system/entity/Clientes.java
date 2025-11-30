package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "\"Clientes\"", schema = "northwind")
public class Clientes {

    @TableId(value = "\"Id\"", type = IdType.AUTO)
    private Long idcliente;

    @TableField("\"Compañía\"")
    private String compania;

    @TableField("\"Apellidos\"")
    private String apellidos;

    @TableField("\"Nombre\"")
    private String nombre;

    @TableField("\"Dirección de correo electrónico\"")
    private String direcciónDeCorreoElectrónico;

    @TableField("\"Cargo\"")
    private String cargo;

    @TableField("\"Teléfono del trabajo\"")
    private String teléfonoDelTrabajo;

    @TableField("\"Teléfono particular\"")
    private String teléfonoParticular;

    @TableField("\"Teléfono móvil\"")
    private String teléfonoMóvil;

    @TableField("\"Número de fax\"")
    private String númeroDeFax;

    @TableField("\"Dirección\"")
    private String dirección;

    @TableField("\"Ciudad\"")
    private String ciudad;

    @TableField("\"Estado o provincia\"")
    private String estadoOProvincia;

    @TableField("\"C Postal\"")
    private String cPostal;

    @TableField("\"País o región\"")
    private String paísORegión;

    @TableField("\"Página Web\"")
    private String páginaWeb;

    @TableField("\"Notas\"")
    private String notas;

    @TableField("\"Datos adjuntos\"")
    private String datosAdjuntos;
}
