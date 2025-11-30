<template>
<q-layout view="hHh lpR fFf">
    <q-page-container>
        <q-table
            title="Listar Usuarios"
            :columns="columns"
            :rows="state.tableData"
            row-key="idusuario"
            :visible-columns="visibleColumns"
        >
            <template v-slot:top>
                <div class="row full-width q-col-gutter-sm items-center">
                    <div class="col-xs-12 col-sm-auto q-mb-md">
                        <q-btn
                            color="primary"
                            icon="add"
                            label="+ usuario"
                            @click="onEdit()"
                        />
                    </div>
                    
                    <div class="col-xs-12 col-sm-4 q-mb-md">
                        <q-input
                            dense
                            debounce="300"
                            v-model="state.blurry"
                            placeholder="Buscar (Nombre + Apellidos)"
                            @keyup.enter="onSearch"
                            clearable
                        >
                            <template v-slot:append>
                                <q-icon name="search" @click="onSearch" class="cursor-pointer" />
                            </template>
                        </q-input>
                    </div>

                    <div class="col-xs-12 col-sm-3 q-mb-md">
                        <q-btn-toggle
                            v-model="state.statusFilter"
                            toggle-color="primary"
                            size="sm"
                            no-caps
                            @update:model-value="onStatusFilterChange"
                            :options="[
                                {label: 'Activos', value: '1'},
                                {label: 'Bajas', value: '0'},
                                {label: 'Todos', value: ''}
                            ]"
                            class="full-width"
                        />
                    </div>
                    
                    <div class="col-xs-12 col-sm-3 q-mb-md">
                        <q-btn-toggle
                            v-model="state.typeFilter"
                            toggle-color="accent"
                            size="sm"
                            no-caps
                            @update:model-value="onTypeFilterChange"
                            :options="[
                                {label: 'ESTUDIANTE', value: 'ESTUDIANTE'},
                                {label: 'DOCENTE', value: 'DOCENTE'},
                                {label: 'TODOS', value: ''}
                            ]"
                            class="full-width"
                        />
                    </div>
                </div>
            </template>
            
            <template v-slot:body-cell-foto="props">
                <q-td :props="props">
                    <q-avatar size="md" color="grey-3" text-color="white">
                        <img v-if="props.row.fotoUrl" :src="props.row.fotoUrl" alt="Foto Persona"/>
                        <q-icon v-else name="person" size="md" />
                    </q-avatar>
                </q-td>
            </template>

            <template v-slot:body-cell-estado="props">
                <q-td :props="props">
                    <q-checkbox
                        :model-value="props.row.estado === 1"
                        checked-icon="check_circle"
                        unchecked-icon="cancel"
                        dense
                        :color="props.row.estado === 1 ? 'positive' : 'negative'"
                        @click="onEstado(props.row)"
                        :title="props.row.estado === 1 ? 'Usuario Activo' : 'Usuario de Baja'"
                    />
                </q-td>
            </template>
            
            <template v-slot:body-cell-actions="props">
                <q-td :props="props">
                    <q-btn icon="edit" size="sm" flat round color="primary" @click="onEdit(props.row)" title="Modificar Datos" />
                    
                    <q-btn icon="delete" size="sm" flat round color="negative" @click="onDelete(props.row)" title="Dar de Baja (Eliminar Lógico)" />
                    
                    <q-btn
                        v-if="props.row.estado === 0"
                        icon="redo"
                        size="sm"
                        flat
                        round
                        color="positive"
                        @click="onEstado(props.row)"
                        title="Habilitar Persona"
                    />
                    
                    <q-btn icon="visibility" size="sm" flat round color="info" @click="onView(props.row)" title="Visualizar/Imprimir Datos" />
                </q-td>
            </template>
        </q-table>
        
        <div class="q-pa-md row justify-center">
            <Pagination
                :current="state.current"
                :total="state.total"
                :page-size="state.size"
                @changePage="onPageChange"
            />
        </div>

    </q-page-container>
    
    <q-dialog v-model="dialogState.isNewUserDialogOpen" persistent>
        <q-card style="min-width: 350px">
            <q-card-section>
                <div class="text-h6">Vincular a Usuario del Sistema</div>
                <div class="text-caption text-grey">
                    Seleccione un usuario de la tabla **sys_user** que no tenga datos personales.
                </div>
            </q-card-section>

            <q-card-section class="q-pt-none">
                <q-select
                    filled
                    v-model="dialogState.selectedSysUser"
                    :options="dialogState.sysUserList"
                    option-value="id"
                    option-label="username"
                    label="Usuario (username)"
                    clearable
                    hint="Seleccione un usuario base para crear el registro de Personal."
                >
                    <template v-slot:selected-item="scope">
                        <q-chip
                            dense
                            square
                            color="primary"
                            text-color="white"
                            :label="`${scope.opt.username} (ID: ${scope.opt.id})`"
                        />
                    </template>
                    
                    <template v-slot:option="scope">
                        <q-item v-bind="scope.itemProps">
                            <q-item-section>
                                <q-item-label>{{ scope.opt.username }}</q-item-label>
                                <q-item-label caption>ID: {{ scope.opt.id }}</q-item-label>
                            </q-item-section>
                        </q-item>
                    </template>
                </q-select>
            </q-card-section>

            <q-card-actions align="right" class="text-primary">
                <q-btn flat label="Cancelar" v-close-popup @click="dialogState.selectedSysUser = null" />
                <q-btn flat 
                       label="Continuar" 
                       @click="onCreateNewPersonal" 
                       :disable="!dialogState.selectedSysUser" />
            </q-card-actions>
        </q-card>
    </q-dialog>

</q-layout>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import { useRouter } from 'vue-router'
import {queryUsuarioTable, querySysUsersForSelection} from '../../api/usuario/usuario' 
import {errorMsg} from '../../utils/message'

//import Pagination from '../../components/Pagination.vue' 
import { useQuasar } from 'quasar'

const $q = useQuasar()
const router = useRouter() 

// ESTADO DE LA TABLA
const state = reactive ({
    blurry:'',
    tableData:[],
    current:1,
    size:10, 
    total: 0,
    statusFilter: '', 
    typeFilter: '', 
})

// ESTADO PARA EL DIÁLOGO DE CREACIÓN
const dialogState = reactive({
    isNewUserDialogOpen: false,
    sysUserList: [],           
    selectedSysUser: null      
})

const columns = [
    {name:'idusuario',label:'Id Usuario',align:'center',field:'idusuario',sortable:true},
    {name:'foto',label:'Foto',align:'center',field:'foto'}, 
    {name:'nombre',label:'Nombre',align:'left',field:'nombre',sortable:true},
    {name:'ap',label:'Apellido Paterno',align:'left',field:'ap',sortable:true},
    {name:'am',label:'Apellido Materno',align:'left',field:'am',sortable:true},
    {name:'estado',label:'Estado',align:'center',field:'estado',sortable:true}, 
    {name:'actions', label:'Acciones', align:'center'} 
]
const visibleColumns = ref(['foto','nombre','ap', 'am', 'estado', 'actions'])


// ===================================
// FUNCIONES DE CONSULTA Y FILTROS
// ===================================

const onSearch = () => {
    state.current = 1 
    queryUsuarioTableFun()
}

const onPageChange = ({page, rowsPerPage}) => {
    state.current = page
    state.size = rowsPerPage
    queryUsuarioTableFun()
}

const onStatusFilterChange = () => {
    state.current = 1
    queryUsuarioTableFun()
}

const onTypeFilterChange = () => {
    state.current = 1
    queryUsuarioTableFun()
}

// Funcion principal para consultar la tabla
const queryUsuarioTableFun = () => {
    let statusParam = null;
    if (state.statusFilter === '1' || state.statusFilter === '0') {
        statusParam = parseInt(state.statusFilter);
    } 
    const typeParam = state.typeFilter === '' ? null : state.typeFilter

    const params = {
        blurry: state.blurry,
        size: state.size,
        currentPage: state.current,
        status: statusParam, 
        type: typeParam,    
    }
    
    queryUsuarioTable(params)
    .then((res)=> {
        if (res.success) {
            state.tableData = res.data.records
            state.total = res.data.total
        } else {
            errorMsg(res.mgs || 'Error al cargar los datos de usuario.')
        }
    })
    .catch(()=>{
        errorMsg('Error al cargar los datos')
    })
}


// ===================================
// FUNCIONES DE CREACIÓN/EDICIÓN (UPSERT)
// ===================================

const fetchSysUserList = () => {
    querySysUsersForSelection()
        .then(res => {
            if (res.success && res.data) {
                dialogState.sysUserList = res.data.length > 0 ? res.data : []
            } else {
                errorMsg(res.msg || 'Error al cargar usuarios del sistema.')
                dialogState.sysUserList = []
            }
        })
        .catch(() => {
            errorMsg('Error de conexión al cargar usuarios del sistema.')
            dialogState.sysUserList = []
        })
}

/**
 * Función que maneja la creación o edición.
 */
const onEdit = (row) => {
    if (row && row.idusuario) {
        // Opción 1: MODIFICAR USUARIO EXISTENTE
        router.push({ 
            name: 'EditUsuario', 
            params: { id: row.idusuario } 
        })
    } else {
        // Opción 2: CREAR NUEVO USUARIO (Abrir el diálogo de selección)
        dialogState.isNewUserDialogOpen = true
        dialogState.selectedSysUser = null
        fetchSysUserList() // Cargar la lista de sys_user
    }
}

/**
 * Lógica después de seleccionar el sys_user en el diálogo.
 */
const onCreateNewPersonal = () => {
    if (!dialogState.selectedSysUser) return
    
    // Navegar al formulario de edición/creación con el ID y Username del sys_user
    const selectedUser = dialogState.selectedSysUser
    dialogState.isNewUserDialogOpen = false

    router.push({ 
        name: 'edit-usuario', 
        params: { id: selectedUser.id },
        // Pasamos el username en el query para indicarle al formulario que es una INSERCIÓN
        query: { username: selectedUser.username, isNew: true } 
    })
}

// ===================================
// OTRAS ACCIONES
// ===================================

const onEstado = (row) => {
    const newEstado = row.estado === 1 ? 0 : 1 
    const actionText = newEstado === 1 ? 'habilitar' : 'dar de baja'
    
    $q.dialog({
        title: 'Confirmación',
        message: `¿Está seguro de que desea ${actionText} al usuario ${row.nombre} ${row.ap}?`,
        cancel: true,
        persistent: true
    }).onOk(() => {
        // Llama a la API de cambio de estado (TODO)
        $q.notify({ type: 'positive', message: `Usuario ${actionText === 'habilitar' ? 'habilitado' : 'dado de baja'} con éxito.` })
        queryUsuarioTableFun()
    })
}

const onDelete = (row) => {
    $q.dialog({
        title: 'Confirmar Eliminación',
        message: `¿Confirma que desea **dar de baja** al usuario ${row.nombre} ${row.ap}? Esta es una eliminación lógica.`,
        cancel: true,
        persistent: true
    }).onOk(() => {
        // Llama a la API de eliminación lógica (TODO)
        $q.notify({ type: 'warning', message: `El usuario ${row.nombre} ha sido dado de baja lógicamente.` })
        queryUsuarioTableFun()
    })
}

const onView = (row) => {
    console.log(`Visualizar/Imprimir datos completos del usuario ID: ${row.idusuario}`)
}

onMounted(() => {
    // CORREGIDO: Se usa el nombre de función correcto
    queryUsuarioTableFun() 
})
</script>