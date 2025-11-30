<template>
  <el-dialog :title="state.title" v-model="visible" draggable :close-on-click-modal="false" @opened="openFun">
    <el-form :model="state.roleForm" :rules="rules" ref="roleRef" label-width="120px">
      <el-form-item v-show="false" prop="id">
        <el-input v-model="state.roleForm.id"></el-input>
      </el-form-item>
      <el-form-item label="Nombre del personaje" prop="roleName">
        <el-input v-model="state.roleForm.roleName" placeholder="Por favor ingrese un nombre de rol"></el-input>
      </el-form-item>
      <el-form-item label="Codigo de Rol" prop="roleCode">
        <el-input v-model="state.roleForm.roleCode" placeholder="Por favor ingrese el código de rol"></el-input>
      </el-form-item>
      <el-form-item label="Descripción del rol" prop="roleIds">
        <el-input type="textarea" v-model="state.roleForm.description" aria-placeholder="Por favor ingrese una descripción del rol"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span>
      <el-button @click="resetForm(roleRef)">reiniciar</el-button>
      <el-button type="primary" :loading="isLoading" @click="submitRole">seguro</el-button>
    </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {editRole} from "../../api/role/sysRole";
import {errorMsg, successMsg} from "../../utils/message";
import {computed, reactive, ref} from "vue";
import {resetForm} from "../../utils/common";

const props = defineProps({
  dialogVisible: {
    type: Boolean,
    require: true,
    default: false
  },
  roleObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const isLoading = ref(false)

const state = reactive({
  title: 'Nuevo',
  roleForm: {
    id: null,
    roleName: '',
    roleCode: '',
    description: ''
  },
  rules: {
    roleName: [{required: true, message: 'El nombre del rol no puede estar vacío', trigger: 'blur'}],
    roleCode: [{required: true, message: 'El código de rol no puede estar vacío', trigger: 'blur'}]
  }
})

const roleRef = ref()

const openFun = () => {
  resetForm(roleRef.value)
  state.title = 'Nuevo'
  isLoading.value = false
  if (props.roleObj.id){
    state.title = 'Editar'
    state.roleForm = props.roleObj
  }
}
//  Entregar
const submitRole = () => {
  roleRef.value.validate((valid) => {
    if (valid){
      isLoading.value = true
      editRole(state.roleForm).then(res => {
        if (res.success){
          successMsg(res.data)
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

// export default {
//   name: "editRole",
//   props: {
//     dialogVisible: {
//       type: Boolean,
//       require: true,
//       default: false
//     },
//     roleObj: Object
//   },
//   computed: {
//     visible: {
//       get: function () {
//         return this.dialogVisible
//       },
//       set: function (val) {
//         this.$emit('update:dialogVisible', val)
//       }
//     }
//   },
//   data(){
//     return{
//       title: '新增',
//       isLoading: false,
//       roleForm: {
//         id: null,
//         roleName: '',
//         roleCode: '',
//         description: ''
//       },
//       rules: {
//         roleName: [{required: true, message: 'El nombre del rol no puede estar vacío', trigger: 'blur'}],
//         roleCode: [{required: true, message: 'El código de rol no puede estar vacío', trigger: 'blur'}]
//       }
//     }
//   },
//   methods: {
//     resetForm,
//     openFun(){
//       if (this.roleObj.id){
//         this.title = 'editar'
//         this.roleForm = this.roleObj
//       }
//     },
//     // entregar
//     submitRole(formName){
//       this.$refs[formName].validate((valid) => {
//         if (valid){
//           this.isLoading = true
//           editRole(this.roleForm).then(res => {
//             if (res.success){
//               successMsg(res.data)
//               this.visible = false
//               this.$emit('get-list')
//             } else {
//               errorMsg(res.msg)
//             }
//             this.isLoading = false
//           })
//         }
//       })
//     }
//   }
// }
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