<template>
  <div class="app-wrapper">
    <div class="main-container">
      <el-container>
        <el-aside :width="isCollapse ? '64px' : '200px'">
          <div class="logo">
            <span v-if="!isCollapse">游戏引擎管理系统</span>
            <span v-else>GEM</span>
          </div>
          <el-menu
            :default-active="activeMenu"
            :collapse="isCollapse"
            :unique-opened="true"
            :collapse-transition="false"
            mode="vertical"
            router
          >
            <el-menu-item index="/home/index">
              <el-icon><HomeFilled /></el-icon>
              <template #title>首页</template>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-container>
          <el-header>
            <div class="navbar">
              <div class="hamburger-container" @click="toggleSideBar">
                <el-icon><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
              </div>
              <div class="right-menu">
                <el-dropdown class="avatar-container" trigger="click">
                  <div class="avatar-wrapper">
                    <el-icon><User /></el-icon>
                    <span class="user-name">管理员</span>
                  </div>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </el-header>
          <el-main>
            <router-view />
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { HomeFilled, Fold, Expand, User } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

const activeMenu = computed(() => {
  const { path } = route
  return path
})

const toggleSideBar = () => {
  isCollapse.value = !isCollapse.value
}

const handleLogout = () => {
  userStore.logout().then(() => {
    router.push('/login')
  })
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
}

.main-container {
  min-height: 100%;
  transition: margin-left 0.28s;
  margin-left: 0;
  position: relative;
}

.el-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
  transition: width 0.28s;
  overflow: hidden;
}

.logo {
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  color: #fff;
  font-weight: bold;
  font-size: 14px;
}

.el-menu {
  border: none;
  height: calc(100vh - 50px);
  width: 100% !important;
  background-color: #304156;
  color: #bfcbd9;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.hamburger-container {
  line-height: 46px;
  height: 100%;
  float: left;
  cursor: pointer;
  transition: background 0.3s;
  -webkit-tap-highlight-color: transparent;
  font-size: 20px;

  &:hover {
    background: rgba(0, 0, 0, 0.025);
  }
}

.right-menu {
  float: right;
  height: 100%;
  line-height: 50px;

  &:focus {
    outline: none;
  }
}

.avatar-container {
  margin-right: 30px;
  cursor: pointer;

  .avatar-wrapper {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #5a5e66;

    .user-name {
      font-size: 14px;
    }
  }
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>

