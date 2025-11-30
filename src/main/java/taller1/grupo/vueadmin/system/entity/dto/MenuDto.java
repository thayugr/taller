package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Setter
@Getter
public class MenuDto {

    private Long id;

    private String title;

    private String url;

    private String parentId;

    private int sort;

    private String type;

    private String permission;
}
