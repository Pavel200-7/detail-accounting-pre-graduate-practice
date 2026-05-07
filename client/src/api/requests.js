import apiClient from './axios'

export const createRequest = (data) => apiClient.post('/requests', data)
export const approveRequest = (id) => apiClient.put(`/requests/${id}/approve`)
export const rejectRequest = (data) => apiClient.put('/requests/reject', data)
export const completeRequest = (id) => apiClient.put(`/requests/${id}/complete`)
export const getRequestById = (id) => apiClient.get(`/requests/${id}`)
export const getRequestsByUser = (userId) => apiClient.get(`/requests/user/${userId}`)
export const getRequestsByStatus = (status) => apiClient.get(`/requests/status/${status}`)