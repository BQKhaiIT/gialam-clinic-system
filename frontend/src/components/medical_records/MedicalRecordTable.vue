<script setup>
import { ChevronLeft, ChevronRight, Eye } from '@lucide/vue'

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

defineEmits(['view', 'page-change'])

const skeletonRows = 5

const formatDate = (date) => {
  if (!date) {
    return '-'
  }

  return new Date(date).toLocaleDateString(
    'en-GB',

    {
      day: '2-digit',

      month: 'short',

      year: 'numeric',
    },
  )
}
</script>

<template>
  <article class="dashboard-card overflow-hidden">
    <div
      class="flex flex-col gap-2 border-b border-slate-200/70 px-5 py-5 sm:px-6 lg:flex-row lg:items-center lg:justify-between"
    >
      <div>
        <h2 class="panel-title mb-1 text-slate-900">Medical Records List</h2>

        <p class="mb-0 text-sm text-slate-500">Clinical diagnosis and treatment history</p>
      </div>

      <div class="text-sm font-medium text-slate-500">
        {{ totalItems }}

        record{{ totalItems === 1 ? '' : 's' }}
      </div>
    </div>

    <div class="table-responsive">
      <table class="table mb-0 align-middle">
        <thead>
          <tr
            class="border-b border-slate-200/70 text-sm uppercase tracking-[0.08em] text-slate-400"
          >
            <th class="px-5 py-4 sm:px-6">ID</th>

            <th class="px-5 py-4">Patient</th>

            <th class="px-5 py-4">Doctor</th>

            <th class="px-5 py-4">Diagnosis</th>

            <th class="px-5 py-4">Treatment</th>

            <th class="px-5 py-4">Created</th>

            <th class="px-5 py-4 text-end sm:px-6">Actions</th>
          </tr>
        </thead>

        <tbody>
          <template v-if="loading">
            <tr v-for="index in skeletonRows" :key="index" class="border-b border-slate-100">
              <td class="px-5 py-4">
                <div class="skeleton h-5 w-16 rounded-full" />
              </td>

              <td class="px-5 py-4">
                <div class="skeleton h-5 w-32 rounded-full" />
              </td>

              <td class="px-5 py-4">
                <div class="skeleton h-5 w-28 rounded-full" />
              </td>

              <td class="px-5 py-4">
                <div class="skeleton h-5 w-40 rounded-full" />
              </td>

              <td class="px-5 py-4">
                <div class="skeleton h-5 w-40 rounded-full" />
              </td>

              <td class="px-5 py-4">
                <div class="skeleton h-5 w-24 rounded-full" />
              </td>

              <td class="px-5 py-4" />
            </tr>
          </template>

          <template v-else-if="records.length">
            <tr
              v-for="record in records"
              :key="record.id"
              class="border-b border-slate-100 transition hover:bg-[#F8FBFC] last:border-b-0"
            >
              <td class="px-5 py-4 font-semibold text-slate-700 sm:px-6">#{{ record.id }}</td>

              <td class="px-5 py-4 font-semibold text-slate-900">
                {{ record.patientName }}
              </td>

              <td class="px-5 py-4 text-slate-600">
                {{ record.doctorName }}
              </td>

              <td class="px-5 py-4 text-slate-600 max-w-[220px] truncate">
                {{ record.diagnosis }}
              </td>

              <td class="px-5 py-4 text-slate-600 max-w-[220px] truncate">
                {{ record.treatment }}
              </td>

              <td class="px-5 py-4 text-slate-600">
                {{ formatDate(record.createdAt) }}
              </td>

              <td class="px-5 py-4 text-end sm:px-6">
                <button
                  type="button"
                  class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#D7E8ED] bg-white text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
                  @click="$emit('view', record)"
                >
                  <Eye class="h-4 w-4" />
                </button>
              </td>
            </tr>
          </template>

          <tr v-else>
            <td colspan="7" class="px-5 py-12 text-center text-sm text-slate-500">
              No medical records found.
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div
      class="flex flex-col gap-3 border-t border-slate-200/70 px-5 py-4 sm:px-6 md:flex-row md:items-center md:justify-between"
    >
      <p class="mb-0 text-sm text-slate-500">
        Showing

        {{ totalItems === 0 ? 0 : (currentPage - 1) * pageSize + 1 }}

        to

        {{
          Math.min(
            currentPage * pageSize,

            totalItems,
          )
        }}

        of

        {{ totalItems }}

        entries
      </p>

      <div class="flex items-center gap-2">
        <button
          type="button"
          class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] bg-white px-3 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:opacity-60"
          :disabled="currentPage <= 1"
          @click="$emit('page-change', currentPage - 1)"
        >
          <ChevronLeft class="h-4 w-4" />

          Prev
        </button>

        <span class="rounded-xl bg-[#F3FAFB] px-3 py-2 text-sm font-semibold text-slate-600">
          {{ currentPage }}

          /

          {{ totalPages }}
        </span>

        <button
          type="button"
          class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] bg-white px-3 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:opacity-60"
          :disabled="currentPage >= totalPages"
          @click="$emit('page-change', currentPage + 1)"
        >
          Next

          <ChevronRight class="h-4 w-4" />
        </button>
      </div>
    </div>
  </article>
</template>
