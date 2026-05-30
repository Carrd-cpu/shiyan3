import request from './request'

export const listFavorites = () => request.get('/favorites')
export const addFavorite = (houseId) => request.post(`/favorites/${houseId}`)
export const removeFavorite = (houseId) => request.delete(`/favorites/${houseId}`)
export const checkFavorite = (houseId) => request.get(`/favorites/check/${houseId}`)
