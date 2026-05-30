import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import UserLayout from '../layouts/UserLayout.vue'
import AdminLayout from '../layouts/AdminLayout.vue'
import HouseListView from '../views/user/HouseListView.vue'
import HouseDetailView from '../views/user/HouseDetailView.vue'
import FavoriteListView from '../views/user/FavoriteListView.vue'
import ProfileView from '../views/user/ProfileView.vue'
import AdminHouseManageView from '../views/admin/AdminHouseManageView.vue'
import AdminReviewView from '../views/admin/AdminReviewView.vue'
import AdminUserView from '../views/admin/AdminUserView.vue'
import { authState, fetchMe } from '../store/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginView },
    {
      path: '/',
      component: UserLayout,
      children: [
        { path: '', redirect: '/houses' },
        { path: 'houses', component: HouseListView },
        { path: 'houses/:id', component: HouseDetailView },
        { path: 'favorites', component: FavoriteListView },
        { path: 'profile', component: ProfileView },
      ],
      meta: { role: 'user' },
    },
    {
      path: '/admin',
      component: AdminLayout,
      children: [
        { path: '', redirect: '/admin/houses' },
        { path: 'houses', component: AdminHouseManageView },
        { path: 'reviews', component: AdminReviewView },
        { path: 'users', component: AdminUserView },
      ],
      meta: { role: 'admin' },
    },
  ],
})

router.beforeEach(async (to) => {
  if (!authState.loaded) {
    await fetchMe()
  }
  if (to.path === '/login') {
    if (authState.user?.role === 'admin') return '/admin/houses'
    if (authState.user?.role === 'user') return '/houses'
    return true
  }

  if (!authState.user) return '/login'
  if (to.meta.role && authState.user.role !== to.meta.role) {
    return authState.user.role === 'admin' ? '/admin/houses' : '/houses'
  }
  return true
})

export default router
