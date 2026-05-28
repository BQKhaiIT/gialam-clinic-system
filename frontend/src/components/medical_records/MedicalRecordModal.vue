<script setup>
import { computed, reactive } from 'vue'
import { createMedicalRecord } from '@/api/medicalRecordApi'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },

  appointments: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['update:modelValue', 'created'])
const form = reactive({
  appointmentId: '',

  diagnosis: '',

  treatment: '',

  notes: '',
})

const closeModal = () => {
  emit('update:modelValue', false)
}

const submit = async () => {
  await createMedicalRecord({
    appointmentId: Number(form.appointmentId),

    diagnosis: form.diagnosis,

    treatment: form.treatment,

    notes: form.notes,
  })

  emit('created')

  closeModal()
}
const availableAppointments = computed(() =>

  props.appointments.filter(

    item =>

      item.status === 'IN_PROGRESS'

  )

)
</script>

<template>
  <div
    v-if="props.modelValue"
    class="fixed inset-0 z-[90] flex items-center justify-center bg-slate-900/40 backdrop-blur-[2px] px-4"
  >
    <div
      class="w-full max-w-[820px] overflow-hidden rounded-[24px] bg-white shadow-[0_24px_60px_rgba(69,102,112,0.18)]"
    >
      <div class="border-b border-[#E2EDF1] px-8 py-6 flex justify-between items-start">
        <div>
          <h2 class="text-[20px] font-bold tracking-[-0.03em] text-slate-900">
            Create Medical Record
          </h2>

          <p class="mt-1 text-sm text-slate-500">Create diagnosis and treatment record.</p>
        </div>

        <button class="text-slate-400 hover:text-slate-700 text-2xl" @click="closeModal">×</button>
      </div>

      <div class="px-8 py-6 grid grid-cols-2 gap-5">
        <div class="col-span-2">
          <label class="mb-2 block text-sm font-medium"> Appointment </label>

          <select
            v-model="form.appointmentId"
            class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]"
          >
            <option value="">Select appointment</option>

            <option v-for="item in availableAppointments" :key="item.id" :value="item.id">
              {{ item.patientName }}

              -

              {{ item.doctorName }}

              -

              {{ item.appointmentDate }}
            </option>
          </select>
        </div>

        <div>
          <label class="mb-2 block text-sm font-medium"> Diagnosis </label>

          <textarea
            v-model="form.diagnosis"
            rows="4"
            class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 resize-none outline-none focus:border-[#2F6F7D]"
          />
        </div>

        <div>
          <label class="mb-2 block text-sm font-medium"> Treatment </label>

          <textarea
            v-model="form.treatment"
            rows="4"
            class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 resize-none outline-none focus:border-[#2F6F7D]"
          />
        </div>

        <div class="col-span-2">
          <label class="mb-2 block text-sm font-medium"> Notes </label>

          <textarea
            v-model="form.notes"
            rows="4"
            class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 resize-none outline-none focus:border-[#2F6F7D]"
          />
        </div>
      </div>

      <div class="border-t border-[#E2EDF1] px-8 py-5 flex justify-end gap-3">
        <button class="rounded-xl border border-[#D7E8EE] px-5 py-3" @click="closeModal">
          Cancel
        </button>

        <button class="rounded-xl bg-[#2F6F7D] px-5 py-3 font-semibold text-white" @click="submit">
          Create Record
        </button>
      </div>
    </div>
  </div>
</template>
