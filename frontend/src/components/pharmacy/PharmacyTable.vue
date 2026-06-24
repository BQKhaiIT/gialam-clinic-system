<script setup>
import {
    ChevronLeft,
    ChevronRight,
    Eye,
    Pencil,
    Trash2,
} from '@lucide/vue'

defineProps({
    records: {
        type: Array,
        default: () => [],
    },

    loading: {
        type: Boolean,
        default: false,
    },

    currentPage: {
        type: Number,
        default: 1,
    },

    totalPages: {
        type: Number,
        default: 1,
    },

    totalItems: {
        type: Number,
        default: 0,
    },

    pageSize: {
        type: Number,
        default: 10,
    },
})

defineEmits([
    'view',
    'edit',
    'delete',
    'page-change',
])

const skeletonRows = 5

const formatPrice = (value) => {
    if (!value) {
        return '0 ₫'
    }

    return Number(value).toLocaleString(
        'vi-VN'
    ) + ' ₫'
}
</script>

<template>
    <article class="dashboard-card overflow-hidden">
        <div
            class="flex flex-col gap-2 border-b border-slate-200/70 px-5 py-5 sm:px-6 lg:flex-row lg:items-center lg:justify-between">
            <div>
                <h2 class="panel-title mb-1 text-slate-900">
                    Medicines Inventory
                </h2>
                <p class="mb-0 text-sm text-slate-500">
                    Pharmacy stock management
                </p>
            </div>

            <div class="text-sm font-medium text-slate-500">
                {{ totalItems }}
                medicine{{ totalItems === 1 ? '' : 's' }}
            </div>
        </div>

        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr class="border-b border-slate-200/70 text-sm uppercase tracking-[0.08em] text-slate-400">
                        <th class="px-5 py-4 sm:px-6">
                            ID
                        </th>

                        <th class="px-5 py-4">
                            Medicine
                        </th>

                        <th class="px-5 py-4">
                            Manufacturer
                        </th>

                        <th class="px-5 py-4">
                            Unit
                        </th>

                        <th class="px-5 py-4">
                            Stock
                        </th>

                        <th class="px-5 py-4">
                            Price
                        </th>

                        <th class="px-5 py-4">
                            Status
                        </th>

                        <th class="px-5 py-4 text-end sm:px-6">
                            Actions
                        </th>
                    </tr>
                </thead>

                <tbody>

                    <template v-if="loading">

                        <tr v-for="index in skeletonRows" :key="index" class="border-b border-slate-100">
                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-12 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-40 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-32 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-16 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-16 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-20 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                                <div class="skeleton h-5 w-20 rounded-full" />
                            </td>

                            <td class="px-5 py-4">
                            </td>
                        </tr>

                    </template>

                    <template v-else-if="records.length">

                        <tr v-for="medicine in records" :key="medicine.id"
                            class="border-b border-slate-100 transition hover:bg-[#F8FBFC] last:border-b-0">

                            <td class="px-5 py-4 font-semibold text-slate-700 sm:px-6">
                                #{{ medicine.id }}
                            </td>

                            <td class="px-5 py-4">
                                <div class="font-semibold text-slate-900">
                                    {{ medicine.name }}
                                </div>

                                <div class="mt-1 text-xs text-slate-500 line-clamp-1">
                                    {{ medicine.description }}
                                </div>
                            </td>

                            <td class="px-5 py-4 text-slate-600">
                                {{ medicine.manufacturer }}
                            </td>

                            <td class="px-5 py-4 text-slate-600">
                                {{ medicine.unit }}
                            </td>

                            <td class="px-5 py-4">
                                <span class="font-semibold" :class="medicine.stockQuantity < 20
                                    ? 'text-amber-600'
                                    : 'text-emerald-600'
                                    ">
                                    {{ medicine.stockQuantity }}
                                </span>
                            </td>

                            <td class="px-5 py-4 text-slate-600">
                                {{ formatPrice(medicine.price) }}
                            </td>

                            <td class="px-5 py-4">
                                <span v-if="medicine.stockQuantity === 0"
                                    class="whitespace-nowrap rounded-full bg-red-100 px-3 py-1 text-xs font-semibold text-red-700">
                                    Out Of Stock
                                </span>


                                <span v-else-if="medicine.stockQuantity < 20"
                                    class="whitespace-nowrap rounded-full bg-amber-100 px-3 py-1 text-xs font-semibold text-amber-700">
                                    Low Stock
                                </span>
                                <span v-else
                                    class="whitespace-nowrap rounded-full bg-emerald-100 px-3 py-1 text-xs font-semibold text-emerald-700">
                                    Available
                                </span>
                            </td>

                            <td class="px-5 py-4 text-end sm:px-6">
                                <div class="flex justify-end gap-2">

                                    <button type="button"
                                        class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#D7E8ED] bg-white text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
                                        @click="$emit('view', medicine)">
                                        <Eye class="h-4 w-4" />
                                    </button>

                                    <button type="button"
                                        class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#D7E8ED] bg-white text-amber-600 transition hover:bg-amber-50"
                                        @click="$emit('edit', medicine)">
                                        <Pencil class="h-4 w-4" />
                                    </button>

                                    <button type="button"
                                        class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#D7E8ED] bg-white text-red-600 transition hover:bg-red-50"
                                        @click="$emit('delete', medicine)">
                                        <Trash2 class="h-4 w-4" />
                                    </button>

                                </div>
                            </td>

                        </tr>

                    </template>

                    <tr v-else>

                        <td colspan="8" class="px-5 py-12 text-center text-sm text-slate-500">
                            No medicines found.
                        </td>

                    </tr>

                </tbody>
            </table>
        </div>

        <div
            class="flex flex-col gap-3 border-t border-slate-200/70 px-5 py-4 sm:px-6 md:flex-row md:items-center md:justify-between">
            <p class="mb-0 text-sm text-slate-500">
                Showing

                {{ totalItems === 0 ? 0 : (currentPage - 1) * pageSize + 1 }}

                to

                {{
                    Math.min(
                        currentPage * pageSize,
                        totalItems
                    )
                }}

                of

                {{ totalItems }}

                entries
            </p>

            <div class="flex items-center gap-2">
                <button type="button"
                    class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] bg-white px-3 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:opacity-60"
                    :disabled="currentPage <= 1" @click="$emit('page-change', currentPage - 1)">
                    <ChevronLeft class="h-4 w-4" />
                    Prev
                </button>

                <span class="rounded-xl bg-[#F3FAFB] px-3 py-2 text-sm font-semibold text-slate-600">
                    {{ currentPage }}
                    /
                    {{ totalPages }}
                </span>

                <button type="button"
                    class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] bg-white px-3 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:opacity-60"
                    :disabled="currentPage >= totalPages" @click="$emit('page-change', currentPage + 1)">
                    Next
                    <ChevronRight class="h-4 w-4" />
                </button>
            </div>
        </div>

    </article>
</template>
