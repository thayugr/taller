import router from "./routers";
import {useStore} from "../store";
import {queryAllMenu} from "../api/menu/sysMenu";
import {errorMsg} from "../utils/message";

//  Definir la lista blanca de enrutamiento
const whiteList = ['/login', '/401', '/404']

//  Crear guardia de ruta
router.beforeEach((to, from, next) => {
    console.log('Solicitar enrutamiento：' + to.path)
    const store = useStore()
    //  Si has iniciado sesión
    if (store.token){
        //  Retorno forzado cuando el usuario cambia manualmente a la ruta raíz
        //  Evitar que el enrutamiento salte de la página principal actual
        if (to.path === '/'){
            router.go(-1)
        }
        // Si inicias sesión repetidamente
        if (to.path === '/login'){
            //  luego salta directamente a la página de inicio
            next({path: '/Layout'})
        } else {
            //  Si la lista del menú de usuario no ha sido extraída
            if (!store.isLoadMenu){
                loadMenus(next, to)
            //  Si ha sido arrancado
            } else {
                //  Si la ruta solicitada no existe en las rutas del sistema
                if (!hashRoute(to)){
                    //  Agregar rutas en caché a las rutas del sistema
                    addRoute()
                    //  Compruebe nuevamente si existe en la nueva ruta del sistema.
                    //  Si existe accede a él
                    if (hashRoute(to)){
                        next(to.fullPath)
                        //  Si no existe, saltará a la página 404.
                    } else {
                        next({path: '/404'})
                    }
                //  de lo contrario
                } else {
                    // acceso directo
                    next()
                }
            }
        }
    } else {
        //  si está en la lista blanca
        if (whiteList.indexOf(to.path) !== -1){
            // luego accede directamente
            next()
        //  Si no existe, salte a la página de inicio de sesión.
        } else {
            next({path: '/login'})
        }
    }
})

/**
 * Extraer la lista del menú de usuario
 * @param next
 * @param to
 */
export function loadMenus(next, to){
    const store = useStore()
    queryAllMenu().then(res => {
        if (res.success){
            if (res.data.length > 0){
                //  Almacenar en caché todos los menús en la tienda
                store.routerAction(res.data)
                //  Modificar el estado del menú desplegable
                store.loadMenuAction(true)
                //  Interrumpir la navegación actual y ejecutar una nueva navegación
                next({...to, replace: true})
            }
        } else {
            errorMsg(res.msg)
        }
    })
}

/**
 * Determinar si la ruta ya existe
 * @param to
 */
export function hashRoute(to){
    let find = router.getRoutes().find(item => item.path === to.path)
    return !!find
}

/**
 *Agregue las rutas extraídas a las rutas del sistema
 */
export function addRoute(){
    const store = useStore()
    let routers = store.routers
    if (routers && routers.length > 0){
        console.info(routers)
        routers.forEach(item => {
            if (item.path){
                router.addRoute('Layout', {
                    path: item.path,
                    name: item.name,
                    component: item.component != null ? () => import(`@/views/${item.component}`) : null
                })
            }
        })
    }
}