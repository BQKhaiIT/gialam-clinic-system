import apiClient from '@/api/axios'

export const getMedicines = async () => {
  const { data } = await apiClient.get('/api/v1/medicines')
  return data
}

export const getMedicineById = async (id) => {
  const { data } = await apiClient.get(`/api/v1/medicines/${id}`)
  return data
}

export const createMedicine = async (payload) => {
  const { data } = await apiClient.post('/api/v1/medicines', payload)
  return data
}

export const updateMedicine = async (id, payload) => {
  const { data } = await apiClient.put(`/api/v1/medicines/${id}`, payload)
  return data
}

export const deleteMedicine = async (id) => {
  const { data } = await apiClient.delete(`/api/v1/medicines/${id}`)
  return data
}
