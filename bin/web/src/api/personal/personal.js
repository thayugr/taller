import request from '@/utils/request'

export function getPersonalTable(params){
    return request({
        //url: '/api/sys/personas/list',
        url: '/api/sys/personal/table',
        method: 'get',
        params
    })
}

export function getPersona(idusuario) {
  return request({ url: `/api/personal/${idusuario}`, method: 'get' })
}

export function createPersona(data) {
  return request({ url: '/api/personal', method: 'post', data })
}

export function updatePersona(idusuario, data) {
  return request({ url: `/api/personal/${idusuario}`, method: 'put', data })
}

export function deletePersona(idusuario) {
  return request({ url: `/api/personal/${idusuario}`, method: 'delete' })
}
