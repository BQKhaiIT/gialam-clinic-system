<script setup>
import {
  Building2,
  CalendarDays,
  Clock3,
  FileText,
  Info,
  Mail,
  NotebookText,
  Phone,
  Stethoscope,
  UserRound,
  X,
  Pill,
  Plus,
} from '@lucide/vue'

import { createPrescription } from '@/api/prescriptionApi'
import { getMedicines } from '@/api/medicineApi'
defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  appointment: {
    type: Object,
    default: null,
  },
})
import { onMounted } from 'vue'

const medicines = ref([])
defineEmits(['close'])

import { reactive, ref } from 'vue'

const loading = ref(false)

const prescriptionForm = reactive({
  note: '',
  medicines: [
    {
      medicineId: '',
      quantity: 1,
      dosage: '',
      instruction: '',
    },
  ],
})

const submitPrescription = async () => {
  try {
    loading.value = true

    await createPrescription({
      medicalRecordId: record.id,
      note: prescriptionForm.note,
      medicines: prescriptionForm.medicines,
    })

    alert('Prescription created successfully!')
  } catch (error) {
    alert(error?.response?.data?.message || 'Create prescription failed')
  } finally {
    loading.value = false
  }
}

const fetchMedicines = async () => {
  try {
    const response = await getMedicines()

    medicines.value = response.data.content || []
  } catch (error) {
    console.error(error)
  }
}

const getStatusClass = (status) => {
  switch (status) {
    case 'PENDING':
      return 'bg-amber-100 text-amber-700'

    case 'CONFIRMED':
      return 'bg-blue-100 text-blue-700'

    case 'IN_PROGRESS':
      return 'bg-violet-100 text-violet-700'

    case 'COMPLETED':
      return 'bg-emerald-100 text-emerald-700'

    case 'CANCELLED':
      return 'bg-rose-100 text-rose-700'

    default:
      return 'bg-slate-100 text-slate-700'
  }
}

onMounted(() => {
  fetchMedicines()
})
</script>

<template>
  <Teleport to="body">
    <Transition name="fade-slide">
      <div v-if="show" class="fixed inset-0 z-[75] overflow-hidden">
        <div class="absolute inset-0 bg-slate-900/35 backdrop-blur-[2px]" @click="$emit('close')"></div>

        <div class="absolute inset-y-0 right-0 flex w-full justify-end p-4 sm:p-6">
          <aside class="dashboard-card box-border flex h-full w-full max-w-[520px] flex-col overflow-hidden">
            <div class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6">
              <div>
                <h2 class="panel-title mb-1 text-slate-900">Appointment Detail</h2>
                <p class="mb-0 text-sm text-slate-500">
                  Detailed information for the selected appointment
                </p>
              </div>
              <button type="button"
                class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9] hover:text-slate-700"
                @click="$emit('close')">
                <X class="h-5 w-5" />
              </button>
            </div>

            <div class="flex-1 overflow-y-auto px-5 py-5 sm:px-6">
              <div class="grid gap-4 sm:grid-cols-2">
                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <UserRound class="h-4 w-4 text-[#2F6F7D]" />
                    Patient
                  </div>
                  <p class="mb-0 text-sm text-slate-600">{{ appointment?.patientName || 'N/A' }}</p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Stethoscope class="h-4 w-4 text-[#2F6F7D]" />
                    Doctor
                  </div>
                  <p class="mb-0 text-sm text-slate-600">{{ appointment?.doctorName || 'N/A' }}</p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <CalendarDays class="h-4 w-4 text-[#2F6F7D]" />
                    Date
                  </div>
                  <p class="mb-0 text-sm text-slate-600">
                    {{ appointment?.appointmentDate || 'N/A' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Clock3 class="h-4 w-4 text-[#2F6F7D]" />
                    Time
                  </div>
                  <p class="mb-0 text-sm text-slate-600">
                    {{ appointment?.appointmentTime || 'N/A' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Info class="h-4 w-4 text-[#2F6F7D]" />
                    Status
                  </div>
                  <span class="inline-flex rounded-full px-3 py-1 text-xs font-semibold"
                    :class="getStatusClass(appointment?.status)">
                    {{ appointment?.status || 'N/A' }}
                  </span>
                </div>
                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <FileText class="h-4 w-4 text-[#2F6F7D]" />
                    Medical Record
                  </div>

                  <p class="mb-0 text-sm font-medium"
                    :class="appointment?.hasMedicalRecord ? 'text-emerald-600' : 'text-rose-500'">
                    {{ appointment?.hasMedicalRecord ? 'Created' : 'Not created' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <NotebookText class="h-4 w-4 text-[#2F6F7D]" />
                    Prescription
                  </div>

                  <p class="mb-0 text-sm font-medium"
                    :class="appointment?.hasPrescription ? 'text-emerald-600' : 'text-rose-500'">
                    {{ appointment?.hasPrescription ? 'Created' : 'Not created' }}
                  </p>
                </div>
                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Info class="h-4 w-4 text-[#2F6F7D]" />
                    Created
                  </div>
                  <p class="mb-0 text-sm text-slate-600">{{ appointment?.createdAt || 'N/A' }}</p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-3 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Stethoscope class="h-4 w-4 text-[#2F6F7D]" />
                    Doctor Information
                  </div>

                  <div class="grid gap-3 sm:grid-cols-2">
                    <div>
                      <div
                        class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Stethoscope class="h-3.5 w-3.5" />
                        Specialization
                      </div>
                      <p class="mb-0 text-sm text-slate-600">
                        {{ appointment?.doctorSpecialization || 'N/A' }}
                      </p>
                    </div>

                    <div>
                      <div
                        class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Building2 class="h-3.5 w-3.5" />
                        Department
                      </div>
                      <p class="mb-0 text-sm text-slate-600">
                        {{ appointment?.doctorDepartment || 'N/A' }}
                      </p>
                    </div>

                    <div>
                      <div
                        class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Phone class="h-3.5 w-3.5" />
                        Phone
                      </div>
                      <p class="mb-0 text-sm text-slate-600">
                        {{ appointment?.doctorPhone || 'N/A' }}
                      </p>
                    </div>

                    <div>
                      <div
                        class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Mail class="h-3.5 w-3.5" />
                        Email
                      </div>
                      <p class="mb-0 text-sm text-slate-600">
                        {{ appointment?.doctorEmail || 'N/A' }}
                      </p>
                    </div>
                  </div>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <FileText class="h-4 w-4 text-[#2F6F7D]" />
                    Reason
                  </div>
                  <p class="mb-0 whitespace-pre-line text-sm text-slate-600">
                    {{ appointment?.reason || 'N/A' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <NotebookText class="h-4 w-4 text-[#2F6F7D]" />
                    Notes
                  </div>
                  <p class="mb-0 whitespace-pre-line text-sm text-slate-600">
                    {{ appointment?.notes || 'N/A' }}
                  </p>
                </div>
              </div>
            </div>
          </aside>
        </div>

        <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
          <div class="mb-4 text-sm font-semibold text-slate-700">
            Workflow Progress
          </div>

          <div class="space-y-3">

            <div class="flex items-center gap-3">
              <div class="h-3 w-3 rounded-full" :class="appointment?.status
                  ? 'bg-emerald-500'
                  : 'bg-slate-300'
                " />
              <span class="text-sm text-slate-600">
                Appointment Created
              </span>
            </div>

            <div class="flex items-center gap-3">
              <div class="h-3 w-3 rounded-full" :class="appointment?.status === 'CONFIRMED'
                  || appointment?.status === 'IN_PROGRESS'
                  || appointment?.status === 'COMPLETED'
                  ? 'bg-emerald-500'
                  : 'bg-slate-300'
                " />
              <span class="text-sm text-slate-600">
                Appointment Confirmed
              </span>
            </div>

            <div class="flex items-center gap-3">
              <div class="h-3 w-3 rounded-full" :class="appointment?.status === 'IN_PROGRESS'
                  || appointment?.status === 'COMPLETED'
                  ? 'bg-emerald-500'
                  : 'bg-slate-300'
                " />
              <span class="text-sm text-slate-600">
                Examination In Progress
              </span>
            </div>

            <div class="flex items-center gap-3">
              <div class="h-3 w-3 rounded-full" :class="appointment?.hasMedicalRecord
                  ? 'bg-emerald-500'
                  : 'bg-slate-300'
                " />
              <span class="text-sm text-slate-600">
                Medical Record Created
              </span>
            </div>

            <div class="flex items-center gap-3">
              <div class="h-3 w-3 rounded-full" :class="appointment?.hasPrescription
                  ? 'bg-emerald-500'
                  : 'bg-slate-300'
                " />
              <span class="text-sm text-slate-600">
                Prescription Created
              </span>
            </div>

            <div class="flex items-center gap-3">
              <div class="h-3 w-3 rounded-full" :class="appointment?.status === 'COMPLETED'
                  ? 'bg-emerald-500'
                  : 'bg-slate-300'
                " />
              <span class="text-sm text-slate-600">
                Appointment Completed
              </span>
            </div>

          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
