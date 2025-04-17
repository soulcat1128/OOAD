package com.wangpeng.bms.mapper;

import com.wangpeng.bms.model.Appeal;

public interface AppealMapper {
    int deleteByPrimaryKey(Integer appealId);

    int insert(Appeal record);

    int insertSelective(Appeal record);

    Appeal selectByPrimaryKey(Integer appealId);

    int updateByPrimaryKeySelective(Appeal record);

    int updateByPrimaryKey(Appeal record);
}
