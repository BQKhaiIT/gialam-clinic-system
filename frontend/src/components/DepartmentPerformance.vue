<script setup>
defineProps({
  departments: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
})
</script>

<template>
  <article class="dashboard-card h-full p-5 sm:p-6">
    <div class="mb-6">
      <h2 class="panel-title mb-1 text-slate-900">Department Performance</h2>
      <p class="mb-0 text-sm text-slate-500">Operational performance by specialty</p>
    </div>

    <div v-if="loading" class="space-y-5">
      <div v-for="index in 4" :key="index" class="space-y-2">
        <div class="skeleton h-5 w-36 rounded-full"></div>
        <div class="skeleton h-3.5 w-full rounded-full"></div>
      </div>
    </div>

    <div v-else class="space-y-5">
      <div v-for="department in departments" :key="department.name">
        <div class="mb-2 flex items-center justify-between gap-3">
          <span class="text-sm font-medium text-slate-600">{{ department.name }}</span>
          <span class="text-sm font-semibold text-[#2F6F7D]">{{ department.value }}%</span>
        </div>
        <div class="h-3.5 overflow-hidden rounded-full bg-[#DCECF1]">
          <div
            class="h-full rounded-full bg-gradient-to-r from-[#2F6F7D] to-[#8DBCC7] transition-all duration-500"
            :style="{ width: `${department.value}%` }"
          ></div>
        </div>
      </div>
    </div>
  </article>
</template>
