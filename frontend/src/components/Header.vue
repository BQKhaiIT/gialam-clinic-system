<script setup>
import { Bell, Clock3, Menu, Search } from '@lucide/vue'

defineProps({
  title: {
    type: String,
    default: '',
  },
  isDarkMode: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['toggle-sidebar'])
</script>

<template>
  <header
    class="sticky top-0 z-30 flex h-[58px] items-center justify-between border-b border-[#D9E8ED] bg-white px-4 transition-colors duration-300 sm:px-5 lg:px-7"
  >
    <div class="flex items-center gap-3">
      <button
        type="button"
        class="inline-flex h-11 w-11 items-center justify-center rounded-2xl border border-slate-200 bg-white text-slate-700 shadow-sm lg:hidden"
        @click="$emit('toggle-sidebar')"
      >
        <Menu class="h-5 w-5" />
      </button>
      <p class="mb-0 text-[19px] font-bold tracking-[-0.02em] text-slate-900 lg:text-[20px]">
        {{ title }}
      </p>
    </div>

    <div class="flex items-center gap-2.5 lg:gap-3.5">

      <button
        type="button"
        class="inline-flex h-9 w-9 items-center justify-center rounded-xl text-slate-600 transition hover:bg-[#EAF4F6] hover:text-slate-900"
      >
        <Bell class="h-[18px] w-[18px]" />
      </button>

      <button
        type="button"
        class="inline-flex h-9 w-9 items-center justify-center rounded-xl text-slate-600 transition hover:bg-[#EAF4F6] hover:text-slate-900"
      >
        <Clock3 class="h-[18px] w-[18px]" />
      </button>

      <div
        class="flex h-10 w-10 items-center justify-center rounded-full bg-[#A4CCD9] text-sm font-bold text-[#2F5D6B]"
      >
        A
      </div>
    </div>
  </header>
</template>
