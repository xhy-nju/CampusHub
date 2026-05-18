import { createRouter, createWebHistory } from 'vue-router'

import { useAuthStore } from '@/stores/auth'

const PlaceholderView = () => import('@/views/PlaceholderView.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: PlaceholderView,
      props: {
        title: 'CampusHub',
        description: 'Frontend scaffold is ready.'
      }
    },
    {
      path: '/login',
      name: 'login',
      component: PlaceholderView,
      props: {
        title: 'Login',
        description: 'Login page will be implemented after the visual reference is confirmed.'
      },
      meta: {
        guestOnly: true
      }
    },
    {
      path: '/register',
      name: 'register',
      component: PlaceholderView,
      props: {
        title: 'Register',
        description: 'Registration page will be implemented after the visual reference is confirmed.'
      },
      meta: {
        guestOnly: true
      }
    },
    {
      path: '/me',
      name: 'profile',
      component: PlaceholderView,
      props: {
        title: 'Profile',
        description: 'This protected route is reserved for the user center.'
      },
      meta: {
        requiresAuth: true
      }
    }
  ]
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return {
      name: 'login',
      query: {
        redirect: to.fullPath
      }
    }
  }

  if (to.meta.guestOnly && authStore.isAuthenticated) {
    return {
      name: 'profile'
    }
  }

  return true
})

export default router
