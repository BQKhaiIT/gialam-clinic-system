import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'

import { useDashboardStore } from '@/stores/dashboardStore'

export const useDashboard = () => {
  const dashboardStore = useDashboardStore()
  const {
    departmentPerformance,
    error,
    hasData,
    initialized,
    isEmpty,
    loading,
    patientVisitTrend,
    quickActions,
    recentAppointments,
    statsCards,
    completedAppointmentsCount,
    inProgressAppointmentsCount,
    pendingAppointmentsCount,
  } = storeToRefs(dashboardStore)

  const refreshDashboard = async () => {
    await dashboardStore.fetchDashboardData()
  }

  onMounted(async () => {
    if (!initialized.value) {
      await refreshDashboard()
    }
  })

  return {
    clearError: dashboardStore.clearError,
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
    completedAppointmentsCount,
    inProgressAppointmentsCount,
    pendingAppointmentsCount,
  }
}
