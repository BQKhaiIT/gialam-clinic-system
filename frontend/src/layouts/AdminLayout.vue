<script setup>
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'

defineProps({
  sidebarItems: {
    type: Array,
    default: () => [],
  },
  isSidebarOpen: {
    type: Boolean,
    default: false,
  },
  isDarkMode: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '',
  },
})

defineEmits(['close-sidebar', 'toggle-sidebar', 'toggle-theme'])
</script>

<template>
  <div
    class="min-h-screen bg-transparent text-[var(--color-text)] lg:grid lg:grid-cols-[228px_minmax(0,1fr)]"
  >
    <Transition name="fade-slide">
      <div
        v-if="isSidebarOpen"
        class="fixed inset-0 z-40 bg-slate-900/35 backdrop-blur-[2px] lg:hidden"
        @click="$emit('close-sidebar')"
      ></div>
    </Transition>

    <Sidebar :items="sidebarItems" :is-open="isSidebarOpen" @close="$emit('close-sidebar')" />

    <div class="min-w-0">
      <Header
        :title="title"
        :is-dark-mode="isDarkMode"
        @toggle-sidebar="$emit('toggle-sidebar')"
        @toggle-theme="$emit('toggle-theme')"
      />

      <main class="min-h-screen px-4 pb-8 pt-4 transition-all duration-300 sm:px-5 lg:px-7 lg:pt-6">
        <slot />
      </main>
    </div>
  </div>
</template>
