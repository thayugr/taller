<template>
  <el-menu :default-active="defaultActive" :unique-opened="true" router :collapse="isCollapse"
           background-color="#545c64" text-color="#fff">
    <div class="logo" @click="changeCollapse">
      <el-image :src="require('../../assets/image/ems.png')" style="width: 40px;"></el-image>
<!--      <span v-if="!isCollapse">EMS-ADMIN</span>-->
    </div>
    <!---Colocar la página de inicio primero de forma predeterminada-->
    <el-menu-item route="/home" index="delantera" @click="openTab('delantera', '/home')"><i class="iconfont icon-home"></i>paginadelantera</el-menu-item>
    <menu-tree :menu-data="menuList"></menu-tree>
<!--    <el-sub-menu v-for="(menu, menuIndex) in menuList" :key="menuIndex" :index="menu.name">-->
<!--      <template #title>-->
<!--        <i :class="menu.icon"></i>-->
<!--        <span>{{menu.name}}</span>-->
<!--      </template>-->
<!--      <el-sub-menu v-if="menu.children.children && menu.children.children.length > 0" index="">-->

<!--      </el-sub-menu>-->
<!--      <el-menu-item-->
<!--          v-else-->
<!--          v-for="(item, itemIndex) in menu.children"-->
<!--          :key="itemIndex"-->
<!--          :index="item.name"-->
<!--          :route="item.path" @click="openTab(item.name, item.path)">-->
<!--        <i :class="item.icon"></i>-->
<!--        {{item.name}}-->
<!--      </el-menu-item>-->
<!--    </el-sub-menu>-->
  </el-menu>
</template>

<script setup>
import {useStore} from "../../store";
import {getMenuTree, getPermission} from "../../api/menu/sysMenu";
import {errorMsg} from "../../utils/message";
import {computed, onMounted, ref} from "vue";
import MenuTree from "../../components/MenuTree"

const store = useStore()

const emit = defineEmits(['update:width'])

const menuList = ref([])

const isCollapse = ref(false)

const defaultActive = computed(() => {
  return store.activeIndex
})

onMounted(() => {
  //  Obtener el árbol del menú de usuario actual
  getMenuTree().then(res => {
    if (res.success){
      menuList.value = res.data
    }
  })
//  Obtener la lista de permisos de botones del usuario actual
  getPermission().then(res => {
    if (res.success){
      store.permissionAction(res.data)
    } else {
      errorMsg(res.msg)
    }
  })
})
//  abrir pagina
const openTab = (name, path) => {
  console.log(name, path)
  //  Agregar el menú actualmente abierto a la lista abierta
  store.addTabAction({name: name, path: path})
  //  Cambiar el menú activo al menú seleccionado
  store.activeIndex = name
}
//  Modificar el estado de colapso
const changeCollapse = () => {
  isCollapse.value = !isCollapse.value
  if (isCollapse.value){
    emit('update:width', '64px')
  } else {
    emit('update:width', '230px')
  }
}
</script>

<style scoped>
  .el-menu{
    height: 100%;
  }
  .logo{
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 20px;
    height: 60px;
    cursor: pointer;
  }
</style>