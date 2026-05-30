<template>
  <div class="login-wrap">
    <el-card class="box-card">
      <h2>系统登录</h2>
      <el-form :model="form" @keyup.enter="onSubmit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" show-password />
        </el-form-item>
        <el-button type="primary" style="width:100%" @click="onSubmit">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { login } from '../api/auth'
import { authState } from '../store/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = reactive({ username: '', password: '' })

const onSubmit = async () => {
  const data = await login(form)
  authState.user = data
  router.push(data.role === 'admin' ? '/admin/houses' : '/houses')
}
</script>

<style scoped>
.login-wrap {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}
.box-card {
  width: 400px;
}
</style>
