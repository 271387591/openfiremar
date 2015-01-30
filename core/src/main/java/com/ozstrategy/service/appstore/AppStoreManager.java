package com.ozstrategy.service.appstore;

import com.ozstrategy.model.appstore.AppStore;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/6/15.
 */
public interface AppStoreManager {
    List<AppStore> listAppStores(Map<String,Object> map,Integer start,Integer limit) throws Exception;
    Integer listAppStoresCount(Map<String,Object> map) throws Exception;
    AppStore getAppStoreById(Long id) throws Exception;
    void save(AppStore appStore) throws Exception;
    void update(AppStore appStore) throws Exception;
    void delete(Long id) throws Exception;
}
