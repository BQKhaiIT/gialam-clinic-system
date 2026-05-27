<script setup>
import { HeartPulse } from '@lucide/vue'
import { logout } from '@/api/authApi'
import { useRoute, useRouter } from 'vue-router'

const props = defineProps({
  items: {
    type: Array,
    default: () => [],
  },
  isOpen: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close'])
const route = useRoute()
const router = useRouter()

const isActiveItem = (item) => {
  const targetPath = item.to

  if (targetPath) {
    return route.path === targetPath
  }

  return false
}

const navigate = async (item) => {
  if (item.action === 'logout') {
    return handleLogout()
  }

  const targetPath = item.to

  if (targetPath && route.path !== targetPath) {
    await router.push(targetPath)
  }

  if (props.isOpen && window.innerWidth < 1024) {
    emit('close')
  }
}
const handleLogout = async () => {
  logout()

  await router.replace('/login')

  if (props.isOpen && window.innerWidth < 1024) {
    emit('close')
  }
}
</script>

<template>
  <aside
    class="fixed inset-y-0 left-0 z-50 flex h-screen w-[228px] flex-col overflow-hidden border-r border-[#D9E8ED] bg-[#F3F9FB] px-4 py-4 shadow-[0_18px_40px_rgba(69,102,112,0.12)] transition-transform duration-300 lg:sticky lg:top-0 lg:translate-x-0 lg:shadow-none"
    :class="isOpen ? 'translate-x-0' : '-translate-x-full'"
  >
    <div class="flex items-center gap-3 border-b border-slate-200/70 pb-4">
      <div
        class="flex h-[32px] w-[32px] shrink-0 items-center justify-center rounded-[10px] bg-[#2F6F7D] text-white shadow-sm"
      >
        <HeartPulse class="h-[16px] w-[16px] stroke-[2.3]" />
      </div>
      <div>
        <p
          class="mb-0 text-[12px] font-bold leading-[1.1] tracking-[-0.02em] text-[#2F5D6B] sm:text-[13px]"
        >
          MediFlow Admin
        </p>
        <p class="mb-0 mt-0.5 text-[11px] text-slate-500">Health Operations</p>
      </div>
    </div>

    <button
      type="button"
      class="mt-3 inline-flex items-center rounded-xl border border-slate-200 px-3 py-2 text-sm font-medium text-slate-600 lg:hidden"
      @click="$emit('close')"
    >
      Close menu
    </button>

    <nav class="scrollbar-hidden mt-4 min-h-0 flex-1 space-y-1.5 overflow-y-auto pb-3 pr-0.5">
      <button
        v-for="item in items"
        :key="item.id"
        type="button"
        class="group flex w-full items-center gap-3 rounded-[16px] px-2.5 py-2.5 text-left transition-all duration-200"
        :class="
          isActiveItem(item)
            ? 'bg-[#CFE5EE] text-[#2F6F7D] shadow-sm'
            : 'text-slate-600 hover:bg-white hover:text-slate-900'
        "
        @click="navigate(item)"
      >
        <span
          class="flex h-[42px] w-[42px] shrink-0 items-center justify-center rounded-[14px] transition-all duration-200"
          :class="
            isActiveItem(item)
              ? 'bg-[#F7FCFE] text-[#2F6F7D] shadow-[inset_0_0_0_1px_rgba(141,188,199,0.18)]'
              : 'bg-[#EAF4F7] text-[#58708A] group-hover:bg-[#DDEEF3] group-hover:text-[#2F5D6B]'
          "
        >
          <component :is="item.icon" class="h-[18px] w-[18px] shrink-0 stroke-[2.2]" />
        </span>
        <span class="min-w-0 text-[14px] font-medium leading-5">{{ item.label }}</span>
      </button>
    </nav>
  </aside>
</template>
