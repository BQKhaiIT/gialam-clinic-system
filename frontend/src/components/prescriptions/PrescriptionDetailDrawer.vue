<script setup>
import {
  UserRound,
  Stethoscope,
  NotebookText,
  Pill,
  X,
} from '@lucide/vue'

defineProps({
  show: Boolean,
  prescription: Object,
})

defineEmits(['close'])
</script>

<template>
  <Teleport to="body">
    <Transition name="fade-slide">
      <div
        v-if="show"
        class="fixed inset-0 z-[75] overflow-hidden"
      >
        <div
          class="absolute inset-0 bg-slate-900/35 backdrop-blur-[2px]"
          @click="$emit('close')"
        ></div>

        <div
          class="absolute inset-y-0 right-0 flex w-full justify-end p-4 sm:p-6"
        >
          <aside
            class="dashboard-card flex h-full w-full max-w-[520px] flex-col overflow-hidden"
          >
            <div
              class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6"
            >
              <div>
                <h2 class="panel-title mb-1 text-slate-900">
                  Prescription Detail
                </h2>

                <p class="mb-0 text-sm text-slate-500">
                  Prescription information
                </p>
              </div>

              <button
                type="button"
                class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 transition hover:bg-[#EFF7F9]"
                @click="$emit('close')"
              >
                <X class="h-5 w-5" />
              </button>
            </div>

            <div class="flex-1 overflow-y-auto px-5 py-5 sm:px-6">
              <div class="grid gap-4">

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <UserRound class="h-4 w-4 text-[#2F6F7D]" />
                    Patient
                  </div>

                  <p class="mb-0 text-sm text-slate-600">
                    {{ prescription?.patientName }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-2 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Stethoscope class="h-4 w-4 text-[#2F6F7D]" />
                    Doctor
                  </div>

                  <p class="mb-0 text-sm text-slate-600">
                    {{ prescription?.doctorName }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-3 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <NotebookText class="h-4 w-4 text-[#2F6F7D]" />
                    Note
                  </div>

                  <p class="mb-0 whitespace-pre-line text-sm text-slate-600">
                    {{ prescription?.note || 'No note' }}
                  </p>
                </div>

                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                  <div class="mb-4 flex items-center gap-2 text-sm font-semibold text-slate-700">
                    <Pill class="h-4 w-4 text-[#2F6F7D]" />
                    Medicines
                  </div>

                  <div
                    v-for="medicine in prescription?.medicines"
                    :key="medicine.id"
                    class="mb-3 rounded-2xl border border-slate-200 bg-white p-3"
                  >
                    <div class="flex items-center justify-between">
                      <h4 class="text-sm font-semibold text-slate-800">
                        {{ medicine.medicineName }}
                      </h4>

                      <span
                        class="rounded-full bg-[#EFF7F9] px-3 py-1 text-xs font-semibold text-[#2F6F7D]"
                      >
                        x{{ medicine.quantity }}
                      </span>
                    </div>

                    <p class="mt-2 mb-1 text-sm text-slate-600">
                      Dosage: {{ medicine.dosage }}
                    </p>

                    <p class="mb-0 text-sm text-slate-600">
                      Instruction: {{ medicine.instruction }}
                    </p>
                  </div>
                </div>

              </div>
            </div>
          </aside>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>