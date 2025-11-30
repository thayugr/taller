import request from '@/utils/request'

export function pagePersonas(params) {
  return request({
    url: '/api/sys/personal/table',
    method: 'get',
    params // { page, size, blurry }
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
