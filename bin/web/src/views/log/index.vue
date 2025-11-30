<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese el operador o descripción" clearable></el-input>
      <el-select class="searchInput" v-model="state.logType" placeholder="Por favor seleccione el tipo de registro" clearable>
        <el-option value="1" label="exito"></el-option>
        <el-option value="2" label="fallar"></el-option>
      </el-select>
      <el-button type="primary" @click="getLogs">consulta</el-button>
    </div>
    <el-table :data="state.tableData" row-key="id" border>
      <el-table-column label="numero de serie" type="index" width="60">
        <template #default="scope">
          <span>{{(state.current - 1) * state.size + 1 + scope.$index}}</span>
        </template>
      </el-table-column>
      <el-table-column label="operador" prop="username" width="100"></el-table-column>
      <el-table-column label="instrucciones de funcionamiento" prop="description" show-overflow-tooltip width="150"></el-table-column>
      <el-table-column label="metodo de solicitud" prop="method" show-overflow-tooltip></el-table-column>
      <el-table-column label="solicitad parametros" prop="params" show-overflow-tooltip></el-table-column>
      <el-table-column label="IP" prop="ip" width="120"></el-table-column>
      <el-table-column label="tipo de registro" prop="logType" width="100">
        <template #default="scope">
          <span>{{scope.row.logType === '1' ? 'exito' : 'fallar'}}</span>
        </template>
      </el-table-column>
      <el-table-column label="detaller del error" prop="exceptionDetail" show-overflow-tooltip>
        <!--Limite la longitud de la visualización del error a 100 para evitar que el contenido sea demasiado largo y provoque fluctuaciones en la página debido al error en show-tooltip-when-overflow.-->
        <template #default="scope">
          <span>{{scope.row.exceptionDetail ? scope.row.exceptionDetail.substring(0, 100) : ''}}</span>
        </template>
      </el-table-column>
      <el-table-column label="La solicitud lleva tiempo" prop="time" width="100"></el-table-column>
      <el-table-column label="tiempo de funcionamiento" prop="createTime" width="150"></el-table-column>
      <el-table-column label="funcionar" prop="option" width="120" align="center">
        <template #default="scope">
          <el-button type="primary" :disabled="scope.row.logType === '1'" @click="showErrorDetails(scope.row.exceptionDetail)">Detaller del Error</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--    paginacion -->
    <pagination v-model:current="state.current" v-model:size="state.size" v-model:total="state.total" @get-list="getLogs"></pagination>
    <error-detail v-model:dialog-visible="dialogVisible" :msg="state.msg"></error-detail>
  </div>
</template>

<script setup>
import {getLogList} from "../../api/log/sysLog";
import {errorMsg} from "../../utils/message";
import Pagination from "../../components/Pagination";
import ErrorDetail from "./ErrorDetail";
import {onMounted, reactive, ref} from "vue";

const state = reactive({
  blurry: '',
  msg: null,
  tableData: [],
  logType: '',
  current: 1,
  size: 10,
  total: 0
})

onMounted(() => {
  getLogs()
})

const dialogVisible = ref(false)

const getLogs = () => {
  const params = {
    blurry: state.blurry,
    currentPage: state.current,
    size: state.size,
    logType: state.logType
  }
  getLogList(params).then(res => {
    if (res.success){
      state.tableData = res.data.records
      state.total = res.data.total
    } else {
      errorMsg(res.msg)
    }
  })
}
//  Mostrar detalles del mensaje de error
const showErrorDetails = (msg) => {
  dialogVisible.value = true
  state.msg = msg
}
</script>

<style scoped>
.el-tooltip__popper{
  margin-left: 20px;
}
</style>