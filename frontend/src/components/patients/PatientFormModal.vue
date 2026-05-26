<script setup>
import { computed, reactive, watch } from 'vue'
import { CalendarDays, MapPin, Phone, ShieldPlus, User, Users, X } from '@lucide/vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  mode: {
    type: String,
    default: 'create',
  },
  patient: {
    type: Object,
    default: null,
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close', 'submit'])

const form = reactive({
  fullName: '',
  dob: '',
  gender: '',
  phone: '',
  address: '',
  bloodType: '',
  allergies: '',
})

const errors = reactive({
  fullName: '',
  dob: '',
  gender: '',
  phone: '',
  address: '',
})

const isViewMode = computed(() => props.mode === 'view')
const isEditMode = computed(() => props.mode === 'edit')
const title = computed(() => {
  if (isViewMode.value) {
    return 'Patient Details'
  }

  return isEditMode.value ? 'Edit Patient' : 'Create Patient'
})
const submitLabel = computed(() => {
  if (props.loading) {
    return isEditMode.value ? 'Updating...' : 'Saving...'
  }

  return isEditMode.value ? 'Update Patient' : 'Create Patient'
})

const resetErrors = () => {
  errors.fullName = ''
  errors.dob = ''
  errors.gender = ''
  errors.phone = ''
  errors.address = ''
}

const syncForm = () => {
  form.fullName = props.patient?.fullName ?? ''
  form.dob = props.patient?.dobRaw ?? props.patient?.dob ?? ''
  form.gender = props.patient?.gender ?? ''
  form.phone = props.patient?.phone ?? ''
  form.address = props.patient?.address ?? ''
  form.bloodType = props.patient?.bloodType ?? ''
  form.allergies = props.patient?.allergies ?? ''
  resetErrors()
}

watch(
  () => [props.show, props.patient, props.mode],
  () => {
    if (props.show) {
      syncForm()
    }
  },
  { immediate: true },
)

const normalizePhone = (value) => String(value).replace(/[^\d+]/g, '')

const validatePhone = (value) => /^(?:\+84|84|0)(3|5|7|8|9)\d{8}$/.test(normalizePhone(value))

const validateForm = () => {
  resetErrors()

  if (!form.fullName.trim()) {
    errors.fullName = 'Full name is required.'
  }

  if (!form.dob) {
    errors.dob = 'Date of birth is required.'
  }

  if (!form.gender) {
    errors.gender = 'Gender is required.'
  }

  if (!form.phone.trim()) {
    errors.phone = 'Phone number is required.'
  } else if (!validatePhone(form.phone)) {
    errors.phone = 'Please enter a valid Vietnam phone number.'
  }

  if (!form.address.trim()) {
    errors.address = 'Address is required.'
  }

  return !errors.fullName && !errors.dob && !errors.gender && !errors.phone && !errors.address
}

const handleSubmit = () => {
  if (isViewMode.value || props.loading) {
    return
  }

  if (!validateForm()) {
    return
  }

  emit('submit', {
    fullName: form.fullName.trim(),
    dob: form.dob,
    gender: form.gender,
    phone: form.phone.trim(),
    address: form.address.trim(),
    bloodType: form.bloodType,
    allergies: form.allergies.trim(),
  })
}
</script>

<template>
  <Teleport to="body">
    <Transition name="fade-slide">
      <div
        v-if="show"
        class="fixed inset-0 z-[70] flex items-center justify-center overflow-x-hidden bg-slate-900/45 px-4 py-4 backdrop-blur-[2px] sm:px-6 sm:py-6"
      >
        <div class="dashboard-card box-border w-full max-w-[900px] overflow-hidden overflow-x-hidden">
          <div class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6">
            <div>
              <h2 class="panel-title mb-1 text-slate-900">{{ title }}</h2>
              <p class="mb-0 text-sm text-slate-500">
                {{
                  isViewMode
                    ? 'Review patient information in the same admin style.'
                    : isEditMode
                      ? 'Update patient information with the current details.'
                      : 'Create a new patient record with required information.'
                }}
              </p>
            </div>
            <button
              type="button"
              class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9] hover:text-slate-700"
              @click="$emit('close')"
            >
              <X class="h-5 w-5" />
            </button>
          </div>

          <div class="max-h-[80vh] overflow-y-auto overflow-x-hidden">
            <div class="grid gap-x-6 gap-y-4 px-5 py-5 sm:grid-cols-2 sm:px-6">
            <div class="sm:col-span-2">
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Full Name</label>
              <div class="glass-input relative">
                <User class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                <input
                  v-model="form.fullName"
                  type="text"
                  class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                />
              </div>
              <p v-if="errors.fullName" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.fullName }}</p>
            </div>

            <div>
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Date Of Birth</label>
              <div class="glass-input relative">
                <CalendarDays class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                <input
                  v-model="form.dob"
                  type="date"
                  class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                />
              </div>
              <p v-if="errors.dob" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.dob }}</p>
            </div>

            <div>
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Gender</label>
              <div class="glass-input relative">
                <Users class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                <select
                  v-model="form.gender"
                  class="block h-[46px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                >
                  <option value="" disabled>Select gender</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>
              <p v-if="errors.gender" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.gender }}</p>
            </div>

            <div>
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Phone</label>
              <div class="glass-input relative">
                <Phone class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                <input
                  v-model="form.phone"
                  type="text"
                  class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                />
              </div>
              <p v-if="errors.phone" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.phone }}</p>
            </div>

            <div class="sm:col-span-2">
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Address</label>
              <div class="glass-input relative">
                <MapPin class="pointer-events-none absolute left-4 top-4 h-4 w-4 text-slate-400" />
                <textarea
                  v-model="form.address"
                  rows="2"
                  class="block w-full resize-none border-0 bg-transparent px-4 pb-3 pl-11 pt-3 text-sm text-slate-700 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                ></textarea>
              </div>
              <p v-if="errors.address" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.address }}</p>
            </div>

            <div>
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Blood Type</label>
              <div class="glass-input relative">
                <ShieldPlus class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                <select
                  v-model="form.bloodType"
                  class="block h-[46px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                >
                  <option value="">Select blood type</option>
                  <option value="A+">A+</option>
                  <option value="A-">A-</option>
                  <option value="B+">B+</option>
                  <option value="B-">B-</option>
                  <option value="AB+">AB+</option>
                  <option value="AB-">AB-</option>
                  <option value="O+">O+</option>
                  <option value="O-">O-</option>
                </select>
              </div>
            </div>

            <div>
              <label class="mb-1.5 block text-sm font-semibold text-slate-700">Allergies</label>
              <div class="glass-input">
                <textarea
                  v-model="form.allergies"
                  rows="2"
                  class="block w-full resize-none border-0 bg-transparent px-4 py-3 text-sm text-slate-700 placeholder:text-slate-400 focus:outline-none disabled:cursor-not-allowed"
                  :disabled="isViewMode"
                  placeholder="Drug allergies&#10;Food allergies&#10;Medical allergies"
                ></textarea>
              </div>
            </div>
            </div>
          </div>

          <div class="flex justify-end gap-3 border-t border-slate-200/70 px-5 py-5 sm:px-6">
            <button
              type="button"
              class="inline-flex items-center justify-center rounded-xl border border-[#D7E8ED] px-4 py-2.5 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
              @click="$emit('close')"
            >
              {{ isViewMode ? 'Close' : 'Cancel' }}
            </button>
            <button
              v-if="!isViewMode"
              type="button"
              class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
              :disabled="loading"
              @click="handleSubmit"
            >
              {{ submitLabel }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
