<script setup>
import {
  NotebookText,
  Pill,
  Plus,
  Trash2,
  X,
} from '@lucide/vue'
import { watch } from 'vue'
import { reactive } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },

  mode: {
    type: String,
    default: 'create',
  },

  prescription: {
    type: Object,
    default: null,
  },

  medicines: {
    type: Array,
    default: () => [],
  },

  loading: {
    type: Boolean,
    default: false,
  },
})
const emit = defineEmits([
  'close',
  'submit',
])

const form = reactive({
  note: '',

  medicines: [
    {
      medicineId: '',
      quantity: 1,
      dosage: '',
      instruction: '',
    },
  ],
})

const addMedicine = () => {
  form.medicines.push({
    medicineId: '',
    quantity: 1,
    dosage: '',
    instruction: '',
  })
}

const removeMedicine = (index) => {
  form.medicines.splice(index, 1)
}

const handleSubmit = () => {
  emit('submit', form)
}
watch(
  () => props.prescription,
  (value) => {
    if (!value) {
      form.note = ''

      form.medicines = [
        {
          medicineId: '',
          quantity: 1,
          dosage: '',
          instruction: '',
        },
      ]

      return
    }

    form.note = value.note || ''

    form.medicines =
      value.medicines?.map((medicine) => ({
        medicineId: medicine.medicineId,
        quantity: medicine.quantity,
        dosage: medicine.dosage,
        instruction: medicine.instruction,
      })) || []
  },
  {
    immediate: true,
  },
)


</script>

<template>
  <Teleport to="body">
    <Transition name="fade-slide">
      <div
        v-if="show"
        class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/45 px-4 py-4 backdrop-blur-[2px]"
      >
        <div
          class="dashboard-card w-full max-w-[900px] overflow-hidden"
        >
          <div
            class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6"
          >
            <div>
              <h2 class="panel-title mb-1 text-slate-900">
                Create Prescription
              </h2>

              <p class="mb-0 text-sm text-slate-500">
                Add medicines for patient
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

          <div class="max-h-[80vh] overflow-y-auto px-5 py-5 sm:px-6">

            <!-- NOTE -->

            <div class="mb-5">
              <label class="mb-2 block text-sm font-semibold text-slate-700">
                Note
              </label>

              <div class="glass-input relative">
                <NotebookText class="absolute left-4 top-4 h-4 w-4 text-slate-400" />

                <textarea
                  v-model="form.note"
                  rows="3"
                  class="block w-full resize-none border-0 bg-transparent px-4 pb-3 pl-11 pt-3 text-sm text-slate-700 focus:outline-none"
                  placeholder="Prescription note..."
                ></textarea>
              </div>
            </div>

            <!-- MEDICINES -->

            <div
              v-for="(item, index) in form.medicines"
              :key="index"
              class="mb-5 rounded-[24px] border border-slate-200 p-5"
            >
              <div class="mb-4 flex items-center justify-between">
                <div class="flex items-center gap-2">
                  <Pill class="h-4 w-4 text-[#2F6F7D]" />

                  <h3 class="text-sm font-semibold text-slate-700">
                    Medicine #{{ index + 1 }}
                  </h3>
                </div>

                <button
                  v-if="form.medicines.length > 1"
                  type="button"
                  class="inline-flex h-9 w-9 items-center justify-center rounded-xl bg-rose-100 text-rose-600"
                  @click="removeMedicine(index)"
                >
                  <Trash2 class="h-4 w-4" />
                </button>
              </div>

              <div class="grid gap-4 sm:grid-cols-2">

                <div class="sm:col-span-2">
                  <label class="mb-1.5 block text-sm font-semibold text-slate-700">
                    Medicine
                  </label>

                  <div class="glass-input">
                    <select
                      v-model="item.medicineId"
                      class="block h-[46px] w-full border-0 bg-transparent px-4 text-sm text-slate-700 focus:outline-none"
                    >
                      <option value="">
                        Select medicine
                      </option>

                      <option
                        v-for="medicine in medicines"
                        :key="medicine.id"
                        :value="medicine.id"
                      >
                        {{ medicine.name }}
                      </option>
                    </select>
                  </div>
                </div>

                <div>
                  <label class="mb-1.5 block text-sm font-semibold text-slate-700">
                    Quantity
                  </label>

                  <div class="glass-input">
                    <input
                      v-model="item.quantity"
                      type="number"
                      min="1"
                      class="block h-[46px] w-full border-0 bg-transparent px-4 text-sm text-slate-700 focus:outline-none"
                    />
                  </div>
                </div>

                <div>
                  <label class="mb-1.5 block text-sm font-semibold text-slate-700">
                    Dosage
                  </label>

                  <div class="glass-input">
                    <input
                      v-model="item.dosage"
                      type="text"
                      class="block h-[46px] w-full border-0 bg-transparent px-4 text-sm text-slate-700 focus:outline-none"
                      placeholder="2 tablets/day"
                    />
                  </div>
                </div>

                <div class="sm:col-span-2">
                  <label class="mb-1.5 block text-sm font-semibold text-slate-700">
                    Instruction
                  </label>

                  <div class="glass-input">
                    <textarea
                      v-model="item.instruction"
                      rows="2"
                      class="block w-full resize-none border-0 bg-transparent px-4 py-3 text-sm text-slate-700 focus:outline-none"
                      placeholder="After meal..."
                    ></textarea>
                  </div>
                </div>

              </div>
            </div>

            <button
              type="button"
              class="inline-flex items-center gap-2 rounded-xl bg-[#EFF7F9] px-4 py-3 text-sm font-semibold text-[#2F6F7D]"
              @click="addMedicine"
            >
              <Plus class="h-4 w-4" />
              Add Medicine
            </button>
          </div>

          <div
            class="flex justify-end gap-3 border-t border-slate-200/70 px-5 py-5 sm:px-6"
          >
            <button
              type="button"
              class="inline-flex items-center justify-center rounded-xl border border-[#D7E8ED] px-4 py-2.5 text-sm font-semibold text-[#2F6F7D]"
              @click="$emit('close')"
            >
              Cancel
            </button>

            <button
              type="button"
              class="inline-flex items-center justify-center rounded-xl bg-[#2F6F7D] px-4 py-2.5 text-sm font-semibold text-white"
              :disabled="loading"
              @click="handleSubmit"
            >
              {{ loading ? 'Saving...' : 'Create Prescription' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>