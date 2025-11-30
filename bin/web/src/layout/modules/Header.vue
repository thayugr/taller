<template>
  <div style="width: 100%;">
    <el-row>
      <el-col :span="12">
        <span>Taller 1</span>
      </el-col>
      <el-col :span="12">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">{{username}}<i class="el-icon-arrow-down el-icon--right"></i></span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="pwd">Cambiar la contraseña</el-dropdown-item>
              <el-dropdown-item command="logout">Abandonar</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>
    <update-password v-model:dialog-visible="dialogVisible" @logout="clearToken"></update-password>
  </div>
</template>

<script setup>
import updatePassword from "../../views/user/updatePassword";
import {useStore} from "../../store";
import routers from "../../router/routers";
import {infoMsg} from "../../utils/message";
import {ref} from "vue";
import {ElMessageBox} from "element-plus";

const store = useStore()

const username = ref(store.userInfo.nickName)

const dialogVisible = ref(false)

const handleCommand = (command) => {
  if (command === 'logout'){
    logout()
  } else if (command === 'pwd'){
    dialogVisible.value = true
  }
}
//  abandonar
const logout = () => {
  ElMessageBox.confirm('¿Está seguro de que desea cerrar sesión en su inicio de sesión actual?', 'pista', {
    confirmButtonText: 'Seguro',
    cancelButtonText: 'Cancelar',
    type: 'warning'
  }).then(() => {
    clearToken()
  }).catch(() => {
    infoMsg('Operacion Cancelada')
  })
}
//  Claro token
const clearToken = () => {
  //  Claro token
  store.token = null
  //  Claro refreshToken
  store.refreshToken = null
  //  Saltar a la página de inicio de sesión
  routers.push({path: '/login'})
}
</script>

<style scoped>
  .el-row{
    width: 100%;
  }
  .el-dropdown{
    float: right;
  }
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>