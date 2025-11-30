<template>
<!--dclientes-->
  <q-layout view="hHh lpR fFf">
     <q-page-container>
      <div id="q-app" style="min-height: 100vh;">
<div class="q-pa-md full-width full-height">
        <q-table
         class="my-sticky-header-column-table"
      flat bordered
          title="Detalle de Clientes"
          color="primary"
          
          table-class="text-grey-8"
          table-header-class="text-brown"
        
          ref="tableRef"
          :rows="state.tableData"
          :columns="columns"
          row-key="idcliente"
          virtual-scroll
          :loading="loading"
          :filter="state.blurry"
          binary-state-sort
          :visible-columns="visibleColumns"
          :rows-per-page-options="[0]"
        >
          <template v-slot:top-left>
            <q-btn
              color="primary"
              icon="add"
              label="Nuevo Cliente"
              @click="onEdit()"
            />
            
          </template>

          <template v-slot:top-right>
            <q-input
              borderless
              dense
              debounce="300"
              v-model="state.blurry"
              placeholder="Buscar"
            >
              <template v-slot:append>
                <q-icon name="search" @click="getModulo1DClientesTableFun" />
              </template>
            </q-input>
          </template>

          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn
                @click="onEdit(props.row)"
                fab-mini
                dense
                square
                outline
                icon="mode_edit"
                color="primary"
                aria-label="Editar"
                class="q-mr-sm"
              />
              <q-btn
                @click="onPedidos(props.row)"
                fab-mini
                dense
                square
                outline
                icon="table_view"
                color="negative"
                aria-label="Detalle"
              />
            </q-td>
          </template>
        </q-table>
    </div>    
         <pagination 
    v-model:current="state.current" 
    v-model:size="state.size" 
    v-model:total="state.total" 
    @get-list="getModulo1DClientesTableFun">
  </pagination>

    
      </div>

      <!-- Componente de pedidos -->
      <pedidoDclientes
        v-model="pedidoDialogVisible"
        :cliente-obj="state.selectedCliente"
        @close="pedidoDialogVisible = false"
      />

      <!-- Componente de edición -->
      <editDClientes
        v-model="clienteDialogVisible"
        :cliente-obj="state.selectedCliente"
        @close="onDialogClose"
      />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getModulo1DClientesTable } from '../../../api/modulo1/modulo1'
import { errorMsg } from '../../../utils/message'
import pedidoDclientes from './pedidoDclientes.vue'
import editDClientes from './editDClientes.vue'
import Pagination from '../../../components/Pagination.vue'

const state = reactive({
  blurry: '',
  tableData: [],
  userObj: {},
  current: 1,
  size: 10,
  total: 0,
  selectedCliente: null,
})

const columns = [
  { name: 'idcliente', label: 'ID Cliente', align: 'center', field: 'idcliente', sortable: true },
  { name: 'nombre', label: 'Nombre', align: 'left', field: 'nombre', sortable: true },
  { name: 'actions', label: 'Acciones', align: 'center', field: 'actions', sortable: false },
]

const visibleColumns = ref(['idcliente', 'nombre', 'actions'])
const loading = ref(false)
// const pagination = ref({ sortBy: 'nombre', descending: false, page: 1, rowsPerPage: 10 })
const pedidoDialogVisible = ref(false)
const clienteDialogVisible = ref(false)

// 🔹 Cargar tabla
const getModulo1DClientesTableFun = () => {
  loading.value = true
  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current
  }
  getModulo1DClientesTable(params)
    .then((res) => {
      loading.value = false
      if (res.success) {
        state.tableData = res.data.records
        state.total = res.data.total
      } else {
        errorMsg(res.msg)
      }
    })
    .catch(() => {
      loading.value = false
      errorMsg('Error al cargar los datos')
    })
}

// 🔹 Ver pedidos
const onPedidos = (row) => {
  if (!row || !row.idcliente) return
  state.selectedCliente = { ...row }
  pedidoDialogVisible.value = true
}

// 🔹 Editar o crear cliente
const onEdit = (row) => {
  if (!row) {
    // Nuevo cliente
    state.selectedCliente = {}
  } else {
    // Editar
    state.selectedCliente = { ...row }
  }
  clienteDialogVisible.value = true
}

// 🔹 Al cerrar el diálogo, recargar la tabla
const onDialogClose = () => {
  clienteDialogVisible.value = false
  getModulo1DClientesTableFun()
}

onMounted(() => {
  getModulo1DClientesTableFun()
})
</script>
<style lang="sass">
.my-sticky-header-column-table
  height: 80vh
  width: 100%
  max-width: 100%
  background: white

  tr th
    position: sticky
    z-index: 2
    background: #00b4ff

  thead tr:first-child th
    top: 0
    z-index: 3

  td:first-child, th:first-child
    position: sticky
    left: 0
    background-color: #e0f7ff
</style>
