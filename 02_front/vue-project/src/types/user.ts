export interface User {
  id?: number
  username: string
  email: string
  password: string
  createdAt?: string // 後端返回的時間字串
}
