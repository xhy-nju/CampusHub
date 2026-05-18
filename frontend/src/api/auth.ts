import { request } from '@/api/http'
import type {
  AuthResponse,
  LoginPayload,
  RegisterPayload,
  UpdateProfilePayload,
  UserSummary
} from '@/types/auth'

export const authApi = {
  register(payload: RegisterPayload) {
    return request<AuthResponse>({
      url: '/auth/register',
      method: 'POST',
      data: payload
    })
  },

  login(payload: LoginPayload) {
    return request<AuthResponse>({
      url: '/auth/login',
      method: 'POST',
      data: payload
    })
  },

  currentUser() {
    return request<UserSummary>({
      url: '/users/me',
      method: 'GET'
    })
  },

  updateProfile(payload: UpdateProfilePayload) {
    return request<UserSummary>({
      url: '/users/me/profile',
      method: 'PUT',
      data: payload
    })
  }
}
