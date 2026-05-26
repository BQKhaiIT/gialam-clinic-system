<script setup>
defineProps({
  actions: {
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
  <article class="dashboard-card p-5 sm:p-6">
    <div class="mb-5">
      <h2 class="mb-0 text-[28px] font-bold tracking-[-0.03em] text-slate-900">Quick Actions</h2>
      <div class="mt-4 h-px w-full bg-[#D7E8ED]"></div>
    </div>

    <div v-if="loading" class="grid grid-cols-2 gap-4">
      <div v-for="index in 4" :key="index" class="skeleton h-[124px] rounded-[22px]"></div>
    </div>

    <div v-else class="grid grid-cols-2 gap-4">
      <button
        v-for="action in actions"
        :key="action.label"
        type="button"
        class="group flex h-[124px] flex-col items-center justify-center gap-3.5 overflow-hidden rounded-[22px] border px-3 text-center transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg"
        :class="
          action.accent
            ? 'border-[#447B87] bg-[#447B87] text-white shadow-md shadow-cyan-900/10'
            : 'border-[#9ECFDE] bg-[#DDF0F8] text-[#3C7180] hover:bg-[#EAF7FB]'
        "
      >
        <component :is="action.icon" class="h-[30px] w-[30px] shrink-0 stroke-[2.1]" />
        <span
          class="max-w-[86px] text-[15px] font-medium leading-[1.35] tracking-[-0.02em] sm:text-[17px]"
        >
          {{ action.label }}
        </span>
      </button>
    </div>
  </article>
</template>
