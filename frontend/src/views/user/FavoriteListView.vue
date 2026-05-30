<template>
  <el-card>
    <h3>我的收藏</h3>
    <el-table :data="list" style="width: 100%">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="region" label="区域" width="120" />
      <el-table-column prop="layout" label="户型" width="120" />
      <el-table-column prop="price" label="总价(万元)" width="140" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button type="primary" link @click="$router.push(`/houses/${scope.row.id}`)">详情</el-button>
          <el-button type="danger" link @click="cancel(scope.row.id)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listFavorites, removeFavorite } from '../../api/favorite'

const list = ref([])

const loadData = async () => {
  list.value = await listFavorites()
}

const cancel = async (id) => {
  await removeFavorite(id)
  await loadData()
}

onMounted(loadData)
</script>
