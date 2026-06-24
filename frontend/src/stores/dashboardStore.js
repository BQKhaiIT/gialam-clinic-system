import { markRaw, computed, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  BarChart3,
  BriefcaseMedical,
  CalendarClock,
  CalendarPlus,
  ClipboardList,
  ClipboardPenLine,
  FlaskConical,
  Package2,
  PackagePlus,
  Pill,
  Settings,
  ShieldCheck,
  Stethoscope,
  UserPlus,
  Users,
} from '@lucide/vue'

import { getAppointments } from '@/api/appointmentApi'
import { getMedicalRecords } from '@/api/medicalRecordApi'
import { getMedicines } from '@/api/medicineApi'
import { getPatients } from '@/api/patientApi'

const NUMBER_FORMATTER = new Intl.NumberFormat('en-US')
const TIME_FORMATTER = new Intl.DateTimeFormat('en-US', {
  hour: '2-digit',
  minute: '2-digit',
})
const WEEKDAY_FORMATTER = new Intl.DateTimeFormat('en-US', {
  weekday: 'short',
})

const COLLECTION_KEYS = [
  'content',
  'items',
  'results',
  'records',
  'patients',
  'appointments',
  'medicines',
  'medicalRecords',
  'data',
]

const APPOINTMENT_DATE_PATHS = [
  'appointmentDateTime',
  'appointmentDate',
  'scheduledAt',
  'scheduledDate',
  'visitDate',
  'date',
  'createdAt',
]

const APPOINTMENT_TIME_PATHS = ['appointmentTime', 'scheduledTime', 'startTime', 'time']
const PATIENT_NAME_PATHS = ['patientName', 'patient.fullName', 'patient.name', 'fullName', 'name']
const DOCTOR_NAME_PATHS = [
  'doctorName',
  'doctor.fullName',
  'doctor.name',
  'physicianName',
  'assignedDoctor',
]
const DOCTOR_ID_PATHS = ['doctorId', 'doctor.id', 'physicianId', 'assignedDoctorId']

const QUICK_ACTIONS = [
  { label: 'Add Patient', icon: markRaw(UserPlus), accent: true },
  { label: 'New Appt.', icon: markRaw(CalendarPlus), accent: false },
  { label: 'Prescribe', icon: markRaw(ClipboardPenLine), accent: false },
  { label: 'Restock', icon: markRaw(PackagePlus), accent: false },
]

const DEFAULT_DEPARTMENT_PERFORMANCE = [
  { name: 'General Medicine', value: 86 },
  { name: 'Pediatrics', value: 72 },
  { name: 'Dentistry', value: 64 },
  { name: 'ENT', value: 58 },
]


const getNestedValue = (source, path) =>
  path.split('.').reduce((currentValue, key) => currentValue?.[key], source)

const pickFirstValue = (source, paths) => {
  for (const path of paths) {
    const value = getNestedValue(source, path)

    if (value !== undefined && value !== null && value !== '') {
      return value
    }
  }

  return null
}

const extractCollection = (payload) => {
  if (Array.isArray(payload)) {
    return payload
  }

  if (!payload || typeof payload !== 'object') {
    return []
  }

  for (const key of COLLECTION_KEYS) {
    if (Array.isArray(payload[key])) {
      return payload[key]
    }
  }

  if (payload.data && typeof payload.data === 'object') {
    for (const key of COLLECTION_KEYS) {
      if (Array.isArray(payload.data[key])) {
        return payload.data[key]
      }
    }
  }

  return []
}

const parseDateTime = (record) => {
  const rawDate = pickFirstValue(record, APPOINTMENT_DATE_PATHS)
  const rawTime = pickFirstValue(record, APPOINTMENT_TIME_PATHS)
  const candidates = []

  if (rawDate instanceof Date) {
    return Number.isNaN(rawDate.getTime()) ? null : rawDate
  }

  if (typeof rawDate === 'number') {
    const parsedDate = new Date(rawDate)
    return Number.isNaN(parsedDate.getTime()) ? null : parsedDate
  }

  if (typeof rawDate === 'string') {
    candidates.push(rawDate)

    if (rawTime) {
      candidates.push(`${rawDate}T${rawTime}`)
      candidates.push(`${rawDate} ${rawTime}`)
    }
  }

  if (!rawDate && typeof rawTime === 'string') {
    candidates.push(rawTime)
  }

  for (const candidate of candidates) {
    const parsedDate = new Date(candidate)

    if (!Number.isNaN(parsedDate.getTime())) {
      return parsedDate
    }
  }

  return null
}

const formatNumber = (value) => NUMBER_FORMATTER.format(value)
const padNumber = (value) => `${value}`.padStart(2, '0')

const getLocalDateKey = (date) =>
  `${date.getFullYear()}-${padNumber(date.getMonth() + 1)}-${padNumber(date.getDate())}`

const isSameCalendarDate = (firstDate, secondDate) =>
  firstDate.getFullYear() === secondDate.getFullYear() &&
  firstDate.getMonth() === secondDate.getMonth() &&
  firstDate.getDate() === secondDate.getDate()

const normalizeStatus = (status) => {
  const normalizedStatus = String(status ?? 'PENDING')
    .trim()
    .toUpperCase()

  if (normalizedStatus === 'COMPLETED') {
    return 'Completed'
  }

  if (normalizedStatus === 'CANCELLED' || normalizedStatus === 'CANCELED') {
    return 'Cancelled'
  }

  if (normalizedStatus === 'PENDING') {
    return 'Pending'
  }

  return normalizedStatus.charAt(0) + normalizedStatus.slice(1).toLowerCase()
}

const normalizeDisplayName = (value, fallbackValue) => {
  if (typeof value === 'string' && value.trim()) {
    return value.trim()
  }

  return fallbackValue
}

const getErrorMessage = (error) =>
  error?.response?.data?.message ||
  error?.response?.data?.error ||
  error?.message ||
  'Unable to load dashboard data.'

export const useDashboardStore = defineStore('dashboard', () => {
  const loading = ref(false)
  const initialized = ref(false)
  const error = ref('')
  const patients = ref([])
  const patientsResponseMeta = ref(null)
  const appointments = ref([])
  const medicines = ref([])
  const medicalRecords = ref([])
  const departmentPerformance = ref(DEFAULT_DEPARTMENT_PERFORMANCE)
  const quickActions = ref(QUICK_ACTIONS)

  const totalPatients = computed(
  () =>
  patientsResponseMeta.value?.totalElements
  ??
  patients.value.length
  )

  const normalizedAppointments = computed(() =>
    appointments.value.map((appointment, index) => {
      const appointmentDate = parseDateTime(appointment)
      const patientName = normalizeDisplayName(
        pickFirstValue(appointment, PATIENT_NAME_PATHS),
        `Patient #${index + 1}`,
      )
      const doctorName = normalizeDisplayName(
        pickFirstValue(appointment, DOCTOR_NAME_PATHS),
        'Unassigned Doctor',
      )
      const doctorIdentifier =
        pickFirstValue(appointment, DOCTOR_ID_PATHS) ||
        doctorName.toLowerCase().replace(/\s+/g, '-')

      return {
        raw: appointment,
        id: appointment.id ?? appointment.appointmentId ?? `${patientName}-${index}`,
        patient: patientName,
        doctor: doctorName,
        doctorIdentifier,
        date: appointmentDate,
        status: normalizeStatus(appointment.status),
      }
    }),
  )

  const todayAppointmentsCount = computed(() => {
    const today = new Date()

    return normalizedAppointments.value.filter(
      (appointment) => appointment.date && isSameCalendarDate(appointment.date, today),
    ).length
  })

  const activeDoctorsCount = computed(
    () =>
      new Set(
        normalizedAppointments.value
          .map((appointment) => appointment.doctorIdentifier)
          .filter(Boolean),
      ).size,
  )

  const pendingAppointmentsCount = computed(
    () =>
      normalizedAppointments.value.filter((appointment) => appointment.status === 'Pending').length,
  )

  const patientVisitTrend = computed(() => {
    const today = new Date()
    const dateRange = Array.from({ length: 7 }, (_, index) => {
      const date = new Date(today)
      date.setHours(0, 0, 0, 0)
      date.setDate(today.getDate() - (6 - index))
      return date
    })

    const visitsByDate = normalizedAppointments.value.reduce((accumulator, appointment) => {
      if (!appointment.date) {
        return accumulator
      }

      const key = getLocalDateKey(appointment.date)
      accumulator[key] = (accumulator[key] ?? 0) + 1
      return accumulator
    }, {})

    return {
      labels: dateRange.map((date) => WEEKDAY_FORMATTER.format(date)),
      values: dateRange.map((date) => visitsByDate[getLocalDateKey(date)] ?? 0),
    }
  })

  const recentAppointments = computed(() =>
    [...normalizedAppointments.value]
      .sort((firstAppointment, secondAppointment) => {
        const firstTime = firstAppointment.date?.getTime() ?? 0
        const secondTime = secondAppointment.date?.getTime() ?? 0
        return secondTime - firstTime
      })
      .slice(0, 5)
      .map((appointment) => ({
        id: appointment.id,
        patient: appointment.patient,
        doctor: appointment.doctor,
        time: appointment.date ? TIME_FORMATTER.format(appointment.date) : 'N/A',
        status: appointment.status,
      })),
  )

  const hasData = computed(
    () =>
      totalPatients.value > 0 ||
      appointments.value.length > 0 ||
      medicines.value.length > 0 ||
      medicalRecords.value.length > 0,
  )

  const isEmpty = computed(() => !loading.value && !error.value && !hasData.value)

  const statsCards = computed(() => [
    {
      title: 'Total Patients',
      value: formatNumber(totalPatients.value),
      subtext: totalPatients.value
        ? `${formatNumber(totalPatients.value)} registered patients`
        : 'No patients yet',
      badgeTone: totalPatients.value ? 'success' : 'info',
      icon: markRaw(Users),
    },
    {
      title: "Today's Appointments",
      value: formatNumber(todayAppointmentsCount.value),
      subtext: todayAppointmentsCount.value
        ? `${formatNumber(todayAppointmentsCount.value)} scheduled today`
        : 'No appointments today',
      badgeTone: todayAppointmentsCount.value ? 'success' : 'info',
      icon: markRaw(CalendarClock),
    },
    {
      title: 'Active Doctors',
      value: formatNumber(activeDoctorsCount.value),
      subtext: activeDoctorsCount.value
        ? `${formatNumber(activeDoctorsCount.value)} doctors with appointments`
        : 'No active doctors found',
      badgeTone: activeDoctorsCount.value ? 'info' : 'warning',
      icon: markRaw(Stethoscope),
    },
    {
      title: 'Pending Appointments',
      value: formatNumber(pendingAppointmentsCount.value),
      subtext: pendingAppointmentsCount.value
        ? `${formatNumber(pendingAppointmentsCount.value)} awaiting confirmation`
        : 'No pending appointments',
      badgeTone: pendingAppointmentsCount.value ? 'warning' : 'success',
      icon: markRaw(ClipboardList),
    },
  ])

  const fetchDashboardData = async () => {
    loading.value = true
    error.value = ''

    try {
      const [patientsResponse, appointmentsResponse, medicinesResponse, medicalRecordsResponse] =
        await Promise.all([getPatients(), getAppointments(), getMedicines(), getMedicalRecords()])

      patientsResponseMeta.value =
        patientsResponse?.data
        ??
        patientsResponse

      patients.value = extractCollection(patientsResponse)
      appointments.value = extractCollection(appointmentsResponse)
      medicines.value = extractCollection(medicinesResponse)
      medicalRecords.value = extractCollection(medicalRecordsResponse)
    } catch (fetchError) {
      patients.value = []
      appointments.value = []
      medicines.value = []
      medicalRecords.value = []
      error.value = getErrorMessage(fetchError)
    } finally {
      loading.value = false
      initialized.value = true
    }
  }

  const clearError = () => {
    error.value = ''
  }

  return {
    activeDoctorsCount,
    appointments,
    clearError,
    departmentPerformance,
    error,
    fetchDashboardData,
    hasData,
    initialized,
    isEmpty,
    loading,
    medicalRecords,
    medicines,
    patientVisitTrend,
    patients,
    quickActions,
    recentAppointments,
    statsCards,
    todayAppointmentsCount,
    totalPatients,
  }
})
