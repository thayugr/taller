<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese un nombre o código de rol" clearable></el-input>
      <el-button type="primary" @click="getRoleListFun">Consulta</el-button>
      <el-button v-if="hasPer('role:add')" @click="editRoleFun" style="float: right;">Nuevo</el-button>
    </div>
    <el-table :data="state.tableData" row-key="id" border height="calc(100vh - 180px)" max-height="calc(100vh - 180px)">
      <el-table-column label="Numero de Serie" type="index" width="60"></el-table-column>
      <el-table-column label="Nombre del personaje" prop="roleName"></el-table-column>
      <el-table-column label="Codigo de Rol" prop="roleCode"></el-table-column>
      <el-table-column label="Descripcion del Rol" prop="description"></el-table-column>
      <el-table-column label="Funcionar" prop="option" width="220px" align="center">
        <template #default="scope">
          <el-button v-if="hasPer('role:authorize')" type="success" @click="authorizeRoleFun(scope.row.id)">Autorizar</el-button>
          <el-button v-if="hasPer('role:edit')" type="primary" @click="editRoleFun(JSON.parse(JSON.stringify(scope.row)))">Editar</el-button>
          <el-button v-if="hasPer('role:del')" type="danger" @click="delRoleFun(scope.row.id, scope.row.roleName)">Borrar</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--Editar-->
    <edit-role v-model:dialog-visible="dialogVisible" :role-obj="state.roleObj" @get-list="getRoleListFun"></edit-role>
    <!--Autorizar-->
    <authorize-role v-model:auth-visible="authVisible" :role-id="state.roleId"></authorize-role>
  </div>
</template>

<script setup>
import editRole from "./editRole";
import authorizeRole from "./authorizeRole";
import {getRoleList, delRole} from "../../api/role/sysRole";
import {errorMsg, infoMsg, successMsg} from "../../utils/message";
import {hasPer} from "../../utils/common";
import {onMounted, reactive, ref} from "vue";
import {ElMessageBox} from "element-plus";

const state = reactive({
  blurry: '',
  tableData: [],
  roleId: null,
  roleObj: {},
})

const dialogVisible = ref(false)

const authVisible = ref(false)

onMounted(() => {
  getRoleListFun()
})

//  Obtener lista de roles
const getRoleListFun = () => {
  getRoleList({blurry: state.blurry}).then(res => {
    if (res.success){
      state.tableData = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}
//  Editar Rol
const editRoleFun = (row) => {
  dialogVisible.value = true
  state.roleObj = row.id ? row : {}
}
// Autorizacion de Rol
const authorizeRoleFun = (id) => {
  authVisible.value = true
  state.roleId = id
}
// Eliminar Rol
const delRoleFun = (id, name) => {
  ElMessageBox.confirm('Confirmar para eliminar rol 【'+name+'】？', 'Pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    delRole({id: id}).then(res => {
      if (res.success){
        successMsg(res.data)
        getRoleListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).catch(() => {
    infoMsg('operacion cancelada')
  })
}
</script>

<style scoped>

</style>