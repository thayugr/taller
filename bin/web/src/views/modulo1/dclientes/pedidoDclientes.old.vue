<template>
  <q-dialog v-model="dialogVisible" persistent>
    <q-card style="width: 80vw; max-width: 1200px;">
      <q-card-section>
        <div class="text-h6">Pedidos del Cliente</div>
      </q-card-section>
<q-table
          color="primary"
          card-class="bg-amber-5 text-brown"
          table-class="text-grey-8"
          table-header-class="text-brown"
          flat
          bordered
          ref="tableRef"
          title="Pedidos"
          :rows="state.tableData"
          :columns="columns"
          row-key="iddepedido"  
          v-model:pagination="pagination"
          :loading="loading"
          :filter="state.blurry"
          binary-state-sort
          :visible-columns="visibleColumns"
        >
          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="state.blurry" placeholder="Buscar">
              <template v-slot:append>
                <q-icon name="search" @click="getModulo1DClientesPedidosTableFun" />
              </template>
            </q-input>
          </template>
          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn @click="onDelete(props.row)" fab-mini dense square outline icon="delete" color="negative" aria-label="Eliminar" />
              <q-btn @click="onPedidos(props.row)" fab-mini dense square outline icon="table_view" color="negative" aria-label="Detalle" />
            </q-td>
          </template>
        </q-table>

        <q-pagination
          v-model="pagination.page"
          :max="Math.ceil(state.total / pagination.rowsPerPage)" 
          @update:model-value="getModulo1DClientesPedidosTableFun"
        />

      
      <q-card-actions align="right">
        <q-btn flat label="Cancelar" color="negative" @click="$emit('close')" />
        <q-btn flat label="Guardar" color="primary" @click="submitForm" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { getModulo1DClientesPedidosTable } from '../../../api/modulo1/modulo1';
import { errorMsg } from '../../../utils/message';
import { defineProps } from 'vue';
//const props = defineProps(['cliente-obj', 'modelValue']);
const props = defineProps ({
  clienteObj: {
    type: Object,
    required: true, // Si es obligatorio recibirla
  }
});

const emit = defineEmits(['close', 'update:modelValue']);

const dialogVisible = ref(props.modelValue);

watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal;
});

watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal);
});
watch(() => props.clienteObj, (newVal) => {
  if (newVal && newVal.idcliente) {
    getModulo1DClientesPedidosTableFun(); // Ejecutar cuando `clienteObj` esté disponible
  }
})

const columns = [
  { name: 'iddepedido', label: 'Id de Pedido', align: 'center', field: 'iddepedido', sortable: true },
  { name: 'actions', label: 'Acciones', align: 'center', field: 'actions', sortable: false },
];
const visibleColumns = ref(['iddepedido', 'actions']);

const state = reactive({
  tableData: [],
  blurry: '',
  total: 0,
});

const pagination = ref({
  page: 1,
  rowsPerPage: 10,
});

const loading = ref(false);

const getModulo1DClientesPedidosTableFun = () => {
  if (!props.clienteObj || !props.clienteObj.idcliente) {
    console.error('No se recibió el `idcliente`.');
    return;
  }

  loading.value = true;
  const params = {
    id: props.clienteObj.idcliente,
    blurry: state.blurry,
    currentPage: pagination.value.page,
    size: pagination.value.rowsPerPage,
  };

  getModulo1DClientesPedidosTable(params)
    .then((res) => {
      loading.value = false;
      if (res.success) {
        state.tableData = res.data.records;
        state.total = res.data.total;
      } else {
        errorMsg(res.msg);
      }
    })
    .catch((error) => {
      loading.value = false;
      console.error('Error en la solicitud:', error);
    });
};

onMounted(() => {
  console.log('ingreso a onMounted');
  getModulo1DClientesPedidosTableFun();
});

const submitForm = () => {
  console.log('Formulario enviado');
};
</script>