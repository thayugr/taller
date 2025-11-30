<template>
  <q-dialog v-model="dialog" persistent>
    <q-card class="q-mb-md" style="min-width: 300px; max-width: 400px;">
      <q-card-section>
        <q-banner>
          <div class="text-h6">Gestion 2024</div>
          <div class="text-h6">Inicio de Sesion de Usuario</div>
        </q-banner>
      </q-card-section>
      <q-card-section>
        <q-form @submit="submitLogin">
          <q-input
            v-model="loginForm.username"
            label="Nombre de usuario"
            :rules="[ val => !!val || 'El nombre de usuario no puede estar vacío' ]"
            prepend-icon="person"
            class="q-mb-md"
          />
          <q-input
            v-model="loginForm.password"
            type="password"
            label="Contraseña"
            :rules="[ val => !!val || 'La contraseña no puede estar vacía' ]"
            prepend-icon="lock"
            class="q-mb-md"
          />
          <div style="display: flex; justify-content: space-between; align-items: center;" class="q-mb-md">
            <q-input
              v-model="loginForm.code"
              label="Código de verificación"
              :rules="[ val => !!val || 'El código de verificación no puede estar vacío' ]"
              prepend-icon="fingerprint"
              style="width: 70%;"
            />
            <q-img :src="state.image" @click="getCode" class="cursor-pointer" style="width: 30%;"/>
          </div>
          <q-btn
            type="submit"
            label="Acceso"
            color="primary"
            :loading="isLoading"
            class="full-width"
          />
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useStore } from './store'
import routers from './router/routers'
import { login, getVerifyCode } from './api/login/login'
import { errorMsg } from './utils/message'

const store = useStore()

const dialog = ref(true)  // Abre el diálogo al iniciar
const isLoading = ref(false)
const state = reactive({ image: '' })
const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  uuid: ''
})

onMounted(() => {
  getCode()
})

// Obtener el código de verificación
const getCode = () => {
  getVerifyCode().then(res => {
    state.image = res.img
    loginForm.uuid = res.uuid
  })
}

const submitLogin = () => {
  if (loginForm.username && loginForm.password && loginForm.code) {
    isLoading.value = true
    login(loginForm).then(res => {
      if (res.success) {
        // Cache token
        store.token = res.data.token
        store.refreshToken = res.data.refreshToken
        store.userInfo = res.data.userDto
        // Cambiar a la página de inicio
        routers.push({ path: '/Layout' })
      } else {
        errorMsg(res.msg)
      }
      isLoading.value = false
    })
  }
}
</script>

<style scoped>
/* Ajusta los estilos aquí si es necesario */
</style>
