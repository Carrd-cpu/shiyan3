package com.shiyan3.service;

import com.shiyan3.dao.FavoriteDao;
import com.shiyan3.dao.HouseDao;
import com.shiyan3.entity.House;

import java.util.List;

public class FavoriteService {
    private final FavoriteDao favoriteDao = new FavoriteDao();
    private final HouseDao houseDao = new HouseDao();

    public boolean isFavorite(int userId, int houseId) {
        return favoriteDao.exists(userId, houseId);
    }

    public boolean addFavorite(int userId, int houseId) {
        House house = houseDao.findById(houseId);
        if (house == null || !"APPROVED".equals(house.getStatus())) {
            return false;
        }
        if (favoriteDao.exists(userId, houseId)) {
            return true;
        }
        return favoriteDao.add(userId, houseId) > 0;
    }

    public boolean removeFavorite(int userId, int houseId) {
        return favoriteDao.remove(userId, houseId) > 0;
    }

    public List<House> listFavorites(int userId) {
        return favoriteDao.listByUserId(userId);
    }
}
