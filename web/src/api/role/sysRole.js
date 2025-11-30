import request from "../../utils/request";

/**
 * Obtener lista de roles
 * @param params
 * @returns {AxiosPromise}
 */
export function getRoleList(params){
    return request({
        url: '/api/sys/role/table',
        method: 'get',
        params
    })
}

/**
 * Editar rol
 * @param data
 * @returns {AxiosPromise}
 */
export function editRole(data){
    return request({
        url: '/api/sys/role/edit',
        method: 'post',
        data
    })
}

/**
 *Autorización de rol
 * @param data
 * @returns {AxiosPromise}
 */
export function authorizeRole(data){
    return request({
        url: '/api/sys/role/menu/edit',
        method: 'post',
        data
    })
}

/**
 * Eliminar rol
 * @param params
 * @returns {AxiosPromise}
 */
export function delRole(params){
    return request({
        url: '/api/sys/role/del',
        method: 'delete',
        params
    })
}

/**
 * Obtener el menú de rol autorizado
 * @param params
 * @returns {AxiosPromise}
 */
export function getMenusByRoleId(params){
    return request({
        url: '/api/sys/role/menu/list',
        method: 'get',
        params
    })
}