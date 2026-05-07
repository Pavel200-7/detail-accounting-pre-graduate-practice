import apiClient from './axios'

export const getUserById = (id) => apiClient.get(`/users/${id}`)
export const setUserDepartment = (data) => apiClient.put('/users/department', data)