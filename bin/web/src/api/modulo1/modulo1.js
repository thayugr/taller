import request from "../../utils/request";

export function getModulo1Table(params){
    return request({
        url: '/api/sys/modulo1/table',
        method: 'get',
        params
    })
}