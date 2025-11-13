<template>
  <div class="home-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>欢迎使用游戏引擎管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>系统简介</h2>
        <p>这是一个基于 Spring Boot 和 Vue 3 开发的前后端分离管理系统。</p>
        <p>参考若依管理框架设计，提供完整的权限管理、用户管理等功能。</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { testHello } from '@/api/test'

onMounted(() => {
  testHello().then((res) => {
    console.log('测试接口返回:', res)
  })
})
</script>

<style lang="scss" scoped>
.home-container {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.welcome-content {
  h2 {
    margin-bottom: 20px;
    color: #409eff;
  }
  p {
    line-height: 1.8;
    margin-bottom: 10px;
  }
}
</style>

