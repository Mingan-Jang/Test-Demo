import apiClient from './axios'
import type { User } from '../types/user'

// 取得所有使用者
export const getAllUsers = async (): Promise<User[]> => {
  const { data } = await apiClient.get('/')
  return data
}

// 查詢單個使用者
export const getUserById = async (id: number): Promise<User> => {
  const { data } = await apiClient.get(`/${id}`)
  return data
}

// 新增使用者
export const createUser = async (user: User): Promise<User> => {
  const { data } = await apiClient.post('/', user)
  return data
}

// 更新使用者
export const updateUser = async (id: number, user: User): Promise<User> => {
  const { data } = await apiClient.put(`/${id}`, user)
  return data
}

// 刪除使用者
export const deleteUser = async (id: number): Promise<void> => {
  await apiClient.delete(`/${id}`)
}
