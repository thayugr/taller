// Archivo: web/src/api/usuario/usuario.js

import request from '../../utils/request'

// Consulta principal
export function queryUsuarioTable(params){
    return request({
        //url: '/api/sys/clientes/list',
        url: '/api/sys/usuario/table',
        method: 'get',
        params
    })
}

// Obtener la lista de sys_user que no tienen datos de Personal
export function querySysUsersForSelection() {
    return request({
        url: '/sys/usuario/sys-users-unassigned', // URL adaptada
        method: 'get'
    })
}

// Obtener detalles completos de un usuario para edición
export function getUsuarioDetails(idusuario) {
    return request({
        url: `/sys/usuario/details/${idusuario}`, // URL adaptada
        method: 'get'
    })
}

// Guardar o actualizar los datos del usuario (Upsert)
export function editUsuario(data) {
    return request({
        url: '/sys/usuario/edit', // URL adaptada
        method: 'post',
        data
    })
}