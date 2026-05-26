import axios from 'axios'

const TOKEN_KEYS = ['token', 'accessToken', 'jwtToken']
const LOGIN_PATH = '/login'

const getToken = () => {
  for (const key of TOKEN_KEYS) {
    const token = window.localStorage.getItem(key)

    if (token) {
      return token
    }
  }

  return null
}

const clearTokens = () => {
  TOKEN_KEYS.forEach((key) => window.localStorage.removeItem(key))
}

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 15000,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  (config) => {
    const token = getToken()

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error) => Promise.reject(error),
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error?.response?.status === 401) {
      clearTokens()

      if (window.location.pathname !== LOGIN_PATH) {
        window.location.replace(LOGIN_PATH)
      }
    }

    return Promise.reject(error)
  },
)

export default api
