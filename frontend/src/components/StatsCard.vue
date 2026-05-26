<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: {
    type: String,
    default: '',
  },
  value: {
    type: String,
    default: '',
  },
  subtext: {
    type: String,
    default: '',
  },
  icon: {
    type: [Object, Function],
    required: true,
  },
  badgeTone: {
    type: String,
    default: 'info',
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const toneClasses = {
  success: 'bg-[#ECFDF3] text-[#15803D]',
  warning: 'bg-[#FFF7ED] text-[#EA580C]',
  info: 'bg-[#EDF7F9] text-[#2F6F7D]',
}

const badgeClass = computed(() => toneClasses[props.badgeTone] ?? toneClasses.info)
</script>

<template>
  <article class="dashboard-card dashboard-card-hover h-full p-5 sm:p-6">
    <template v-if="loading">
      <div class="skeleton mb-4 h-5 w-32 rounded-full"></div>
      <div class="skeleton mb-5 h-11 w-28 rounded-2xl"></div>
      <div class="skeleton h-8 w-36 rounded-full"></div>
    </template>

    <template v-else>
      <div class="flex items-start justify-between gap-4">
        <div>
          <p class="mb-2 text-sm font-medium text-slate-500">{{ title }}</p>
          <p class="mb-4 text-4xl font-bold tracking-[-0.03em] text-slate-900">{{ value }}</p>
        </div>
        <div
          class="flex h-[56px] w-[56px] shrink-0 items-center justify-center rounded-[20px] bg-[#F3FAFB] text-[#2F6F7D]"
        >
          <component :is="icon" class="h-[27px] w-[27px] shrink-0 stroke-[2.1]" />
        </div>
      </div>
      <span class="inline-flex rounded-full px-3 py-1 text-sm font-semibold" :class="badgeClass">
        {{ subtext }}
      </span>
    </template>
  </article>
</template>
