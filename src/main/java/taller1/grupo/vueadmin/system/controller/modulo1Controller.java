package taller1.grupo.vueadmin.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.Clientes2Dto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.ClientesService;
import taller1.grupo.vueadmin.system.service.Modulo1Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class modulo1Controller extends ResultUtil {
    private final Modulo1Service modulo1Service;
    private final ClientesService clientesService;

    @Log("Consultar lista modulo1 los 10")
    @GetMapping("/modulo1/table")
    public ResponseEntity<Object> queryModulo1Table(QueryDto queryDto) {
        try {
            return success(true, modulo1Service.queryModulo1Table(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Detalle de Clientes")
    @GetMapping("/modulo1/tabledclientes")
    public ResponseEntity<Object> queryModulo1DclientesTable(QueryDto queryDto) {
        try {
            System.out.println(modulo1Service.queryModulo1DClientesTable(queryDto));
            return success(true, modulo1Service.queryModulo1DClientesTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Editar Cliente")
    @PostMapping("/modulo1/edit")
    @PreAuthorize("hasRole('ROLE_DOCENTE')")
    public ResponseEntity<Object> editCliente(@RequestBody Clientes2Dto clientes2dto) {
        try {
            System.out.println("valor del Id" + clientes2dto.getIdcliente());
            // System.out.println(clientesService.getClientesByClientesId(clientes2dto.getIdcliente()));

            String tag = StringUtil.getEditType(clientes2dto.getIdcliente());
            clientesService.editClientes(clientes2dto);
            return success(true, tag);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Eliminar modulo 1")
    @DeleteMapping("/modulo1/del")

    public ResponseEntity<Object> delModulo1(Long id) {
        try {
            clientesService.delClientes(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    // Lista de pedidos de un cliente
    /// modulo1/tabledclientespedido
    @Log("Lista los pedidos de un cliente seleccionado")
    @GetMapping("/modulo1/tabledclientespedido")
    // tabledclientespedido
    public ResponseEntity<Object> queryModulo1TableClientesPedido(Long id) {
        try {
            System.out.println("valor de id:" + id);
            // System.out.println(queryDto);

            return success(true, clientesService.queryModulo1TableClientesPedido(id));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

}
