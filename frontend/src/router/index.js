import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({ showSpinner: false })

const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
    },
  },
  {
    path: '/home',
    component: () => import('@/layout/index.vue'),
    redirect: '/home/index',
    children: [
      {
        path: 'index',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: {
          title: '首页',
          icon: 'HomeFilled',
        },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  NProgress.start()
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 游戏引擎管理系统`
  }
  next()
})

router.afterEach(() => {
  NProgress.done()
})

export default router

