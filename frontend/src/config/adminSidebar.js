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
  LogOut,
} from '@lucide/vue'

export const ADMIN_SIDEBAR_ITEMS = Object.freeze([
  { id: 'dashboard', label: 'Dashboard', icon: markRaw(LayoutDashboard), to: '/' },
  { id: 'patients', label: 'Patients', icon: markRaw(Users), to: '/patients' },
  { id: 'appointments', label: 'Appointments', icon: markRaw(CalendarDays), to: '/appointments' },
  { id: 'doctors', label: 'Doctors', icon: markRaw(UserRound), to: '/doctors' },
  {
    id: 'medical-records',
    label: 'Medical Records',
    icon: markRaw(FileText),
    to: '/medical-records',
  },
  { id: 'prescriptions', label: 'Prescriptions', icon: markRaw(Pill), to: '/prescriptions' },
  { id: 'pharmacy', label: 'Pharmacy Inventory', icon: markRaw(Package2), to: '/pharmacy' },
  { id: 'laboratory', label: 'Laboratory', icon: markRaw(FlaskConical), to: '/laboratory' },
  { id: 'staff', label: 'Staff Management', icon: markRaw(BriefcaseMedical), to: '/staff' },
  { id: 'reports', label: 'Reports & Analytics', icon: markRaw(BarChart3), to: '/reports' },
  { id: 'audit', label: 'Audit Logs', icon: markRaw(ShieldCheck), to: '/audit' },
  { id: 'settings', label: 'System Settings', icon: markRaw(Settings), to: '/settings' },
  {
    id: 'logout',
    label: 'Logout',
    icon: markRaw(LogOut),
    action: 'logout',
  },
])
