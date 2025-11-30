import axios from "axios";
import routers from '../router/routers'
import {useStore} from "../store";
import {errorMsg} from "./message";

//  Crear instancia de axios
const instance = axios.create({
    baseURL: process.env.NODE_ENV === 'production' ? process.env.VUE_APP_BASE_URL : '/',
    timeout: 60000   //  Solicitar tiempo de espera (milisegundos)
})

//  request拦截器
instance.interceptors.request.use(
    config => {
        const store = useStore()
        // Si has iniciado sesión
        if (store.token){
            //  Agregar token en el encabezado de la solicitud
            config.headers['Authorization'] = 'Bearer ' + store.token
        }
        // Tipo de solicitud unificada json
        config.headers['Content-Type'] = 'application/json'
        return config
    },
    error => {
        Promise.reject(error)
    }
)

//  response拦截器
instance.interceptors.response.use(
    response => {
        //  La solicitud es exitosa y los datos se devuelven directamente.
        return response.data
    },
    error => {
        const store = useStore()
        if (!error.response){
            errorMsg(error.message)
        } else {
            //  Solicitar código de retorno
            let code;
            if (error.response){
                code = error.response.status
            }
            //  Error de devolución de solicitud
            const data = error.response.data
            if (code){
                //  Si no está autorizado
                if (code === 401){
                    // Indica que el token ha caducado. Utilice refrescoToken para actualizar el token actual.
                    const refresh = store.refreshToken
                    //  si existe
                    if (refresh){
                        return againRequest(refresh, error)
                    //  de lo contrario
                    } else {
                        //  claro token
                        store.tokenAction(null)
                        //  Y vaya a la página de inicio de sesión para iniciar sesión nuevamente.
                        routers.push({
                            path: '/login',
                            query: {
                                backto: routers.currentRoute.fullPath
                            }
                        })
                    }
                    // Si no hay permiso
                } else if (code === 403){
                    // Saltar directamente a la página 401
                    routers.replace({path: '/401'})
                    //  Si es una excepción del servidor u otra excepción
                } else {
                    //  Si hay información anormal, muestre la información anormal.
                    if (data){
                        errorMsg(data.detail)
                    }
                }
            } else {
                errorMsg('Error en la solicitud de interfaz')
            }
        }
        return Promise.reject(error)
    }
)

/**
 * pedido
 * @param error
 * @returns {Promise<void>}
 */
async function againRequest(refresh, error){
    const store = useStore()
    await refreshToken(refresh)
    const config = error.response.config
    config.headers['Authorization'] = 'Bearer ' + store.token
    const res = await axios.request(config)
    return res.data
}

/**
 * refrescar token
 * @param refresh
 * @param config
 */
export function refreshToken(refresh){
    const store = useStore()
    //  token
    return axios({
        url: '/auth/refresh',
        method: 'put',
        headers: {
            Authorization: `Bearer ${refresh}`
        }
    }).then(res => {
        if (res.data.success){
            //  token
            store.tokenAction(res.data.data)
        } else {
            errorMsg(res.msg)
            //  token
            store.tokenAction(null)
        }
    })
}

export default instance