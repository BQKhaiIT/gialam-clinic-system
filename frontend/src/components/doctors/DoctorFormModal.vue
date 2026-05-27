<script setup>
import { computed, reactive, watch } from 'vue'
import {
  Activity,
  Building2,
  CalendarDays,
  Mail,
  MapPin,
  Phone,
  ShieldCheck,
  Stethoscope,
  UserRound,
  Users,
  X,
} from '@lucide/vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  mode: {
    type: String,
    default: 'create',
  },
  doctor: {
    type: Object,
    default: null,
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close', 'submit'])

const genderOptions = [
  { value: 'MALE', label: 'Male' },
  { value: 'FEMALE', label: 'Female' },
  { value: 'OTHER', label: 'Other' },
]

const statusOptions = [
  { value: 'ACTIVE', label: 'Active' },
  { value: 'INACTIVE', label: 'Inactive' },
  { value: 'ON_LEAVE', label: 'On Leave' },
]

const form = reactive({
  fullName: '',
  specialization: '',
  phone: '',
  email: '',
  licenseNumber: '',
  gender: '',
  dob: '',
  address: '',
  department: '',
  status: 'ACTIVE',
})

const errors = reactive({
  fullName: '',
  specialization: '',
  phone: '',
  email: '',
  licenseNumber: '',
  gender: '',
  dob: '',
  address: '',
  department: '',
  status: '',
})

const isEditMode = computed(() => props.mode === 'edit')
const title = computed(() => (isEditMode.value ? 'Edit Doctor' : 'Add Doctor'))
const submitLabel = computed(() => {
  if (props.loading) {
    return isEditMode.value ? 'Updating...' : 'Saving...'
  }

  return isEditMode.value ? 'Update Doctor' : 'Create Doctor'
})

const resetErrors = () => {
  errors.fullName = ''
  errors.specialization = ''
  errors.phone = ''
  errors.email = ''
  errors.licenseNumber = ''
  errors.gender = ''
  errors.dob = ''
  errors.address = ''
  errors.department = ''
  errors.status = ''
}

const syncForm = () => {
  form.fullName = props.doctor?.fullName ?? ''
  form.specialization = props.doctor?.specialization ?? ''
  form.phone = props.doctor?.phone ?? ''
  form.email = props.doctor?.email ?? ''
  form.licenseNumber = props.doctor?.licenseNumber ?? ''
  form.gender = props.doctor?.gender ?? ''
  form.dob = props.doctor?.dobRaw ?? props.doctor?.dob ?? ''
  form.address = props.doctor?.address ?? ''
  form.department = props.doctor?.department ?? ''
  form.status = props.doctor?.status ?? 'ACTIVE'
  resetErrors()
}

watch(
  () => [props.show, props.doctor, props.mode],
  () => {
    if (props.show) {
      syncForm()
    }
  },
  { immediate: true },
)

const normalizePhone = (value) => String(value).replace(/\s+/g, '')
const validateEmail = (value) => /^\S+@\S+\.\S+$/.test(String(value))
const validatePhone = (value) => /^[0-9+()\-]{8,20}$/.test(normalizePhone(value))

const validateForm = () => {
  resetErrors()

  if (!form.fullName.trim()) {
    errors.fullName = 'Full name is required.'
  }

  if (!form.specialization.trim()) {
    errors.specialization = 'Specialization is required.'
  }

  if (!form.phone.trim()) {
    errors.phone = 'Phone is required.'
  } else if (!validatePhone(form.phone)) {
    errors.phone = 'Please enter a valid phone number.'
  }

  if (!form.email.trim()) {
    errors.email = 'Email is required.'
  } else if (!validateEmail(form.email)) {
    errors.email = 'Please enter a valid email address.'
  }

  if (!form.licenseNumber.trim()) {
    errors.licenseNumber = 'License number is required.'
  }

  if (!form.gender) {
    errors.gender = 'Gender is required.'
  }

  if (!form.dob) {
    errors.dob = 'Date of birth is required.'
  }

  if (!form.address.trim()) {
    errors.address = 'Address is required.'
  }

  if (!form.department.trim()) {
    errors.department = 'Department is required.'
  }

  if (!form.status) {
    errors.status = 'Status is required.'
  }

  return Object.values(errors).every((value) => !value)
}

const handleSubmit = () => {
  if (props.loading) {
    return
  }

  if (!validateForm()) {
    return
  }

  emit('submit', {
    fullName: form.fullName.trim(),
    specialization: form.specialization.trim(),
    phone: form.phone.trim(),
    email: form.email.trim(),
    licenseNumber: form.licenseNumber.trim(),
    gender: form.gender,
    dob: form.dob,
    address: form.address.trim(),
    department: form.department.trim(),
    status: form.status,
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
        <div
          class="dashboard-card box-border w-full max-w-[980px] overflow-hidden overflow-x-hidden"
        >
          <div
            class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6"
          >
            <div>
              <h2 class="panel-title mb-1 text-slate-900">{{ title }}</h2>
              <p class="mb-0 text-sm text-slate-500">
                {{ isEditMode ? 'Update doctor information.' : 'Create a new doctor record.' }}
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
                  <UserRound
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.fullName"
                    type="text"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.fullName" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.fullName }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700"
                  >Specialization</label
                >
                <div class="glass-input relative">
                  <Stethoscope
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.specialization"
                    type="text"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.specialization" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.specialization }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Department</label>
                <div class="glass-input relative">
                  <Building2
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.department"
                    type="text"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.department" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.department }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Phone</label>
                <div class="glass-input relative">
                  <Phone
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.phone"
                    type="text"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.phone" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.phone }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Email</label>
                <div class="glass-input relative">
                  <Mail
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.email"
                    type="email"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.email" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.email }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700"
                  >License Number</label
                >
                <div class="glass-input relative">
                  <ShieldCheck
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.licenseNumber"
                    type="text"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.licenseNumber" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.licenseNumber }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Gender</label>
                <div class="glass-input relative">
                  <Users
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <select
                    v-model="form.gender"
                    class="block h-[46px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  >
                    <option value="" disabled>Select gender</option>
                    <option
                      v-for="option in genderOptions"
                      :key="option.value"
                      :value="option.value"
                    >
                      {{ option.label }}
                    </option>
                  </select>
                </div>
                <p v-if="errors.gender" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.gender }}
                </p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">DOB</label>
                <div class="glass-input relative">
                  <CalendarDays
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    v-model="form.dob"
                    type="date"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.dob" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.dob }}</p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Status</label>
                <div class="glass-input relative">
                  <Activity
                    class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
                  />
                  <select
                    v-model="form.status"
                    class="block h-[46px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  >
                    <option
                      v-for="option in statusOptions"
                      :key="option.value"
                      :value="option.value"
                    >
                      {{ option.label }}
                    </option>
                  </select>
                </div>
                <p v-if="errors.status" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.status }}
                </p>
              </div>

              <div class="sm:col-span-2">
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Address</label>
                <div class="glass-input relative">
                  <MapPin
                    class="pointer-events-none absolute left-4 top-4 h-4 w-4 text-slate-400"
                  />
                  <textarea
                    v-model="form.address"
                    rows="3"
                    class="block w-full resize-none border-0 bg-transparent px-4 pb-3 pl-11 pt-3 text-sm text-slate-700 focus:outline-none"
                  ></textarea>
                </div>
                <p v-if="errors.address" class="mb-0 mt-1.5 text-sm text-[#C2414E]">
                  {{ errors.address }}
                </p>
              </div>
            </div>
          </div>

          <div class="flex justify-end gap-3 border-t border-slate-200/70 px-5 py-5 sm:px-6">
            <button
              type="button"
              class="inline-flex items-center justify-center rounded-xl border border-[#D7E8ED] px-4 py-2.5 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
              @click="$emit('close')"
            >
              Cancel
            </button>
            <button
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
