<template>
  <el-container class="layout">
    <el-header class="header">
      <div>房产销售系统（用户端）</div>
      <div>
        <el-button text @click="$router.push('/houses')">房源列表</el-button>
        <el-button text @click="$router.push('/favorites')">我的收藏</el-button>
        <el-button text @click="$router.push('/profile')">个人信息</el-button>
        <el-button type="danger" plain @click="handleLogout">退出</el-button>
      </div>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { logout } from '../api/auth'
import { authState } from '../store/auth'

const handleLogout = async () => {
  await logout()
  authState.user = null
  location.href = '/login'
}
</script>

<style scoped>
.layout { min-height: 100vh; }
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
</style>
