package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Setter;

import java.util.Date;

import lombok.Getter;

@Setter
@Getter
public class Modulo1top10Dto {
    private Long idpedido;
    private Date fechapedido;
    private Date fechaenvio;
    private String nombrecompania;
    private float importeventas;
}
