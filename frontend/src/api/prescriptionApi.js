import api from './axios'

export const getPrescriptions = (params) => {
  return api.get('/api/v1/prescriptions', {
    params,
  })
}

export const getPrescriptionById = (id) => {
  return api.get(`/api/v1/prescriptions/${id}`)
}

export const createPrescription = (data) => {
  return api.post('/api/v1/prescriptions', data)
}

export const updatePrescription = (id, data) => {
  return api.put(`/api/v1/prescriptions/${id}`, data)
}

export const deletePrescription = (id) => {
  return api.delete(`/api/v1/prescriptions/${id}`)
}