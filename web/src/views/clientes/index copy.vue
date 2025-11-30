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
          :rows-per-page-options="[0]"
          flat
        >
          <!-- Encabezados de la tabla -->
          <q-tr slot="header" class="bg-grey-2 text-bold">
            <q-th label="Compañía">Compañía</q-th>
            <q-th label="Apellidos">Apellidos</q-th>
            <q-th label="Nombre">Nombre</q-th>
            <q-th label="Dirección">Dirección</q-th>
            <q-th label="Ciudad">Ciudad</q-th>
            <q-th label="Acciones" align="center">Acciones</q-th>
          </q-tr>

          <!-- Filas de la tabla -->
          <q-tr v-for="row in state.tableData" :key="row.id">
            <q-td>{{ row.Compañia }}</q-td>
            <q-td>{{ row.Apellidos }}</q-td>
            <q-td>{{ row.Nombre }}</q-td>
            <q-td>{{ row.Direccion }}</q-td>
            <q-td>{{ row.Ciudad }}</q-td>
            <q-td align="center">
              <q-btn label="Editar" color="primary" @click="editClienteFun(row)" />
              <q-btn label="Eliminar" color="negative" @click="delClienteFun(row.id, row.Nombre)" />
            </q-td>
          </q-tr>
        </q-table>

        <!-- Componente de edición de cliente -->
        <edit-cliente v-model:dialog-visible="dialogVisible" :cliente-obj="state.clienteObj" @get-list="getClientesListFun"></edit-cliente>
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
  tableData: [],
  clienteObj: {}
});

const dialogVisible = ref(false);

onMounted(() => {
  getClientesListFun();
});

// Obtener la lista de clientes
const getClientesListFun = () => {
  getClientesList({ blurry: state.blurry }).then(res => {
    if (res.success) {
      state.tableData = res.data;
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

  