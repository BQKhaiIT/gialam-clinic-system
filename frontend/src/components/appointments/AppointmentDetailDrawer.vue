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
} from '@lucide/vue'

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


defineEmits(['close'])
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
                <p class="mb-0 text-sm text-slate-500">Detailed information for the selected appointment</p>
              </div>
              <button
                type="button"
                class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9] hover:text-slate-700"
                @click="$emit('close')"
              >
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
                  <p class="mb-0 text-sm text-slate-600">{{ appointment?.appointmentDate || 'N/A' }}</p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Clock3 class="h-4 w-4 text-[#2F6F7D]" />
                    Time
                  </div>
                  <p class="mb-0 text-sm text-slate-600">{{ appointment?.appointmentTime || 'N/A' }}</p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Info class="h-4 w-4 text-[#2F6F7D]" />
                    Status
                  </div>
                  <p class="mb-0 text-sm text-slate-600">{{ appointment?.status || 'N/A' }}</p>
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
                      <div class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Stethoscope class="h-3.5 w-3.5" />
                        Specialization
                      </div>
                      <p class="mb-0 text-sm text-slate-600">{{ appointment?.doctorSpecialization || 'N/A' }}</p>
                    </div>

                    <div>
                      <div class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Building2 class="h-3.5 w-3.5" />
                        Department
                      </div>
                      <p class="mb-0 text-sm text-slate-600">{{ appointment?.doctorDepartment || 'N/A' }}</p>
                    </div>

                    <div>
                      <div class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Phone class="h-3.5 w-3.5" />
                        Phone
                      </div>
                      <p class="mb-0 text-sm text-slate-600">{{ appointment?.doctorPhone || 'N/A' }}</p>
                    </div>

                    <div>
                      <div class="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.08em] text-slate-400">
                        <Mail class="h-3.5 w-3.5" />
                        Email
                      </div>
                      <p class="mb-0 text-sm text-slate-600">{{ appointment?.doctorEmail || 'N/A' }}</p>
                    </div>
                  </div>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <FileText class="h-4 w-4 text-[#2F6F7D]" />
                    Reason
                  </div>
                  <p class="mb-0 whitespace-pre-line text-sm text-slate-600">{{ appointment?.reason || 'N/A' }}</p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <NotebookText class="h-4 w-4 text-[#2F6F7D]" />
                    Notes
                  </div>
                  <p class="mb-0 whitespace-pre-line text-sm text-slate-600">{{ appointment?.notes || 'N/A' }}</p>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
