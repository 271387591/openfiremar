package com.ozstrategy.service.userrole;

import com.ozstrategy.model.userrole.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;


public interface UserManager extends UserDetailsService {
    List<User> listUsers(Map<String,Object> map, Integer start,Integer limit) throws Exception;
    List<User> listAllUsers(Map<String,Object> map) throws Exception;
    List<User> getUserByRoleId(Long roleId) throws Exception;
    Integer listUsersCount(Map<String,Object> map) throws Exception;
    void deleteUser(Long userId) throws Exception;
    Integer updateUserPassword(User user, String oldPassword, String newPassword, boolean admin) throws Exception;
    User getUserById(Long id) throws Exception;
    User getUserByUsername(String username,Long projectId) throws Exception;
    void saveOrUpdate(User user)throws Exception;
    void authorizationUser(User user) throws Exception;
    void updateUser(User user) throws Exception;


    Integer getUserCountByProjectId(Long projectId) throws Exception;
    
    User getUserByNickName(Long project,String nickName);
} 
