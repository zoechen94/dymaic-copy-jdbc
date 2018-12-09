package com.xsjt.service;

import com.xsjt.bean.InfoDO;
import com.xsjt.bean.RoleDO;
import com.xsjt.bean.UserDO;

import java.util.List;

/**
 * @author: 喜🐑
 * @create: 2018-12-09 15:06
 */

public interface TestService {
    List<UserDO> listUser();
    List<RoleDO> listRole();
    List<InfoDO> listInfo();
 }
