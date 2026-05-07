<template>
  <div>
    <AppHeader />
    
    <!-- Форма создания заявки -->
    <div class="create-form">
      <h2>➕ Создать заявку</h2>
      <div class="form-group">
        <textarea v-model="newRequest.comment" placeholder="Комментарий (необязательно)" rows="2"></textarea>
      </div>
      
      <div class="items-group">
        <h4>Позиции:</h4>
        <div v-for="(item, idx) in newRequest.items" :key="idx" class="item-row">
          <select v-model="item.partId" required>
            <option :value="null">Выберите деталь</option>
            <option v-for="part in parts" :key="part.id" :value="part.id">
              {{ part.name }} (остаток: {{ part.stock?.quantity || 0 }} {{ getUnitText(part.unit) }})
            </option>
          </select>
          <input type="number" v-model.number="item.quantity" min="1" placeholder="Кол-во">
          <button @click="removeItem(idx)" class="remove-btn">✕</button>
        </div>
        <button @click="addItem" class="add-btn">+ Добавить позицию</button>
      </div>
      
      <button @click="submitRequest" :disabled="loading" class="submit-btn">
        {{ loading ? 'Отправка...' : '📤 Отправить заявку' }}
      </button>
    </div>

    <!-- Секции с заявками -->
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
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { getParts } from '../api/parts'
import { createRequest, getRequestsByUser } from '../api/requests'
import AppHeader from '../components/AppHeader.vue'
import RequestSection from '../components/RequestSection.vue'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const parts = ref([])
const userRequests = ref([])

const newRequest = ref({
  comment: '',
  items: [{ partId: null, quantity: 1 }]
})

const pendingRequests = computed(() => 
  userRequests.value.filter(r => r.status === 'PENDING' || r.status === 'NEED_PURCHASE')
)
const approvedRequests = computed(() => userRequests.value.filter(r => r.status === 'APPROVED'))
const rejectedRequests = computed(() => userRequests.value.filter(r => r.status === 'REJECTED'))
const completedRequests = computed(() => userRequests.value.filter(r => r.status === 'COMPLETED'))

const addItem = () => {
  newRequest.value.items.push({ partId: null, quantity: 1 })
}

const removeItem = (idx) => {
  newRequest.value.items.splice(idx, 1)
}

const getUnitText = (unit) => {
  const units = { PCS: 'шт', KG: 'кг', M: 'м', PACK: 'уп', SET: 'компл', L: 'л' }
  return units[unit] || unit
}

const submitRequest = async () => {
  const validItems = newRequest.value.items.filter(i => i.partId && i.quantity > 0)
  if (validItems.length === 0) {
    alert('Добавьте хотя бы одну позицию')
    return
  }
  
  loading.value = true
  try {
    await createRequest({
      comment: newRequest.value.comment,
      items: validItems.map(i => ({ partId: i.partId, quantity: i.quantity }))
    })
    alert('✅ Заявка создана')
    newRequest.value = { comment: '', items: [{ partId: null, quantity: 1 }] }
    await loadUserRequests()
  } catch (err) {
    console.error(err)
    alert('❌ Ошибка при создании заявки')
  } finally {
    loading.value = false
  }
}

const loadUserRequests = async () => {
  const userId = authStore.getUserId()
  if (!userId) return
  try {
    const res = await getRequestsByUser(userId)
    userRequests.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const loadParts = async () => {
  try {
    const res = await getParts()
    parts.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const openRequest = (id) => {
  router.push(`/requests/${id}`)
}

onMounted(() => {
  loadParts()
  loadUserRequests()
})
</script>

<style scoped>
.create-form {
  background: white;
  margin: 2rem;
  padding: 1.5rem;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.create-form h2 {
  margin-bottom: 1rem;
  color: #2c3e50;
}
.form-group {
  margin-bottom: 1rem;
}
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: inherit;
  font-size: 0.9rem;
  resize: vertical;
}
.items-group {
  margin: 1rem 0;
}
.items-group h4 {
  margin-bottom: 0.5rem;
  color: #495057;
}
.item-row {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}
.item-row select, .item-row input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}
.remove-btn {
  background: #dc3545;
  color: white;
  border: none;
  padding: 0 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}
.add-btn {
  background: #28a745;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 0.5rem;
}
.submit-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  margin-top: 1rem;
}
.submit-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}
.dashboard {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 1rem;
  padding: 0 2rem 2rem;
}
</style>