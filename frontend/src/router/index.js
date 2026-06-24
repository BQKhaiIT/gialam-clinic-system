import { createRouter, createWebHistory } from 'vue-router'

import Appointments from '@/views/admin/Appointments.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import Doctors from '@/views/admin/Doctors.vue'
import MedicalRecords from '@/views/admin/MedicalRecords.vue'
import Patients from '@/views/admin/Patients.vue'
import Prescriptions from '@/views/admin/Prescriptions.vue'
import PharmacyInventory from '@/views/admin/PharmacyInventory.vue'

import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    {
      path: '/register',
      name: 'register',
      component: Register,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },

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

    {
      path: '/medical-records',
      name: 'medical-records',
      component: MedicalRecords,
    },

    {
      path: '/prescriptions',
      name: 'prescriptions',
      component: Prescriptions,
    },

    {
      path: '/pharmacy',
      name: 'pharmacy',
      component: PharmacyInventory,
    },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')

  const publicRoutes = ['/login', '/register']

  if (!publicRoutes.includes(to.path) && !token) {
    return '/login'
  }

  if (publicRoutes.includes(to.path) && token) {
    return '/'
  }
})

export default router
