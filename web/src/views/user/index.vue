<template>
  <div>
    <div class="searchDiv">
      <el-input class="searchInput" v-model="state.blurry" placeholder="Por favor ingrese nombre de usuario o apodo" clearable></el-input>
      <el-button type="primary" @click="getUserListFun">Consulta</el-button>
      <el-button v-if="hasPer('user:add')" @click="editUserFun" style="float: right;">Nuevo</el-button>
    </div>
    <el-table :data="state.tableData" row-key="id" border>
      <el-table-column label="nombre de usuario" prop="username"></el-table-column>
      <el-table-column label="Apodo" prop="nickName"></el-table-column>
      <el-table-column label="Role" prop="roles"></el-table-column>
      <el-table-column label="Estado" prop="enabled" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.enabled" type="success">permitir</el-tag>
          <el-tag v-else type="danger">desactivar</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="funcionar" prop="option" width="220px" align="center">
        <template #default="scope">
          <span v-if="scope.row.username !== state.username">
            <el-button v-if="hasPer('user:enabled')" :type="buttonType(scope.row.enabled)"
                       @click="enabledUserFun(JSON.parse(JSON.stringify(scope.row)))">
              {{scope.row.enabled ? 'desactivar' : 'permitir'}}</el-button>
            <el-button v-if="hasPer('user:edit')" type="primary" @click="editUserFun(JSON.parse(JSON.stringify(scope.row)))">editar</el-button>
            <el-button v-if="hasPer('user:del')" type="danger" @click="deleteUser(scope.row.id, scope.row.username)">borrar</el-button>
          </span>
        </template>
      </el-table-column>
    </el-table>
    <!--paginacion-->
    <pagination 
    v-model:current="state.current" 
    v-model:size="state.size" 
    v-model:total="state.total" 
    @get-list="getUserListFun">
  </pagination>
    <!--editar-->
    <edit-user v-model:dialog-visible="dialogVisible" :user-obj="state.userObj" @get-list="getUserListFun"></edit-user>
  </div>
</template>

<script setup>
import editUser from "./editUser";
import Pagination from "../../components/Pagination";
import {getUserList, delUser, enabledUser} from "../../api/user/sysUser";
import {errorMsg, infoMsg, successMsg} from "../../utils/message";
import {hasPer} from "../../utils/common";
import {useStore} from "../../store";
import {onMounted, reactive, ref} from "vue";
import {ElMessageBox} from "element-plus";

const store = useStore()

const state = reactive({
  blurry: '',
  username: store.userInfo.username,
  tableData: [],
  userObj: {},
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)

onMounted(() => {
  getUserListFun()
})

const getUserListFun = () => {
  const params = {
    blurry: state.blurry,
    size: state.size,
    currentPage: state.current
  }
  getUserList(params).then(res => {
    if (res.success){
      state.tableData = res.data.records
      state.total = res.data.total
    }
  })
}

const editUserFun = (row) => {
  dialogVisible.value = true
  state.userObj = row.id ? row : {}
}

const deleteUser = (id, username) => {
  ElMessageBox.confirm('Confirmar para eliminar usuario 【' + username + '】？', 'pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    delUser({id: id}).then(res => {
      if (res.success){
        successMsg(res.data)
        getUserListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).catch(() => {
    infoMsg('Operacion Cancelada')
  })
}
//  Activar/desactivar usuarios
const enabledUserFun = (row) => {
  row.enabled = !row.enabled
  const str = row.enabled ? 'permitir' : 'desactivar'
  const color = row.enabled ? '#67C23A' : '#F56C6C'
  ElMessageBox.confirm('Seguro <span style="color: '+color+'">' + str + '</span>usuario【' + row.nickName + '】？', 'pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning',
    dangerouslyUseHTMLString: true
  }).then(() => {
    enabledUser(row).then(res => {
      if (res.success){
        successMsg(res.data)
        getUserListFun()
      } else {
        errorMsg(res.msg)
      }
    })
  }).catch(() => {
    infoMsg('Operacion Cancelada')
  })
}

const buttonType = (type) => {
  if (type){
    return 'warning'
  } else {
    return 'success'
  }
}
</script>

<style scoped>

</style>