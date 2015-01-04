package com.ozstrategy.dao.openfire;

import com.ozstrategy.model.openfire.OpenfireUser;

/**
 * Created by lihao on 12/30/14.
 */
public interface OpenfireUserDao {
    void save(OpenfireUser user);
    void update(OpenfireUser user);
    
}
