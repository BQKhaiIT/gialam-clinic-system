<script setup>
import { computed } from 'vue'
import { Building2, Search, Stethoscope, UserPlus, X } from '@lucide/vue'

const props = defineProps({
  fullName: {
    type: String,
    default: '',
  },
  department: {
    type: String,
    default: '',
  },
  specialization: {
    type: String,
    default: '',
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

defineEmits([
  'update:fullName',
  'update:department',
  'update:specialization',
  'add-doctor',
  'clear',
])

const hasFilters = computed(
  () => props.fullName.trim() || props.department.trim() || props.specialization.trim(),
)
</script>

<template>
  <article class="dashboard-card p-5 sm:p-6">
    <div class="flex flex-col gap-4">
      <div class="flex flex-col gap-2 lg:flex-row lg:items-center lg:justify-between">
        <div>
          <h2 class="panel-title mb-1 text-slate-900">Doctors Management</h2>
          <p class="mb-0 text-sm text-slate-500">Search, review, and maintain doctor records</p>
        </div>

        <div class="flex flex-col gap-3 sm:flex-row sm:items-center">
          <button
            v-if="hasFilters"
            type="button"
            class="inline-flex items-center justify-center gap-2 rounded-xl border border-[#D7E8ED] px-4 py-2.5 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
            @click="$emit('clear')"
          >
            <X class="h-4 w-4" />
            Clear Filters
          </button>

          <button
            type="button"
            class="inline-flex items-center justify-center gap-2 rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="loading"
            @click="$emit('add-doctor')"
          >
            <UserPlus class="h-4 w-4" />
            Add Doctor
          </button>
        </div>
      </div>

      <div class="grid gap-3 lg:grid-cols-3">
        <label class="glass-input relative">
          <Search
            class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
          />
          <input
            :value="fullName"
            type="text"
            class="block h-[44px] w-full border-0 bg-transparent pl-11 pr-4 text-[14px] text-slate-700 placeholder:text-slate-400 focus:outline-none"
            placeholder="Search doctor name..."
            @input="$emit('update:fullName', $event.target.value)"
          />
        </label>

        <label class="glass-input relative">
          <Building2
            class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
          />
          <input
            :value="department"
            type="text"
            class="block h-[44px] w-full border-0 bg-transparent pl-11 pr-4 text-[14px] text-slate-700 placeholder:text-slate-400 focus:outline-none"
            placeholder="Filter by department..."
            @input="$emit('update:department', $event.target.value)"
          />
        </label>

        <label class="glass-input relative">
          <Stethoscope
            class="pointer-events-none absolute left-4 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-slate-400"
          />
          <input
            :value="specialization"
            type="text"
            class="block h-[44px] w-full border-0 bg-transparent pl-11 pr-4 text-[14px] text-slate-700 placeholder:text-slate-400 focus:outline-none"
            placeholder="Filter by specialization..."
            @input="$emit('update:specialization', $event.target.value)"
          />
        </label>
      </div>
    </div>
  </article>
</template>
