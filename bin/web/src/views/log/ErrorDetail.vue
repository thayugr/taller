<template>
  <el-dialog title="Detalles del error" v-model="visible">
    <pre>{{ msg}}</pre>
  </el-dialog>
</template>

<script setup>
import {computed} from "vue";

const props = defineProps({
  dialogVisible: {
    type: Boolean,
    require: true,
    default: false
  },
  msg: String
})

const emit = defineEmits(['update:dialogVisible'])

const visible = computed({
  get: () => props.dialogVisible,
  set: (val) => emit('update:dialogVisible', val)
})
</script>

<style scoped>

</style>