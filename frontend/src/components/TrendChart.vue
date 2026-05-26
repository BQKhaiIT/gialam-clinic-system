<script setup>
import { computed } from 'vue'
import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  Title,
  Tooltip,
} from 'chart.js'
import { Bar } from 'vue-chartjs'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

const props = defineProps({
  labels: {
    type: Array,
    default: () => [],
  },
  values: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

const hasData = computed(() => props.values.some((value) => Number(value) > 0))

const chartData = computed(() => ({
  labels: props.labels,
  datasets: [
    {
      label: 'Visits',
      data: props.values,
      borderRadius: 10,
      maxBarThickness: 34,
      backgroundColor: ['#A4CCD9', '#8DBCC7', '#A4CCD9', '#8DBCC7', '#6EA5B1', '#5E909D'],
      hoverBackgroundColor: '#2F6F7D',
    },
  ],
}))

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  animation: {
    duration: 800,
    easing: 'easeOutQuart',
  },
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      backgroundColor: '#173038',
      padding: 12,
      cornerRadius: 10,
    },
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: { color: '#64748B', font: { size: 12, weight: 600 } },
      border: { display: false },
    },
    y: {
      beginAtZero: true,
      ticks: { color: '#94A3B8', stepSize: 20 },
      grid: { color: 'rgba(141, 188, 199, 0.18)' },
      border: { display: false },
    },
  },
}))
</script>

<template>
  <article class="dashboard-card p-5 sm:p-6">
    <div class="mb-5">
      <h2 class="panel-title mb-1 text-slate-900">Patient Visits Trend</h2>
      <p class="mb-0 text-sm text-slate-500">Daily walk-in and booked appointments</p>
    </div>

    <div
      v-if="loading"
      class="grid h-[300px] grid-cols-6 items-end gap-3 rounded-[20px] bg-[#F8FBFC] p-6"
    >
      <div
        v-for="index in 6"
        :key="index"
        class="skeleton rounded-t-2xl"
        :style="{ height: `${18 + index * 12}%` }"
      ></div>
    </div>

    <div v-else-if="hasData" class="h-[300px] rounded-[20px] bg-[#F8FBFC] p-4">
      <Bar :data="chartData" :options="chartOptions" />
    </div>

    <div
      v-else
      class="flex h-[300px] items-center justify-center rounded-[20px] border border-dashed border-[#D7E8ED] bg-[#F8FBFC] p-6 text-center text-sm text-slate-500"
    >
      No appointment trend data available.
    </div>
  </article>
</template>
