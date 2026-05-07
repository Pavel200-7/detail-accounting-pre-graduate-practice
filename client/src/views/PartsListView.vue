<template>
  <div>
    <AppHeader />
    <div class="container">
      <div class="toolbar">
        <h2>🔩 Детали и комплектующие</h2>
        <button @click="openCreateModal" class="btn-primary">+ Добавить деталь</button>
      </div>

      <div class="parts-grid" v-if="parts.length">
        <PartCard
          v-for="part in parts"
          :key="part.id"
          :part="part"
          @click="goToPart(part.id)"
        />
      </div>
      <div v-else class="empty-state">Нет деталей. Добавьте первую деталь!</div>
    </div>

    <!-- Модальное окно создания детали -->
    <div v-if="modalVisible" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ editingPart ? 'Редактировать деталь' : 'Создать деталь' }}</h3>
        <form @submit.prevent="savePart">
          <div class="form-group">
            <label>Название *</label>
            <input v-model="form.name" required minlength="3" maxlength="255">
          </div>
          <div class="form-group">
            <label>Артикул (SKU) *</label>
            <input v-model="form.sku" required maxlength="100">
          </div>
          <div class="form-group">
            <label>Единица измерения *</label>
            <select v-model="form.unit" required>
              <option value="PCS">Штуки (шт)</option>
              <option value="KG">Килограммы (кг)</option>
              <option value="M">Метры (м)</option>
              <option value="PACK">Пачки (уп)</option>
              <option value="SET">Комплекты (компл)</option>
              <option value="L">Литры (л)</option>
            </select>
          </div>
          <div class="form-group">
            <label>Категория *</label>
            <select v-model="form.category" required>
              <option value="CONSUMABLE">Расходный материал</option>
              <option value="EQUIPMENT">Оборудование</option>
              <option value="INVENTORY">Инвентарь</option>
              <option value="TOOL">Инструмент</option>
              <option value="COMPONENT">Комплектующая</option>
            </select>
          </div>
          <div class="form-group">
            <label>Цена *</label>
            <input type="number" v-model.number="form.price" step="0.01" min="0.01" required>
          </div>
          <div class="form-group">
            <label>Описание</label>
            <textarea v-model="form.description" rows="3" maxlength="255"></textarea>
          </div>
          <div class="modal-buttons">
            <button type="button" @click="closeModal" class="btn-secondary">Отмена</button>
            <button type="submit" class="btn-primary">Сохранить</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getParts, createPart } from '../api/parts'
import AppHeader from '../components/AppHeader.vue'
import PartCard from '../components/PartCard.vue'

const router = useRouter()
const parts = ref([])
const modalVisible = ref(false)
const editingPart = ref(null)
const form = ref({
  name: '',
  sku: '',
  unit: 'PCS',
  category: 'CONSUMABLE',
  price: 0,
  description: ''
})

const loadParts = async () => {
  try {
    const res = await getParts()
    parts.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const openCreateModal = () => {
  editingPart.value = null
  form.value = {
    name: '',
    sku: '',
    unit: 'PCS',
    category: 'CONSUMABLE',
    price: 0,
    description: ''
  }
  modalVisible.value = true
}

const closeModal = () => {
  modalVisible.value = false
  editingPart.value = null
}

const savePart = async () => {
  try {
    await createPart(form.value)
    await loadParts()
    closeModal()
    alert('Деталь успешно добавлена')
  } catch (err) {
    console.error(err)
    alert(err.response?.data?.error || 'Ошибка при создании детали')
  }
}

const goToPart = (id) => {
  router.push(`/parts/${id}`)
}

onMounted(() => {
  loadParts()
})
</script>

<style scoped>
.container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}
.btn-primary {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
}
.btn-secondary {
  background: #6c757d;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
}
.parts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}
.empty-state {
  text-align: center;
  padding: 3rem;
  background: white;
  border-radius: 16px;
  color: #6c757d;
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
  padding: 2rem;
  border-radius: 16px;
  min-width: 450px;
  max-width: 500px;
}
.modal-content h3 {
  margin-bottom: 1rem;
}
.form-group {
  margin-bottom: 1rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.25rem;
  font-weight: 500;
}
.form-group input, .form-group select, .form-group textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 6px;
}
.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}
</style>