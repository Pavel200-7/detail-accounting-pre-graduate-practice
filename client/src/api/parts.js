import apiClient from './axios'

export const getParts = () => apiClient.get('/parts')
export const getPartById = (id) => apiClient.get(`/parts/${id}`)
export const createPart = (data) => apiClient.post('/parts', data)
export const createMovement = (data) => apiClient.post('/parts/movement', data)
export const getPartMovements = (id) => apiClient.get(`/parts/${id}/movements`)