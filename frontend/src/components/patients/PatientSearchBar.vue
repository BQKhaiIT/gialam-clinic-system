<script setup>
import { Search, UserPlus, X } from '@lucide/vue'

defineProps({
  modelValue: {
    type: String,
    default: '',
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['update:modelValue', 'add-patient', 'clear'])
</script>

<template>
  <article class="dashboard-card p-5 sm:p-6">
    <div class="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
      <div>
        <h2 class="panel-title mb-1 text-slate-900">Patients Management</h2>
        <p class="mb-0 text-sm text-slate-500">Search, review, and register patient records</p>
      </div>

      <div class="flex flex-col gap-3 sm:flex-row sm:items-center">
        <label class="glass-input relative min-w-0 sm:w-[320px]">
          <Search
            class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
          />
          <input
            :value="modelValue"
            type="text"
            class="block h-[44px] w-full border-0 bg-transparent pl-11 pr-11 text-[14px] text-slate-700 placeholder:text-slate-400 focus:outline-none"
            placeholder="Search by name or phone..."
            @input="$emit('update:modelValue', $event.target.value)"
          />
          <button
            v-if="modelValue"
            type="button"
            class="absolute right-3 top-1/2 inline-flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-xl text-slate-400 transition hover:bg-[#EAF4F6] hover:text-slate-600"
            @click="$emit('clear')"
          >
            <X class="h-4 w-4" />
          </button>
        </label>

        <button
          type="button"
          class="inline-flex items-center justify-center gap-2 rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="loading"
          @click="$emit('add-patient')"
        >
          <UserPlus class="h-4 w-4" />
          Add Patient
        </button>
      </div>
    </div>
  </article>
</template>
