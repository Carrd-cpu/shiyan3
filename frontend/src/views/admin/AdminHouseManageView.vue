<template>
  <el-card>
    <el-form :inline="true" :model="query">
      <el-form-item label="关键字"><el-input v-model="query.keyword" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width:120px">
          <el-option label="待审核" value="PENDING" />
          <el-option label="已发布" value="APPROVED" />
          <el-option label="驳回" value="REJECTED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="success" @click="openDialog()">新增房源</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="region" label="区域" width="120" />
      <el-table-column prop="layout" label="户型" width="120" />
      <el-table-column prop="price" label="总价" width="120" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
          <el-button link type="danger" @click="remove(scope.row.id)">删除</el-button>
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

  <el-dialog v-model="visible" title="房源信息" width="600px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="小区"><el-input v-model="form.community" /></el-form-item>
      <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
      <el-form-item label="区域"><el-input v-model="form.region" /></el-form-item>
      <el-form-item label="户型"><el-input v-model="form.layout" /></el-form-item>
      <el-form-item label="面积"><el-input-number v-model="form.area" :min="0" /></el-form-item>
      <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" /></el-form-item>
      <el-form-item label="图片URL"><el-input v-model="form.imageUrl" /></el-form-item>
      <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" @click="save">保存（将变为待审核）</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { listAdminHouses, createHouse, updateHouse, deleteHouse } from '../../api/house'

const query = reactive({ page: 1, size: 10, keyword: '', status: '' })
const list = ref([])
const total = ref(0)
const visible = ref(false)
const form = reactive({ id: null, title: '', community: '', address: '', region: '', layout: '', area: 0, price: 0, imageUrl: '', description: '' })

const normalizeQueryParam = (value) => {
  if (value == null || (typeof value === 'string' && value.trim() === '')) return undefined
  return value
}

const loadData = async () => {
  try {
    const params = {
      page: query.page,
      size: query.size,
      keyword: normalizeQueryParam(query.keyword),
      status: normalizeQueryParam(query.status),
    }
    const data = await listAdminHouses(params)
    list.value = data?.list ?? []
    total.value = data?.total ?? 0
  } catch (error) {
    // Optional: useful for debugging empty page states caused by request errors.
    console.error('[AdminHouseManageView] loadData failed', error)
    list.value = []
    total.value = 0
  }
}

const search = () => {
  query.page = 1
  loadData()
}

const handlePageChange = (page) => {
  query.page = page
  loadData()
}

const openDialog = (row) => {
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, title: '', community: '', address: '', region: '', layout: '', area: 0, price: 0, imageUrl: '', description: '' })
  visible.value = true
}

const save = async () => {
  if (form.id) await updateHouse(form.id, form)
  else await createHouse(form)
  visible.value = false
  await loadData()
}

const remove = async (id) => {
  await deleteHouse(id)
  await loadData()
}

onMounted(loadData)
</script>
