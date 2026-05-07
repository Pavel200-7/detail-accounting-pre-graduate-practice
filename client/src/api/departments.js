import apiClient from './axios'

export const getDepartments = () => apiClient.get('/departments')
export const getDepartmentById = (id) => apiClient.get(`/departments/${id}`)
export const createDepartment = (data) => apiClient.post('/departments', data)
export const updateDepartment = (id, data) => apiClient.put(`/departments/${id}`, data)
export const deleteDepartment = (id) => apiClient.delete(`/departments/${id}`)
export const getDepartmentUsers = (id) => apiClient.get(`/departments/${id}/users`)