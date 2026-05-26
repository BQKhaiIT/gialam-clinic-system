<script setup>
import { ChevronLeft, ChevronRight, Eye, Pencil, Trash2 } from '@lucide/vue'

const props = defineProps({
  patients: {
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

defineEmits(['view', 'edit', 'delete', 'page-change'])

const tableSkeletonRows = 6
</script>

<template>
  <article class="dashboard-card overflow-hidden">
    <div class="flex flex-col gap-2 border-b border-slate-200/70 px-5 py-5 sm:px-6 lg:flex-row lg:items-center lg:justify-between">
      <div>
        <h2 class="panel-title mb-1 text-slate-900">Patients List</h2>
        <p class="mb-0 text-sm text-slate-500">All registered patients in the system</p>
      </div>
      <div class="text-sm font-medium text-slate-500">
        {{ totalItems }} patient{{ totalItems === 1 ? '' : 's' }}
      </div>
    </div>

    <div class="table-responsive">
      <table class="table mb-0 align-middle">
        <thead>
          <tr class="border-b border-slate-200/70 text-sm uppercase tracking-[0.08em] text-slate-400">
            <th class="px-5 py-4 sm:px-6">Patient ID</th>
            <th class="px-5 py-4">Full Name</th>
            <th class="px-5 py-4">Phone</th>
            <th class="px-5 py-4">Gender</th>
            <th class="px-5 py-4">Date Of Birth</th>
            <th class="px-5 py-4">Created Date</th>
            <th class="px-5 py-4 text-end sm:px-6">Actions</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="loading">
            <tr
              v-for="index in tableSkeletonRows"
              :key="index"
              class="border-b border-slate-100 last:border-b-0"
            >
              <td class="px-5 py-4 sm:px-6"><div class="skeleton h-5 w-16 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-40 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-28 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-18 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-24 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-28 rounded-full"></div></td>
              <td class="px-5 py-4 text-end sm:px-6">
                <div class="ms-auto flex w-fit gap-2">
                  <div class="skeleton h-10 w-10 rounded-2xl"></div>
                  <div class="skeleton h-10 w-10 rounded-2xl"></div>
                  <div class="skeleton h-10 w-10 rounded-2xl"></div>
                </div>
              </td>
            </tr>
          </template>

          <template v-else-if="patients.length">
            <tr
              v-for="patient in patients"
              :key="patient.id"
              class="border-b border-slate-100 transition hover:bg-[#F8FBFC] last:border-b-0"
            >
              <td class="px-5 py-4 font-semibold text-slate-700 sm:px-6">#{{ patient.id }}</td>
              <td class="px-5 py-4 font-semibold text-slate-900">{{ patient.fullName }}</td>
              <td class="px-5 py-4 text-slate-600">{{ patient.phone }}</td>
              <td class="px-5 py-4 text-slate-600">{{ patient.gender }}</td>
              <td class="px-5 py-4 text-slate-600">{{ patient.dob }}</td>
              <td class="px-5 py-4 text-slate-600">{{ patient.createdAt }}</td>
              <td class="px-5 py-4 text-end sm:px-6">
                <div class="flex justify-end gap-2">
                  <button
                    type="button"
                    class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#D7E8ED] bg-white text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
                    @click="$emit('view', patient)"
                  >
                    <Eye class="h-4 w-4" />
                  </button>
                  <button
                    type="button"
                    class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#D7E8ED] bg-white text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
                    @click="$emit('edit', patient)"
                  >
                    <Pencil class="h-4 w-4" />
                  </button>
                  <button
                    type="button"
                    class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-[#F5C2C7] bg-white text-[#C2414E] transition hover:bg-[#FFF5F6]"
                    @click="$emit('delete', patient)"
                  >
                    <Trash2 class="h-4 w-4" />
                  </button>
                </div>
              </td>
            </tr>
          </template>

          <tr v-else>
            <td colspan="7" class="px-5 py-12 text-center text-sm text-slate-500 sm:px-6">
              No patients found.
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
        {{ Math.min(currentPage * pageSize, totalItems) }}
        of
        {{ totalItems }}
        entries
      </p>

      <div class="flex items-center gap-2 self-end md:self-auto">
        <button
          type="button"
          class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] bg-white px-3 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="currentPage <= 1"
          @click="$emit('page-change', currentPage - 1)"
        >
          <ChevronLeft class="h-4 w-4" />
          Prev
        </button>

        <span class="rounded-xl bg-[#F3FAFB] px-3 py-2 text-sm font-semibold text-slate-600">
          {{ currentPage }} / {{ totalPages }}
        </span>

        <button
          type="button"
          class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] bg-white px-3 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9] disabled:cursor-not-allowed disabled:opacity-60"
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
