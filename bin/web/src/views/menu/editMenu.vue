<template>
  <el-dialog :title="state.title" v-model="visible" draggable :close-on-click-modal="false" @opened="openFun">
    <el-form :model="menuForm" :rules="rules" ref="menuRef" label-width="120px">
      <el-form-item v-show="false" prop="id">
        <el-input v-model="menuForm.id"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="Directorio principal" prop="parentId">
            <el-tree-select key-node="value" v-model="menuForm.parentId" :data="options" check-strictly :render-after-expand="false"></el-tree-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="clasificar" prop="sort">
            <el-input v-model="menuForm.sort" placeholder="Por favor ingrese clasificación" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="tipo de menu" prop="type">
        <el-radio-group v-model="menuForm.type">
          <el-radio :label="'1'">menu</el-radio>
          <el-radio :label="'2'">pagina</el-radio>
          <el-radio :label="'3'">boton</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="nombre del menu" prop="name">
        <el-input v-model="menuForm.name" placeholder="Por favor ingresa un nombre de menú" clearable></el-input>
      </el-form-item>
      <el-form-item label="seleccionar icono" prop="icon">
            <el-popover placement="bottom" trigger="click" width="300px;">
              <template #reference>
                <el-input v-model="menuForm.icon" prefix-icon="PorFavorSeleccioneUnIconoDeMenu" clearable></el-input>
              </template>
              <el-row class="icon-row">
                <el-col v-for="(item, index) in iconList" :key="index" :span="8">
                  <i :class="item.font_class" style="font-size: 40px;" @click="checkIcon(item.font_class)"></i>
                  <i style="display: flow-root;">{{item.name}}</i>
                </el-col>
              </el-row>
            </el-popover>
      </el-form-item>
      <el-form-item v-if="menuForm.type === '2' || menuForm.type === '3'" label="camino de acceso" prop="path">
        <el-input v-model="menuForm.path" placeholder="Por favor ingrese la ruta del menú"></el-input>
      </el-form-item>
      <el-form-item v-if="menuForm.type === '2'" label="ruta del componente" prop="component">
        <el-input v-model="menuForm.component" placeholder="Por favor ingresa a la pagina component"></el-input>
      </el-form-item>
      <el-form-item v-if="menuForm.type === '3'" label="Permisos de botones" prop="permission">
        <el-input v-model="menuForm.permission" placeholder="Por favor ingrese los permisos"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span>
        <el-button @click="resetForm(menuRef)">reiniciar</el-button>
        <el-button type="primary" :loading="isLoading" @click="submitMenu">seguro</el-button>
    </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {getMenuTreeSelect, editMenu} from "../../api/menu/sysMenu";
import {errorMsg, successMsg} from "../../utils/message";
import {resetForm} from "../../utils/common";
import {computed, reactive, ref} from "vue";

const props = defineProps({
  dialogVisible: {
    type: Boolean,
    require: true,
    default: false
  },
  menuObj: Object
})

const emit = defineEmits(['update:dialogVisible', 'get-list'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})

const menuForm = ref({
  id: null,
  parentId: 0,
  name: '',
  path: '',
  component: '',
  permission: '',
  type: '1',
  icon: ''
})

const rules = reactive({
  parentId: [{required: true, message: 'El menú de nivel superior no puede estar vacío', trigger: 'change'}],
  sort: [{required: true, message: 'La clasificación no puede estar vacía', trigger: 'blur'}],
  name: [{required: true, message: 'El nombre del menú no puede estar vacío', trigger: 'blur'}],
  path: [{required: true, message: 'La ruta del menú no puede estar vacía', trigger: 'blur'}],
  component: [{required: true, message: 'La ruta del componente no puede estar vacía', trigger: 'blur'}],
  permission: [{required: true, message: 'Los permisos no pueden estar vacíos', trigger: 'blur'}],
})

const state = {
  title: 'Nuevo',
  iconList: []
}

const iconList = ref([])

const options = ref([
  {
    value: 0,
    label: 'directorio de nivel superior',
    children: []
  }
])

const menuRef = ref()

const isLoading = ref(false)

const openFun = () => {
  state.title = 'Nuevo'
  resetForm(menuRef.value)
  isLoading.value = false
  getMenuTreeFun()
  if (props.menuObj.id){
    state.title = 'Editar'
    menuForm.value = props.menuObj
  }
  getIconList()
}
//  Obtener árbol de menú desplegable
const getMenuTreeFun = () => {
  getMenuTreeSelect().then(res => {
    if (res.success){
      options.value[0].children = res.data
    } else {
      errorMsg(res.msg)
    }
  })
}
//  entregar
const submitMenu = () => {
  menuRef.value.validate((valid) => {
    if (valid){
      isLoading.value = true
      editMenu(menuForm.value).then(res => {
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

const getIconList = () => {
  const iconJson = require('../../assets/iconfont/iconfont.json')
  const iconClassList = JSON.parse(JSON.stringify(iconJson.glyphs))
  iconList.value = iconClassList.map(item => {
    item.font_class = 'iconfont icon-' + item.font_class
    return item
  })
}

const checkIcon = (value) => {
  menuForm.value.icon = value
}
</script>

<style scoped>
 .icon-row{
   text-align: center;
   height: 300px;
   overflow-y: auto;
 }
</style>