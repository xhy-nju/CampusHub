import axios, { AxiosHeaders, type AxiosRequestConfig } from 'axios'

import type { ApiResponse } from '@/types/api'

export const AUTH_TOKEN_STORAGE_KEY = 'campushub_access_token'

const API_MESSAGE_MAP: Record<string, string> = {
  'Student number already exists': '该学号已注册，请直接登录或更换学号',
  'Invalid student number or password': '学号或密码错误',
  'User account is disabled': '账号已被禁用，请联系管理员',
  'studentNo can only contain letters, numbers, underscores, and hyphens': '学号只能包含字母、数字、下划线和连字符',
  'Invalid request body': '请求内容格式不正确'
}

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

export function getAuthToken() {
  return localStorage.getItem(AUTH_TOKEN_STORAGE_KEY) ?? sessionStorage.getItem(AUTH_TOKEN_STORAGE_KEY)
}

export function persistAuthToken(token: string, remember: boolean) {
  if (remember) {
    localStorage.setItem(AUTH_TOKEN_STORAGE_KEY, token)
    sessionStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
    return
  }

  sessionStorage.setItem(AUTH_TOKEN_STORAGE_KEY, token)
  localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
}

export function clearAuthToken() {
  localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
  sessionStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
}

http.interceptors.request.use((config) => {
  const token = getAuthToken()

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

export function getApiErrorMessage(error: unknown, fallback = '请求失败，请稍后再试') {
  if (!axios.isAxiosError<ApiResponse<unknown>>(error)) {
    return fallback
  }

  if (error.code === 'ECONNABORTED') {
    return '请求超时，请稍后再试'
  }

  if (!error.response) {
    return '无法连接服务器，请确认后端服务已启动'
  }

  const message = error.response.data?.message
  if (!message || message === 'ok') {
    return fallback
  }

  return API_MESSAGE_MAP[message] ?? message
}
