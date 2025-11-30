package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
public class ClientesProductosDto {
    private Long iddepedido;
    private Long iddeempleado;
    private Long iddecliente;
    private Date fechadepedido;
    private Date fechadeenvio;
    private Float subtotal;
    private Integer gastosdeenvio;
    private Integer impuestos;
    private Integer totaldepedido;
    private String nombredeenvio;
    private String direcciondeenvio;
    private Date fechadepago;
    private String estado;
}
