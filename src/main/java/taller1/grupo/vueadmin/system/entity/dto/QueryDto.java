package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class QueryDto {

    private String blurry;

    private long currentPage = 1;

    private long size = 10;
}
