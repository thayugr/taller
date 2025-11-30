package taller1.grupo.vueadmin.system.entity.dto;

import java.time.LocalDate;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonaUpsertDTO {
  private Long   idusuario;     // sys_user.id (obligatorio)
  private String nombre;
  private String ap;
  private String am;
  private Integer estado;
  private LocalDate fnac;
  private String ecivil;
  private String genero;
  private String dir;
  private String telf;
  private String tipo;
  private String foto;
  private Long   cedula;        // academico.datos.cedula
}
