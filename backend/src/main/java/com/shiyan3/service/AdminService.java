package com.shiyan3.service;

import com.shiyan3.dao.HouseDao;
import com.shiyan3.dao.UserDao;
import com.shiyan3.entity.House;
import com.shiyan3.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService {
    private final HouseDao houseDao = new HouseDao();
    private final UserDao userDao = new UserDao();

    public int createHouse(House house) {
        return houseDao.create(house);
    }

    public int updateHouse(House house) {
        return houseDao.update(house);
    }

    public int deleteHouse(int id) {
        return houseDao.delete(id);
    }

    public Map<String, Object> listHouses(String keyword, String status, int page, int size) {
        int offset = (page - 1) * size;
        List<House> list = houseDao.listAdmin(keyword, status, offset, size);
        int total = houseDao.countAdmin(keyword, status);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    public Map<String, Object> listPending(int page, int size) {
        int offset = (page - 1) * size;
        List<House> list = houseDao.listPending(offset, size);
        int total = houseDao.countPending();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    public int audit(int houseId, String status, String reason) {
        return houseDao.audit(houseId, status, reason);
    }

    public Map<String, Object> listUsers(int page, int size) {
        int offset = (page - 1) * size;
        List<User> list = userDao.listUsers(offset, size);
        int total = userDao.countUsers();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    public int updateUserStatus(Integer id, Integer enabled) {
        return userDao.updateUserStatus(id, enabled);
    }
}
