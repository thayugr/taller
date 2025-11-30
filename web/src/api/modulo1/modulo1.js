import request from "../../utils/request";

export function getModulo1Table(params){
    return request({
        url: '/api/sys/modulo1/table',
        method: 'get',
        params
    })
}

export function editModulo1(data){
    return request({
        url: '/api/sys/modulo1/edit',
        method: 'post',
        data
    })
}


// detalle de clientes 
export function getModulo1DClientesTable(params){
    return request({
        url: '/api/sys/modulo1/tabledclientes',
        method: 'get',
        params
    })
}

// defino la funcion asociada al methodo en spring
// editar el cliente 
export function editCliente(data){
    return request({
        url: '/api/sys/modulo1/edit',
        method: 'post',
        data
    })
}

// eliminar 
export function delModulo1(params){
    return request({
        url: '/api/sys/modulo1/del',
        method: 'delete',
        params
    })
}

// DETALLE DE PEDIDOS SELECCIONADO POR EL CLIENTE
//getModulo1DClientesPedidosTable
export function getModulo1DClientesPedidosTable(params){
    return request({
        url: '/api/sys/modulo1/tabledclientespedido',
        method: 'get',
        params
    })
}
