<script setup>
defineProps({
  health: {
    type: Object,
    default: () => ({}),
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const bars = [
  { key: 'cpu', label: 'Server CPU', tone: 'bg-[#2F6F7D]' },
  { key: 'memory', label: 'Memory Usage', tone: 'bg-[#F59E0B]' },
  { key: 'storage', label: 'Database Storage', tone: 'bg-[#4B8C9B]' },
]
</script>

<template>
  <article class="dashboard-card p-5 sm:p-6">
    <div class="mb-5">
      <h2 class="panel-title mb-1 text-slate-900">System Health</h2>
      <p class="mb-0 text-sm text-slate-500">Real-time platform health overview</p>
    </div>

    <div v-if="loading" class="space-y-5">
      <div v-for="index in 4" :key="index" class="space-y-2">
        <div class="skeleton h-4 w-28 rounded-full"></div>
        <div class="skeleton h-3.5 w-full rounded-full"></div>
      </div>
    </div>

    <div v-else class="space-y-5">
      <div v-for="bar in bars" :key="bar.key">
        <div class="mb-2 flex items-center justify-between text-sm">
          <span class="font-medium text-slate-600">{{ bar.label }}</span>
          <span class="font-bold text-slate-700">{{ health[bar.key] }}%</span>
        </div>
        <div class="h-3.5 overflow-hidden rounded-full bg-[#DCECF1]">
          <div
            class="h-full rounded-full transition-all duration-500"
            :class="bar.tone"
            :style="{ width: `${health[bar.key]}%` }"
          ></div>
        </div>
      </div>

      <div class="rounded-[20px] border border-[#D7E8ED] bg-[#F6FBFC] px-4 py-4">
        <div class="mb-2 flex items-center justify-between gap-3">
          <span class="text-sm font-medium text-slate-600">API Status</span>
          <span class="inline-flex items-center gap-2 text-sm font-bold text-[#15803D]">
            <span class="h-2.5 w-2.5 rounded-full bg-[#22C55E]"></span>
            {{ health.apiStatus }}
          </span>
        </div>
        <p class="mb-0 text-sm text-slate-500">{{ health.uptime }}</p>
      </div>
    </div>
  </article>
</template>
