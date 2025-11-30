import request from "../../utils/request";

//  Acceso
export function login(data){
    return request({
        url: '/api/auth/login',
        method: 'post',
        data
    })
}

// Obtener código de verificación
export function getVerifyCode(params){
    return request({
        url: '/api/auth/code',
        method: 'get',
        params
    })
}