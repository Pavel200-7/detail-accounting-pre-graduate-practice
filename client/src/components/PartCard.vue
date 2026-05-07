<template>
  <div class="part-card" @click="$emit('click')">
    <h3>{{ part.name }}</h3>
    <p class="sku">📦 {{ part.sku || '—' }}</p>
    <div class="stock-info">
      <span class="stock-value">{{ part.stock?.quantity || 0 }}</span>
      <span class="stock-unit">{{ getUnitText(part.unit) }}</span>
    </div>
    <p v-if="part.price" class="price">💰 {{ part.price.toLocaleString() }} ₽</p>
  </div>
</template>

<script setup>
defineProps({
  part: { type: Object, required: true }
})
defineEmits(['click'])

const getUnitText = (unit) => {
  const units = { PCS: 'шт', KG: 'кг', M: 'м', PACK: 'уп', SET: 'компл', L: 'л' }
  return units[unit] || unit
}
</script>

<style scoped>
.part-card {
  background: white;
  border-radius: 12px;
  padding: 1.2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #e0e0e0;
}
.part-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.12);
  border-color: #28a745;
}
.part-card h3 {
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-size: 1.1rem;
}
.sku {
  color: #6c757d;
  font-size: 0.8rem;
  margin-bottom: 0.5rem;
}
.stock-info {
  margin: 0.75rem 0;
  display: flex;
  align-items: baseline;
  gap: 0.25rem;
}
.stock-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #28a745;
}
.stock-unit {
  color: #6c757d;
  font-size: 0.8rem;
}
.price {
  color: #007bff;
  font-weight: 500;
  margin-top: 0.5rem;
}
</style>