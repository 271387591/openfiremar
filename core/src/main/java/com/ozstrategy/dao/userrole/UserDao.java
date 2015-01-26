package com.ozstrategy.dao.userrole;

import com.ozstrategy.dao.UniversalDao;
import com.ozstrategy.model.userrole.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface UserDao extends UniversalDao<User,Long>{

    List<User> listUsers(Map<String,Object> map, RowBounds rowBounds);
    Integer listUsersCount(Map<String,Object> map);
    User getUserById(Long id);
    User getUserByUsername(@Param("username")String username,@Param("projectId")Long projectId);
    Integer getUserCountByProjectId(Long projectId);
    List<User> getUserByRoleId(Long roleId);
} 
