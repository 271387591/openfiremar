package com.ozstrategy.dao.appstore;

import com.ozstrategy.model.appstore.AppStore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/6/15.
 */
public interface AppStoreDao {
    List<AppStore> listAppStores(Map<String,Object> map,RowBounds rowBounds);
    Integer listAppStoresCount(Map<String,Object> map);
    AppStore getAppStoreById(Long id);
    void save(AppStore appStore);
    void delete(Long id);
    void update(AppStore appStore);
    AppStore getCurrent(@Param("platForm")String platForm);
}
