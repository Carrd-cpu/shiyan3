<template>
  <el-card>
    <h3>个人信息</h3>
    <el-form :model="form" label-width="100px" style="max-width: 500px">
      <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
      <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="新密码"><el-input v-model="form.password" show-password /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">保存</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { getProfile, updateProfile } from '../../api/user'
import { ElMessage } from 'element-plus'

const form = reactive({ username: '', nickname: '', phone: '', password: '' })

const loadData = async () => {
  const data = await getProfile()
  form.username = data.username
  form.nickname = data.nickname
  form.phone = data.phone
}

const save = async () => {
  await updateProfile(form)
  form.password = ''
  ElMessage.success('保存成功')
}

onMounted(loadData)
</script>
