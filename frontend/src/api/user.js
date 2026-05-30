import request from './request'

export const getProfile = () => request.get('/users/profile')
export const updateProfile = (data) => request.put('/users/profile', data)

export const listUsers = (params) => request.get('/admin/users', { params })
export const updateUserStatus = (id, data) => request.put(`/admin/users/${id}/status`, data)
