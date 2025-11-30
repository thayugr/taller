package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// Extiende el DTO base para heredar blurry, currentPage, y size
public class QueryUsuarioDto extends QueryDto {

    private Integer status;

    private String type;
}