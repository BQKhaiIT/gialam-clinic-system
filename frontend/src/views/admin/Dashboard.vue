<script setup>
import { computed } from 'vue'

import AppointmentTable from '@/components/AppointmentTable.vue'
import DepartmentPerformance from '@/components/DepartmentPerformance.vue'
import QuickActions from '@/components/QuickActions.vue'
import StatsCard from '@/components/StatsCard.vue'
import TrendChart from '@/components/TrendChart.vue'
import { useAdminLayout } from '@/composables/useAdminLayout'
import { useDashboard } from '@/composables/useDashboard'
import AdminLayout from '@/layouts/AdminLayout.vue'

const { closeSidebar, isDarkMode, isSidebarOpen, sidebarItems, toggleSidebar, toggleTheme } =
  useAdminLayout()

const {
  departmentPerformance,
  error,
  hasData,
  isEmpty,
  loading,
  patientVisitTrend,
  quickActions,
  recentAppointments,
  refreshDashboard,
  statsCards,
} = useDashboard()

const hasRecentAppointments = computed(() => recentAppointments.value.length > 0)
const isTrendEmpty = computed(() => patientVisitTrend.value.values.every((value) => value === 0))
const inProgressAppointments = computed(
  () => recentAppointments.value.filter((item) => item.status === 'IN_PROGRESS').length,
)

const completedAppointments = computed(
  () => recentAppointments.value.filter((item) => item.status === 'COMPLETED').length,
)
</script>

<template>
  <AdminLayout title="Clinic Management System" :sidebar-items="sidebarItems" :is-sidebar-open="isSidebarOpen"
    :is-dark-mode="isDarkMode" @close-sidebar="closeSidebar" @toggle-sidebar="toggleSidebar"
    @toggle-theme="toggleTheme">
    <section class="space-y-6">
      <div v-if="error"
        class="flex flex-col gap-4 rounded-[24px] border border-[#F5C2C7] bg-[#FFF5F6] p-5 text-sm text-[#8B1E2D] sm:flex-row sm:items-center sm:justify-between sm:p-6">
        <div>
          <p class="mb-1 text-base font-bold">Unable to load dashboard data</p>
          <p class="mb-0">{{ error }}</p>
        </div>
        <button type="button"
          class="inline-flex items-center justify-center rounded-xl bg-[#8B1E2D] px-4 py-2.5 font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="loading" @click="refreshDashboard">
          Retry
        </button>
      </div>

      <div class="flex justify-end">
        <button type="button"
          class="inline-flex items-center justify-center rounded-xl border border-[#D7E8ED] bg-white px-4 py-2.5 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="loading" @click="refreshDashboard">
          Refresh Data
        </button>
      </div>

      <div class="grid gap-5 md:grid-cols-2 2xl:grid-cols-4">
        <StatsCard v-for="card in statsCards" :key="card.title" :title="card.title" :value="card.value"
          :subtext="card.subtext" :icon="card.icon" :badge-tone="card.badgeTone" :loading="loading" />
      </div>

      <div class="grid gap-4 md:grid-cols-2">
        <div class="rounded-2xl bg-white p-5 shadow-sm">
          <p class="text-sm text-slate-500">In Progress Examinations</p>

          <h3 class="mt-2 text-3xl font-bold text-violet-600">
            {{ inProgressAppointments }}
          </h3>
        </div>

        <div class="rounded-2xl bg-white p-5 shadow-sm">
          <p class="text-sm text-slate-500">Completed Appointments</p>

          <h3 class="mt-2 text-3xl font-bold text-emerald-600">
            {{ completedAppointments }}
          </h3>
        </div>
      </div>

      <div v-if="isEmpty" class="rounded-[28px] border border-dashed border-[#B9D7DF] bg-[#F7FCFD] p-8 text-center">
        <h2 class="mb-2 text-[24px] font-bold tracking-[-0.03em] text-slate-900">
          No dashboard data available
        </h2>
        <p class="mb-5 text-sm text-slate-500">
          Patient, appointment, medicine, and medical record data will appear here once the API
          returns records.
        </p>
        <button type="button"
          class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90"
          @click="refreshDashboard">
          Reload Dashboard
        </button>
      </div>

      <div class="grid gap-6 2xl:grid-cols-[minmax(0,1.8fr)_360px]">
        <div class="space-y-6">
          <div class="grid gap-6 xl:grid-cols-[minmax(0,1.35fr)_minmax(300px,1fr)]">
            <div class="space-y-4">
              <TrendChart :labels="patientVisitTrend.labels" :values="patientVisitTrend.values" :loading="loading" />
              <div v-if="!loading && hasData && isTrendEmpty"
                class="rounded-[22px] border border-dashed border-[#D7E8ED] bg-[#F8FBFC] px-4 py-3 text-sm text-slate-500">
                No appointment activity recorded in the last 7 days.
              </div>
            </div>
            <DepartmentPerformance :departments="departmentPerformance" :loading="loading" />
          </div>

          <AppointmentTable v-if="loading || hasRecentAppointments" :appointments="recentAppointments"
            :loading="loading" />

          <article v-else
            class="dashboard-card rounded-[28px] border border-dashed border-[#D7E8ED] bg-[#F8FBFC] p-8 text-center">
            <h2 class="mb-2 text-[22px] font-bold tracking-[-0.03em] text-slate-900">
              No recent appointments
            </h2>
            <p class="mb-0 text-sm text-slate-500">
              Recent bookings will appear here after appointment data is available.
            </p>
          </article>
        </div>

        <div class="space-y-6">
          <QuickActions :actions="quickActions" :loading="loading" />
        </div>
      </div>
    </section>
  </AdminLayout>
</template>
