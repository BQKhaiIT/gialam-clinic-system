import apiClient from '@/api/axios'

export const getAppointments = async (params = {}) => {
  const { data } = await apiClient.get('/api/v1/appointments', {
    params,
  })
  return data
}

export const getAppointmentById = async (id) => {
  const { data } = await apiClient.get(`/api/v1/appointments/${id}`)
  return data
}

export const createAppointment = async (payload) => {
  const { data } = await apiClient.post('/api/v1/appointments', payload)
  return data
}

export const updateAppointment = async (id, payload) => {
  const { data } = await apiClient.put(`/api/v1/appointments/${id}`, payload)
  return data
}

export const deleteAppointment = async (id) => {
  const { data } = await apiClient.delete(`/api/v1/appointments/${id}`)
  return data
}
export const updateAppointmentStatus = async (

  id,

  payload

) => {

  const { data } = await apiClient.put(

    `/api/v1/appointments/${id}/status`,

    payload

  )

  return data

}
