<template>
  <el-card>
    <h3>用户列表</h3>
    <el-table :data="list">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="role" label="角色" width="100" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="启用" width="120">
        <template #default="scope">
          <el-switch :model-value="scope.row.enabled === 1" @change="(v) => toggleStatus(scope.row, v)" :disabled="scope.row.role === 'admin'" />
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="margin-top:16px"
      background
      layout="prev, pager, next, total"
      :current-page="query.page"
      :page-size="query.size"
      :total="total"
      @current-change="(p)=>{query.page=p;loadData()}"
    />
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { listUsers, updateUserStatus } from '../../api/user'

const query = reactive({ page: 1, size: 10 })
const list = ref([])
const total = ref(0)

const loadData = async () => {
  const data = await listUsers(query)
  list.value = data.list
  total.value = data.total
}

const toggleStatus = async (row, enabled) => {
  await updateUserStatus(row.id, { enabled: enabled ? 1 : 0 })
  await loadData()
}

onMounted(loadData)
</script>
