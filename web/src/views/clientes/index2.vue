<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
      <q-page class="q-pa-md">
        <div class="searchDiv q-gutter-sm row items-center">
          <q-input class="searchInput" v-model="state.blurry" placeholder="Ingrese nombre o ID de cliente" clearable />
          <q-btn label="Consultas" color="primary" @click="getClientesListFun" />
          <q-btn v-if="hasPer('clientes:add')" label="Nuevo Cliente" color="primary" @click="editClienteFun" class="q-ml-auto" />
        </div>

        <q-table
          :rows="state.tableData"
          row-key="id"
          class="q-mt-md"
          :row-key="name"
          rows-per-page-label="Paginas:"
        >
          
        </q-table>
        
      
    <!--editar-->

        <!-- Componente de edición de cliente -->
        <edit-cliente v-model:dialog-visible="dialogVisible" :cliente-obj="state.clientesObj" @get-list="getClientesListFun"></edit-cliente>
        
      </q-page>
    </q-page-container>
    
  </q-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getClientesList } from '../../api/clientes/clientes'; // API de clientes
import { errorMsg, infoMsg, successMsg } from '../../utils/message';
import { hasPer } from '../../utils/common'; // Para validar permisos
import { Dialog } from 'quasar';

const state = reactive({
  blurry: '',
  tableData: [], // Asegúrate de inicializarlo como un array vacío
  clienteObj: {},
  current: 1,
  size: 10,
  total: 0
});

const dialogVisible = ref(false);

onMounted(() => {
  getClientesListFun();
});

// Obtener la lista de clientes

const getClientesListFun = () => {
  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current
  }
    getClientesList(params).then(res => {
    if (res.success) {
      state.tableData = res.data.records; // Asegúrate de que res.data sea un array
      state.total = res.data.total
    } else {
      errorMsg(res.msg);
    }
  });
};

// Eliminar cliente
const delClienteFun = (id, name) => {
  Dialog.create({
    title: 'Confirmación',
    message: `¿Estás seguro de eliminar el cliente ${name}?`,
    cancel: true,
    persistent: true
  }).onOk(() => {
    delCliente({ id }).then(res => {
      if (res.success) {
        successMsg(res.data);
        getClientesListFun();
      } else {
        errorMsg(res.msg);
      }
    });
  }).onCancel(() => {
    infoMsg('Operación cancelada');
  });
};
</script>

<style scoped>
.searchDiv {
  margin-bottom: 20px;
}
</style>
