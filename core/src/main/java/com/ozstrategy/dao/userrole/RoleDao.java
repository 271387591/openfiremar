package com.ozstrategy.dao.userrole;

import com.ozstrategy.dao.UniversalDao;
import com.ozstrategy.model.userrole.Role;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RoleDao extends UniversalDao<Role,Long>{
    List<Role> listRoles(Map<String,Object> map, RowBounds rowBounds);
    Integer listRolesCount(Map<String,Object> map);
    Role getRoleByName(String name);
    Role getRoleById(Long id);
    List<Role> getRoleByUserId(Long userId);
    
} 
