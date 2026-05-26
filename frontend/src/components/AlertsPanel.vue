<script setup>
defineProps({
  alerts: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const toneMap = {
  danger: 'border-[#FECACA] bg-[#FEF2F2] text-[#B91C1C]',
  warning: 'border-[#FED7AA] bg-[#FFF7ED] text-[#C2410C]',
  info: 'border-[#BFDBFE] bg-[#EFF6FF] text-[#1D4ED8]',
}
</script>

<template>
  <article class="dashboard-card p-5 sm:p-6">
    <div class="mb-5">
      <h2 class="panel-title mb-1 text-slate-900">Alerts & Notices</h2>
      <p class="mb-0 text-sm text-slate-500">Priority notifications for administrators</p>
    </div>

    <div v-if="loading" class="space-y-4">
      <div v-for="index in 3" :key="index" class="rounded-[20px] border p-4">
        <div class="skeleton mb-3 h-5 w-32 rounded-full"></div>
        <div class="skeleton h-4 w-full rounded-full"></div>
      </div>
    </div>

    <div v-else class="space-y-4">
      <div
        v-for="alert in alerts"
        :key="alert.title"
        class="rounded-[20px] border-l-4 p-4 transition duration-200 hover:translate-y-[-2px]"
        :class="toneMap[alert.tone]"
      >
        <div class="mb-2 flex items-center gap-2">
          <component :is="alert.icon" class="h-5 w-5" />
          <h3 class="mb-0 text-base font-bold">{{ alert.title }}</h3>
        </div>
        <p class="mb-0 text-sm leading-6">{{ alert.message }}</p>
      </div>
    </div>
  </article>
</template>
