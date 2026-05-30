package com.shiyan3.service;

import com.shiyan3.dao.HouseDao;
import com.shiyan3.entity.House;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseService {
    private final HouseDao houseDao = new HouseDao();

    public Map<String, Object> listApproved(String keyword, String region, String layout, Double minPrice, Double maxPrice, int page, int size) {
        int offset = (page - 1) * size;
        List<House> list = houseDao.listApproved(keyword, region, layout, minPrice, maxPrice, offset, size);
        int total = houseDao.countApproved(keyword, region, layout, minPrice, maxPrice);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    public House getById(int id) {
        return houseDao.findById(id);
    }
}
