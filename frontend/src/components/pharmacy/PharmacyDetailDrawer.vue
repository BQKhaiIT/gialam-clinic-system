<script setup>
import {
    Package,
    Building2,
    Coins,
    Boxes,
    FileText,
    Pencil,
    X,
} from '@lucide/vue'

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
    'edit',
])

const closeDrawer = () => {
    emit('update:modelValue', false)
}
</script>

<template>
    <Teleport to="body">
        <Transition name="fade-slide">
            <div v-if="modelValue" class="fixed inset-0 z-[75] overflow-hidden">
                <div class="absolute inset-0 bg-slate-900/35 backdrop-blur-[2px]" @click="closeDrawer" />
                <div class="absolute inset-y-0 right-0 flex h-screen w-full justify-end p-4 sm:p-6">
                    <aside
                        class="dashboard-card flex h-full max-h-screen w-full max-w-[700px] flex-col overflow-hidden bg-slate-50">
                        <div class="flex items-start justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6">
                            <div>
                                <h2 class="panel-title mb-1 text-slate-900">
                                    Medicine Detail
                                </h2>

                                <p class="text-sm text-slate-500">
                                    Pharmacy inventory information
                                </p>
                            </div>

                            <button
                                class="inline-flex h-10 w-10 items-center justify-center rounded-2xl text-slate-500 hover:bg-[#EFF7F9]"
                                @click="closeDrawer">
                                <X class="h-5 w-5" />
                            </button>
                        </div>

                        <div class="flex-1 overflow-y-auto overscroll-contain px-5 py-5 sm:px-6">
                            <div class="grid gap-4">

                                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                                    <div class="mb-2 flex items-center gap-2 text-sm font-semibold">
                                        <Package class="h-4 w-4 text-[#2F6F7D]" />
                                        Medicine Name
                                    </div>

                                    <p class="text-slate-600">
                                        {{ medicine?.name }}
                                    </p>
                                </div>

                                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                                    <div class="mb-2 flex items-center gap-2 text-sm font-semibold">
                                        <Building2 class="h-4 w-4 text-[#2F6F7D]" />
                                        Manufacturer
                                    </div>

                                    <p class="text-slate-600">
                                        {{ medicine?.manufacturer }}
                                    </p>
                                </div>

                                <div class="grid gap-4 sm:grid-cols-2">

                                    <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                                        <div class="mb-2 flex items-center gap-2 text-sm font-semibold">
                                            <Boxes class="h-4 w-4 text-[#2F6F7D]" />
                                            Stock Quantity
                                        </div>

                                        <p class="text-slate-600">
                                            {{ medicine?.stockQuantity }}
                                        </p>
                                    </div>

                                    <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                                        <div class="mb-2 flex items-center gap-2 text-sm font-semibold">
                                            <Package class="h-4 w-4 text-[#2F6F7D]" />
                                            Unit
                                        </div>

                                        <p class="text-slate-600">
                                            {{ medicine?.unit }}
                                        </p>
                                    </div>

                                </div>

                                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                                    <div class="mb-2 flex items-center gap-2 text-sm font-semibold">
                                        <Coins class="h-4 w-4 text-[#2F6F7D]" />
                                        Price
                                    </div>

                                    <p class="text-slate-600">
                                        {{ Number(medicine?.price || 0).toLocaleString('vi-VN') }} ₫
                                    </p>
                                </div>

                                <div class="rounded-[20px] bg-[#F8FBFC] p-4">
                                    <div class="mb-2 flex items-center gap-2 text-sm font-semibold">
                                        <FileText class="h-4 w-4 text-[#2F6F7D]" />
                                        Description
                                    </div>

                                    <p class="whitespace-pre-line text-slate-600">
                                        {{ medicine?.description || 'No description' }}
                                    </p>
                                </div>

                            </div>
                        </div>

                        <div class="border-t border-slate-200 px-6 py-4">
                            <button
                                class="inline-flex items-center gap-2 rounded-xl bg-[#2F6F7D] px-5 py-3 font-semibold text-white"
                                @click="$emit('edit', medicine)">
                                <Pencil class="h-4 w-4" />
                                Edit Medicine
                            </button>
                        </div>
                    </aside>
                </div>

            </div>
        </Transition>

    </Teleport>
</template>
