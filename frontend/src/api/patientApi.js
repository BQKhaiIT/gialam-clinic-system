import apiClient from '@/api/axios'

export const getPatients = async (params = {}) => {
  const { data } = await apiClient.get('/api/v1/patients', {
    params,
  })
  return data
}

export const searchPatients = async (keyword, params = {}) => {
  const { data } = await apiClient.get('/api/v1/patients/search', {
    params: {
      keyword,
      ...params,
    },
  })

  return data
}

export const createPatient = async (payload) => {
  const { data } = await apiClient.post('/api/v1/patients', payload)
  return data
}

export const updatePatient = async (id, payload) => {
  const { data } = await apiClient.put(`/api/v1/patients/${id}`, payload)
  return data
}

export const deletePatient = async (id) => {
  const { data } = await apiClient.delete(`/api/v1/patients/${id}`)
  return data
}
