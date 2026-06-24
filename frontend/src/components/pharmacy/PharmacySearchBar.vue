<script setup>
import {
    Plus,
    Search,
} from '@lucide/vue'

const keyword = defineModel({
    type: String,
    default: '',
})

const emit = defineEmits([
    'search',
    'create',
])

const handleSearch = () => {
    emit('search', keyword.value)
}
</script>

<template>
    <div class="dashboard-card p-5">
        <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
            <div class="relative flex-1">
                <Search class="absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                <input v-model="keyword" type="text" placeholder="Search medicine..."
                    class="w-full rounded-2xl border border-slate-200 py-3 pl-11 pr-4 outline-none focus:border-[#2F6F7D]"
                    @keyup.enter="handleSearch" />
            </div>

            <div class="flex gap-3">
                <button class="rounded-2xl border border-slate-200 px-5 py-3 font-medium" @click="handleSearch">
                    Search
                </button>

                <button
                    class="inline-flex items-center gap-2 rounded-2xl bg-[#2F6F7D] px-5 py-3 font-semibold text-white"
                    @click="$emit('create')">
                    <Plus class="h-4 w-4" />
                    Add Medicine
                </button>
            </div>
        </div>

    </div>
</template>
