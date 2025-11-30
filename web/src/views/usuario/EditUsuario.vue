<template>
    <q-page padding>
        <div class="q-pa-md">
            <q-card flat bordered>
                <q-card-section class="bg-primary text-white">
                    <div class="text-h6">
                        {{ isNew ? 'Crear Nuevo Registro de Personal' : 'Modificar Datos de Persona' }}
                    </div>
                    <div class="text-subtitle1">
                        Usuario Base: **{{ state.usuarioDto.username || 'Cargando...' }}** (ID: {{ state.usuarioDto.idusuario }})
                    </div>
                </q-card-section>

                <q-separator />

                <q-card-section class="q-pa-lg">
                    <q-form @submit="onSubmit" class="q-gutter-md">
                        <div class="row q-col-gutter-md">
                            
                            <div class="col-xs-12 col-sm-6">
                                <q-input
                                    filled
                                    v-model.number="state.usuarioDto.cedula"
                                    label="Cédula"
                                    hint="Campo de la tabla academico.datos"
                                    type="number"
                                />
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <q-input
                                    filled
                                    v-model="state.usuarioDto.nombre"
                                    label="Nombre *"
                                    lazy-rules
                                    :rules="[val => val && val.length > 0 || 'El nombre es requerido']"
                                />
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.ap" label="Apellido Paterno" />
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.am" label="Apellido Materno" />
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.fnac" mask="date" :rules="['date']" label="Fecha de Nacimiento">
                                    <template v-slot:append>
                                        <q-icon name="event" class="cursor-pointer">
                                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                                <q-date v-model="state.usuarioDto.fnac" mask="YYYY-MM-DD" /> 
                                            </q-popup-proxy>
                                        </q-icon>
                                    </template>
                                </q-input>
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <q-select
                                    filled
                                    v-model="state.usuarioDto.tipo"
                                    :options="['ESTUDIANTE', 'DOCENTE', 'ADMINISTRATIVO']"
                                    label="Tipo de Usuario"
                                />
                            </div>
                            
                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.ecivil" label="Estado Civil" />
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.genero" label="Género" />
                            </div>
                            <div class="col-xs-12">
                                <q-input filled v-model="state.usuarioDto.dir" label="Dirección" />
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.telf" label="Teléfono" />
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <q-input filled v-model="state.usuarioDto.foto" label="URL Foto" />
                            </div>
                        </div>
                        
                        <q-card-actions align="right">
                            <q-btn label="Cancelar" type="button" color="negative" flat @click="router.back()" />
                            <q-btn label="Guardar Datos" type="submit" color="primary" :loading="state.loading" />
                        </q-card-actions>
                    </q-form>
                </q-card-section>
            </q-card>
        </div>
    </q-page>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUsuarioDetails, editUsuario } from '../../api/usuario/usuario'
import { successMsg, errorMsg } from '../../utils/message'
import { useQuasar } from 'quasar'

const $q = useQuasar()
const route = useRoute()
const router = useRouter()

const state = reactive({
    usuarioDto: {
        idusuario: null,
        username: '',
        cedula: null,
        nombre: '',
        ap: '',
        am: '',
        estado: 1, 
        fnac: null, 
        ecivil: '',
        genero: '',
        dir: '',
        telf: '',
        tipo: 'ESTUDIANTE',
        foto: '',
    },
    loading: false
})

const idusuario = computed(() => route.params.id)

// Determina si es un nuevo registro
const isNew = computed(() => route.query.isNew === 'true')


const fetchUsuarioData = () => {
    state.loading = true
    getUsuarioDetails(idusuario.value)
        .then(res => {
            if (res.success && res.data) {
                // Modo EDICIÓN: Cargar datos existentes
                state.usuarioDto = {
                    ...res.data,
                    fnac: res.data.fnac ? res.data.fnac.toString() : null
                }
            } else {
                errorMsg(res.msg || 'Error al cargar detalles del usuario.')
                router.back()
            }
        })
        .catch((err) => {
            const msg = err.response && err.response.data && err.response.data.msg ? err.response.data.msg : 'Error desconocido de red.'
            errorMsg('Error al cargar datos: ' + msg)
            router.back()
        })
        .finally(() => {
            state.loading = false
        })
}

const onSubmit = () => {
    $q.dialog({
        title: isNew.value ? 'Confirmar Creación' : 'Confirmar Modificación',
        message: `¿Confirma ${isNew.value ? 'la creación' : 'la modificación'} de los datos personales para el usuario ${state.usuarioDto.username}?`,
        cancel: true,
        persistent: true
    }).onOk(() => {
        state.loading = true
        
        editUsuario(state.usuarioDto)
            .then(res => {
                if (res.success) {
                    successMsg(`Datos de usuario ${isNew.value ? 'creados' : 'actualizados'} correctamente.`)
                    router.push({ name: 'listar-usuarios' }) 
                } else {
                    errorMsg(res.msg || 'Error al guardar los datos.')
                }
            })
            .catch(err => {
                 const msg = err.response && err.response.data && err.response.data.msg ? err.response.data.msg : 'Error al conectar con el servidor.'
                 errorMsg('Error: ' + msg)
            })
            .finally(() => {
                state.loading = false
            })
    })
}

onMounted(() => {
    if (idusuario.value) {
        if (isNew.value) {
             // Modo CREACIÓN: Inicializar con el ID y Username del sys_user
             state.usuarioDto.idusuario = idusuario.value
             state.usuarioDto.username = route.query.username
        } else {
             // Modo EDICIÓN: Cargar datos del servidor
             fetchUsuarioData()
        }
    } else {
        // ID no proporcionado, regresa
        router.back()
    }
})
</script>