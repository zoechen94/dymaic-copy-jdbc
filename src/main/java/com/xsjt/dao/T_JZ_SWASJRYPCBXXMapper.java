package com.xsjt.dao;

import com.xsjt.bean.T_JZ_SWASJRYPCBXX;

import java.util.List;

public interface T_JZ_SWASJRYPCBXXMapper {
    int deleteByPrimaryKey(String id);

    int insert(T_JZ_SWASJRYPCBXX record);

    int insertSelective(T_JZ_SWASJRYPCBXX record);

    T_JZ_SWASJRYPCBXX selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(T_JZ_SWASJRYPCBXX record);

    int updateByPrimaryKey(T_JZ_SWASJRYPCBXX record);

    List<T_JZ_SWASJRYPCBXX> listAll();
}