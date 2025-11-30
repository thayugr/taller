<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="min-width: 500px">
      <q-card-section class="text-h6">
        {{ form.idcliente ? 'Editar Cliente' : 'Nuevo Cliente' }}
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input v-model="form.nombre" label="Nombre" />
          <q-input v-model="form.apellidos" label="Apellidos" />
          <q-input v-model="form.compania" label="Compañía" />
          <q-input v-model="form.cargo" label="Cargo" />
          <q-input v-model="form.telefono" label="Teléfono" />
          <q-input v-model="form.email" label="Correo electrónico" />
        </div>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="negative" @click="cerrarDialogo" />
        <q-btn flat label="Guardar" color="primary" @click="guardarCliente" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { reactive, watch, computed } from 'vue'
import { editModulo1 } from '../../../api/modulo1/modulo1'
import { successMsg, errorMsg } from '../../../utils/message'

// Props desde index.vue
const props = defineProps({
  modelValue: { type: Boolean, default: false },
  clienteObj: { type: Object, default: () => ({}) },
})

// Emit para controlar el v-model
const emit = defineEmits(['update:modelValue', 'close'])

// Computed para vincular el diálogo
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

// Estado del formulario
const form = reactive({
  idcliente: null,
  nombre: '',
  apellidos: '',
  compania: '',
  cargo: '',
  telefono: '',
  email: '',
})

// 🔹 Sincroniza datos al abrir
watch(
  () => props.clienteObj,
  (nuevo) => {
    if (nuevo) Object.assign(form, nuevo)
  },
  { immediate: true }
)

// 🔹 Limpia al crear uno nuevo
watch(
  () => props.modelValue,
  (visible) => {
    if (visible && !props.clienteObj?.idcliente) {
      Object.keys(form).forEach((k) => (form[k] = ''))
      form.idcliente = null
    }
  }
)

// 🔹 Cerrar diálogo
const cerrarDialogo = () => {
  emit('update:modelValue', false)
  emit('close')
}

// 🔹 Guardar (nuevo o editar)
const guardarCliente = async () => {
  try {
    if (form.idcliente) {
      // modificar
      const res = await editModulo1(form)
      if (res.success) successMsg('Cliente actualizado correctamente')
      else errorMsg(res.msg || 'Error al actualizar cliente')
    } else {
      // nuevo
      const res = await editModulo1(form)
      if (res.success) successMsg('Cliente registrado correctamente')
      else errorMsg(res.msg || 'Error al registrar cliente')
    }

    cerrarDialogo()
  } catch (err) {
    console.error(err)
    errorMsg('Error al guardar cliente')
  }
}
</script>
