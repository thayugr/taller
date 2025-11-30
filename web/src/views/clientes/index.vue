<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
      
      <div class="q-pa-md">
        <div class="searchDiv">
          <q-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese nombre de usuario o apodo" clearable></q-input>
          <q-button type="primary" @click="getUserListFun">Consulta</q-button>
        </div>
        <q-table
          flat
          bordered
          ref="tableRef"
          title="Clientes"
          :rows="state.tableData"
          :columns="columns"
          row-key="id"
          v-model:pagination="pagination"
          :loading="loading"
          :filter="filter"
          binary-state-sort
          @request="onRequest"
        >
          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="state.blurry" placeholder="Buscar">
              <template v-slot:append>
                <q-icon name="search" @click="getClientesListFun" />
              </template>
            </q-input>
          </template>
        </q-table>
        <q-pagination
          v-model="pagination.page"
          :max="Math.ceil(state.total / pagination.rowsPerPage)" 
          @update:model-value="getClientesListFun"
        />
      </div>
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getClientesList } from '../../api/clientes/clientes';
import { errorMsg } from '../../utils/message';

const state = reactive({
  blurry: '',
  tableData: [],
  current: 1,
  size: 10,
  total: 0
});

const columns = [
  { name: 'compania', required: true, label: 'Compañía', align: 'left', field: 'compania', sortable: true },
  { name: 'apellidos', required: true, label: 'Apellidos', align: 'left', field: 'apellidos', sortable: true },
  { name: 'nombre', required: true, label: 'Nombre', align: 'left', field: 'nombre', sortable: true }
];

const loading = ref(false);
const pagination = ref({
  sortBy: 'nombre',
  descending: false,
  page: 1,
  rowsPerPage: 10,
});

const getClientesListFun = () => {
  loading.value = true;
  const params = {
    // texto a buscar
    blurry: state.blurry,
    // tamaño 
    size: state.size,
    // Usa la página actual de la paginación
    currentPage: pagination.value.page 
  };

  getClientesList(params)
    .then((res) => {
      loading.value = false;
      if (res.success) {
        state.tableData = res.data.records;
        state.total = res.data.total;
      } else {
        errorMsg(res.msg);
      }
    })
    .catch(() => {
      loading.value = false;
    });
};

const onRequest = (props) => {
  const { page, rowsPerPage, sortBy, descending } = props.pagination;
  pagination.value.sortBy = sortBy || '';
  pagination.value.descending = descending;
  pagination.value.page = page;
  pagination.value.rowsPerPage = rowsPerPage;

  state.current = page; // Actualiza el estado actual
  state.size = rowsPerPage; // Actualiza el tamaño de la página
  console.log('estado current cliente');
  console.log(state);
  getClientesListFun();
  
};

onMounted(() => {
  getClientesListFun();
  console.log('estado cliente');
  console.log(state);
});
</script>
