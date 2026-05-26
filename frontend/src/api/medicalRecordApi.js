import apiClient from '@/api/axios'

export const getMedicalRecords = async () => {
  const { data } = await apiClient.get('/api/v1/medical-records')
  return data
}

export const createMedicalRecord = async (payload) => {
  const { data } = await apiClient.post('/api/v1/medical-records', payload)
  return data
}

export const updateMedicalRecord = async (id, payload) => {
  const { data } = await apiClient.put(`/api/v1/medical-records/${id}`, payload)
  return data
}
