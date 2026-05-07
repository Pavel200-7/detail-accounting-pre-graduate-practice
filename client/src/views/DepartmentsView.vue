<template>
  <div>
    <AppHeader />
    <div class="container">
      <div class="toolbar">
        <h2>🏢 Управление отделами</h2>
        <button @click="openModal()" class="btn-primary">+ Создать отдел</button>
      </div>

      <div class="departments-tree" v-if="departments.length">
        <DepartmentNode
          v-for="dept in departments"
          :key="dept.id"
          :department="dept"
          @edit="openModal"
          @delete="handleDelete"
        />
      </div>
      <div v-else class="empty-state">Нет отделов. Создайте первый отдел!</div>
    </div>

    <!-- Модальное окно -->
    <div v-if="modalVisible" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ editingDepartment ? 'Редактировать отдел' : 'Создать отдел' }}</h3>
        <form @submit.prevent="saveDepartment">
          <div class="form-group">
            <label>Название *</label>
            <input v-model="form.name" required minlength="3" maxlength="255">
          </div>
          <div class="form-group">
            <label>Код (уникальный, до 16 символов) *</label>
            <input v-model="form.code" required maxlength="16" :disabled="!!editingDepartment">
          </div>
          <div class="form-group">
            <label>Родительский отдел</label>
            <select v-model="form.parentId">
              <option :value="null">— Нет (корневой) —</option>
              <option v-for="d in allDepartmentsFlat" :key="d.id" :value="d.id" :disabled="d.id === editingDepartment?.id">
                {{ d.name }}
              </option>
            </select>
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
import { ref, onMounted, computed } from 'vue'
import { getDepartments, createDepartment, updateDepartment, deleteDepartment } from '../api/departments'
import AppHeader from '../components/AppHeader.vue'
import DepartmentNode from '../components/DepartmentNode.vue'

const departments = ref([])
const modalVisible = ref(false)
const editingDepartment = ref(null)
const form = ref({
  name: '',
  code: '',
  parentId: null,
  description: ''
})

const allDepartmentsFlat = computed(() => {
  const flatten = (list, result = []) => {
    for (const item of list) {
      result.push({ id: item.id, name: item.name })
      if (item.children) flatten(item.children, result)
    }
    return result
  }
  return flatten(departments.value)
})

const loadDepartments = async () => {
  try {
    const res = await getDepartments()
    departments.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const openModal = (dept = null) => {
  if (dept) {
    editingDepartment.value = dept
    form.value = {
      name: dept.name,
      code: dept.code,
      parentId: dept.parentId || null,
      description: dept.description || ''
    }
  } else {
    editingDepartment.value = null
    form.value = { name: '', code: '', parentId: null, description: '' }
  }
  modalVisible.value = true
}

const closeModal = () => {
  modalVisible.value = false
  editingDepartment.value = null
}

const saveDepartment = async () => {
  try {
    if (editingDepartment.value) {
      await updateDepartment(editingDepartment.value.id, {
        name: form.value.name,
        description: form.value.description
      })
    } else {
      await createDepartment(form.value)
    }
    await loadDepartments()
    closeModal()
  } catch (err) {
    console.error(err)
    alert(err.response?.data?.error || 'Ошибка сохранения')
  }
}

const handleDelete = async (id) => {
  if (confirm('Удалить отдел? Все дочерние отделы, пользователи и заявки должны отсутствовать.')) {
    try {
      await deleteDepartment(id)
      await loadDepartments()
    } catch (err) {
      alert(err.response?.data?.error || 'Нельзя удалить отдел')
    }
  }
}

onMounted(() => {
  loadDepartments()
})
</script>

<style scoped>
.container {
  padding: 2rem;
  max-width: 1000px;
  margin: 0 auto;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}
.toolbar h2 {
  color: #2c3e50;
}
.btn-primary {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
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
.departments-tree {
  background: white;
  border-radius: 16px;
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
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
  margin-bottom: 1.5rem;
  color: #2c3e50;
}
.modal-content .form-group {
  margin-bottom: 1rem;
}
.modal-content label {
  display: block;
  margin-bottom: 0.25rem;
  font-weight: 500;
  font-size: 0.9rem;
}
.modal-content input, .modal-content select, .modal-content textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 0.9rem;
}
.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}
</style>