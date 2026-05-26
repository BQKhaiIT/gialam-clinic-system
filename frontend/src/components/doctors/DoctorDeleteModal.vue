<script setup>
import { AlertTriangle, X } from '@lucide/vue'

defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  doctor: {
    type: Object,
    default: null,
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['close', 'confirm'])
</script>

<template>
  <Teleport to="body">
    <Transition name="fade-slide">
      <div
        v-if="show"
        class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/45 px-4 py-6 backdrop-blur-[2px]"
      >
        <div class="dashboard-card w-full max-w-lg overflow-hidden">
          <div class="flex items-start justify-between px-5 py-5 sm:px-6">
            <div class="flex items-start gap-4">
              <div
                class="flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl bg-[#FFF5F6] text-[#C2414E]"
              >
                <AlertTriangle class="h-6 w-6" />
              </div>
              <div>
                <h2 class="panel-title mb-1 text-slate-900">Delete Doctor</h2>
                <p class="mb-0 text-sm text-slate-500">
                  This action will remove
                  <span class="font-semibold text-slate-700">{{ doctor?.fullName }}</span>
                  from the doctor list.
                </p>
              </div>
            </div>
            <button
              type="button"
              class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9] hover:text-slate-700"
              @click="$emit('close')"
            >
              <X class="h-5 w-5" />
            </button>
          </div>

          <div class="border-y border-slate-200/70 bg-[#F8FBFC] px-5 py-4 text-sm text-slate-600 sm:px-6">
            Doctor ID: <span class="font-semibold text-slate-900">#{{ doctor?.id }}</span>
          </div>

          <div class="flex justify-end gap-3 px-5 py-5 sm:px-6">
            <button
              type="button"
              class="inline-flex items-center justify-center rounded-xl border border-[#D7E8ED] px-4 py-2.5 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
              @click="$emit('close')"
            >
              Cancel
            </button>
            <button
              type="button"
              class="inline-flex items-center justify-center rounded-xl bg-[#C2414E] px-4 py-2.5 text-sm font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
              :disabled="loading"
              @click="$emit('confirm')"
            >
              {{ loading ? 'Deleting...' : 'Delete Doctor' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
