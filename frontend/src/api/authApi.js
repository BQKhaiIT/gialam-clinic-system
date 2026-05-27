import api from '@/api/axios'

export const login = async (payload) => {

  const { data } = await api.post(

    '/api/v1/auth/login',

    payload

  )

  return data

}

export const register = async (payload) => {

  const { data } = await api.post(

    '/api/v1/auth/register',

    payload

  )

  return data

}

export const logout = () => {

  localStorage.removeItem('token')

  localStorage.removeItem('user')

}