<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue'

import { createDoctor, deleteDoctor, getDoctors, searchDoctors, updateDoctor } from '@/api/doctorApi'
import DoctorDeleteModal from '@/components/doctors/DoctorDeleteModal.vue'
import DoctorFormModal from '@/components/doctors/DoctorFormModal.vue'
import DoctorSearchBar from '@/components/doctors/DoctorSearchBar.vue'
import DoctorTable from '@/components/doctors/DoctorTable.vue'
import { useAdminLayout } from '@/composables/useAdminLayout'
import AdminLayout from '@/layouts/AdminLayout.vue'

const PAGE_SIZE = 10

const { closeSidebar, isDarkMode, isSidebarOpen, sidebarItems, toggleSidebar, toggleTheme } =
  useAdminLayout()

const loading = ref(false)
const submitting = ref(false)
const deleting = ref(false)
const error = ref('')
const doctors = ref([])
const currentPage = ref(1)
const totalPages = ref(1)
const totalItems = ref(0)
const showFormModal = ref(false)
const formMode = ref('create')
const selectedDoctor = ref(null)
const showDeleteModal = ref(false)
const skipNextFilterWatch = ref(false)
const toast = ref({
  show: false,
  type: 'success',
  message: '',
})

const filters = reactive({
  fullName: '',
  department: '',
  specialization: '',
})

let searchTimer = null
let toastTimer = null

const formatDate = (value, options = {}, isDateOnly = false) => {
  if (!value) {
    return 'N/A'
  }

  const parsedDate = new Date(isDateOnly ? `${value}T00:00:00` : value)

  if (Number.isNaN(parsedDate.getTime())) {
    return value
  }

  return new Intl.DateTimeFormat('en-GB', options).format(parsedDate)
}

const formatLabel = (value) =>
  String(value ?? '')
    .toLowerCase()
    .split('_')
    .filter(Boolean)
    .map((segment) => segment.charAt(0).toUpperCase() + segment.slice(1))
    .join(' ')

const normalizeDoctor = (doctor) => ({
  ...doctor,
  gender: String(doctor.gender ?? 'OTHER').toUpperCase(),
  genderLabel: formatLabel(doctor.gender ?? 'OTHER'),
  status: String(doctor.status ?? 'ACTIVE').toUpperCase(),
  statusLabel: formatLabel(doctor.status ?? 'ACTIVE'),
  dobRaw: doctor.dob ?? '',
  dobDisplay: doctor.dob ? formatDate(doctor.dob, { day: '2-digit', month: 'short', year: 'numeric' }, true) : 'N/A',
  createdAtDisplay: doctor.createdAt
    ? formatDate(doctor.createdAt, {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    : 'N/A',
  updatedAtDisplay: doctor.updatedAt
    ? formatDate(doctor.updatedAt, {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    : 'N/A',
})

const hasFilters = computed(
  () => filters.fullName.trim() || filters.department.trim() || filters.specialization.trim(),
)

const isEmpty = computed(() => !loading.value && !error.value && totalItems.value === 0)

const getFilterPayload = () => ({
  fullName: filters.fullName.trim(),
  department: filters.department.trim(),
  specialization: filters.specialization.trim(),
})

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

const applyDoctorResponse = (response) => {
  const pageData = response?.data
  const content = Array.isArray(pageData?.content) ? pageData.content : []

  doctors.value = content.map(normalizeDoctor)
  totalItems.value = Number(pageData?.totalElements ?? content.length)
  totalPages.value = Math.max(1, Number(pageData?.totalPages ?? 1))
}

const fetchDoctorsData = async () => {
  loading.value = true
  error.value = ''

  try {
    const params = {
      page: currentPage.value - 1,
      size: PAGE_SIZE,
    }

    const response = hasFilters.value
      ? await searchDoctors(getFilterPayload(), params)
      : await getDoctors(params)

    applyDoctorResponse(response)
  } catch (fetchError) {
    doctors.value = []
    totalItems.value = 0
    totalPages.value = 1
    error.value =
      fetchError?.response?.data?.message ||
      fetchError?.response?.data?.error ||
      fetchError?.message ||
      'Unable to load doctors.'
  } finally {
    loading.value = false
  }
}

const openCreateModal = () => {
  formMode.value = 'create'
  selectedDoctor.value = null
  showFormModal.value = true
}

const openEditModal = (doctor) => {
  formMode.value = 'edit'
  selectedDoctor.value = doctor
  showFormModal.value = true
}

const closeFormModal = () => {
  if (submitting.value) {
    return
  }

  showFormModal.value = false
}

const openDeleteModal = (doctor) => {
  selectedDoctor.value = doctor
  showDeleteModal.value = true
}

const closeDeleteModal = () => {
  if (deleting.value) {
    return
  }

  showDeleteModal.value = false
}

const handleSubmitDoctor = async (payload) => {
  submitting.value = true
  error.value = ''

  try {
    if (formMode.value === 'edit' && selectedDoctor.value?.id) {
      await updateDoctor(selectedDoctor.value.id, payload)
    } else {
      currentPage.value = 1
      await createDoctor(payload)
    }

    showFormModal.value = false
    selectedDoctor.value = null
    await fetchDoctorsData()
    showToast(
      'success',
      formMode.value === 'edit' ? 'Doctor updated successfully.' : 'Doctor created successfully.',
    )
  } catch (submitError) {
    const message =
      submitError?.response?.data?.message ||
      submitError?.response?.data?.error ||
      submitError?.message ||
      (formMode.value === 'edit' ? 'Unable to update doctor.' : 'Unable to create doctor.')

    error.value = message
    showToast('error', message)
  } finally {
    submitting.value = false
  }
}

const handleDeleteDoctor = async () => {
  if (!selectedDoctor.value?.id) {
    return
  }

  deleting.value = true
  error.value = ''

  try {
    if (currentPage.value > 1 && doctors.value.length === 1) {
      currentPage.value -= 1
    }

    await deleteDoctor(selectedDoctor.value.id)
    showDeleteModal.value = false
    await fetchDoctorsData()
    showToast('success', 'Doctor deleted successfully.')
  } catch (deleteError) {
    const message =
      deleteError?.response?.data?.message ||
      deleteError?.response?.data?.error ||
      deleteError?.message ||
      'Unable to delete doctor.'

    error.value = message
    showToast('error', message)
  } finally {
    deleting.value = false
  }
}

const handlePageChange = async (page) => {
  if (page < 1 || page > totalPages.value || page === currentPage.value || loading.value) {
    return
  }

  currentPage.value = page
  await fetchDoctorsData()
}

const handleClearFilters = async () => {
  skipNextFilterWatch.value = true

  if (searchTimer) {
    window.clearTimeout(searchTimer)
  }

  filters.fullName = ''
  filters.department = ''
  filters.specialization = ''
  currentPage.value = 1
  await fetchDoctorsData()
}

watch(
  () => [filters.fullName, filters.department, filters.specialization],
  () => {
    if (skipNextFilterWatch.value) {
      skipNextFilterWatch.value = false
      return
    }

    currentPage.value = 1

    if (searchTimer) {
      window.clearTimeout(searchTimer)
    }

    searchTimer = window.setTimeout(async () => {
      await fetchDoctorsData()
    }, 400)
  },
)

onMounted(async () => {
  await fetchDoctorsData()
})

onBeforeUnmount(() => {
  if (searchTimer) {
    window.clearTimeout(searchTimer)
  }

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
    title="Doctors Management"
    :sidebar-items="sidebarItems"
    :is-sidebar-open="isSidebarOpen"
    :is-dark-mode="isDarkMode"
    @close-sidebar="closeSidebar"
    @toggle-sidebar="toggleSidebar"
    @toggle-theme="toggleTheme"
  >
    <section class="space-y-6">
      <DoctorSearchBar
        v-model:full-name="filters.fullName"
        v-model:department="filters.department"
        v-model:specialization="filters.specialization"
        :loading="loading || submitting || deleting"
        @add-doctor="openCreateModal"
        @clear="handleClearFilters"
      />

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
        <h2 class="mb-2 text-[24px] font-bold tracking-[-0.03em] text-slate-900">No doctors available</h2>
        <p class="mb-5 text-sm text-slate-500">
          Add a doctor or adjust the current search filters.
        </p>
        <button
          type="button"
          class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90"
          @click="openCreateModal"
        >
          Create First Doctor
        </button>
      </div>

      <DoctorTable
        v-else
        :doctors="doctors"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="totalItems"
        :page-size="PAGE_SIZE"
        @edit="openEditModal"
        @delete="openDeleteModal"
        @page-change="handlePageChange"
      />
    </section>

    <DoctorFormModal
      :show="showFormModal"
      :mode="formMode"
      :doctor="selectedDoctor"
      :loading="submitting"
      @close="closeFormModal"
      @submit="handleSubmitDoctor"
    />

    <DoctorDeleteModal
      :show="showDeleteModal"
      :doctor="selectedDoctor"
      :loading="deleting"
      @close="closeDeleteModal"
      @confirm="handleDeleteDoctor"
    />
  </AdminLayout>
</template>
