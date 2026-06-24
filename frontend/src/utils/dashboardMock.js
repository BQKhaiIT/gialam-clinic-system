import { markRaw } from 'vue'
import {
  AlertCircle,
  BadgeInfo,
  BarChart3,
  BriefcaseMedical,
  CalendarClock,
  CalendarDays,
  CalendarPlus,
  ClipboardList,
  ClipboardPenLine,
  FileText,
  FlaskConical,
  Package2,
  PackagePlus,
  Pill,
  Settings,
  ShieldCheck,
  Stethoscope,
  TriangleAlert,
  UserPlus,
  Users,
} from '@lucide/vue'

export const statsCards = [
  {
    title: 'Total Patients',
    value: '12,458',
    subtext: '+12% vs last month',
    badgeTone: 'success',
    icon: markRaw(Users),
  },
  {
    title: "Today's Appointments",
    value: '142',
    subtext: '+5% vs yesterday',
    badgeTone: 'success',
    icon: markRaw(CalendarClock),
  },
  {
    title: 'Active Doctors',
    value: '34',
    subtext: 'Across 8 departments',
    badgeTone: 'info',
    icon: markRaw(Stethoscope),
  },
  {
    title: 'Pending Appointments',
    value: '28',
    subtext: 'Requires action',
    badgeTone: 'warning',
    icon: markRaw(ClipboardList),
  },
]

export const patientVisitsTrend = {
  labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
  values: [50, 70, 60, 95, 85, 120],
}

export const departmentPerformance = [
  { name: 'General Medicine', value: 85 },
  { name: 'Pediatrics', value: 70 },
  { name: 'Dentistry', value: 55 },
  { name: 'ENT', value: 40 },
]

export const recentAppointments = [
  {
    patient: 'Sarah Jenkins',
    doctor: 'Dr Robert Chen',
    time: '09:00 AM',
    status: 'Completed',
  },
  {
    patient: 'Michael Chang',
    doctor: 'Dr Sarah Jenkins',
    time: '09:30 AM',
    status: 'Pending',
  },
  {
    patient: 'Emily Davis',
    doctor: 'Dr Alan Turing',
    time: '10:15 AM',
    status: 'Cancelled',
  },
  {
    patient: 'James Wilson',
    doctor: 'Dr Robert Chen',
    time: '11:00 AM',
    status: 'Pending',
  },
]

export const alertItems = [
  {
    title: 'Critical Stock',
    message: 'Amoxicillin inventory below 10%',
    tone: 'danger',
    icon: markRaw(AlertCircle),
  },
  {
    title: 'Pending Approvals',
    message: '5 doctor registrations require review',
    tone: 'warning',
    icon: markRaw(TriangleAlert),
  },
  {
    title: 'System Update',
    message: 'Database maintenance at 02:00',
    tone: 'info',
    icon: markRaw(BadgeInfo),
  },
]

export const quickActions = [
  { label: 'Add Patient', icon: markRaw(UserPlus), accent: true },
  { label: 'New Appt.', icon: markRaw(CalendarPlus) },
  { label: 'Prescribe', icon: markRaw(ClipboardPenLine) },
  { label: 'Restock', icon: markRaw(PackagePlus) },
]
