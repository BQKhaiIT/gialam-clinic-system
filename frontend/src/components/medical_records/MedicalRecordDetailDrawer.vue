<script setup>
import { ClipboardList, FileText, NotebookText, Stethoscope, UserRound, X } from '@lucide/vue'

defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },

  record: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['update:modelValue'])

const closeDrawer = () => {
  emit('update:modelValue', false)
}
</script>

<template>
  <Teleport to="body">
    <Transition name="fade-slide">
      <div v-if="modelValue" class="fixed inset-0 z-[75] overflow-hidden">
        <div class="absolute inset-0 bg-slate-900/35 backdrop-blur-[2px]" @click="closeDrawer" />

        <div class="absolute inset-y-0 right-0 flex w-full justify-end p-4 sm:p-6">
          <aside
            class="dashboard-card box-border flex h-full w-full max-w-[520px] flex-col overflow-hidden"
          >
            <div
              class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6"
            >
              <div>
                <h2 class="panel-title mb-1 text-slate-900">Medical Record Detail</h2>

                <p class="mb-0 text-sm text-slate-500">Detailed clinical information</p>
              </div>

              <button
                type="button"
                class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9] hover:text-slate-700"
                @click="closeDrawer"
              >
                <X class="h-5 w-5" />
              </button>
            </div>

            <div class="flex-1 overflow-y-auto px-5 py-5 sm:px-6">
              <div class="grid gap-4 sm:grid-cols-2">
                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <UserRound class="h-4 w-4 text-[#2F6F7D]" />

                    Patient
                  </div>

                  <p class="mb-0 text-sm text-slate-600">
                    {{ record?.patientName || 'N/A' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Stethoscope class="h-4 w-4 text-[#2F6F7D]" />

                    Doctor
                  </div>

                  <p class="mb-0 text-sm text-slate-600">
                    {{ record?.doctorName || 'N/A' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <ClipboardList class="h-4 w-4 text-[#2F6F7D]" />

                    Diagnosis
                  </div>

                  <p class="mb-0 whitespace-pre-line text-sm leading-7 text-slate-600">
                    {{ record?.diagnosis || 'No diagnosis' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <FileText class="h-4 w-4 text-[#2F6F7D]" />

                    Treatment
                  </div>

                  <p class="mb-0 whitespace-pre-line text-sm leading-7 text-slate-600">
                    {{ record?.treatment || 'No treatment' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4 sm:col-span-2">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <NotebookText class="h-4 w-4 text-[#2F6F7D]" />

                    Clinical Notes
                  </div>

                  <p class="mb-0 whitespace-pre-line text-sm leading-7 text-slate-600">
                    {{ record?.notes || 'No notes' }}
                  </p>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
