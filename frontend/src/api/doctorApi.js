import apiClient from '@/api/axios'

const compactParams = (params = {}) =>
  Object.fromEntries(
    Object.entries(params).filter(([, value]) => value !== null && value !== undefined && `${value}`.trim() !== ''),
  )

export const getDoctors = async (params = {}) => {
  const { data } = await apiClient.get('/api/v1/doctors', {
    params,
  })
  return data
}

export const getDoctorById = async (id) => {
  const { data } = await apiClient.get(`/api/v1/doctors/${id}`)
  return data
}

export const searchDoctors = async (filters = {}, params = {}) => {
  const { data } = await apiClient.get('/api/v1/doctors/search', {
    params: {
      ...compactParams(filters),
      ...params,
    },
  })
  return data
}

export const createDoctor = async (payload) => {
  const { data } = await apiClient.post('/api/v1/doctors', payload)
  return data
}

export const updateDoctor = async (id, payload) => {
  const { data } = await apiClient.put(`/api/v1/doctors/${id}`, payload)
  return data
}

export const deleteDoctor = async (id) => {
  const { data } = await apiClient.delete(`/api/v1/doctors/${id}`)
  return data
}
