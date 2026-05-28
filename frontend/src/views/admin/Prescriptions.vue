<script setup>
import { computed, onBeforeUnmount, ref, watch } from 'vue'

import {
  createPrescription,
  deletePrescription,
  getPrescriptions,
  updatePrescription,
} from '@/api/prescriptionApi'

import { getMedicines } from '@/api/medicineApi'

import PrescriptionDetailDrawer from '@/components/prescriptions/PrescriptionDetailDrawer.vue'
import PrescriptionFormModal from '@/components/prescriptions/PrescriptionFormModal.vue'
import PrescriptionSearchBar from '@/components/prescriptions/PrescriptionSearchBar.vue'
import PrescriptionTable from '@/components/prescriptions/PrescriptionTable.vue'

import { useAdminLayout } from '@/composables/useAdminLayout'
import AdminLayout from '@/layouts/AdminLayout.vue'

const PAGE_SIZE = 10
const FETCH_SIZE = 1000

const {
  closeSidebar,
  isDarkMode,
  isSidebarOpen,
  sidebarItems,
  toggleSidebar,
  toggleTheme,
} = useAdminLayout()

const loading = ref(false)
const submitting = ref(false)
const deleting = ref(false)

const error = ref('')

const prescriptions = ref([])
const medicines = ref([])

const searchKeyword = ref('')

const currentPage = ref(1)

const showFormModal = ref(false)
const showDetailDrawer = ref(false)

const formMode = ref('create')

const selectedPrescription = ref(null)

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

  return new Intl.DateTimeFormat('en-GB', options).format(
    parsedDate,
  )
}

const normalizePrescription = (prescription) => ({
  ...prescription,

  createdAt: prescription.createdAt
    ? formatDate(prescription.createdAt, {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
      })
    : 'N/A',
})

const extractPrescriptions = (response) => {
  const content = response?.data?.data

  return Array.isArray(content)
    ? content.map(normalizePrescription)
    : []
}

const totalItems = computed(
  () => prescriptions.value.length,
)

const totalPages = computed(() =>
  Math.max(
    1,
    Math.ceil(totalItems.value / PAGE_SIZE),
  ),
)

const paginatedPrescriptions = computed(() => {
  const start =
    (currentPage.value - 1) * PAGE_SIZE

  return prescriptions.value.slice(
    start,
    start + PAGE_SIZE,
  )
})

const isEmpty = computed(
  () =>
    !loading.value &&
    !error.value &&
    prescriptions.value.length === 0,
)

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

const fetchPrescriptions = async () => {
  loading.value = true
  error.value = ''

  try {
    const response = await getPrescriptions({
      page: 0,
      size: FETCH_SIZE,
      keyword: searchKeyword.value.trim(),
    })

    prescriptions.value =
      extractPrescriptions(response)
  } catch (fetchError) {
    prescriptions.value = []

    error.value =
      fetchError?.response?.data?.message ||
      fetchError?.response?.data?.error ||
      fetchError?.message ||
      'Unable to load prescriptions.'
  } finally {
    loading.value = false
  }
}

const fetchMedicines = async () => {
  try {
    const response = await getMedicines({
      page: 0,
      size: FETCH_SIZE,
    })

    medicines.value =
      response?.data?.data || []
  } catch (error) {
    console.error(error)
  }
}

const openCreateModal = () => {
  formMode.value = 'create'

  selectedPrescription.value = null

  showFormModal.value = true
}

const openEditModal = (prescription) => {
  formMode.value = 'edit'

  selectedPrescription.value = prescription

  showFormModal.value = true
}

const openViewDrawer = (prescription) => {
  selectedPrescription.value = prescription

  showDetailDrawer.value = true
}

const closeFormModal = () => {
  if (submitting.value) {
    return
  }

  showFormModal.value = false
}

const closeDetailDrawer = () => {
  showDetailDrawer.value = false
}

const handleSubmitPrescription = async (
  payload,
) => {
  submitting.value = true

  error.value = ''

  try {
    if (
      formMode.value === 'edit' &&
      selectedPrescription.value?.id
    ) {
      await updatePrescription(
        selectedPrescription.value.id,
        payload,
      )
    } else {
      await createPrescription(payload)
    }

    showFormModal.value = false

    selectedPrescription.value = null

    await fetchPrescriptions()

    resetPage()

    showToast(
      'success',
      formMode.value === 'edit'
        ? 'Prescription updated successfully.'
        : 'Prescription created successfully.',
    )
  } catch (submitError) {
    const message =
      submitError?.response?.data?.message ||
      submitError?.response?.data?.error ||
      submitError?.message ||
      'Unable to save prescription.'

    error.value = message

    showToast('error', message)
  } finally {
    submitting.value = false
  }
}

const handleDeletePrescription = async (
  prescription,
) => {
  const confirmed = confirm(
    `Delete prescription of ${prescription.patientName}?`,
  )

  if (!confirmed) {
    return
  }

  deleting.value = true

  error.value = ''

  try {
    await deletePrescription(prescription.id)

    await fetchPrescriptions()

    showToast(
      'success',
      'Prescription deleted successfully.',
    )

    if (currentPage.value > totalPages.value) {
      currentPage.value = totalPages.value
    }
  } catch (deleteError) {
    error.value =
      deleteError?.response?.data?.message ||
      deleteError?.response?.data?.error ||
      deleteError?.message ||
      'Unable to delete prescription.'
  } finally {
    deleting.value = false
  }
}

const handleSearchClear = async () => {
  if (searchTimer) {
    window.clearTimeout(searchTimer)
  }

  searchKeyword.value = ''

  resetPage()

  await fetchPrescriptions()
}

watch(searchKeyword, () => {
  resetPage()

  if (searchTimer) {
    window.clearTimeout(searchTimer)
  }

  searchTimer = window.setTimeout(async () => {
    await fetchPrescriptions()
  }, 500)
})

watch(totalPages, (pageCount) => {
  if (currentPage.value > pageCount) {
    currentPage.value = pageCount
  }
})

fetchPrescriptions()
fetchMedicines()

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
    title="Prescriptions Management"
    :sidebar-items="sidebarItems"
    :is-sidebar-open="isSidebarOpen"
    :is-dark-mode="isDarkMode"
    @close-sidebar="closeSidebar"
    @toggle-sidebar="toggleSidebar"
    @toggle-theme="toggleTheme"
  >
    <section class="space-y-6">

      <PrescriptionSearchBar
        v-model="searchKeyword"
        :loading="loading"
        @add-prescription="openCreateModal"
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
        <h2
          class="mb-2 text-[24px] font-bold tracking-[-0.03em] text-slate-900"
        >
          No prescriptions available
        </h2>

        <p class="mb-5 text-sm text-slate-500">
          Add a prescription to populate this table.
        </p>

        <button
          type="button"
          class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90"
          @click="openCreateModal"
        >
          Add First Prescription
        </button>
      </div>

      <PrescriptionTable
        v-else
        :prescriptions="paginatedPrescriptions"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-items="totalItems"
        :page-size="PAGE_SIZE"
        @view="openViewDrawer"
        @edit="openEditModal"
        @delete="handleDeletePrescription"
        @page-change="currentPage = $event"
      />
    </section>

    <PrescriptionFormModal
      :show="showFormModal"
      :mode="formMode"
      :prescription="selectedPrescription"
      :medicines="medicines"
      :loading="submitting"
      @close="closeFormModal"
      @submit="handleSubmitPrescription"
    />

    <PrescriptionDetailDrawer
      :show="showDetailDrawer"
      :prescription="selectedPrescription"
      @close="closeDetailDrawer"
    />
  </AdminLayout>
</template>