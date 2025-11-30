<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
  <q-page class="q-pa-md">
    <div class="row items-center q-gutter-sm q-mb-sm">
      <q-input dense filled debounce="400" v-model="blurry" placeholder="Buscar (nombre, ap, am, telf, cédula)"
               class="col-12 col-md-6"
               @update:model-value="reloadFirstPage"/>
      <q-space/>
      <q-btn color="primary" icon="person_add" label="Nueva persona" @click="openCreate"/>
    </div>

    <q-table
      :rows="rows"
      :columns="columns"
      row-key="idusuario"
      :loading="loading"
      :pagination="pagination"
      @request="onRequest"
      flat bordered
    >
      <template #body-cell-actions="{ row }">
        <q-td>
          <q-btn flat round icon="edit"     size="sm" @click="openEdit(row)"/>
          <q-btn flat round icon="delete"   size="sm" color="negative" @click="onDelete(row)"/>
        </q-td>
      </template>
    </q-table>

    <PersonaDialog v-model="dialogOpen"
                   :idusuario="selectedId"
                   :edit="isEdit"
                   @saved="reloadCurrentPage"/>
  </q-page>
   </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { pagePersonas, deletePersona } from '@/api/personal/personal'
import { useQuasar } from 'quasar'
import PersonaDialog from './PersonaDialog.vue'

const $q = useQuasar()

const columns = [
  { name:'idusuario', label:'ID Usuario', field:'idusuario', align:'left', sortable:true },
  { name:'nombre', label:'Nombre', field:'nombre', sortable:true },
  { name:'ap', label:'Apellido Paterno', field:'ap', sortable:true },
  { name:'am', label:'Apellido Materno', field:'am', sortable:true },
  { name:'telf', label:'Teléfono', field:'telf' },
  { name:'cedula', label:'Cédula', field:'cedula' },
  { name:'tipo', label:'Tipo', field:'tipo' },
  { name:'actions', label:'Acciones', field:'actions', align:'center' }
]

const rows = ref([])
const loading = ref(false)
const blurry = ref('')

const pagination = reactive({
  page: 1,
  rowsPerPage: 10,
  rowsNumber: 0,
  sortBy: 'nombre',
  descending: false
})

async function fetchPage() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.rowsPerPage,
      blurry: blurry.value || undefined
    }
    const { data } = await pagePersonas(params)
    rows.value = data.records || []
    pagination.rowsNumber = Number(data.total || 0)
  } finally {
    loading.value = false
  }
}

function onRequest(props) {
  const { page, rowsPerPage } = props.pagination
  pagination.page = page
  pagination.rowsPerPage = rowsPerPage
  fetchPage()
}

function reloadFirstPage() {
  pagination.page = 1
  fetchPage()
}

function reloadCurrentPage() {
  fetchPage()
}

const dialogOpen = ref(false)
const isEdit = ref(false)
const selectedId = ref(null)

function openCreate() {
  isEdit.value = false
  selectedId.value = null // se pedirá en el form el idusuario (o se elige del listado de usuarios)
  dialogOpen.value = true
}

function openEdit(row) {
  isEdit.value = true
  selectedId.value = row.idusuario
  dialogOpen.value = true
}

function onDelete(row) {
  $q.dialog({
    title: 'Confirmar',
    message: `¿Eliminar persona de usuario #${row.idusuario}?`,
    ok: true,
    cancel: true
  }).onOk(async () => {
    await deletePersona(row.idusuario)
    $q.notify({ type: 'positive', message: 'Persona eliminada' })
    reloadCurrentPage()
  })
}

// carga inicial
fetchPage()
</script>