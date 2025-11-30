<template>
  <el-tabs v-model="activeIndex" type="card" @tab-click="clickTab" @tab-remove="removeTab">
    <el-tab-pane
        v-for="(item, index) in tabs"
        :closable="item.isClose"
        :key="index"
        :label="item.name"
        :name="item.name">
      <router-view></router-view>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup>
import {storeToRefs} from 'pinia'
import {useStore} from "../../store";
import {useRouter} from 'vue-router'
// import routers from "../../router/routers";
import {computed} from "vue";

const router = useRouter()

const store = useStore()

const { activeIndex } = storeToRefs(store)

const tabs = computed(() => {
  return store.openTabs
})
//Haga clic en la pestaña
const clickTab = (tab) => {
  store.activeIndex = tab.paneName
  // Saltar a la pestaña correspondiente
  router.push({name: tab.paneName})
}
  // eliminar pestaña
const removeTab = (name) => {
   // Recorre las pestañas abiertas actualmente
    tabs.value.forEach((tab, index) => {
      // Si la pestaña actualmente activada está cerrada
      if (tab.name === name){
        // Establece la siguiente pestaña en estado activo
        // Si el estado activo actual es el último, establece la pestaña anterior en el estado activo
        const nextTab = tabs.value[index + 1] || tabs.value[index - 1]
        if (nextTab){
          store.activeIndex = nextTab.name
          // Saltar a la página actual
          router.push({path: nextTab.path})
        }
      }
    })
    //Eliminar la pestaña actualmente eliminada en el caché de pestañas abiertas
    store.removeTabAction(name)
}
</script>

<style scoped>
 :deep(.el-tabs__content){
  height: calc(100vh - 120px);
  padding-left: 30px;
  padding-right: 30px;
}
 :deep(.el-tabs__nav-wrap){
  height: 40px;
  border-top: 1px solid #d8dce5;
  border-bottom: 1px solid #d8dce5;
  background-color: rgb(247, 247, 247);
}

:deep(.el-tabs__nav){
  border: none!important;
  height: 40px;
  display: flex;
  align-items: center;
}

:deep(.el-tabs__item.is-active){
  height: 30px;
  line-height: 30px;
  background-color: #42b983;
  border-color: #42b983;
  color: white;
}

:deep(.el-tabs__item.is-active::before) {
  content: "";
  background-color: #fff;
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 4px;
}
</style>