package com.ozstrategy.service.appstore;

import com.ozstrategy.model.appstore.AppStore;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/6/15.
 */
public interface AppStoreManager {
    List<AppStore> listAppStores(Map<String,Object> map,Integer start,Integer limit);
    Integer listAppStoresCount(Map<String,Object> map);
    AppStore getAppStoreById(Long id);
    void save(AppStore appStore);
    void update(AppStore appStore);
    void delete(Long id);
}
