package taller1.grupo.vueadmin.system.entity.vo;

import java.time.LocalDate;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonaVO {
  private Long   idusuario;
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
  private Long   cedula;
}
