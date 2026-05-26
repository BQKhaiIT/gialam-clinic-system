<script setup>
import { computed, onBeforeUnmount, ref, watch } from 'vue'

import { createPatient, deletePatient, getPatients, searchPatients, updatePatient } from '@/api/patientApi'
import PatientDeleteModal from '@/components/patients/PatientDeleteModal.vue'
import PatientFormModal from '@/components/patients/PatientFormModal.vue'
import PatientSearchBar from '@/components/patients/PatientSearchBar.vue'
import PatientTable from '@/components/patients/PatientTable.vue'
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
const searchKeyword = ref('')
const patients = ref([])
const currentPage = ref(1)
const showFormModal = ref(false)
const formMode = ref('create')
const selectedPatient = ref(null)
const showDeleteModal = ref(false)
const skipNextSearchWatch = ref(false)
const toast = ref({
  show: false,
  type: 'success',
  message: '',
})
let searchTimer = null
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

const normalizePatient = (patient) => ({
  ...patient,
  dobRaw: patient.dob ?? '',
  dob: patient.dob ? formatDate(patient.dob, { day: '2-digit', month: 'short', year: 'numeric' }) : 'N/A',
  createdAt: patient.createdAt
    ? formatDate(patient.createdAt, {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
      })
    : 'N/A',
})

const extractPatients = (response) => {
  const content = response?.data?.content
  return Array.isArray(content) ? content.map(normalizePatient) : []
}

const totalItems = computed(() => patients.value.length)
const totalPages = computed(() => Math.max(1, Math.ceil(totalItems.value / PAGE_SIZE)))
const paginatedPatients = computed(() => {
  const start = (currentPage.value - 1) * PAGE_SIZE
  return patients.value.slice(start, start + PAGE_SIZE)
})
const isEmpty = computed(() => !loading.value && !error.value && patients.value.length === 0)

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

const fetchPatients = async () => {
  loading.value = true
  error.value = ''

  try {
    const response = searchKeyword.value.trim()
      ? await searchPatients(searchKeyword.value.trim(), { page: 0, size: FETCH_SIZE })
      : await getPatients({ page: 0, size: FETCH_SIZE })

    patients.value = extractPatients(response)
  } catch (fetchError) {
    patients.value = []
    error.value =
      fetchError?.response?.data?.message ||
      fetchError?.response?.data?.error ||
      fetchError?.message ||
      'Unable to load patients.'
  } finally {
    loading.value = false
  }
}

const openCreateModal = () => {
  formMode.value = 'create'
  selectedPatient.value = null
  showFormModal.value = true
}

const openEditModal = (patient) => {
  formMode.value = 'edit'
  selectedPatient.value = patient
  showFormModal.value = true
}

const openViewModal = (patient) => {
  formMode.value = 'view'
  selectedPatient.value = patient
  showFormModal.value = true
}

const closeFormModal = () => {
  if (submitting.value) {
    return
  }

  showFormModal.value = false
}

const openDeleteModal = (patient) => {
  selectedPatient.value = patient
  showDeleteModal.value = true
}

const closeDeleteModal = () => {
  if (deleting.value) {
    return
  }

  showDeleteModal.value = false
}

const handleSubmitPatient = async (payload) => {
  submitting.value = true
  error.value = ''

  try {
    const submitPayload = {
      fullName: payload.fullName,
      dob: payload.dob,
      gender: payload.gender,
      phone: payload.phone,
      address: payload.address,
      bloodType: payload.bloodType,
      allergies: payload.allergies,
    }

    if (formMode.value === 'edit' && selectedPatient.value?.id) {
      await updatePatient(selectedPatient.value.id, submitPayload)
    } else {
      await createPatient(submitPayload)
    }

    showFormModal.value = false
    selectedPatient.value = null
    await fetchPatients()
    resetPage()
    showToast(
      'success',
      formMode.value === 'edit'
        ? 'Patient updated successfully.'
        : 'Patient created successfully.',
    )
  } catch (submitError) {
    const message =
      submitError?.response?.data?.message ||
      submitError?.response?.data?.error ||
      submitError?.message ||
      (formMode.value === 'edit' ? 'Unable to update patient.' : 'Unable to create patient.')

    error.value = message
    showToast('error', message)
  } finally {
    submitting.value = false
  }
}

const handleDeletePatient = async () => {
  if (!selectedPatient.value?.id) {
    return
  }

  deleting.value = true
  error.value = ''

  try {
    await deletePatient(selectedPatient.value.id)
    showDeleteModal.value = false
    await fetchPatients()

    if (currentPage.value > totalPages.value) {
      currentPage.value = totalPages.value
    }
  } catch (deleteError) {
    error.value =
      deleteError?.response?.data?.message ||
      deleteError?.response?.data?.error ||
      deleteError?.message ||
      'Unable to delete patient.'
  } finally {
    deleting.value = false
  }
}

const handleSearchClear = async () => {
  skipNextSearchWatch.value = true

  if (searchTimer) {
    window.clearTimeout(searchTimer)
  }

  searchKeyword.value = ''
  resetPage()
  await fetchPatients()
}

watch(searchKeyword, () => {
  if (skipNextSearchWatch.value) {
    skipNextSearchWatch.value = false
    return
  }

  resetPage()

  if (searchTimer) {
    window.clearTimeout(searchTimer)
  }

  searchTimer = window.setTimeout(async () => {
    await fetchPatients()
  }, 500)
})

watch(totalPages, (pageCount) => {
  if (currentPage.value > pageCount) {
    currentPage.value = pageCount
  }
})

fetchPatients()

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
    <div
      v-if="toast.show"
      class="fixed right-4 top-4 z-[80] w-full max-w-sm"
    >
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
    title="Patients Management"
    :sidebar-items="sidebarItems"
    :is-sidebar-open="isSidebarOpen"
    :is-dark-mode="isDarkMode"
    @close-sidebar="closeSidebar"
    @toggle-sidebar="toggleSidebar"
    @toggle-theme="toggleTheme"
  >
    <section class="space-y-6">
      <PatientSearchBar
        v-model="searchKeyword"
        :loading="loading || submitting || deleting"
        @add-patient="openCreateModal"
        @clear="handleSearchClear"
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
        <h2 class="mb-2 text-[24px] font-bold tracking-[-0.03em] text-slate-900">
          No patients available
        </h2>
        <p class="mb-5 text-sm text-slate-500">
          Add a patient or try a different search keyword to populate this table.
        </p>
        <button
          type="button"
          class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90"
          @click="openCreateModal"
        >
          Add First Patient
        </button>
      </div>

      <PatientTable
        v-else
        :patients="paginatedPatients"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="totalItems"
        :page-size="PAGE_SIZE"
        @view="openViewModal"
        @edit="openEditModal"
        @delete="openDeleteModal"
        @page-change="currentPage = $event"
      />
    </section>

    <PatientFormModal
      :show="showFormModal"
      :mode="formMode"
      :patient="selectedPatient"
      :loading="submitting"
      @close="closeFormModal"
      @submit="handleSubmitPatient"
    />

    <PatientDeleteModal
      :show="showDeleteModal"
      :patient="selectedPatient"
      :loading="deleting"
      @close="closeDeleteModal"
      @confirm="handleDeletePatient"
    />
  </AdminLayout>
</template>
