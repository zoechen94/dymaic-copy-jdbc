package com.xsjt.dao;

import com.xsjt.bean.RoleDO;
import com.xsjt.dynamicDataSource.TargetDataSource;

import java.util.List;

public interface RoleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);


    List<RoleDO> listRole();
}