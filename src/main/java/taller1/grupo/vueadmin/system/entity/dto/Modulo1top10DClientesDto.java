package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Setter;

import java.util.Date;

import lombok.Getter;

@Setter
@Getter
public class Modulo1top10DClientesDto {
    private Long idcliente;
    private String compania;
    private String nombre;
    private String apellidos;
    private String cargo;
    private String telefonotrabajo;
    private String numerofax;
}