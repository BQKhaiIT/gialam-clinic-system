import { ref } from 'vue'
import { defineStore } from 'pinia'

import { ADMIN_SIDEBAR_ITEMS } from '@/config/adminSidebar'

export const useAdminLayoutStore = defineStore('adminLayout', () => {
  const isSidebarOpen = ref(false)
  const isDarkMode = ref(false)
  const sidebarItems = ref(ADMIN_SIDEBAR_ITEMS)

  const initializeLayout = () => {
    if (window.innerWidth >= 1024) {
      isSidebarOpen.value = true
    }
  }

  const closeSidebar = () => {
    isSidebarOpen.value = false
  }

  const toggleSidebar = () => {
    isSidebarOpen.value = !isSidebarOpen.value
  }

  const toggleTheme = () => {
    isDarkMode.value = !isDarkMode.value
  }

  return {
    closeSidebar,
    initializeLayout,
    isDarkMode,
    isSidebarOpen,
    sidebarItems,
    toggleSidebar,
    toggleTheme,
  }
})
