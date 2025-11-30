<template>
  <el-dialog title="Cambiar la contraseña" v-model="visible" :close-on-click-modal="false">
    <el-form :model="passwordForm" :rules="rules" ref="passwordRef" label-width="120px">
      <el-form-item label="Contraseña Original" prop="password">
        <el-input type="password" v-model="passwordForm.password" placeholder="Por favor ingrese la contraseña original" clearable></el-input>
      </el-form-item>
      <el-form-item label="Nueva contraseña" prop="newPassword">
        <el-input type="password" v-model="passwordForm.newPassword" placeholder="Por favor ingrese una nueva contraseña" clearable></el-input>
      </el-form-item>
      <el-form-item label="confirmar Contraseña" prop="confirmPassword">
        <el-input type="password" v-model="passwordForm.confirmPassword" placeholder="Por favor ingrese la contraseña de confirmación" clearable></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span>
      <el-button @click="resetForm(passwordRef)">reiniciar</el-button>
      <el-button type="primary" @click="submitPassword">seguro</el-button>
    </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {updatePwd} from "../../api/user/sysUser";
import {errorMsg, successMsg} from "../../utils/message";
import {computed, ref, reactive} from "vue";
import {resetForm} from "../../utils/common";
const props = defineProps({
  dialogVisible: {
    type: Boolean,
    require: true,
    default: false
  }
})

const emit = defineEmits(['update:dialog-visible'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (value) => emit('update:dialog-visible', value)
})

const passwordForm = reactive({
  password: '',
  newPassword: '',
  confirmPassword: '',
})

const passwordRef = ref()

const validateNew = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('Por favor ingrese la contraseña'));
  } else {
    if (passwordForm.confirmPassword !== '') {
      passwordRef.value.validateField('confirmPassword');
    }
    callback();
  }
}

const validateConfirm = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('Por favor ingrese la contraseña de confirmación'));
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('¡Las contraseñas ingresadas dos veces son inconsistentes!'));
  } else {
    callback();
  }
}

const rules = reactive( {
  password: [{required: true, message: 'La contraseña original no puede estar vacía', trigger: 'blur'}],
  newPassword: [
      {required: true, message: 'La nueva contraseña no puede estar vacía', trigger: 'blur'},
      {validator: validateNew, trigger: 'blur'}
  ],
  confirmPassword: [
      {required: true, message: 'Confirmar contraseña no puede estar vacía', trigger: 'blur'},
      {validator: validateConfirm, trigger: 'blur'}
  ]
})
// entregar
const submitPassword = () => {
  passwordRef.value.validate((valid) => {
    if (valid){
      updatePwd(passwordForm).then(res => {
        if (res.success){
          successMsg(res.data)
          visible.value = false
          emit('logout')
        } else {
          errorMsg(res.msg)
        }
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