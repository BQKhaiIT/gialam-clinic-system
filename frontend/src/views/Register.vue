<script setup>
import { reactive, ref } from 'vue'

import { useRouter } from 'vue-router'

import { User, Lock, Mail, Eye, EyeOff } from '@lucide/vue'
import { register } from '@/api/authApi'

const router = useRouter()

const loading = ref(false)

const errorMessage = ref('')

const successMessage = ref('')

const showPassword = ref(false)

const form = reactive({
  username: '',

  password: '',

  fullName: '',

  email: '',

  phone: '',
})

const validate = () => {
  errorMessage.value = ''

  if (!form.username.trim()) {
    errorMessage.value = 'Username required'

    return false
  }

  if (!form.email.trim()) {
    errorMessage.value = 'Email required'

    return false
  }

  if (!form.password.trim()) {
    errorMessage.value = 'Password required'

    return false
  }

  if (form.password.length < 6) {
    errorMessage.value = 'Password min 6 chars'

    return false
  }

  return true
}

const handleRegister = async () => {
  if (loading.value) return

  if (!validate()) return

  try {
    loading.value = true

    errorMessage.value = ''

    await register({
      username: form.username,

      password: form.password,

      fullName: form.fullName,

      email: form.email,

      phone: form.phone,
    })

    successMessage.value = 'Verification email sent'

    setTimeout(() => {
      router.push('/login')
    }, 2500)
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || 'Register failed'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-[#F4FAFC] flex items-center justify-center px-4">
    <div class="w-full max-w-md rounded-3xl bg-white shadow-lg p-8 border border-[#E4F1F4]">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-[#2F6F7D]">Gia Lam Clinic</h1>

        <p class="text-slate-500 mt-2">Create new account</p>
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
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Full Name </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <User class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.fullName"
              type="text"
              placeholder="Enter full name"
              class="w-full pl-12 pr-4 py-3 rounded-2xl outline-none"
            />
          </div>
        </div>

        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Username </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <User class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.username"
              type="text"
              placeholder="Enter username"
              class="w-full pl-12 pr-4 py-3 rounded-2xl outline-none"
            />
          </div>
        </div>

        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Email </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <Mail class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.email"
              type="email"
              placeholder="Enter email"
              class="w-full pl-12 pr-4 py-3 rounded-2xl outline-none"
            />
          </div>
        </div>

        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Phone </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <User class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.phone"
              type="text"
              placeholder="Enter phone number"
              class="w-full pl-12 pr-4 py-3 rounded-2xl outline-none"
            />
          </div>
        </div>

        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2"> Password </label>

          <div class="relative rounded-2xl border border-[#D8E8ED]">
            <Lock class="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-slate-400" />

            <input
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="Enter password"
              class="w-full pl-12 pr-12 py-3 rounded-2xl outline-none"
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
          @click="handleRegister"
          :disabled="loading"
          class="w-full bg-[#2F6F7D] hover:opacity-90 disabled:opacity-50 text-white font-semibold py-3 rounded-2xl transition"
        >
          {{ loading ? 'Creating...' : 'Register' }}
        </button>

        <div class="text-center pt-2">
          <span class="text-sm text-slate-500"> Already have an account? </span>

          <RouterLink to="/login" class="ml-2 text-sm font-semibold text-[#2F6F7D] hover:underline">
            Login
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>
