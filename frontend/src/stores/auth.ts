import { defineStore } from 'pinia'

import { authApi } from '@/api/auth'
import { clearAuthToken, getAuthToken, persistAuthToken } from '@/api/http'
import type { AuthResponse, LoginPayload, RegisterPayload, UserSummary } from '@/types/auth'

interface AuthState {
  token: string | null
  user: UserSummary | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: getAuthToken(),
    user: null
  }),

  getters: {
    isAuthenticated: (state) => Boolean(state.token)
  },

  actions: {
    async register(payload: RegisterPayload, remember = true) {
      const response = await authApi.register(payload)
      this.applyAuth(response.data, remember)
      return response.data
    },

    async login(payload: LoginPayload, remember = false) {
      const response = await authApi.login(payload)
      this.applyAuth(response.data, remember)
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
      clearAuthToken()
    },

    applyAuth(auth: AuthResponse, remember: boolean) {
      this.token = auth.accessToken
      this.user = auth.user
      persistAuthToken(auth.accessToken, remember)
    }
  }
})
