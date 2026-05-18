import axios, { AxiosHeaders, type AxiosRequestConfig } from 'axios'

import type { ApiResponse } from '@/types/api'

export const AUTH_TOKEN_STORAGE_KEY = 'campushub_access_token'

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem(AUTH_TOKEN_STORAGE_KEY)

  if (token) {
    config.headers = AxiosHeaders.from(config.headers)
    config.headers.set('Authorization', `Bearer ${token}`)
  }

  return config
})

http.interceptors.response.use(
  (response) => response.data,
  (error) => Promise.reject(error)
)

export function request<T>(config: AxiosRequestConfig): Promise<ApiResponse<T>> {
  return http.request<ApiResponse<T>, ApiResponse<T>>(config)
}
