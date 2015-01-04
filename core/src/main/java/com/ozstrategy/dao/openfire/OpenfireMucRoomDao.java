package com.ozstrategy.dao.openfire;

import com.ozstrategy.model.openfire.OpenfireMucRoom;

/**
 * Created by lihao on 1/2/15.
 */
public interface OpenfireMucRoomDao {
    void save(OpenfireMucRoom room);
    void update(OpenfireMucRoom room);
    void delete(Long id);
}
