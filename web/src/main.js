import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import {createPinia} from "pinia";
import 'element-plus/dist/index.css'
//import zhCn from 'element-plus/lib/locale/lang/zh-cn'
import App from './App.vue'
import router from "./router/routers";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import './assets/css/public.css'
import './assets/iconfont/iconfont.css'

import './utils/message'
import './router/index'
import './utils/common'
import { Quasar } from 'quasar'
import quasarUserOptions from './quasar-user-options'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(Quasar, quasarUserOptions)

app.use(pinia)
//app.use(ElementPlus, {locale: zhCn, size: 'small'})
app.use(ElementPlus,{size:'small'})
app.use(router)

app.mount('#app')
