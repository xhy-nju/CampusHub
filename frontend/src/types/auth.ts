export interface RegisterPayload {
  studentNo: string
  password: string
  nickname?: string
  college?: string
  contact?: string
}

export interface LoginPayload {
  studentNo: string
  password: string
}

export interface UpdateProfilePayload {
  nickname: string
  avatarUrl?: string
  college?: string
  contact?: string
  bio?: string
}

export interface UserProfile {
  nickname: string
  avatarUrl: string | null
  college: string | null
  contact: string | null
  bio: string | null
}

export interface UserSummary {
  id: number
  studentNo: string
  role: string
  status: string
  creditScore: number
  profile: UserProfile | null
}

export interface AuthResponse {
  tokenType: string
  accessToken: string
  expiresInSeconds: number
  user: UserSummary
}
