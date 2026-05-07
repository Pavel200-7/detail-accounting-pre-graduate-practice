<template>
  <div class="department-node">
    <div class="department-item">
      <div class="dept-info">
        <span class="expand-icon" @click.stop="toggleExpand" v-if="hasChildren">
          {{ expanded ? '▼' : '▶' }}
        </span>
        <span class="dept-name">{{ department.name }}</span>
        <span class="dept-code">{{ department.code }}</span>
      </div>
      <div class="actions">
        <button @click.stop="$emit('edit', department)" class="btn-edit">✏️</button>
        <button @click.stop="$emit('delete', department.id)" class="btn-delete">🗑️</button>
      </div>
    </div>
    <div v-if="hasChildren && expanded" class="children">
      <DepartmentNode
        v-for="child in department.children"
        :key="child.id"
        :department="child"
        @edit="$emit('edit', $event)"
        @delete="$emit('delete', $event)"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  department: { type: Object, required: true }
})
defineEmits(['edit', 'delete'])

const expanded = ref(true)
const hasChildren = computed(() => props.department.children?.length > 0)

const toggleExpand = () => {
  expanded.value = !expanded.value
}
</script>

<style scoped>
.department-node {
  margin-left: 0;
}
.department-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 0.5rem;
  transition: background 0.2s;
}
.department-item:hover {
  background: #e9ecef;
}
.dept-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
}
.expand-icon {
  cursor: pointer;
  color: #6c757d;
  font-size: 0.8rem;
  width: 20px;
}
.dept-name {
  font-weight: 500;
  color: #2c3e50;
}
.dept-code {
  color: #6c757d;
  font-size: 0.8rem;
  font-family: monospace;
}
.actions {
  display: flex;
  gap: 0.5rem;
}
.btn-edit, .btn-delete {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.1rem;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  transition: background 0.2s;
}
.btn-edit:hover { background: #e0e0e0; }
.btn-delete:hover { background: #f8d7da; }
.children {
  margin-left: 1.5rem;
  margin-top: 0.5rem;
}
</style>