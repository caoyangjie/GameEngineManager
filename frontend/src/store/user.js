import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const name = ref('')
  const avatar = ref('')
  const roles = ref([])
  const permissions = ref([])

  // 登录
  function loginAction(userInfo) {
    const username = userInfo.username.trim()
    const password = userInfo.password
    const code = userInfo.code
    const uuid = userInfo.uuid
    return new Promise((resolve, reject) => {
      login(username, password, code, uuid)
        .then((res) => {
          setToken(res.token)
          token.value = res.token
          resolve()
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  // 获取用户信息
  function getInfoAction() {
    return new Promise((resolve, reject) => {
      getInfo(token.value)
        .then((res) => {
          const data = res.user
          if (!data) {
            reject('验证失败，请重新登录。')
          }
          if (!data.roles || data.roles.length <= 0) {
            reject('getInfo: roles must be a non-null array!')
          }
          roles.value = data.roles
          permissions.value = data.permissions
          name.value = data.userName
          avatar.value = data.avatar
          resolve(res)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  // 退出登录
  function logoutAction() {
    return new Promise((resolve, reject) => {
      logout(token.value)
        .then(() => {
          token.value = ''
          roles.value = []
          permissions.value = []
          removeToken()
          resolve()
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  return {
    token,
    name,
    avatar,
    roles,
    permissions,
    login: loginAction,
    getInfo: getInfoAction,
    logout: logoutAction,
  }
})

