// Archivo: src/main/java/taller1/grupo/vueadmin/system/entity/dto/UsuarioDto.java

package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO para la edición/creación de Usuario (Personal y Datos)
 */
@Data
public class UsuarioDto {

    // ES ES UNA PRUEBA qtableusuarios
    
    // Campos heredados de sys_user (solo para referencia y llave)
    private Long idusuario; // Corresponde a sys_user.id y datos.cod
    private String username; // Para mostrar en el formulario

    // Campos de la tabla academico.datos
    private Long cedula; // Único campo de dato en 'datos'

    // Campos de la tabla academico.personal (la mayoría de los datos personales)
    private String nombre;
    private String ap;
    private String am;
    private Integer estado; // 1: Activo, 0: Baja
    private LocalDate fnac;
    private String ecivil;
    private String genero;
    private String dir;
    private String telf;
    private String tipo;
    private String foto; 
}