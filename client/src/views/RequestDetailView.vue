<template>
  <div>
    <AppHeader />
    <div class="container" v-if="request">
      <div class="detail-card">
        <div class="header">
          <h2>📄 Заявка #{{ request.id?.slice(0, 8) }}</h2>
          <span class="status" :class="statusClass">{{ statusText }}</span>
        </div>

        <div class="info-grid">
          <div><strong>Статус:</strong> {{ statusText }}</div>
          <div><strong>Дата создания:</strong> {{ formatDate(request.createdAt) }}</div>
          <div v-if="request.submittedAt"><strong>Дата отправки:</strong> {{ formatDate(request.submittedAt) }}</div>
          <div v-if="request.approvedAt"><strong>Дата утверждения:</strong> {{ formatDate(request.approvedAt) }}</div>
          <div v-if="request.executedAt"><strong>Дата выполнения:</strong> {{ formatDate(request.executedAt) }}</div>
          <div><strong>Сотрудник:</strong> {{ request.employee?.fullName }}</div>
          <div><strong>Отдел:</strong> {{ request.department?.name }}</div>
          <div><strong>Email:</strong> {{ request.employee?.email }}</div>
          <div v-if="request.comment" class="full-width"><strong>Комментарий:</strong> {{ request.comment }}</div>
          <div v-if="request.rejectionReason" class="full-width rejection"><strong>Причина отказа:</strong> {{ request.rejectionReason }}</div>
        </div>

        <div class="items-table">
          <h3>📦 Позиции заявки</h3>
          <table>
            <thead>
              <tr><th>Деталь</th><th>Артикул</th><th>Количество</th><th>Ед.</th><th>Доступно на складе</th></tr>
            </thead>
            <tbody>
              <tr v-for="item in request.items" :key="item.id">
                <td><strong>{{ item.part?.name }}</strong></td>
                <td>{{ item.part?.sku || '—' }}</td>
                <td>{{ item.quantity }}</td>
                <td>{{ getUnitText(item.part?.unit) }}</td>
                <td :class="{ 'not-enough': (item.part?.availableQuantity || 0) < item.quantity }">
                  {{ item.part?.availableQuantity || 0 }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="actions" v-if="request.status === 'PENDING' || request.status === 'NEED_PURCHASE'">
          <button @click="approve" class="btn-approve">✅ Утвердить</button>
          <button @click="showRejectModal = true" class="btn-reject">❌ Отклонить</button>
        </div>
        <div class="actions" v-if="request.status === 'APPROVED'">
          <button @click="complete" class="btn-complete">📦 Выполнить</button>
          <button @click="showRejectModal = true" class="btn-reject">❌ Отклонить</button>
        </div>
      </div>
    </div>

    <!-- Модалка отклонения -->
    <div v-if="showRejectModal" class="modal" @click.self="showRejectModal = false">
      <div class="modal-content">
        <h3>Причина отклонения</h3>
        <textarea v-model="rejectionReason" rows="3" placeholder="Укажите причину..."></textarea>
        <div class="modal-buttons">
          <button @click="showRejectModal = false" class="btn-secondary">Отмена</button>
          <button @click="reject" class="btn-primary">Отклонить</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getRequestById, approveRequest, rejectRequest, completeRequest } from '../api/requests'
import AppHeader from '../components/AppHeader.vue'

const route = useRoute()
const router = useRouter()
const request = ref(null)
const showRejectModal = ref(false)
const rejectionReason = ref('')

const statusText = computed(() => {
  const map = {
    PENDING: '⏳ Ожидает',
    NEED_PURCHASE: '🛒 Требуется закупка',
    APPROVED: '✅ Утверждена',
    REJECTED: '❌ Отклонена',
    COMPLETED: '📦 Выполнена'
  }
  return map[request.value?.status] || request.value?.status
})

const statusClass = computed(() => {
  const map = {
    PENDING: 'status-pending',
    NEED_PURCHASE: 'status-pending',
    APPROVED: 'status-approved',
    REJECTED: 'status-rejected',
    COMPLETED: 'status-completed'
  }
  return map[request.value?.status] || ''
})

const loadRequest = async () => {
  try {
    const res = await getRequestById(route.params.id)
    request.value = res.data
  } catch (err) {
    console.error(err)
    router.push('/requests')
  }
}

const approve = async () => {
  if (confirm('Утвердить заявку?')) {
    try {
      await approveRequest(route.params.id)
      await loadRequest()
      alert('✅ Заявка утверждена')
    } catch (err) {
      alert(err.response?.data?.error || '❌ Ошибка')
    }
  }
}

const reject = async () => {
  if (!rejectionReason.value.trim()) {
    alert('Укажите причину отклонения')
    return
  }
  try {
    await rejectRequest({
      id: route.params.id,
      rejectionReason: rejectionReason.value
    })
    showRejectModal.value = false
    await loadRequest()
    alert('✅ Заявка отклонена')
  } catch (err) {
    alert(err.response?.data?.error || '❌ Ошибка')
  }
}

const complete = async () => {
  if (confirm('Выполнить заявку? Товары будут списаны со склада.')) {
    try {
      await completeRequest(route.params.id)
      await loadRequest()
      alert('✅ Заявка выполнена')
    } catch (err) {
      alert(err.response?.data?.error || '❌ Ошибка')
    }
  }
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleString('ru-RU')
}

const getUnitText = (unit) => {
  const units = { PCS: 'шт', KG: 'кг', M: 'м', PACK: 'уп', SET: 'компл', L: 'л' }
  return units[unit] || unit
}

onMounted(() => {
  loadRequest()
})
</script>

<style scoped>
.container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
.detail-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #eee;
}
.header h2 {
  color: #2c3e50;
  margin: 0;
}
.status {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}
.status-pending { background: #fff3cd; color: #856404; }
.status-approved { background: #d4edda; color: #155724; }
.status-rejected { background: #f8d7da; color: #721c24; }
.status-completed { background: #d1ecf1; color: #0c5460; }
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}
.full-width {
  grid-column: 1 / -1;
}
.rejection {
  color: #dc3545;
}
.items-table {
  margin: 1.5rem 0;
}
.items-table h3 {
  margin-bottom: 0.75rem;
}
.items-table table {
  width: 100%;
  border-collapse: collapse;
}
.items-table th, .items-table td {
  border: 1px solid #dee2e6;
  padding: 0.6rem;
  text-align: left;
}
.items-table th {
  background: #f8f9fa;
}
.not-enough {
  color: #dc3545;
  font-weight: bold;
}
.actions {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}
.btn-approve, .btn-complete {
  background: #28a745;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  cursor: pointer;
}
.btn-reject {
  background: #dc3545;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  cursor: pointer;
}
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 1.5rem;
  border-radius: 16px;
  min-width: 400px;
}
.modal-content h3 {
  margin-bottom: 1rem;
}
.modal-content textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 0.5rem 0;
}
.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}
.btn-secondary {
  background: #6c757d;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
}
.btn-primary {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
}
</style>