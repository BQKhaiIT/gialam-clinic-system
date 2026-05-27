<script setup>
import { ref, onMounted, computed } from 'vue'

import { getMedicalRecords } from '@/api/medicalRecordApi'

import { getAppointments } from '@/api/appointmentApi'

import { useAdminLayout } from '@/composables/useAdminLayout'

import AdminLayout from '@/layouts/AdminLayout.vue'

import MedicalRecordTable from '@/components/medical_records/MedicalRecordTable.vue'

import MedicalRecordModal from '@/components/medical_records/MedicalRecordModal.vue'

import MedicalRecordDetailDrawer from '@/components/medical_records/MedicalRecordDetailDrawer.vue'

const {
  closeSidebar,

  isDarkMode,

  isSidebarOpen,

  sidebarItems,

  toggleSidebar,

  toggleTheme,
} = useAdminLayout()

const loading = ref(false)

const records = ref([])

const appointments = ref([])

const search = ref('')

const selected = ref(null)

const drawerOpen = ref(false)

const showModal = ref(false)

const error = ref('')

const loadData = async () => {
  loading.value = true

  error.value = ''

  try {
    const [medical, appointment] = await Promise.all([getMedicalRecords(), getAppointments()])

    records.value = medical?.data?.content || []

    appointments.value = appointment?.data?.content || []
  } catch (err) {
    error.value = err?.response?.data?.message || 'Unable to load medical records.'
  } finally {
    loading.value = false
  }
}

const filtered = computed(() => {
  const keyword = search.value

    .toLowerCase()

    .trim()

  if (!keyword) {
    return records.value
  }

  return records.value.filter(
    (record) =>
      record.patientName

        ?.toLowerCase()

        .includes(keyword) ||
      record.doctorName

        ?.toLowerCase()

        .includes(keyword) ||
      record.diagnosis

        ?.toLowerCase()

        .includes(keyword),
  )
})

const openDrawer = (item) => {
  selected.value = item

  drawerOpen.value = true
}

onMounted(loadData)
</script>

<template>
  <AdminLayout
    title="Medical Records"
    :sidebar-items="sidebarItems"
    :is-sidebar-open="isSidebarOpen"
    :is-dark-mode="isDarkMode"
    @close-sidebar="closeSidebar"
    @toggle-sidebar="toggleSidebar"
    @toggle-theme="toggleTheme"
  >
    <section class="space-y-6">
      <div class="dashboard-card rounded-[28px] p-5 sm:p-6">
        <div class="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h2 class="text-[24px] font-bold tracking-[-0.03em] text-slate-900">Medical Records</h2>

            <p class="mt-1 text-sm text-slate-500">
              Manage diagnosis, treatment and clinical records
            </p>
          </div>

          <div class="flex flex-col gap-3 sm:flex-row">
            <input
              v-model="search"
              placeholder="
Search patient,
doctor,
diagnosis
"
              class="h-11 rounded-xl border border-[#D7E8EE] bg-white px-4 text-sm outline-none focus:border-[#2F6F7D] w-[280px]"
            />

            <button
              class="rounded-xl bg-[#2F6F7D] px-5 text-white font-semibold hover:opacity-90"
              @click="showModal = true"
            >
              Create Record
            </button>
          </div>
        </div>
      </div>

      <div v-if="error" class="rounded-[22px] border border-red-200 bg-red-50 p-4 text-red-700">
        {{ error }}
      </div>

      <div
        v-if="!loading && filtered.length === 0"
        class="dashboard-card rounded-[28px] border border-dashed border-[#B9D7DF] bg-[#F7FCFD] p-8 text-center"
      >
        <h2 class="font-bold text-xl">No medical records</h2>

        <p class="text-sm text-slate-500 mt-2">Create your first clinical record</p>
      </div>

      <MedicalRecordTable v-else :records="filtered" :loading="loading" @view="openDrawer" />
    </section>

    <MedicalRecordModal v-model="showModal" :appointments="appointments" @created="loadData" />

    <MedicalRecordDetailDrawer v-model="drawerOpen" :record="selected" />
  </AdminLayout>
</template>
