<template>
  <q-dialog v-model="open" persistent>
    <q-card style="min-width:420px;max-width:640px">
      <q-card-section class="text-h6">
        {{ isEdit ? 'Modificar Persona' : 'Adicionar Persona' }}
      </q-card-section>

      <q-card-section>
        <q-form @submit.prevent="onSubmit">
          <div class="row q-col-gutter-md">
            <q-input class="col-12" v-model.number="form.idusuario" :disable="isEdit" label="ID Usuario (sys_user.id)*" :rules="[v=>!!v||'Requerido']"/>
            <q-input class="col-12" v-model="form.nombre" label="Nombre*" :rules="[v=>!!v||'Requerido']"/>
            <q-input class="col-6" v-model="form.ap" label="Apellido Paterno"/>
            <q-input class="col-6" v-model="form.am" label="Apellido Materno"/>
            <q-input class="col-6" v-model="form.fnac" label="Fecha Nac. (YYYY-MM-DD)"/>
            <q-input class="col-6" v-model="form.genero" label="Género (M/F)"/>
            <q-input class="col-6" v-model="form.ecivil" label="Estado Civil"/>
            <q-input class="col-6" v-model="form.telf" label="Teléfono"/>
            <q-input class="col-12" v-model="form.dir"  label="Dirección"/>
            <q-input class="col-6" v-model.number="form.cedula"  label="Cédula (acad.datos)"/>
            <q-input class="col-6" v-model="form.tipo" label="Tipo (docente/estudiante)"/>
            <q-input class="col-12" v-model="form.foto" label="Foto (URL)"/>
          </div>
          <div class="row justify-end q-gutter-sm q-mt-md">
            <q-btn flat label="Cancelar" color="primary" @click="close"/>
            <q-btn unelevated label="Guardar" color="primary" type="submit" :loading="loading"/>
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, watch, reactive } from 'vue'
import { createPersona, updatePersona, getPersona } from '@/api/personal/personal'
import { useQuasar } from 'quasar'

const $q = useQuasar()
const props = defineProps({
  modelValue: { type: Boolean, default: false },
  idusuario:  { type: Number, default: null },
  edit:       { type: Boolean, default: false }
})
const emit = defineEmits(['update:modelValue','saved'])

const open = ref(false)
const loading = ref(false)
const isEdit = ref(false)

const form = reactive({
  idusuario: null, nombre:'', ap:'', am:'', estado:null, fnac:'',
  ecivil:'', genero:'', dir:'', telf:'', tipo:'', foto:'', cedula:null
})

watch(() => props.modelValue, async (v) => {
  open.value = v
  isEdit.value = props.edit
  // Si viene desde edición al abrir, precargar
  if (v) {
    if (isEdit.value && props.idusuario) {
      form.idusuario = props.idusuario
      const { data } = await getPersona(props.idusuario)
      if (data) Object.assign(form, { ...form, ...data })
    } else {
      // creación
      Object.assign(form, { idusuario: props.idusuario || null, nombre:'', ap:'', am:'', estado:null, fnac:'',
        ecivil:'', genero:'', dir:'', telf:'', tipo:'', foto:'', cedula:null })
    }
  }
})

function close(){ emit('update:modelValue', false) }

async function onSubmit() {
  try {
    loading.value = true
    const payload = { ...form }
    if (isEdit.value) {
      await updatePersona(form.idusuario, payload)
      $q.notify({ type:'positive', message:'Persona actualizada' })
    } else {
      await createPersona(payload)
      $q.notify({ type:'positive', message:'Persona creada' })
    }
    emit('saved')
    close()
  } catch (e) {
    $q.notify({ type:'negative', message: e?.response?.data?.message || 'Error' })
  } finally {
    loading.value = false
  }
}
</script>