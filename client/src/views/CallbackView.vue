<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 100vh; font-family: sans-serif;">
    <div>
      <h2>Вход в систему...</h2>
      <p>Пожалуйста, подождите...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuthStore } from '../stores/authStore'

const authStore = useAuthStore()

onMounted(() => {
  const hash = window.location.hash
  
  if (hash && hash.includes('access_token')) {
    const params = new URLSearchParams(hash.slice(1))
    const accessToken = params.get('access_token')
    const refreshToken = params.get('refresh_token')
    
    if (accessToken) {
      // Сохраняем токены
      authStore.setTokens(accessToken, refreshToken)
      // Редиректим на главную
      window.location.replace('/')
    } else {
      window.location.replace('/?error=no_token')
    }
  } else {
    window.location.replace('/?error=no_token')
  }
})
</script>