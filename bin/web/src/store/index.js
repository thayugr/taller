import {defineStore} from "pinia";

export const useStore = defineStore('ems', {
    persist: true,
    state: () => {
        return {
            token: null,
            //  Refrescar token
            refreshToken: null,
            // Información del usuario que ha iniciado sesión actualmente
            userInfo: null,
            //  Si el menú de usuario ha sido extraído
            isLoadMenu: false,
            // Todos los menús (sistema + autorización dinámica de usuario)
            routers: null,
            //  Lista de permisos
            permission: null,
            //  Menú actualmente activo
            activeIndex: 'delantera',
            //  Todos los menús abiertos
            openTabs: []
        }
    },
    actions: {
        //  Ya sea para abrir el menú de usuario
        loadMenuAction(payload){
            this.isLoadMenu = payload
        },
        //  Lista del menú de usuario de caché
        routerAction(payload){
            this.routers = payload
        },
        //  Permisos de usuario de almacenamiento en caché
        permissionAction(payload){
            this.permission = payload
        },
        //  Usuarios de caché token
        tokenAction(payload){
            this.token = payload
        },
        //  Menú actualmente activo
        activeIndexAction(payload){
            this.activeIndex = payload
        },
        //  Agregar un menú abierto
        addTabAction(payload){
            //  Agregar si no existe
            if (this.openTabs.filter(tab => tab.name === payload.name).length === 0){
                payload.isClose = payload.name !== 'delantera'
                this.openTabs.push(payload)
            }
        },
        // Cerrar un menú abierto
        removeTabAction(payload){
            this.openTabs = this.openTabs.filter((tab) => tab.name !== payload)
        },
        //  Borarr todo tabs
        clearTabAction(){
            this.openTabs = []
        }
    }
})