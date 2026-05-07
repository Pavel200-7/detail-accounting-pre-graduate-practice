<template>
  <div class="request-card" @click="$emit('click')">
    <div class="card-header">
      <span class="status" :class="statusClass">{{ statusText }}</span>
      <span class="date">{{ formatDate(request.createdAt) }}</span>
    </div>
    <div class="card-body">
      <p><strong>Отдел:</strong> {{ request.department?.name || '—' }}</p>
      <p><strong>Сотрудник:</strong> {{ request.employee?.fullName || '—' }}</p>
      <p><strong>Позиций:</strong> {{ request.items?.length || 0 }}</p>
      <p v-if="request.comment" class="comment">📝 {{ request.comment }}</p>
      <p v-if="request.rejectionReason" class="rejection">⚠️ {{ request.rejectionReason }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  request: { type: Object, required: true }
})
defineEmits(['click'])

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleString('ru-RU')
}

const statusText = computed(() => {
  const map = {
    PENDING: '⏳ Ожидает',
    NEED_PURCHASE: '🛒 Требуется закупка',
    APPROVED: '✅ Утверждена',
    REJECTED: '❌ Отклонена',
    COMPLETED: '📦 Выполнена'
  }
  return map[props.request.status] || props.request.status
})

const statusClass = computed(() => {
  const map = {
    PENDING: 'status-pending',
    NEED_PURCHASE: 'status-pending',
    APPROVED: 'status-approved',
    REJECTED: 'status-rejected',
    COMPLETED: 'status-completed'
  }
  return map[props.request.status] || ''
})
</script>

<style scoped>
.request-card {
  background: white;
  border-radius: 12px;
  padding: 1rem;
  margin-bottom: 1rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #e0e0e0;
}
.request-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.12);
  border-color: #007bff;
}
.card-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.75rem;
}
.status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}
.status-pending { background: #fff3cd; color: #856404; }
.status-approved { background: #d4edda; color: #155724; }
.status-rejected { background: #f8d7da; color: #721c24; }
.status-completed { background: #d1ecf1; color: #0c5460; }
.date { color: #6c757d; font-size: 0.8rem; }
.card-body p { margin: 0.4rem 0; font-size: 0.9rem; }
.comment { color: #495057; font-style: italic; margin-top: 0.5rem; }
.rejection { color: #dc3545; margin-top: 0.5rem; font-size: 0.85rem; }
</style>