<template>
  <el-container class="layout">
    <el-header class="header">
      <div>房产销售系统（管理端）</div>
      <div>
        <el-button text @click="$router.push('/admin/houses')">房源管理</el-button>
        <el-button text @click="$router.push('/admin/reviews')">审核列表</el-button>
        <el-button text @click="$router.push('/admin/users')">用户管理</el-button>
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
