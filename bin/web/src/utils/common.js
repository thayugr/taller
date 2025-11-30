import {useStore} from "../store";
/**
 * Restablecer elogios
 * @param ref
 */

export function resetForm(ref){
    ref.resetFields()
}

/**
 * Determina si tienes permiso
 * @param per
 * @returns {boolean}
 */
export function hasPer(per){
    const store = useStore()
    const permission = store.permission
    return permission.indexOf(per) > -1
}