import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { jwtDecode } from 'jwt-decode'

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref(localStorage.getItem('access_token') || null)
  const refreshToken = ref(localStorage.getItem('refresh_token') || null)

  const setTokens = (access, refresh) => {
    accessToken.value = access
    refreshToken.value = refresh
    if (access) {
      localStorage.setItem('access_token', access)
    } else {
      localStorage.removeItem('access_token')
    }
    if (refresh) {
      localStorage.setItem('refresh_token', refresh)
    } else {
      localStorage.removeItem('refresh_token')
    }
  }

  // Используем computed с getter'ом
  const isAuthenticated = computed(() => {
    return !!accessToken.value
  })

  const getUserId = () => {
    if (!accessToken.value) return null
    try {
      const decoded = jwtDecode(accessToken.value)
      return decoded.sub
    } catch (err) {
      console.error('Failed to decode token:', err)
      return null
    }
  }

  const getUserInfo = () => {
    if (!accessToken.value) return null
    try {
      const decoded = jwtDecode(accessToken.value)
      return {
        id: decoded.sub,
        name: decoded.name || decoded.preferred_username,
        email: decoded.email
      }
    } catch (err) {
      console.error('Failed to decode token:', err)
      return null
    }
  }

  const logout = () => {
    setTokens(null, null)
    setTimeout(() => {
      window.location.href = '/api/v1/auth/authorize'
    }, 100)
  }

  const redirectToKeycloak = () => {
    // Используем setTimeout, чтобы избежать проблем с навигацией
    setTimeout(() => {
      window.location.href = '/api/v1/auth/authorize'
    }, 100)
  }

  return {
    accessToken,
    refreshToken,
    setTokens,
    isAuthenticated,
    getUserId,
    getUserInfo,
    logout,
    redirectToKeycloak
  }
})