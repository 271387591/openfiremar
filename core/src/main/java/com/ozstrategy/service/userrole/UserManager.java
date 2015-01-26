package com.ozstrategy.service.userrole;

import com.ozstrategy.model.userrole.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;


public interface UserManager extends UserDetailsService {
    List<User> listUsers(Map<String,Object> map, Integer start,Integer limit);
    List<User> listAllUsers(Map<String,Object> map);
    List<User> getUserByRoleId(Long roleId);
    Integer listUsersCount(Map<String,Object> map);
    void deleteUser(Long userId) throws Exception;
    Integer updateUserPassword(User user, String oldPassword, String newPassword, boolean admin) throws Exception;
    User getUserById(Long id);
    User getUserByUsername(String username,Long projectId);
    void saveOrUpdate(User user)throws Exception;
    void authorizationUser(User user);
    void updateUser(User user);


    Integer getUserCountByProjectId(Long projectId);
} 
