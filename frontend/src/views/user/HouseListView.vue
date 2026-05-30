<template>
  <el-card>
    <el-form :inline="true" :model="query" @submit.prevent>
      <el-form-item label="区域"><el-input v-model="query.region" clearable /></el-form-item>
      <el-form-item label="户型"><el-input v-model="query.layout" clearable /></el-form-item>
      <el-form-item label="最低价"><el-input-number v-model="query.minPrice" :min="0" /></el-form-item>
      <el-form-item label="最高价"><el-input-number v-model="query.maxPrice" :min="0" /></el-form-item>
      <el-form-item label="关键字"><el-input v-model="query.keyword" clearable /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" style="width: 100%">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="region" label="区域" width="120" />
      <el-table-column prop="layout" label="户型" width="120" />
      <el-table-column prop="price" label="总价(万元)" width="120" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="primary" link @click="$router.push(`/houses/${scope.row.id}`)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="margin-top: 16px"
      background
      layout="prev, pager, next, total"
      :current-page="query.page"
      :page-size="query.size"
      :total="total"
      @current-change="(p) => { query.page = p; loadData() }"
    />
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { listHouses } from '../../api/house'

const query = reactive({ page: 1, size: 10, region: '', layout: '', minPrice: null, maxPrice: null, keyword: '' })
const list = ref([])
const total = ref(0)

const loadData = async () => {
  const data = await listHouses(query)
  list.value = data.list
  total.value = data.total
}

onMounted(loadData)
</script>
