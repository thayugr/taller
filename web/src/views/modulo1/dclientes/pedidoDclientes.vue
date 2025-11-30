<template>
    <q-dialog v-model="dialogVisible" persistent>
      <q-card style="width: 80vw; max-width: 1200px;">
        <q-card-section>
          <div class="text-h6">Pedidos del Cliente</div>
        </q-card-section>
  
        <q-table
          :rows="state.tableData"
          :columns="columns"
          :loading="loading"
          row-key="iddepedido"
          color="primary"
          flat
          bordered
        >
          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="state.blurry" placeholder="Buscar">
              <template v-slot:append>
                <q-icon name="search" @click="getModulo1DClientesPedidosTableFun" />
              </template>
            </q-input>
          </template>
        </q-table>
  
        <q-pagination
          v-model="pagination.page"
          :max="Math.ceil(state.total / pagination.rowsPerPage)"
          @update:model-value="getModulo1DClientesPedidosTableFun"
        />
  
        <q-card-actions align="right">
          <q-btn flat label="Cerrar" color="negative" @click="$emit('close')" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </template>
  
  <script setup>

  import { ref, reactive, watch } from 'vue';
  import { getModulo1DClientesPedidosTable } from '../../../api/modulo1/modulo1';
  import { errorMsg } from '../../../utils/message';
  import { date } from 'quasar';
  console.log('ingreso aqui');
  const props = defineProps({
    clienteObj: { type: Object, default: () => ({}) },
    modelValue: { type: Boolean, default: false },
  });
  console.log('clienteObj');
  console.log(props.clienteObj);
  
  const emit = defineEmits(['close', 'update:modelValue']);
  
  const dialogVisible = ref(props.modelValue);
  watch(() => props.modelValue, (newVal) => (dialogVisible.value = newVal));
  watch(dialogVisible, (newVal) => emit('update:modelValue', newVal));
  
  const columns = [
    { name: 'iddepedido', label: 'ID Pedido', align: 'center', field: 'iddepedido', sortable: true },
    { name: 'Fecha de pedido', required: true, label: 'Fecha de pedido', align: 'left', field: 'fechadepedido', sortable: true, format: val => date.formatDate(val, 'DD-MM-YYYY')   },
    { name: 'Fecha de envio', required: true, label: 'Fecha de envio', align: 'left', field: 'fechadeenvio', sortable: true, format: val => date.formatDate(val, 'DD-MM-YYYY')   },
    { name: 'subtotal', label: 'Sub Total', align: 'center', field: 'subtotal', sortable: true },
    { name: 'totalpedido', label: 'Total Pedido', align: 'center', field: 'totalpedido', sortable: true },
    { name: 'gastosdeenvio', label: 'Gastos de Envio', align: 'center', field: 'gastosdeenvio', sortable: true },
    
    
];
  const state = reactive({
    tableData: [],
    blurry: '',
    total: 0,
  });
  
  const pagination = ref({ page: 1, rowsPerPage: 10 });
  const loading = ref(false);
  
  
  const getModulo1DClientesPedidosTableFun = () => {
    if (!props.clienteObj.idcliente) return;
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
          console.log('res.data');
          console.log(res.data);  
          state.tableData = res.data || [];
          state.total = res.data.total || 0;
          dialogVisible = true;
        } else {
          errorMsg(res.msg);
        }
      })
      .catch(() => {
        loading.value = false;
        errorMsg('Error al cargar los pedidos');
      });
  };
  
  watch(
    () => props.clienteObj,
    (newVal) => {
      if (newVal && newVal.idcliente) {
        //console.log('Cliente seleccionado:', newVal);
        getModulo1DClientesPedidosTableFun();
      }
    },
    { immediate: true }
  );
  </script>
  