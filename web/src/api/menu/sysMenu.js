import request from "../../utils/request";

/**
 *Extrae todos los menús del usuario actual.
 * @returns {AxiosPromise}
 */
export function queryAllMenu(){
    return request({
        url: '/api/sys/menu/all',
        method: 'get'
    })
}

/**
 * Obtener el árbol del menú del usuario actualmente conectado
 * @returns {AxiosPromise}
 */
export function getMenuTree(){
    return request({
        url: '/api/sys/menu/tree',
        method: 'get'
    })
}

/**
 * Obtener lista de menú
 * @param params
 * @returns {AxiosPromise}
 */
export function getMenuTable(params){
    return request({
        url: '/api/sys/menu/table',
        method: 'get',
        params
    })
}

/**
 * Editar menú
 * @param data
 * @returns {AxiosPromise}
 */
export function editMenu(data){
    return request({
        url: '/api/sys/menu/edit',
        method: 'post',
        data
    })
}

/**
 * Eliminar menú según ID
 * @param params
 * @returns {AxiosPromise}
 */
export function delMenu(params){
    return request({
        url: '/api/sys/menu/del',
        method: 'delete',
        params
    })
}

/**
 * Obtener lista de permisos de botones
 * @returns {AxiosPromise}
 */
export function getPermission(){
    return request({
        url: '/api/sys/menu/permission',
        method: 'get'
    })
}


export function getMenuTreeSelect(){
    return request({
        url: '/api/sys/menu/select',
        method: 'get'
    })
}
