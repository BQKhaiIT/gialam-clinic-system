<script setup>
import { reactive, ref } from 'vue'

import { RouterLink, useRouter } from 'vue-router'

import { User, Lock, Eye, EyeOff } from '@lucide/vue'

import { login } from '@/api/authApi'
import { onMounted } from 'vue'

const router = useRouter()

const loading = ref(false)
const showPassword = ref(false)
const errorMessage = ref('')

const form = reactive({
  username: '',
  password: '',
})

const validate = () => {
  errorMessage.value = ''

  if (!form.username.trim()) {
    errorMessage.value = 'Username is required'
    return false
  }

  if (!form.password.trim()) {
    errorMessage.value = 'Password is required'
    return false
  }

  return true
}

const handleLogin = async () => {
  if (loading.value) return

  if (!validate()) return

  try {
    loading.value = true

    const response = await login({
      username: form.username.trim(),
      password: form.password,
    })

    const auth = response.data

    localStorage.setItem('token', auth.token)

    localStorage.setItem(
      'user',
      JSON.stringify({
        id: auth.id,
        username: auth.username,
        fullName: auth.fullName,
        role: auth.role,
      }),
    )

    router.push('/')
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || 'Login failed'
  } finally {
    loading.value = false
  }
}
onMounted(() => {
  const params = new URLSearchParams(window.location.search)

  if (params.get('verified') === 'true') {
    errorMessage.value = ''

    successMessage.value = 'Account verified successfully'
  }
})
const successMessage = ref('')
</script>

<template>
  <div class="min-h-screen bg-[#F4FAFC] flex items-center justify-center px-4">
    <div class="w-full max-w-md rounded-3xl bg-white shadow-lg p-8 border border-[#E4F1F4]">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-[#2F6F7D]">Gia Lam Clinic</h1>

        <p class="text-slate-500 mt-2">Admin Management System</p>
      </div>

      <div
        v-if="errorMessage"
        class="mb-4 rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-red-600 text-sm"
      >
        {{ errorMessage }}
      </div>
      <div
        v-if="successMessage"
        class="mb-4 rounded-xl bg-green-50 border border-green-200 px-4 py-3 text-green-700 text-sm"
      >
        {{ successMessage }}
      </div>
      <div class="space-y-5">
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Username </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <User class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.username"
              type="text"
              placeholder="Enter username"
              class="w-full pl-12 pr-4 py-3 rounded-2xl outline-none"
              @keyup.enter="handleLogin"
            />
          </div>
        </div>

        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Password </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <Lock class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.password"
              :placeholder="'Enter password'"
              :type="showPassword ? 'text' : 'password'"
              class="w-full pl-12 pr-12 py-3 rounded-2xl outline-none"
              @keyup.enter="handleLogin"
            />

            <button
              type="button"
              class="absolute right-4 top-1/2 -translate-y-1/2"
              @click="showPassword = !showPassword"
            >
              <Eye v-if="!showPassword" class="w-5 h-5 text-slate-400" />

              <EyeOff v-else class="w-5 h-5 text-slate-400" />
            </button>
          </div>
        </div>

        <button
          @click="handleLogin"
          :disabled="loading"
          class="w-full bg-[#2F6F7D] hover:opacity-90 disabled:opacity-50 text-white font-semibold py-3 rounded-2xl transition"
        >
          {{ loading ? 'Signing in...' : 'Login' }}
        </button>
        <div class="text-center pt-2">
          <span class="text-sm text-slate-500"> Don't have an account? </span>

          <RouterLink
            to="/register"
            class="ml-2 text-sm font-semibold text-[#2F6F7D] hover:underline"
          >
            Register
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>
