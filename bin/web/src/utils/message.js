import {ElMessage} from "element-plus";

/**
 * mensaje de éxito
 * @param msg
 */
export function successMsg(msg){
    ElMessage.success(msg)
}

/**
 *mensaje de error
 * @param msg
 */
export function errorMsg(msg){
    ElMessage.error(msg)
}

/**
 * mensaje de advertencia
 * @param msg
 */
export function warningMsg(msg){
    ElMessage.warning(msg)
}

/**
 * Mensaje rápido
 * @param msg
 */
export function infoMsg(msg){
    ElMessage.info(msg)
}