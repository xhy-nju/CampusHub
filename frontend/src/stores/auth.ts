import { defineStore } from 'pinia'

import { authApi } from '@/api/auth'
import { AUTH_TOKEN_STORAGE_KEY } from '@/api/http'
import type { AuthResponse, LoginPayload, RegisterPayload, UserSummary } from '@/types/auth'

interface AuthState {
  token: string | null
  user: UserSummary | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem(AUTH_TOKEN_STORAGE_KEY),
    user: null
  }),

  getters: {
    isAuthenticated: (state) => Boolean(state.token)
  },

  actions: {
    async register(payload: RegisterPayload) {
      const response = await authApi.register(payload)
      this.applyAuth(response.data)
      return response.data
    },

    async login(payload: LoginPayload) {
      const response = await authApi.login(payload)
      this.applyAuth(response.data)
      return response.data
    },

    async fetchCurrentUser() {
      if (!this.token) {
        return null
      }

      const response = await authApi.currentUser()
      this.user = response.data
      return response.data
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
    },

    applyAuth(auth: AuthResponse) {
      this.token = auth.accessToken
      this.user = auth.user
      localStorage.setItem(AUTH_TOKEN_STORAGE_KEY, auth.accessToken)
    }
  }
})
