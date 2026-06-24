<script setup>
import { computed, onBeforeUnmount, ref, watch } from 'vue'

import {
    getMedicines,
    searchMedicines,
    deleteMedicine,
} from '@/api/medicineApi'

import PharmacyTable from '@/components/pharmacy/PharmacyTable.vue'
import PharmacyFormModal from '@/components/pharmacy/PharmacyFormModal.vue'
import PharmacyDetailDrawer from '@/components/pharmacy/PharmacyDetailDrawer.vue'
import PharmacySearchBar from '@/components/pharmacy/PharmacySearchBar.vue'

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
const error = ref('')

const medicines = ref([])
const searchKeyword = ref('')

const currentPage = ref(1)

const showFormModal = ref(false)
const showDetailDrawer = ref(false)

const selectedMedicine = ref(null)

let searchTimer = null

const totalItems = computed(() => medicines.value.length)

const totalPages = computed(() =>
    Math.max(
        1,
        Math.ceil(totalItems.value / PAGE_SIZE),
    ),
)

const paginatedMedicines = computed(() => {
    const start =
        (currentPage.value - 1) * PAGE_SIZE

    return medicines.value.slice(
        start,
        start + PAGE_SIZE,
    )
})

const fetchMedicines = async () => {
    loading.value = true
    error.value = ''

    try {
        const response =
            searchKeyword.value.trim()
                ? await searchMedicines(
                    searchKeyword.value.trim(),
                    0,
                    FETCH_SIZE,
                )
                : await getMedicines(
                    0,
                    FETCH_SIZE,
                )

        medicines.value =
            response?.data?.content || []
    } catch (err) {
        medicines.value = []

        error.value =
            err?.response?.data?.message ||
            err?.message ||
            'Unable to load medicines.'
    } finally {
        loading.value = false
    }
}

const openCreateModal = () => {
    selectedMedicine.value = null
    showFormModal.value = true
}

const openEditModal = (medicine) => {
    selectedMedicine.value = medicine
    showFormModal.value = true
}

const openDetailDrawer = (medicine) => {
    selectedMedicine.value = medicine
    showDetailDrawer.value = true
}

const handleDelete = async (medicine) => {
    const confirmed = confirm(
        `Delete "${medicine.name}" ?`,
    )

    if (!confirmed) return

    await deleteMedicine(medicine.id)

    await fetchMedicines()
}

const handleSaved = async () => {
    showFormModal.value = false

    await fetchMedicines()
}

watch(searchKeyword, () => {
    if (searchTimer) {
        clearTimeout(searchTimer)
    }

    searchTimer = setTimeout(() => {
        fetchMedicines()
    }, 500)
})

fetchMedicines()

onBeforeUnmount(() => {
    if (searchTimer) {
        clearTimeout(searchTimer)
    }
})
</script>

<template>
    <AdminLayout title="Pharmacy Inventory" :sidebar-items="sidebarItems" :is-sidebar-open="isSidebarOpen"
        :is-dark-mode="isDarkMode" @close-sidebar="closeSidebar" @toggle-sidebar="toggleSidebar"
        @toggle-theme="toggleTheme">
        <div class="space-y-6">

            <div class="grid gap-4 md:grid-cols-3">
                <div class="dashboard-card p-5">
                    <p class="text-sm text-slate-500">
                        Total Medicines
                    </p>

                    <h3 class="mt-2 text-3xl font-bold text-slate-900">
                        {{ totalItems }}
                    </h3>
                </div>

                <div class="dashboard-card p-5">
                    <p class="text-sm text-slate-500">
                        Available Medicines
                    </p>

                    <h3 class="mt-2 text-3xl font-bold text-emerald-600">
                        {{
                            medicines.filter(
                                item => item.stockQuantity > 0
                            ).length
                        }}
                    </h3>
                </div>

                <div class="dashboard-card p-5">
                    <p class="text-sm text-slate-500">
                        Low Stock
                    </p>

                    <h3 class="mt-2 text-3xl font-bold text-amber-500">
                        {{
                            medicines.filter(
                                item => item.stockQuantity < 20).length}} </h3>
                </div>
            </div>

            <PharmacySearchBar v-model="searchKeyword" @create="openCreateModal" />

            <PharmacyTable :records="paginatedMedicines" :loading="loading" :current-page="currentPage"
                :total-pages="totalPages" :total-items="totalItems" :page-size="PAGE_SIZE" @view="openDetailDrawer"
                @edit="openEditModal" @delete="handleDelete" @page-change="currentPage = $event" />

            <PharmacyFormModal v-model="showFormModal" :medicine="selectedMedicine" @saved="handleSaved" />

            <PharmacyDetailDrawer v-model="showDetailDrawer" :medicine="selectedMedicine" @edit="openEditModal" />

        </div>
    </AdminLayout>
</template>
