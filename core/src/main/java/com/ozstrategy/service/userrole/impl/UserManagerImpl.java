package com.ozstrategy.service.userrole.impl;

import com.ozstrategy.Constants;
import com.ozstrategy.dao.openfire.OpenfireUserDao;
import com.ozstrategy.dao.userrole.UserDao;
import com.ozstrategy.dao.userrole.UserRoleDao;
import com.ozstrategy.exception.UserNotAuthenticationException;
import com.ozstrategy.exception.UserNotFoundException;
import com.ozstrategy.model.openfire.OpenfireUser;
import com.ozstrategy.model.userrole.Role;
import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.userrole.UserManager;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("userManager")
public class UserManagerImpl implements UserManager {

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private OpenfireUserDao openfireUserDao;
    
    
    

    public List<User> listUsers(Map<String, Object> map, Integer start, Integer limit) throws Exception {
        
        return userDao.listUsers(map,new RowBounds(start,limit));
    }

    public List<User> listAllUsers(Map<String, Object> map) throws Exception {
        return userDao.listUsers(map,RowBounds.DEFAULT);
    }

    public List<User> getUserByRoleId(Long roleId) throws Exception {
        return userDao.getUserByRoleId(roleId);
    }

    public Integer listUsersCount(Map<String, Object> map) throws Exception {
        return userDao.listUsersCount(map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteUser(Long userId) throws Exception{
    }

    @Transactional(rollbackFor = Throwable.class)
    public Integer updateUserPassword(User user, String oldPassword, String newPassword, boolean admin)  throws Exception{
        String password=user.getPassword();
        if(admin){
            user.setPassword(passwordEncoder.encodePassword(newPassword, null));
        }else{
            if(!passwordEncoder.isPasswordValid(password,oldPassword,null)){
                return 1;
            }
            user.setPassword(passwordEncoder.encodePassword(newPassword, null));
        }
        userDao.update(user);
        OpenfireUser openfireUser=new OpenfireUser().copy(user);
        openfireUserDao.update(openfireUser);
        return 0;
    }

    public User getUserById(Long id) throws Exception {
        return userDao.getUserById(id);
    }

    public User getUserByUsername(String username,Long projectId) throws Exception {
        return userDao.getUserByUsername(username,projectId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveOrUpdate(User user) throws Exception{
        boolean save=true;
        if(user.getId()!=null){
            userDao.update(user);
            save=false;
        }else{
            String password=user.getPassword();
            if(StringUtils.isNotEmpty(password)){
                user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
            }
            userDao.save(user);
        }
        Set<Role> roleSet=user.getRoles();
        if(roleSet!=null && roleSet.size()>0){
            if(!save){
                userRoleDao.removeUserRoleByUserId(user.getId());
            }
            for(Role role : roleSet){
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("userId",user.getId());
                map.put("roleId",role.getId());
                userRoleDao.saveUserRole(map);
            }
        }
        OpenfireUser openfireUser=new OpenfireUser();
        openfireUser=openfireUser.copy(user);
        if(!save){
            openfireUserDao.update(openfireUser);
        }else{
            openfireUserDao.save(openfireUser); 
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void authorizationUser(User user) throws Exception {
        userDao.update(user);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void updateUser(User user) throws Exception {
        userDao.update(user);
    }


    public Integer getUserCountByProjectId(Long projectId) throws Exception {
        return userDao.getUserCountByProjectId(projectId);
    }

    public User getUserByNickName(Long project, String nickName) {
        return userDao.getUserByNickName(project,nickName);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] usernames=username.split(",");
        if(usernames[1].equals("0")){
            usernames[1]=null;
        }
        Long projectId= usernames[1]!=null?Long.parseLong(usernames[1]):null;
        User user=userDao.getUserByUsername(usernames[0],projectId);
        if(user==null){
            throw new UserNotFoundException(Constants.USER_NOT_Found); 
        }
        if(!user.getAuthentication()){
            throw new UserNotAuthenticationException(Constants.USER_NOT_Authentication);
        }
        return user;
    }
} 
