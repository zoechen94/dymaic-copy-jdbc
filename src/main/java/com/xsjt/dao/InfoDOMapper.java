package com.xsjt.dao;

import com.xsjt.bean.InfoDO;

import java.util.List;

public interface InfoDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InfoDO record);

    int insertSelective(InfoDO record);

    InfoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InfoDO record);

    int updateByPrimaryKey(InfoDO record);

    List<InfoDO> listInfo();
}