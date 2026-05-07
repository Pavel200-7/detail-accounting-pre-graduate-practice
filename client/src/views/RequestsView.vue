<template>
  <div>
    <AppHeader />
    <div class="container">
      <h2>📋 Все заявки</h2>
      <div class="dashboard">
        <RequestSection
          title="⏳ Ожидают / Требуют закупки"
          :requests="pendingRequests"
          @openRequest="openRequest"
        />
        <RequestSection
          title="✅ Утверждены"
          :requests="approvedRequests"
          @openRequest="openRequest"
        />
        <RequestSection
          title="❌ Отклонены"
          :requests="rejectedRequests"
          @openRequest="openRequest"
        />
        <RequestSection
          title="📦 Выполнены"
          :requests="completedRequests"
          @openRequest="openRequest"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getRequestsByStatus } from '../api/requests'
import AppHeader from '../components/AppHeader.vue'
import RequestSection from '../components/RequestSection.vue'

const router = useRouter()
const pending = ref([])
const approved = ref([])
const rejected = ref([])
const completed = ref([])

const pendingRequests = computed(() => pending.value)
const approvedRequests = computed(() => approved.value)
const rejectedRequests = computed(() => rejected.value)
const completedRequests = computed(() => completed.value)

const loadRequests = async () => {
  try {
    const [pendingRes, approvedRes, rejectedRes, completedRes, needPurchaseRes] = await Promise.all([
      getRequestsByStatus('PENDING'),
      getRequestsByStatus('APPROVED'),
      getRequestsByStatus('REJECTED'),
      getRequestsByStatus('COMPLETED'),
      getRequestsByStatus('NEED_PURCHASE')
    ])
    pending.value = [...pendingRes.data, ...needPurchaseRes.data]
    approved.value = approvedRes.data
    rejected.value = rejectedRes.data
    completed.value = completedRes.data
  } catch (err) {
    console.error(err)
  }
}

const openRequest = (id) => {
  router.push(`/requests/${id}`)
}

onMounted(() => {
  loadRequests()
})
</script>

<style scoped>
.container {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}
.container h2 {
  color: #2c3e50;
  margin-bottom: 1.5rem;
}
.dashboard {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 1rem;
}
</style>