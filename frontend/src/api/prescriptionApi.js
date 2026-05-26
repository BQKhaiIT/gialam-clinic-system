import apiClient from '@/api/axios'

export const createPrescription = async (payload) => {
  const { data } = await apiClient.post('/prescriptions', payload)
  return data
}
