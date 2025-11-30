import request from "../../utils/request";

/**
 * Obtener lista de registro
 * @param params
 * @returns {AxiosPromise}
 */
export function getLogList(params){
    return request({
        url: '/api/sys/log/list',
        method: 'get',
        params
    })
}