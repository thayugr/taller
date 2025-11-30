<template>
  <el-dialog :title="state.title" v-model="visible" :close-on-click-modal="false" @opened="openFun">
    <el-form :model="state.userForm" :rules="state.rules" ref="userRef" label-width="120px">
      <el-form-item v-show="false" prop="id">
        <el-input v-model="state.userForm.id"></el-input>
      </el-form-item>
      <el-form-item label="nombre de usuario" prop="username">
        <el-input v-model="state.userForm.username" placeholder="Por favor ingrese el nombre de usuario"></el-input>
      </el-form-item>
      <el-form-item label="Apoodo" prop="nickName">
        <el-input v-model="state.userForm.nickName" placeholder="Por favor ingresa un apodo"></el-input>
      </el-form-item>
      <el-form-item label="rol de usuario" prop="roleIds">
        <el-select v-model="state.userForm.roleIds" multiple placeholder="Por favor seleccione un rol">
          <el-option v-for="(item, index) in state.roleList" :key="index" :label="item.roleName" :value="parseInt(item.id)"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span>
      <el-button @click="resetForm(userRef)">reiniciar</el-button>
      <el-button type="primary" :loading="isLoading" @click="submitUser">seguro</el-button>
    </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {editUser} from "../../api/user/sysUser";
import {getRoleList} from "../../api/role/sysRole";
import {errorMsg, successMsg} from "../../utils/message";
import {resetForm} from "../../utils/common";
import {computed, reactive, ref} from "vue";

const props = defineProps({
  dialogVisible: {
    type: Boolean,
    required: true,
    default: false
  },
  userObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)

const userRef = ref()

const state = reactive({
  title: 'Nuevo',
  userForm: {
    id: null,
    username: '',
    nickName: '',
    roleIds: []
  },
  rules: {
    username: [{required: true, message: 'El nombre de usuario no puede estar vacío', trigger: 'blur'}],
    nickName: [{required: true, message: 'El apodo del usuario no puede estar vacío', trigger: 'blur'}],
    roleIds: [{required: true, message: 'El rol de usuario no puede estar vacío', trigger: 'change'}]
  },
  roleList: []
})

const openFun = () => {
  resetForm(userRef.value)
  state.title = 'Nuevo'
  getRoleListFun()
  isLoading.value = false
  if (props.userObj.id){
    state.title = 'Editar'
    state.userForm = props.userObj
    state.userForm.roleIds = state.userForm.roleIds[0].split(',')
    // Convertir números de cadena en roleIds a números
    state.userForm.roleIds = state.userForm.roleIds.map(Number)
  }
}
//  Obtener lista de roles
const getRoleListFun = () => {
  getRoleList({}).then(res => {
    if (res.success){
      state.roleList = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}
//  Entregar
const submitUser = () => {
  console.log('INSERTAR');
  userRef.value.validate((valid) => {
    if (valid){
      isLoading.value = true
      editUser(state.userForm).then(res => {
        if (res.success){
          successMsg(res.data)
          console.log(res.data)
          visible.value = false
          emit('get-list')
        } else {
          errorMsg(res.msg)
        }
        isLoading.value = false
      })
    }
  })
}
</script>

<style scoped>
 :deep(.vue-treeselect__control){
  height: 28px;
}
 :deep(.el-form-item__content){
   line-height: 28px;
   font-size: 12px;
 }
</style>