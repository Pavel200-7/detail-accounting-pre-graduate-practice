import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { 
    path: '/', 
    name: 'home', 
    component: () => import('../views/HomeView.vue'), 
    meta: { requiresAuth: true } 
  },
  { 
    path: '/callback', 
    name: 'callback', 
    component: () => import('../views/CallbackView.vue'), 
    meta: { requiresAuth: false }
  },
  { 
    path: '/departments', 
    name: 'departments', 
    component: () => import('../views/DepartmentsView.vue'), 
    meta: { requiresAuth: true } 
  },
  { 
    path: '/parts', 
    name: 'parts', 
    component: () => import('../views/PartsListView.vue'), 
    meta: { requiresAuth: true } 
  },
  { 
    path: '/parts/:id', 
    name: 'partDetail', 
    component: () => import('../views/PartDetailView.vue'), 
    meta: { requiresAuth: true } 
  },
  { 
    path: '/requests', 
    name: 'requests', 
    component: () => import('../views/RequestsView.vue'), 
    meta: { requiresAuth: true } 
  },
  { 
    path: '/requests/:id', 
    name: 'requestDetail', 
    component: () => import('../views/RequestDetailView.vue'), 
    meta: { requiresAuth: true } 
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Функция для проверки аутентификации (без store)
const isAuthenticated = () => {
  return !!localStorage.getItem('access_token')
}

router.beforeEach((to, from, next) => {
  // Пропускаем callback без проверки
  if (to.path === '/callback') {
    next()
    return
  }
  
  const authenticated = isAuthenticated()
  
  if (to.meta.requiresAuth && !authenticated) {
    // Перенаправляем на Keycloak
    window.location.href = '/api/v1/auth/authorize'
  } else {
    next()
  }
})

export default router