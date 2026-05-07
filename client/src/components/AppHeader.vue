<template>
  <header class="header">
    <h1 @click="$router.push('/')" class="logo">📦 Складской учёт</h1>
    <nav class="nav">
      <button @click="$router.push('/departments')" class="nav-btn">🏢 Отделы</button>
      <button @click="$router.push('/parts')" class="nav-btn">🔩 Детали</button>
      <button @click="$router.push('/requests')" class="nav-btn">📋 Заявки</button>
      <button @click="handleLogout" class="logout-btn">🚪 Выйти</button>
    </nav>
  </header>
</template>

<script setup>
import { useAuthStore } from '../stores/authStore'

const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
}
</script>

<style scoped>
.header {
  background: linear-gradient(135deg, #1a2a3a 0%, #0f1a24 100%);
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.logo {
  cursor: pointer;
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
}
.nav {
  display: flex;
  gap: 1rem;
}
.nav-btn, .logout-btn {
  background: rgba(255,255,255,0.1);
  border: none;
  color: white;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.95rem;
  transition: all 0.2s;
}
.nav-btn:hover {
  background: rgba(255,255,255,0.2);
  transform: translateY(-1px);
}
.logout-btn {
  background: rgba(220, 53, 69, 0.8);
}
.logout-btn:hover {
  background: #dc3545;
}
</style>