import { onMounted, watch } from 'vue'
import { storeToRefs } from 'pinia'

import { useAdminLayoutStore } from '@/stores/adminLayoutStore'

export const useAdminLayout = () => {
  const adminLayoutStore = useAdminLayoutStore()
  const { isDarkMode, isSidebarOpen, sidebarItems } = storeToRefs(adminLayoutStore)

  watch(
    isDarkMode,
    (enabled) => {
      document.body.classList.toggle('dashboard-dark', enabled)
    },
    { immediate: true },
  )

  onMounted(() => {
    adminLayoutStore.initializeLayout()
  })

  return {
    closeSidebar: adminLayoutStore.closeSidebar,
    isDarkMode,
    isSidebarOpen,
    sidebarItems,
    toggleSidebar: adminLayoutStore.toggleSidebar,
    toggleTheme: adminLayoutStore.toggleTheme,
  }
}
