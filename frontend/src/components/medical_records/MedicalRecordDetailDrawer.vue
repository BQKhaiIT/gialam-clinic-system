<script setup>
import {
  ClipboardList,
  FileText,
  NotebookText,
  Stethoscope,
  UserRound,
  X,
  Pill,
  Plus,
} from '@lucide/vue'

import { reactive, ref, onMounted, watch } from 'vue' // BUG 10: Thêm watch

import { createPrescription } from '@/api/prescriptionApi'
import { getMedicines } from '@/api/medicineApi'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  record: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['update:modelValue', 'prescription-created'])
const loading = ref(false)
const medicines = ref([])

const prescriptionForm = reactive({
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

const removeMedicine = (index) => {
  // Chỉ cho phép xoá nếu có nhiều hơn 1 nhóm thuốc
  if (prescriptionForm.medicines.length > 1) {
    prescriptionForm.medicines.splice(index, 1)
  }
}

// BUG 10: Reset form khi mở record khác
watch(
  () => props.record,
  () => {
    prescriptionForm.note = ''
    prescriptionForm.medicines = [
      {
        medicineId: '',
        quantity: 1,
        dosage: '',
        instruction: '',
      },
    ]
  },
)

const fetchMedicines = async () => {
  try {
    const response = await getMedicines()
    medicines.value = response.data.content || []
  } catch (error) {
    console.error(error)
  }
}

const submitPrescription = async () => {
  try {
    const invalidMedicine = prescriptionForm.medicines.some(
      (item) => !item.medicineId || item.quantity <= 0,
    )

    if (invalidMedicine) {
      alert('Invalid medicine information')
      return
    }
    loading.value = true

    await createPrescription({
      medicalRecordId: props.record.id,
      note: prescriptionForm.note,
      medicines: prescriptionForm.medicines,
    })

    alert('Prescription created successfully!')
    emit('prescription-created')
    closeDrawer() // BUG 6: Đóng drawer sau khi tạo thành công

    prescriptionForm.note = ''
    prescriptionForm.medicines = [
      {
        medicineId: '',
        quantity: 1,
        dosage: '',
        instruction: '',
      },
    ]
  } catch (error) {
    alert(error?.response?.data?.message || 'Create prescription failed')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchMedicines()
})

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
            class="dashboard-card box-border flex h-full w-full max-w-[800px] flex-col overflow-hidden bg-slate-50"
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

                <div
                  v-if="!record?.hasPrescription"
                  class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:col-span-2"
                >
                  <div class="mb-5 flex items-center gap-3">
                    <div
                      class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[#E8F4F6]"
                    >
                      <Pill class="h-5 w-5 text-[#2F6F7D]" />
                    </div>
                    <div>
                      <h3 class="text-base font-semibold text-slate-900">Create Prescription</h3>
                      <p class="text-sm text-slate-500">Add medicines and dosage instructions</p>
                    </div>
                  </div>

                  <div class="space-y-5">
                    <div>
                      <label class="mb-1.5 block text-sm font-medium text-slate-700"
                        >Prescription Note</label
                      >
                      <textarea
                        v-model="prescriptionForm.note"
                        rows="2"
                        placeholder="Enter prescription notes..."
                        class="w-full rounded-xl border border-slate-200 px-4 py-2.5 text-sm outline-none transition-colors focus:border-[#2F6F7D]"
                      />
                    </div>

                    <div
                      v-for="(medicine, index) in prescriptionForm.medicines"
                      :key="index"
                      class="rounded-xl border border-slate-100 bg-slate-50 p-4"
                    >
                      <div class="mb-3 flex items-center justify-between">
                        <h4 class="text-sm font-semibold text-slate-700">
                          Medicine #{{ index + 1 }}
                        </h4>

                        <button
                          v-if="prescriptionForm.medicines.length > 1"
                          type="button"
                          class="flex items-center gap-1 rounded-lg px-2 py-1 text-xs font-medium text-slate-400 transition hover:bg-red-50 hover:text-red-600"
                          @click="removeMedicine(index)"
                        >
                          <X class="h-3.5 w-3.5" />
                          Remove
                        </button>
                      </div>

                      <div class="grid gap-4 sm:grid-cols-2">
                        <div>
                          <label class="mb-1.5 block text-sm font-medium text-slate-600"
                            >Medicine</label
                          >
                          <select
                            v-model="medicine.medicineId"
                            class="w-full rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm outline-none transition-colors focus:border-[#2F6F7D]"
                          >
                            <option disabled value="">Select medicine</option>
                            <option v-for="item in medicines" :key="item.id" :value="item.id">
                              {{ item.name }} (Stock: {{ item.stockQuantity }})
                              {{ item.stockQuantity < 10 ? ' ⚠ Low Stock' : '' }}
                            </option>
                          </select>
                        </div>

                        <div>
                          <label class="mb-1.5 block text-sm font-medium text-slate-600"
                            >Quantity</label
                          >
                          <input
                            v-model="medicine.quantity"
                            type="number"
                            min="1"
                            placeholder="Quantity"
                            class="w-full rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm outline-none transition-colors focus:border-[#2F6F7D]"
                          />
                        </div>

                        <div>
                          <label class="mb-1.5 block text-sm font-medium text-slate-600"
                            >Dosage</label
                          >
                          <input
                            v-model="medicine.dosage"
                            type="text"
                            placeholder="Example: 2 tablets/day"
                            class="w-full rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm outline-none transition-colors focus:border-[#2F6F7D]"
                          />
                        </div>

                        <div>
                          <label class="mb-1.5 block text-sm font-medium text-slate-600"
                            >Instruction</label
                          >
                          <input
                            v-model="medicine.instruction"
                            type="text"
                            placeholder="After meal..."
                            class="w-full rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm outline-none transition-colors focus:border-[#2F6F7D]"
                          />
                        </div>
                      </div>
                    </div>

                    <div
                      class="flex flex-col gap-3 border-t border-slate-100 pt-5 sm:flex-row sm:items-center sm:justify-between"
                    >
                      <button
                        type="button"
                        class="inline-flex w-full items-center justify-center gap-2 rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm font-medium text-slate-700 shadow-sm transition hover:bg-slate-50 sm:w-auto"
                        @click="
                          prescriptionForm.medicines.push({
                            medicineId: '',
                            quantity: 1,
                            dosage: '',
                            instruction: '',
                          })
                        "
                      >
                        <Plus class="h-4 w-4" />
                        Add Medicine
                      </button>

                      <button
                        type="button"
                        class="inline-flex w-full items-center justify-center rounded-xl bg-[#2F6F7D] px-6 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-[#255864] sm:w-auto"
                        :class="loading ? 'cursor-not-allowed opacity-50' : ''"
                        :disabled="loading"
                        @click="submitPrescription"
                      >
                        {{ loading ? 'Creating...' : 'Create Prescription' }}
                      </button>
                    </div>
                  </div>
                </div>

                <div
                  v-else
                  class="flex items-center justify-center rounded-2xl border border-emerald-200 bg-emerald-50 p-5 text-sm font-medium text-emerald-700 sm:col-span-2"
                >
                  <Pill class="mr-2 h-4 w-4" />
                  Prescription already created
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
