<script setup>
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { AlertTriangle, CalendarDays, Search, Stethoscope, UserPlus, X } from '@lucide/vue'

import {
  createAppointment,
  deleteAppointment,
  getAppointmentById,
  getAppointments,
  updateAppointment,
} from '@/api/appointmentApi'
import { getDoctors } from '@/api/doctorApi'
import { getPatients } from '@/api/patientApi'
import AppointmentDetailDrawer from '@/components/appointments/AppointmentDetailDrawer.vue'
import AppointmentFormModal from '@/components/appointments/AppointmentFormModal.vue'
import AppointmentTable from '@/components/appointments/AppointmentTable.vue'
import { useAdminLayout } from '@/composables/useAdminLayout'
import AdminLayout from '@/layouts/AdminLayout.vue'

const PAGE_SIZE = 10
const FETCH_SIZE = 1000

const { closeSidebar, isDarkMode, isSidebarOpen, sidebarItems, toggleSidebar, toggleTheme } =
  useAdminLayout()

const loading = ref(false)
const submitting = ref(false)
const deleting = ref(false)
const error = ref('')
const appointments = ref([])
const patients = ref([])
const doctors = ref([])
const searchKeyword = ref('')
const filterDate = ref('')
const selectedDoctorId = ref('')
const currentPage = ref(1)
const showFormModal = ref(false)
const formMode = ref('create')
const selectedAppointment = ref(null)
const selectedAppointmentDetail = ref(null)
const showDetailDrawer = ref(false)
const showDeleteModal = ref(false)
const toast = ref({
  show: false,
  type: 'success',
  message: '',
})
let toastTimer = null

const formatDate = (value, options = {}) => {
  if (!value) {
    return 'N/A'
  }

  const parsedDate = new Date(value)

  if (Number.isNaN(parsedDate.getTime())) {
    return value
  }

  return new Intl.DateTimeFormat('en-GB', options).format(parsedDate)
}

const formatTime = (value) => {
  if (!value) {
    return 'N/A'
  }

  const timeValue = String(value).slice(0, 5)

  if (!/^\d{2}:\d{2}$/.test(timeValue)) {
    return value
  }

  const [hours, minutes] = timeValue.split(':').map(Number)
  return new Intl.DateTimeFormat('en-GB', {
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(2000, 0, 1, hours, minutes))
}

const normalizeAppointment = (appointment) => ({
  ...appointment,
  appointmentDateRaw: appointment.appointmentDate ?? '',
  appointmentTimeRaw: appointment.appointmentTime ? String(appointment.appointmentTime).slice(0, 5) : '',
  appointmentDate: appointment.appointmentDate
    ? formatDate(appointment.appointmentDate, { day: '2-digit', month: 'short', year: 'numeric' })
    : 'N/A',
  appointmentTime: appointment.appointmentTime ? formatTime(appointment.appointmentTime) : 'N/A',
  createdAt: appointment.createdAt
    ? formatDate(appointment.createdAt, {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    : 'N/A',
  status: String(appointment.status ?? 'PENDING').toUpperCase(),
})

const extractAppointments = (response) => {
  const content = response?.data?.content
  return Array.isArray(content) ? content.map(normalizeAppointment) : []
}

const extractPatients = (response) => {
  const content = response?.data?.content
  return Array.isArray(content) ? content : []
}

const extractDoctors = (response) => {
  const content = response?.data?.content
  return Array.isArray(content)
    ? content.map((doctor) => ({
        ...doctor,
        status: String(doctor.status ?? 'ACTIVE').toUpperCase(),
      }))
    : []
}

const doctorOptions = computed(() =>
  Array.from(
    new Map(
      [
        ...appointments.value
          .filter((appointment) => appointment.doctorId && appointment.doctorName)
          .map((appointment) => ({
            id: appointment.doctorId,
            fullName: appointment.doctorName,
            specialization: appointment.doctorSpecialization ?? '',
            department: appointment.doctorDepartment ?? '',
            phone: appointment.doctorPhone ?? '',
            email: appointment.doctorEmail ?? '',
            status: String(appointment.doctorStatus ?? 'ACTIVE').toUpperCase(),
          })),
        ...doctors.value.map((doctor) => ({
          id: doctor.id,
          fullName: doctor.fullName,
          specialization: doctor.specialization,
          department: doctor.department,
          phone: doctor.phone,
          email: doctor.email,
          status: doctor.status,
        })),
      ].map((doctor) => [doctor.id, doctor]),
    ).values(),
  ).sort((firstDoctor, secondDoctor) => firstDoctor.fullName.localeCompare(secondDoctor.fullName)),
)

const filteredAppointments = computed(() => {
  const normalizedKeyword = searchKeyword.value.trim().toLowerCase()

  return appointments.value.filter((appointment) => {
    const matchesKeyword =
      !normalizedKeyword ||
      appointment.patientName?.toLowerCase().includes(normalizedKeyword) ||
      appointment.doctorName?.toLowerCase().includes(normalizedKeyword) ||
      appointment.status?.toLowerCase().includes(normalizedKeyword) ||
      appointment.reason?.toLowerCase().includes(normalizedKeyword)

    const matchesDate = !filterDate.value || appointment.appointmentDateRaw === filterDate.value
    const matchesDoctor =
      !selectedDoctorId.value || String(appointment.doctorId ?? '') === String(selectedDoctorId.value)

    return matchesKeyword && matchesDate && matchesDoctor
  })
})

const totalItems = computed(() => filteredAppointments.value.length)
const totalPages = computed(() => Math.max(1, Math.ceil(totalItems.value / PAGE_SIZE)))
const paginatedAppointments = computed(() => {
  const start = (currentPage.value - 1) * PAGE_SIZE
  return filteredAppointments.value.slice(start, start + PAGE_SIZE)
})
const isEmpty = computed(() => !loading.value && !error.value && filteredAppointments.value.length === 0)

const showToast = (type, message) => {
  toast.value = {
    show: true,
    type,
    message,
  }

  if (toastTimer) {
    window.clearTimeout(toastTimer)
  }

  toastTimer = window.setTimeout(() => {
    toast.value.show = false
  }, 3000)
}

const resetPage = () => {
  currentPage.value = 1
}

const fetchAppointmentsData = async () => {
  loading.value = true
  error.value = ''

  try {
    const [appointmentsResponse, patientsResponse, doctorsResponse] = await Promise.all([
      getAppointments({ page: 0, size: FETCH_SIZE }),
      getPatients({ page: 0, size: FETCH_SIZE }),
      getDoctors({ page: 0, size: FETCH_SIZE }),
    ])

    appointments.value = extractAppointments(appointmentsResponse)
    patients.value = extractPatients(patientsResponse)
    doctors.value = extractDoctors(doctorsResponse)
  } catch (fetchError) {
    appointments.value = []
    patients.value = []
    doctors.value = []
    error.value =
      fetchError?.response?.data?.message ||
      fetchError?.response?.data?.error ||
      fetchError?.message ||
      'Unable to load appointments.'
  } finally {
    loading.value = false
  }
}

const openCreateModal = () => {
  formMode.value = 'create'
  selectedAppointment.value = null
  showFormModal.value = true
}

const openEditModal = (appointment) => {
  formMode.value = 'edit'
  selectedAppointment.value = appointment
  showFormModal.value = true
}

const closeFormModal = () => {
  if (submitting.value) {
    return
  }

  showFormModal.value = false
}

const openDeleteModal = (appointment) => {
  selectedAppointment.value = appointment
  showDeleteModal.value = true
}

const closeDeleteModal = () => {
  if (deleting.value) {
    return
  }

  showDeleteModal.value = false
}

const openDetailDrawer = async (appointment) => {
  error.value = ''

  try {
    const response = await getAppointmentById(appointment.id)
    selectedAppointmentDetail.value = normalizeAppointment(response?.data ?? appointment)
    showDetailDrawer.value = true
  } catch (detailError) {
    const message =
      detailError?.response?.data?.message ||
      detailError?.response?.data?.error ||
      detailError?.message ||
      'Unable to load appointment detail.'
    error.value = message
    showToast('error', message)
  }
}

const closeDetailDrawer = () => {
  showDetailDrawer.value = false
}

const handleSubmitAppointment = async (payload) => {
  submitting.value = true
  error.value = ''

  try {
    if (formMode.value === 'edit' && selectedAppointment.value?.id) {
      await updateAppointment(selectedAppointment.value.id, payload)
    } else {
      await createAppointment(payload)
    }

    showFormModal.value = false
    selectedAppointment.value = null
    await fetchAppointmentsData()
    resetPage()
    showToast(
      'success',
      formMode.value === 'edit'
        ? 'Appointment updated successfully.'
        : 'Appointment created successfully.',
    )
  } catch (submitError) {
    const message =
      submitError?.response?.data?.message ||
      submitError?.response?.data?.error ||
      submitError?.message ||
      (formMode.value === 'edit' ? 'Unable to update appointment.' : 'Unable to create appointment.')
    error.value = message
    showToast('error', message)
  } finally {
    submitting.value = false
  }
}

const handleDeleteAppointment = async () => {
  if (!selectedAppointment.value?.id) {
    return
  }

  deleting.value = true
  error.value = ''

  try {
    await deleteAppointment(selectedAppointment.value.id)
    showDeleteModal.value = false
    await fetchAppointmentsData()
    if (currentPage.value > totalPages.value) {
      currentPage.value = totalPages.value
    }
    showToast('success', 'Appointment deleted successfully.')
  } catch (deleteError) {
    const message =
      deleteError?.response?.data?.message ||
      deleteError?.response?.data?.error ||
      deleteError?.message ||
      'Unable to delete appointment.'
    error.value = message
    showToast('error', message)
  } finally {
    deleting.value = false
  }
}

watch([searchKeyword, filterDate, selectedDoctorId], () => {
  resetPage()
})

watch(totalPages, (pageCount) => {
  if (currentPage.value > pageCount) {
    currentPage.value = pageCount
  }
})

onMounted(async () => {
  await fetchAppointmentsData()
})

onBeforeUnmount(() => {
  if (toastTimer) {
    window.clearTimeout(toastTimer)
  }
})
</script>

<template>
  <Transition name="fade-slide">
    <div v-if="toast.show" class="fixed right-4 top-4 z-[80] w-full max-w-sm">
      <div
        class="dashboard-card rounded-[22px] px-4 py-3 text-sm font-semibold shadow-[0_18px_40px_rgba(69,102,112,0.18)]"
        :class="
          toast.type === 'success'
            ? 'border-[#B7E4C7] bg-[#F0FFF5] text-[#166534]'
            : 'border-[#F5C2C7] bg-[#FFF5F6] text-[#8B1E2D]'
        "
      >
        {{ toast.message }}
      </div>
    </div>
  </Transition>

  <AdminLayout
    title="Appointments Management"
    :sidebar-items="sidebarItems"
    :is-sidebar-open="isSidebarOpen"
    :is-dark-mode="isDarkMode"
    @close-sidebar="closeSidebar"
    @toggle-sidebar="toggleSidebar"
    @toggle-theme="toggleTheme"
  >
    <section class="space-y-6">
      <article class="dashboard-card p-5 sm:p-6">
        <div class="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h2 class="panel-title mb-1 text-slate-900">Appointments Management</h2>
            <p class="mb-0 text-sm text-slate-500">Create, review, update, and filter appointment records</p>
          </div>

          <div class="flex flex-col gap-3 md:flex-row md:items-center">
            <label class="glass-input relative min-w-0 md:w-[280px]">
              <Search
                class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
              />
              <input
                v-model="searchKeyword"
                type="text"
                class="block h-[44px] w-full border-0 bg-transparent pl-11 pr-4 text-[14px] text-slate-700 placeholder:text-slate-400 focus:outline-none"
                placeholder="Search patient, doctor, status..."
              />
            </label>

            <label class="glass-input relative min-w-0 md:w-[220px]">
              <CalendarDays
                class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
              />
              <input
                v-model="filterDate"
                type="date"
                class="block h-[44px] w-full border-0 bg-transparent pl-11 pr-4 text-[14px] text-slate-700 focus:outline-none"
              />
            </label>

            <label class="glass-input relative min-w-0 md:w-[260px]">
              <Stethoscope
                class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
              />
              <select
                v-model="selectedDoctorId"
                class="block h-[44px] w-full appearance-none border-0 bg-transparent pl-11 pr-4 text-[14px] text-slate-700 focus:outline-none"
              >
                <option value="">All doctors</option>
                <option v-for="doctor in doctorOptions" :key="doctor.id" :value="String(doctor.id)">
                  {{ doctor.fullName }}{{ doctor.specialization ? ` • ${doctor.specialization}` : '' }}
                </option>
              </select>
            </label>

            <button
              type="button"
              class="inline-flex items-center justify-center gap-2 rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
              :disabled="loading || submitting || deleting"
              @click="openCreateModal"
            >
              <UserPlus class="h-4 w-4" />
              Add Appointment
            </button>
          </div>
        </div>
      </article>

      <div
        v-if="error"
        class="rounded-[24px] border border-[#F5C2C7] bg-[#FFF5F6] px-5 py-4 text-sm text-[#8B1E2D] sm:px-6"
      >
        {{ error }}
      </div>

      <div
        v-if="isEmpty"
        class="dashboard-card rounded-[28px] border border-dashed border-[#B9D7DF] bg-[#F7FCFD] p-8 text-center"
      >
        <h2 class="mb-2 text-[24px] font-bold tracking-[-0.03em] text-slate-900">
          No appointments available
        </h2>
        <p class="mb-5 text-sm text-slate-500">
          Create a new appointment or adjust your search and date filters.
        </p>
        <button
          type="button"
          class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90"
          @click="openCreateModal"
        >
          Create First Appointment
        </button>
      </div>

      <AppointmentTable
        v-else
        :appointments="paginatedAppointments"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="totalItems"
        :page-size="PAGE_SIZE"
        @view="openDetailDrawer"
        @edit="openEditModal"
        @delete="openDeleteModal"
        @page-change="currentPage = $event"
      />
    </section>

    <AppointmentFormModal
      :show="showFormModal"
      :mode="formMode"
      :appointment="selectedAppointment"
      :patients="patients"
      :doctors="doctorOptions"
      :loading="submitting"
      @close="closeFormModal"
      @submit="handleSubmitAppointment"
    />

    <AppointmentDetailDrawer
      :show="showDetailDrawer"
      :appointment="selectedAppointmentDetail"
      @close="closeDetailDrawer"
    />

    <Teleport to="body">
      <Transition name="fade-slide">
        <div
          v-if="showDeleteModal"
          class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/45 px-4 py-6 backdrop-blur-[2px]"
        >
          <div class="dashboard-card w-full max-w-lg overflow-hidden">
            <div class="flex items-start justify-between px-5 py-5 sm:px-6">
              <div class="flex items-start gap-4">
                <div
                  class="flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl bg-[#FFF5F6] text-[#C2414E]"
                >
                  <AlertTriangle class="h-6 w-6" />
                </div>
                <div>
                  <h2 class="panel-title mb-1 text-slate-900">Delete Appointment</h2>
                  <p class="mb-0 text-sm text-slate-500">
                    This action will permanently remove the appointment for
                    <span class="font-semibold text-slate-700">{{ selectedAppointment?.patientName }}</span>.
                  </p>
                </div>
              </div>
              <button
                type="button"
                class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9] hover:text-slate-700"
                @click="closeDeleteModal"
              >
                <X class="h-5 w-5" />
              </button>
            </div>

            <div class="border-y border-slate-200/70 bg-[#F8FBFC] px-5 py-4 text-sm text-slate-600 sm:px-6">
              Appointment ID:
              <span class="font-semibold text-slate-900">#{{ selectedAppointment?.id }}</span>
            </div>

            <div class="flex justify-end gap-3 px-5 py-5 sm:px-6">
              <button
                type="button"
                class="inline-flex items-center justify-center rounded-xl border border-[#D7E8ED] px-4 py-2.5 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
                @click="closeDeleteModal"
              >
                Cancel
              </button>
              <button
                type="button"
                class="inline-flex items-center justify-center rounded-xl bg-[#C2414E] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
                :disabled="deleting"
                @click="handleDeleteAppointment"
              >
                {{ deleting ? 'Deleting...' : 'Delete Appointment' }}
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </AdminLayout>
</template>
