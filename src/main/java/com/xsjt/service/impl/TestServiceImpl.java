package com.xsjt.service.impl;

import com.xsjt.bean.InfoDO;
import com.xsjt.bean.RoleDO;
import com.xsjt.bean.UserDO;
import com.xsjt.dao.InfoDOMapper;
import com.xsjt.dao.RoleDOMapper;
import com.xsjt.dao.UserDOMapper;
import com.xsjt.dynamicDataSource.TargetDataSource;
import com.xsjt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<UserDO> listUser() {
        return userDOMapper.listUser();
    }

    @TargetDataSource(name = "ds1")
    @Override
    public List<RoleDO> listRole() {
        return roleDOMapper.listRole();
    }

    @TargetDataSource(name = "ds2")
    @Override
    public List<InfoDO> listInfo() {
        return infoDOMapper.listInfo();
    }
}
