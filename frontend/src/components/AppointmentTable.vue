<script setup>
import { ChevronDown, MoreVertical } from '@lucide/vue'

defineProps({
  appointments: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const statusClasses = {
  COMPLETED: 'bg-[#ECFDF3] text-[#15803D]',
  PENDING: 'bg-[#FFF7ED] text-[#EA580C]',
  CANCELLED: 'bg-[#FEF2F2] text-[#DC2626]',
}

const getStatusClass = (status) => {
  const normalizedStatus = String(status ?? 'PENDING').toUpperCase()
  return statusClasses[normalizedStatus] ?? statusClasses.PENDING
}
</script>

<template>
  <article class="dashboard-card overflow-hidden">
    <div class="flex items-center justify-between border-b border-slate-200/70 px-5 py-5 sm:px-6">
      <div>
        <h2 class="panel-title mb-1 text-slate-900">Recent Appointments</h2>
        <p class="mb-0 text-sm text-slate-500">Latest bookings and visit outcomes</p>
      </div>
      <button
        type="button"
        class="inline-flex items-center gap-2 rounded-xl border border-[#D7E8ED] px-4 py-2 text-sm font-semibold text-[#2F6F7D] transition hover:bg-[#EFF7F9]"
      >
        View All
        <ChevronDown class="h-4 w-4" />
      </button>
    </div>

    <div class="table-responsive">
      <table class="table mb-0 align-middle">
        <thead>
          <tr
            class="border-b border-slate-200/70 text-sm uppercase tracking-[0.08em] text-slate-400"
          >
            <th class="px-5 py-4 sm:px-6">Patient</th>
            <th class="px-5 py-4">Doctor</th>
            <th class="px-5 py-4">Time</th>
            <th class="px-5 py-4">Status</th>
            <th class="px-5 py-4 text-end sm:px-6">Action</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="loading">
            <tr v-for="index in 4" :key="index" class="border-b border-slate-100 last:border-b-0">
              <td class="px-5 py-4 sm:px-6">
                <div class="skeleton h-5 w-32 rounded-full"></div>
              </td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-32 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-5 w-20 rounded-full"></div></td>
              <td class="px-5 py-4"><div class="skeleton h-8 w-24 rounded-full"></div></td>
              <td class="px-5 py-4 text-end sm:px-6">
                <div class="skeleton ms-auto h-10 w-10 rounded-2xl"></div>
              </td>
            </tr>
          </template>

          <template v-else-if="appointments.length">
            <tr
              v-for="appointment in appointments"
              :key="appointment.id ?? `${appointment.patient}-${appointment.time}`"
              class="border-b border-slate-100 transition hover:bg-[#F8FBFC] last:border-b-0"
            >
              <td class="px-5 py-4 font-semibold text-slate-900 sm:px-6">
                {{ appointment.patient }}
              </td>
              <td class="px-5 py-4 text-slate-600">{{ appointment.doctor }}</td>
              <td class="px-5 py-4 text-slate-600">{{ appointment.time }}</td>
              <td class="px-5 py-4">
                <span
                  class="inline-flex rounded-full px-3 py-2 text-xs font-bold uppercase tracking-[0.1em]"
                  :class="getStatusClass(appointment.status)"
                >
                  {{ appointment.status }}
                </span>
              </td>
              <td class="px-5 py-4 text-end sm:px-6">
                <div class="dropdown">
                  <button
                    class="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-transparent text-slate-500 transition hover:border-slate-200 hover:bg-white"
                    type="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    <MoreVertical class="h-4 w-4" />
                  </button>
                  <ul class="dropdown-menu dropdown-menu-end rounded-3 border-0 shadow-lg">
                    <li><button class="dropdown-item">View details</button></li>
                    <li><button class="dropdown-item">Reschedule</button></li>
                    <li><button class="dropdown-item text-danger">Cancel appointment</button></li>
                  </ul>
                </div>
              </td>
            </tr>
          </template>

          <tr v-else>
            <td colspan="5" class="px-5 py-10 text-center text-sm text-slate-500 sm:px-6">
              No recent appointments available.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </article>
</template>
