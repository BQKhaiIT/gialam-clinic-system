<script setup>
import { reactive, watch, computed, ref } from 'vue'

import {
    createMedicine,
    updateMedicine,
} from '@/api/medicineApi'

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false,
    },

    medicine: {
        type: Object,
        default: null,
    },
})

const emit = defineEmits([
    'update:modelValue',
    'saved',
])

const saving = ref(false)

const form = reactive({
    name: '',
    unit: '',
    manufacturer: '',
    stockQuantity: 0,
    price: 0,
    description: '',
})

const isEditMode = computed(
    () => !!props.medicine?.id,
)

function resetForm() {
    form.name = ''
    form.unit = ''
    form.manufacturer = ''
    form.stockQuantity = 0
    form.price = 0
    form.description = ''
}

watch(
    () => [
        props.modelValue,
        props.medicine,
    ],
    ([opened, value]) => {
        if (!opened) {
            resetForm()
            return
        }

        if (!value) {
            resetForm()
            return
        }

        form.name = value.name || ''
        form.unit = value.unit || ''
        form.manufacturer =
            value.manufacturer || ''

        form.stockQuantity =
            value.stockQuantity || 0

        form.price =
            value.price || 0

        form.description =
            value.description || ''
    },
    {
        immediate: true,
    },
)

const closeModal = () => {
    resetForm()

    emit(
        'update:modelValue',
        false,
    )
}

const submit = async () => {
    if (saving.value) {
        return
    }

    try {
        if (!form.name.trim()) {
            alert(
                'Medicine name is required',
            )
            return
        }

        const payload = {
            name: form.name.trim(),

            unit: form.unit,

            manufacturer:
                form.manufacturer,

            stockQuantity: Number(
                form.stockQuantity,
            ),

            price: Number(form.price),

            description:
                form.description,
        }

        if (
            payload.stockQuantity < 0
        ) {
            alert(
                'Stock quantity must be greater than or equal to 0',
            )
            return
        }

        if (payload.price < 0) {
            alert(
                'Price must be greater than or equal to 0',
            )
            return
        }

        saving.value = true

        if (isEditMode.value) {
            await updateMedicine(
                props.medicine.id,
                payload,
            )

            alert(
                'Medicine updated successfully',
            )
        } else {
            await createMedicine(
                payload,
            )

            alert(
                'Medicine created successfully',
            )
        }

        emit('saved')

        closeModal()
    } catch (error) {
        console.error(error)

        alert(
            error?.response?.data
                ?.message ||
            'Operation failed',
        )
    } finally {
        saving.value = false
    }
}
</script>

<template>
    <div v-if="modelValue"
        class="fixed inset-0 z-[90] flex items-center justify-center bg-slate-900/40 backdrop-blur-[2px] px-4"
        @click.self="closeModal">
        <div
            class="w-full max-w-[900px] max-h-[90vh] overflow-hidden rounded-[24px] bg-white shadow-[0_24px_60px_rgba(69,102,112,0.18)]">
            <div class="flex items-start justify-between border-b border-[#E2EDF1] px-8 py-6">
                <div>
                    <h2 class="text-[20px] font-bold tracking-[-0.03em] text-slate-900">
                        {{
                            isEditMode
                                ? 'Edit Medicine'
                                : 'Create Medicine'
                        }}
                    </h2>

                    <p class="mt-1 text-sm text-slate-500">
                        Manage medicine inventory
                    </p>
                </div>

                <button class="text-2xl text-slate-400 transition hover:text-slate-700" @click="closeModal">
                    ×
                </button>
            </div>

            <div class="grid max-h-[65vh] grid-cols-2 gap-5 overflow-y-auto px-8 py-6">
                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Medicine Name
                    </label>

                    <input v-model="form.name" type="text"
                        class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]" />
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Unit
                    </label>

                    <input v-model="form.unit" type="text" placeholder="Box, Bottle..."
                        class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]" />
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Manufacturer
                    </label>

                    <input v-model="form.manufacturer" type="text"
                        class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]" />
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Stock Quantity
                    </label>

                    <input v-model="form.stockQuantity" type="number" min="0"
                        class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]" />
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Price (VNĐ)
                    </label>

                    <input v-model="form.price" type="number" min="0"
                        class="w-full rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]" />
                </div>

                <div />

                <div class="col-span-2">
                    <label class="mb-2 block text-sm font-medium">
                        Description
                    </label>

                    <textarea v-model="form.description" rows="5"
                        class="w-full resize-none rounded-[14px] border border-[#D7E8EE] px-4 py-3 outline-none focus:border-[#2F6F7D]" />
                </div>
            </div>

            <div class="flex justify-end gap-3 border-t border-[#E2EDF1] px-8 py-5">
                <button class="rounded-xl border border-[#D7E8EE] px-5 py-3" @click="closeModal">
                    Cancel
                </button>

                <button
                    class="rounded-xl bg-[#2F6F7D] px-5 py-3 font-semibold text-white disabled:cursor-not-allowed disabled:opacity-50"
                    :disabled="saving" @click="submit">
                    {{
                        saving
                            ? 'Saving...'
                            : isEditMode
                                ? 'Update Medicine'
                                : 'Create Medicine'
                    }}
                </button>
            </div>
        </div>
    </div>
</template>