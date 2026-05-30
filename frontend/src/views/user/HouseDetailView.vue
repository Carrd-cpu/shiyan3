<template>
  <el-card v-if="detail">
    <h2>{{ detail.title }}</h2>
    <img v-if="detail.imageUrl" :src="detail.imageUrl" style="width: 360px; max-width:100%; margin-bottom:12px" />
    <p>小区：{{ detail.community }}</p>
    <p>地址：{{ detail.address }}</p>
    <p>区域：{{ detail.region }}</p>
    <p>户型：{{ detail.layout }}</p>
    <p>面积：{{ detail.area }} ㎡</p>
    <p>价格：{{ detail.price }} 万元</p>
    <p>描述：{{ detail.description }}</p>
    <el-button :type="favorite ? 'danger' : 'primary'" @click="toggleFavorite">
      {{ favorite ? '取消收藏' : '收藏' }}
    </el-button>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getHouseDetail } from '../../api/house'
import { addFavorite, removeFavorite, checkFavorite } from '../../api/favorite'
import { authState } from '../../store/auth'

const route = useRoute()
const detail = ref(null)
const favorite = ref(false)

const loadData = async () => {
  const id = route.params.id
  detail.value = await getHouseDetail(id)
  if (authState.user?.role === 'user') {
    const data = await checkFavorite(id)
    favorite.value = data.favorite
  }
}

const toggleFavorite = async () => {
  if (!authState.user || authState.user.role !== 'user') return
  const id = route.params.id
  if (favorite.value) {
    await removeFavorite(id)
    favorite.value = false
  } else {
    await addFavorite(id)
    favorite.value = true
  }
}

onMounted(loadData)
</script>
