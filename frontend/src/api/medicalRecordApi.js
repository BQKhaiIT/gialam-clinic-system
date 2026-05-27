import api from '@/api/axios'

export const getMedicalRecords = async (params = {}) => {

  const { data } = await api.get(
    '/api/v1/medical-records',
    {
      params
    }
  )

  return data
}

export const createMedicalRecord = async (payload) => {

  const { data } = await api.post(
    '/api/v1/medical-records',
    payload
  )

  return data
}

export const updateMedicalRecord = async (
  id,
  payload
) => {

  const { data } = await api.put(
    `/api/v1/medical-records/${id}`,
    payload
  )

  return data
}