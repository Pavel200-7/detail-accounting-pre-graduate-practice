<template>
  <div class="request-section">
    <div class="section-header">
      <h3>{{ title }}</h3>
      <span class="count">{{ requests.length }}</span>
    </div>
    <div class="requests-list" v-if="requests.length">
      <RequestCard
        v-for="req in requests"
        :key="req.id"
        :request="req"
        @click="$emit('openRequest', req.id)"
      />
    </div>
    <div v-else class="empty-state">✨ Нет заявок</div>
  </div>
</template>

<script setup>
import RequestCard from './RequestCard.vue'

defineProps({
  title: { type: String, required: true },
  requests: { type: Array, default: () => [] }
})
defineEmits(['openRequest'])
</script>

<style scoped>
.request-section {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 1rem;
  margin: 0.5rem;
  flex: 1;
  min-width: 300px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #dee2e6;
}
.section-header h3 {
  font-size: 1.1rem;
  color: #2c3e50;
  margin: 0;
}
.count {
  background: #007bff;
  color: white;
  padding: 0.2rem 0.6rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}
.requests-list {
  max-height: 600px;
  overflow-y: auto;
}
.empty-state {
  text-align: center;
  color: #6c757d;
  padding: 2rem;
  font-size: 0.9rem;
}
</style>