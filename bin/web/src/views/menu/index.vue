<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese un nombre de menú o ruta" clearable></el-input>
      <el-button type="primary" @click="getMenuList">Consulta</el-button>
      <el-button v-if="hasPer('menu:add')" @click="editMenuFun" style="float: right;">Nuevo</el-button>
    </div>
    <el-table :data="state.tableData" row-key="id" border height="calc(100vh - 180px)" max-height="calc(100vh - 180px)">
      <el-table-column label="Nombre del Menu" prop="name"></el-table-column>
      <el-table-column label="ruta del menú" prop="path"></el-table-column>
      <el-table-column label="component" prop="component"></el-table-column>
      <el-table-column label="Permisos" prop="permission"></el-table-column>
      <el-table-column label="Tipo" prop="type">
        <template #default="scope">
          <span v-if="scope.row.type === '1'">Menu</span>
          <span v-else-if="scope.row.type === '2'">Pagina</span>
          <span v-else-if="scope.row.type === '3'">Boton</span>
        </template>
      </el-table-column>
      <el-table-column label="Clasificar" prop="sort"></el-table-column>
      <el-table-column label="Funcionar" prop="option" width="150px" align="center">
        <template #default="scope">
          <el-button v-if="hasPer('menu:edit')" type="primary" @click="editMenuFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('menu:del')" type="danger" @click="delMenuFun(scope.row.id, scope.row.name)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-menu v-model:dialog-visible="dialogVisible" :menu-obj="state.menuObj" @get-list="getMenuList"></edit-menu>
  </div>
</template>

<script setup>
import {getMenuTable, delMenu} from "../../api/menu/sysMenu";
import {errorMsg, infoMsg, successMsg} from "../../utils/message";
import {hasPer} from "../../utils/common";
import editMenu from "./editMenu";
import {onMounted, reactive, ref} from "vue";
import {ElMessageBox} from "element-plus";

const dialogVisible = ref(false)

const state = reactive({
  blurry: '',
  menuObj: {},
  tableData: []
})

onMounted(() => {
  getMenuList()
})

//  Obtener lista de menú
const getMenuList = () => {
  getMenuTable({blurry: state.blurry}).then(res => {
    if (res.success){
      state.tableData = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}
//  Editar Menu
const editMenuFun = (row) => {
  dialogVisible.value = true
  state.menuObj = row.id ? row : {}
  console.log('hizo click menu')
}
//  Eliminar Menu
const delMenuFun = (id, name) => {
  ElMessageBox.confirm('Confirmar eliminar menú【'+ name + '】？', 'Pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    delMenu({id: id}).then(res => {
      if (res.success){
        successMsg(res.data)
        getMenuList()
      } else {
        errorMsg(res.msg)
      }
    })
  }).catch(() => {
    infoMsg('Operacion Cancelada')
  })
}
</script>

<style scoped>

</style>