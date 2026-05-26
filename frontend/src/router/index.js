import { createRouter, createWebHistory } from 'vue-router'

import Appointments from '@/views/admin/Appointments.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import Doctors from '@/views/admin/Doctors.vue'
import Patients from '@/views/admin/Patients.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: Dashboard,
    },
    {
      path: '/patients',
      name: 'patients',
      component: Patients,
    },
    {
      path: '/appointments',
      name: 'appointments',
      component: Appointments,
    },
    {
      path: '/doctors',
      name: 'doctors',
      component: Doctors,
    },
  ],
})

export default router
