<template>
  <div>
    <template v-for="(menu, menuIndex) in props.menuData">
      <el-sub-menu :key="menuIndex" :index="menu.name" v-if="menu.children">
        <template #title>
          <i :class="menu.icon"></i>
          <span slot="title">{{menu.name}}</span>
        </template>
        <menu-tree :menuData="menu.children"></menu-tree>
      </el-sub-menu>
      <el-menu-item v-else :key="menu.name + menuIndex" :index="menu.name"
                    :route="menu.path"
                    @click="openTab(menu.name, menu.path)">
        <i :class="menu.icon"></i>
        {{menu.name}}
      </el-menu-item>
    </template>
  </div>
</template>

<script setup>
import {useStore} from "../store";

const props = defineProps({
  menuData: {
    type: Array
  }
})

const store = useStore()

const openTab = (name, path) => {
  //  Agregar el menú actualmente abierto a la lista abierta
  store.addTabAction({name: name, path: path})
  //  Cambiar el menú activo al menú seleccionado
  store.activeIndexAction(name)
}
</script>

<style scoped>
.el-menu{
  height: 100%;
}
/*Dado que la etiqueta <el-menu> de element-ui espera anidar <el-menu-item>, <el-submenu>,
Uno de <el-menu-item-group>, pero tiene un <div> anidado, por lo que el texto no se puede ocultar cuando se pliega*/
/*texto oculto*/
.el-menu--collapse  .el-submenu__title span{
  display: none;
}
/*Ocultar > */
.el-menu--collapse  .el-submenu__title .el-submenu__icon-arrow{
  display: none;
}
</style>