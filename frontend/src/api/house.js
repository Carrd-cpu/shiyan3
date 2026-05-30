import request from './request'

export const listHouses = (params) => request.get('/houses', { params })
export const getHouseDetail = (id) => request.get(`/houses/${id}`)

export const listAdminHouses = (params) => request.get('/admin/houses', { params })
export const createHouse = (data) => request.post('/admin/houses', data)
export const updateHouse = (id, data) => request.put(`/admin/houses/${id}`, data)
export const deleteHouse = (id) => request.delete(`/admin/houses/${id}`)

export const listReviews = (params) => request.get('/admin/reviews', { params })
export const reviewHouse = (id, data) => request.post(`/admin/reviews/${id}`, data)
