<template>
  <div>
    <AppHeader />
    <div class="container" v-if="part">
      <div class="detail-card">
        <h2>{{ part.name }}</h2>
        <div class="info-grid">
          <div><strong>Артикул:</strong> {{ part.sku || '—' }}</div>
          <div><strong>Единица:</strong> {{ getUnitText(part.unit) }}</div>
          <div><strong>Категория:</strong> {{ part.category }}</div>
          <div><strong>Цена:</strong> {{ part.price?.toLocaleString() }} ₽</div>
          <div><strong>Остаток:</strong> <span class="stock-big">{{ part.stock?.quantity || 0 }}</span></div>
          <div><strong>Зарезервировано:</strong> {{ part.stock?.reservedQuantity || 0 }}</div>
          <div><strong>Доступно:</strong> <span :class="availableQuantity < 10 ? 'low-stock' : ''">{{ availableQuantity }}</span></div>
          <div class="full-width"><strong>Описание:</strong> {{ part.description || '—' }}</div>
        </div>
      </div>

      <div class="movement-form">
        <h3>➕ Добавить движение</h3>
        <form @submit.prevent="addMovement">
          <select v-model="movementForm.type" required>
            <option value="RECEIPT">📥 Приход</option>
            <option value="CONSUMPTION">📤 Расход</option>
          </select>
          <input type="number" v-model.number="movementForm.quantity" min="1" required placeholder="Количество">
          <input type="text" v-model="movementForm.documentType" placeholder="Тип документа">
          <input type="text" v-model="movementForm.comment" placeholder="Комментарий">
          <button type="submit">Добавить</button>
        </form>
      </div>

      <div class="movements-card">
        <h3>📜 История движений</h3>
        <table v-if="movements.length">
          <thead>
            <tr><th>Дата</th><th>Тип</th><th>Кол-во</th><th>До/После</th><th>Кто</th><th>Комментарий</th></tr>
          </thead>
          <tbody>
            <tr v-for="m in movements" :key="m.id">
              <td>{{ formatDate(m.performedAt) }}</td>
              <td :class="m.type === 'RECEIPT' ? 'receipt' : 'consumption'">
                {{ m.type === 'RECEIPT' ? '📥 Приход' : '📤 Расход' }}
              </td>
              <td>{{ m.quantity }}</td>
              <td>{{ m.quantityBefore }} → {{ m.quantityAfter }}</td>
              <td>{{ m.performedByFullName || '—' }}</td>
              <td>{{ m.comment || '—' }}</td>
            </tr>
          </tbody>
        </table>
        <div v-else class="empty-state">Нет движений</div>
      </div>
    </div>
    <div v-else class="container">Загрузка...</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getPartById, getPartMovements, createMovement } from '../api/parts'
import AppHeader from '../components/AppHeader.vue'

const route = useRoute()
const router = useRouter()
const part = ref(null)
const movements = ref([])
const movementForm = ref({
  type: 'RECEIPT',
  quantity: 1,
  documentType: '',
  comment: ''
})

const availableQuantity = computed(() => {
  if (!part.value?.stock) return 0
  return (part.value.stock.quantity || 0) - (part.value.stock.reservedQuantity || 0)
})

const loadData = async () => {
  const id = route.params.id
  try {
    const [partRes, movementsRes] = await Promise.all([
      getPartById(id),
      getPartMovements(id)
    ])
    part.value = partRes.data
    movements.value = movementsRes.data.movements || []
  } catch (err) {
    console.error(err)
    router.push('/parts')
  }
}

const addMovement = async () => {
  try {
    await createMovement({
      partId: route.params.id,
      type: movementForm.value.type,
      quantity: movementForm.value.quantity,
      documentType: movementForm.value.documentType,
      comment: movementForm.value.comment
    })
    await loadData()
    movementForm.value = { type: 'RECEIPT', quantity: 1, documentType: '', comment: '' }
    alert('✅ Движение добавлено')
  } catch (err) {
    alert(err.response?.data?.error || '❌ Ошибка')
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
  loadData()
})
</script>

<style scoped>
.container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
.detail-card, .movements-card, .movement-form {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.detail-card h2 {
  color: #2c3e50;
  margin-bottom: 1rem;
}
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
  margin-top: 1rem;
}
.full-width {
  grid-column: 1 / -1;
}
.stock-big {
  font-size: 1.2rem;
  font-weight: bold;
  color: #28a745;
}
.low-stock {
  color: #dc3545;
  font-weight: bold;
}
.movement-form form {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
  margin-top: 1rem;
}
.movement-form input, .movement-form select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}
.movement-form button {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 6px;
  cursor: pointer;
}
.movements-card table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  font-size: 0.85rem;
}
.movements-card th, .movements-card td {
  border: 1px solid #dee2e6;
  padding: 0.5rem;
  text-align: left;
}
.movements-card th {
  background: #f8f9fa;
}
.receipt { color: #28a745; font-weight: 500; }
.consumption { color: #dc3545; font-weight: 500; }
.empty-state {
  text-align: center;
  padding: 2rem;
  color: #6c757d;
}
</style>