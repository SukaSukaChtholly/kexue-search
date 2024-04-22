import { createApp, useAttrs } from 'vue'
import App from './App.vue'
import 'normalize.css'
import router from './router/index'
import { createPinia } from 'pinia'
import { VueSpinnersPlugin } from 'vue3-spinners';
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import ElementPlus from 'element-plus'

/* 分页跳转中文显示 */
zhCn.el.pagination.goto = "跳转至"

createApp(App).use(ElementPlus, { locale: zhCn }).use(VueSpinnersPlugin).use(createPinia()).use(router).mount('#app')
