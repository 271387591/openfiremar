package com.ozstrategy.dao.userrole;

import com.ozstrategy.model.userrole.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RoleDao{
    List<Role> listRoles(Map<String,Object> map, RowBounds rowBounds);
    Integer listRolesCount(Map<String,Object> map);
    Role getRoleByName(@Param("name")String name,@Param("projectId")Long projectId);
    Role getRoleById(Long id);
    List<Role> getRoleByUserId(Long userId);
    int save(Role role);
    int update(Role role);
    int delete(Role role);
    
} 
