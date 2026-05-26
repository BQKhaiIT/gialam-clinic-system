import { markRaw } from 'vue'
import {
  BarChart3,
  BriefcaseMedical,
  CalendarDays,
  FileText,
  FlaskConical,
  LayoutDashboard,
  Package2,
  Pill,
  Settings,
  ShieldCheck,
  UserRound,
  Users,
} from '@lucide/vue'

export const ADMIN_SIDEBAR_ITEMS = Object.freeze([
  { id: 'dashboard', label: 'Dashboard', icon: markRaw(LayoutDashboard), to: '/' },
  { id: 'patients', label: 'Patients', icon: markRaw(Users), to: '/patients' },
  { id: 'appointments', label: 'Appointments', icon: markRaw(CalendarDays), to: '/appointments' },
  { id: 'doctors', label: 'Doctors', icon: markRaw(UserRound), to: '/doctors' },
  { id: 'medical-records', label: 'Medical Records', icon: markRaw(FileText) },
  { id: 'prescriptions', label: 'Prescriptions', icon: markRaw(Pill) },
  { id: 'pharmacy', label: 'Pharmacy Inventory', icon: markRaw(Package2) },
  { id: 'laboratory', label: 'Laboratory', icon: markRaw(FlaskConical) },
  { id: 'staff', label: 'Staff Management', icon: markRaw(BriefcaseMedical) },
  { id: 'reports', label: 'Reports & Analytics', icon: markRaw(BarChart3) },
  { id: 'audit', label: 'Audit Logs', icon: markRaw(ShieldCheck) },
  { id: 'settings', label: 'System Settings', icon: markRaw(Settings) },
])
