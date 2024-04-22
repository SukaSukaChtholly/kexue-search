import { createRouter, createWebHashHistory } from 'vue-router'


const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/views/index.vue')
    },
    {
      path: '/list',
      component: () => import('@/views/list.vue')
    },
    {
      path: '/player',
      component: () => import('@/views/player.vue')
    },
    {
      path: '/error',
      component: () => import('@/views/error/error.vue')
    },
    {
      path: '/:pathMatcher(.*)*',
      redirect: '/error'
    }
  ]
})

// router.beforeEach((to, from, next) => {
//   // 设置页面标题，如果路由定义了 meta.title，则使用该标题，否则使用默认标题
//   document.title = to.meta.title ?? import.meta.env.VITE_API_WEB_NAME;
//   next();
// })

export default router