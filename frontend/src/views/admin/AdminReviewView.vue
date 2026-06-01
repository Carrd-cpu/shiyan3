<template>
  <el-card>
    <h3>待审核房源</h3>
    <el-table :data="list">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="community" label="小区" />
      <el-table-column prop="region" label="区域" width="120" />
      <el-table-column prop="price" label="总价" width="120" />
      <el-table-column label="操作" width="280">
        <template #default="scope">
          <el-button type="success" link @click="approve(scope.row.id)">通过</el-button>
          <el-button type="danger" link @click="reject(scope.row.id)">驳回</el-button>
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
      @current-change="handlePageChange"
    />
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { listReviews, reviewHouse } from '../../api/house'

const query = reactive({ page: 1, size: 10 })
const list = ref([])
const total = ref(0)

const loadData = async () => {
  try {
    const data = await listReviews(query)
    list.value = data?.list ?? []
    total.value = data?.total ?? 0
  } catch {
    list.value = []
    total.value = 0
  }
}

const handlePageChange = (page) => {
  query.page = page
  loadData()
}

const approve = async (id) => {
  await reviewHouse(id, { action: 'APPROVED' })
  await loadData()
}

const reject = async (id) => {
  const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回房源', { inputValue: '' })
  await reviewHouse(id, { action: 'REJECTED', reason: value })
  await loadData()
}

onMounted(loadData)
</script>
