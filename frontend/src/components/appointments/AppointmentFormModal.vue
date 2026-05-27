<script setup>
import { computed, reactive, watch } from 'vue'
import { CalendarDays, Clock3, FileText, NotebookText, Stethoscope, UserRound, X } from '@lucide/vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  mode: {
    type: String,
    default: 'create',
  },
  appointment: {
    type: Object,
    default: null,
  },
  patients: {
    type: Array,
    default: () => [],
  },
  doctors: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close', 'submit'])

const form = reactive({
  patientId: '',
  doctorId: '',
  appointmentDate: '',
  appointmentTime: '',
  reason: '',
  notes: '',
})

const errors = reactive({
  patientId: '',
  doctorId: '',
  appointmentDate: '',
  appointmentTime: '',
})

const isEditMode = computed(() => props.mode === 'edit')
const title = computed(() => (isEditMode.value ? 'Edit Appointment' : 'Create Appointment'))
const submitLabel = computed(() => {
  if (props.loading) {
    return isEditMode.value ? 'Updating...' : 'Saving...'
  }

  return isEditMode.value ? 'Update Appointment' : 'Create Appointment'
})

const selectedDoctor = computed(() =>
  props.doctors.find((doctor) => String(doctor.id) === String(form.doctorId)),
)

const resetErrors = () => {
  errors.patientId = ''
  errors.doctorId = ''
  errors.appointmentDate = ''
  errors.appointmentTime = ''
}

const syncForm = () => {
  form.patientId = props.appointment?.patientId ? String(props.appointment.patientId) : ''
  form.doctorId = props.appointment?.doctorId ? String(props.appointment.doctorId) : ''
  form.appointmentDate = props.appointment?.appointmentDateRaw ?? ''
  form.appointmentTime = props.appointment?.appointmentTimeRaw ?? ''
  form.reason = props.appointment?.reason ?? ''
  form.notes = props.appointment?.notes ?? ''
  resetErrors()
}

watch(
  () => [props.show, props.appointment, props.mode],
  () => {
    if (props.show) {
      syncForm()
    }
  },
  { immediate: true },
)

const validateForm = () => {
  resetErrors()

  if (!form.patientId) {
    errors.patientId = 'Patient is required.'
  }

  if (!form.doctorId) {
    errors.doctorId = 'Doctor is required.'
  }

  if (!form.appointmentDate) {
    errors.appointmentDate = 'Date is required.'
  }

  if (!form.appointmentTime) {
    errors.appointmentTime = 'Time is required.'
  }

  return !errors.patientId && !errors.doctorId && !errors.appointmentDate && !errors.appointmentTime
}

const handleSubmit = () => {

 if (props.loading) return

 if (!validateForm()) return

 emit('submit', {

    patientId:
      Number(form.patientId),

    doctorId:
      Number(form.doctorId),

    appointmentDate:
      form.appointmentDate,

    appointmentTime:
      form.appointmentTime,

    reason:
      form.reason.trim(),

    notes:
      form.notes.trim(),

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
                Schedule and manage appointment information with the existing admin style.
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
              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Patient</label>
                <div class="glass-input relative">
                  <UserRound class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                  <select
                    v-model="form.patientId"
                    class="block h-[46px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  >
                    <option value="" disabled>Select patient</option>
                    <option v-for="patient in patients" :key="patient.id" :value="String(patient.id)">
                      {{ patient.fullName }}
                    </option>
                  </select>
                </div>
                <p v-if="errors.patientId" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.patientId }}</p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Doctor</label>
                <div class="glass-input relative">
                  <Stethoscope class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                  <select
                    v-model="form.doctorId"
                    class="block h-[46px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  >
                    <option value="" disabled>Select doctor</option>
                    <option v-for="doctor in doctors" :key="doctor.id" :value="String(doctor.id)">
                      {{ doctor.fullName }}{{ doctor.specialization ? ` • ${doctor.specialization}` : '' }}
                    </option>
                  </select>
                </div>
                <p v-if="selectedDoctor" class="mb-0 mt-1.5 text-xs font-medium text-slate-500">
                  {{ selectedDoctor.department || 'General Department' }}{{ selectedDoctor.status ? ` • ${selectedDoctor.status}` : '' }}
                </p>
                <p v-if="errors.doctorId" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.doctorId }}</p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Date</label>
                <div class="glass-input relative">
                  <CalendarDays class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                  <input
                    v-model="form.appointmentDate"
                    type="date"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.appointmentDate" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.appointmentDate }}</p>
              </div>

              <div>
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Time</label>
                <div class="glass-input relative">
                  <Clock3 class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                  <input
                    v-model="form.appointmentTime"
                    type="time"
                    class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-4 text-sm text-slate-700 focus:outline-none"
                  />
                </div>
                <p v-if="errors.appointmentTime" class="mb-0 mt-1.5 text-sm text-[#C2414E]">{{ errors.appointmentTime }}</p>
              </div>

              <div class="sm:col-span-2">
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Reason</label>
                <div class="glass-input relative">
                  <FileText class="pointer-events-none absolute left-4 top-4 h-4 w-4 text-slate-400" />
                  <textarea
                    v-model="form.reason"
                    rows="2"
                    class="block w-full resize-none border-0 bg-transparent px-4 pb-3 pl-11 pt-3 text-sm text-slate-700 focus:outline-none"
                    placeholder="Reason for appointment"
                  ></textarea>
                </div>
              </div>

              <div class="sm:col-span-2">
                <label class="mb-1.5 block text-sm font-semibold text-slate-700">Notes</label>
                <div class="glass-input relative">
                  <NotebookText class="pointer-events-none absolute left-4 top-4 h-4 w-4 text-slate-400" />
                  <textarea
                    v-model="form.notes"
                    rows="2"
                    class="block w-full resize-none border-0 bg-transparent px-4 pb-3 pl-11 pt-3 text-sm text-slate-700 focus:outline-none"
                    placeholder="Additional notes"
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
