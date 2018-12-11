package com.xsjt.service.impl;

import com.xsjt.bean.InfoDO;
import com.xsjt.bean.RoleDO;
import com.xsjt.bean.T_JZ_SWASJRYPCBXX;
import com.xsjt.bean.UserDO;
import com.xsjt.dao.InfoDOMapper;
import com.xsjt.dao.RoleDOMapper;
import com.xsjt.dao.T_JZ_SWASJRYPCBXXMapper;
import com.xsjt.dao.UserDOMapper;
import com.xsjt.dynamicDataSource.DynamicDataSource;
import com.xsjt.dynamicDataSource.DynamicDataSourceContextHolder;
import com.xsjt.dynamicDataSource.TargetDataSource;
import com.xsjt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 喜🐑
 * @create: 2018-12-09 15:09
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private InfoDOMapper infoDOMapper;
    @Autowired
    private RoleDOMapper roleDOMapper;
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private T_JZ_SWASJRYPCBXXMapper t_jz_swasjrypcbxxMapper;

    @Override
    public List<UserDO> listUser() {
        return userDOMapper.listUser();
    }


    @Override
    public List<RoleDO> listRole() {
        DynamicDataSourceContextHolder.setDataSourceType("ds1");
        return roleDOMapper.listRole();
    }

    @TargetDataSource(name = "ds2")
    @Override
    public List<InfoDO> listInfo() {
        return infoDOMapper.listInfo();
    }

    @TargetDataSource(name = "ds3")
    @Override
    public List<T_JZ_SWASJRYPCBXX> listOracleData() {
        return t_jz_swasjrypcbxxMapper.listAll();
    }

    @Override
    public Map<String, Object> getDs1AndDs3() {
        Map<String,Object> map=new HashMap<>();
        map.put("master",userDOMapper.listUser());
        DynamicDataSourceContextHolder.setDataSourceType("ds3");
        map.put("oracle",t_jz_swasjrypcbxxMapper.listAll());
        return map;
    }
}
