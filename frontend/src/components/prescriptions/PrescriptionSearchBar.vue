<script setup>
import { Plus, Search, X } from '@lucide/vue'

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

const emit = defineEmits([
  'update:modelValue',
  'add-prescription',
  'clear',
])

const handleInput = (event) => {
  emit('update:modelValue', event.target.value)
}
</script>

<template>
  <article
    class="dashboard-card overflow-hidden"
  >
    <div
      class="flex flex-col gap-4 px-5 py-5 sm:px-6 lg:flex-row lg:items-center lg:justify-between"
    >

      <!-- LEFT -->

      <div class="flex-1">
        <h2
          class="panel-title mb-1 text-slate-900"
        >
          Prescriptions
        </h2>

        <p class="mb-0 text-sm text-slate-500">
          Search and manage patient prescriptions
        </p>
      </div>

      <!-- RIGHT -->

      <div
        class="flex flex-col gap-3 sm:flex-row sm:items-center"
      >

        <!-- SEARCH -->

        <div
          class="glass-input relative min-w-[280px]"
        >
          <Search
            class="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
          />

          <input
            :value="modelValue"
            type="text"
            placeholder="Search prescription..."
            class="block h-[46px] w-full border-0 bg-transparent pl-11 pr-10 text-sm text-slate-700 focus:outline-none"
            @input="handleInput"
          />

          <button
            v-if="modelValue"
            type="button"
            class="absolute right-3 top-1/2 inline-flex h-7 w-7 -translate-y-1/2 items-center justify-center rounded-full text-slate-400 transition hover:bg-slate-200 hover:text-slate-700"
            @click="$emit('clear')"
          >
            <X class="h-4 w-4" />
          </button>
        </div>

        <!-- BUTTON -->

        <button
          type="button"
          class="inline-flex items-center justify-center gap-2 rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="loading"
          @click="$emit('add-prescription')"
        >
          <Plus class="h-4 w-4" />

          Add Prescription
        </button>

      </div>
    </div>
  </article>
</template>