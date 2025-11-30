<template>
  <q-dialog v-model="dialogVisible">
    <q-card class="myClass">
      <q-card-section>
        <div class="text-h6">{{ state.title }}</div>
      </q-card-section>

      <q-card-section>
        <q-form @submit="submitForm">
          <q-input v-model="state.form.idcliente" label="Id" :disable="true" />  <!-- Campo Id deshabilitado -->
          <q-input v-model="state.form.compania" label="Compañía" />
          <q-input v-model="state.form.apellidos" label="Apellidos" />
          <q-input v-model="state.form.nombre" label="Nombre" />
          <q-input v-model="state.form.numerofax" label="Numero fax" />
          <!-- ...otros campos del formulario... -->
        </q-form>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="negative" @click="visible = false" />
        <q-btn flat label="Guardar" color="primary" @click="submitForm" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue';
import { editCliente } from '../../../api/modulo1/modulo1';
const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  clienteObj: {
    type: Object,
    required: true,
  }
});

const emit = defineEmits(['update:dialogVisible', 'get-list']);
const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
});

const state = reactive({
  title: 'Editar Cliente',
  form: {
    idcliente: null,
    compania: '',
    apellidos: '',
    nombre: '',
    email: '',
    cargo: '',
    telefonoTrabajo: '',
    telefonoParticular: '',
    telefonoMovil: '',
    numeroFax: '',
    direccion: '',
    ciudad: '',
    estadoProvincia: '',
    codigoPostal: '',
    paisRegion: '',
    paginaWeb: '',
    notas: '',
    datosAdjuntos: ''
  }
});


// Observa los cambios en el cliente seleccionado
watch(() => props.clienteObj, (newCliente) => {
  if (newCliente && newCliente.idcliente) {
    openFun();  // Actualiza los datos del formulario
  }
});

const openFun = () => {
  if (props.clienteObj.idcliente) {
    state.title = 'Editar Cliente';
    state.form = { ...props.clienteObj };  // Asigna los datos del cliente al formulario
  }
  const dialogVisible = ref(props.modelValue);

};

const submitForm = () => {
  emit('get-list', state.form);  // Envía los datos actualizados

  visible.value = false;
  console.log('se presiono aceptar');
  editCliente(state.form) .then(res => {
    if (res.success) {
     
      console.log('los datos se guardaron correctamente');
    } else {
      errorMsg(res.msg);
    }

  } ).catch(() => {
    loading.value = false;
  });
};
</script>

<style>
.myClass {
  max-width: 1200px;
  width: 100%;
}
</style>
