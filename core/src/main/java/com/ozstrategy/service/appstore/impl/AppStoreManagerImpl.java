package com.ozstrategy.service.appstore.impl;

import com.ozstrategy.dao.appstore.AppStoreDao;
import com.ozstrategy.model.appstore.AppStore;
import com.ozstrategy.service.appstore.AppStoreManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/6/15.
 */
@Service("appStoreManager")
public class AppStoreManagerImpl implements AppStoreManager {
    @Autowired
    private AppStoreDao appStoreDao;
    public List<AppStore> listAppStores(Map<String, Object> map, Integer start, Integer limit) {
        return appStoreDao.listAppStores(map,new RowBounds(start,limit));
    }

    public Integer listAppStoresCount(Map<String, Object> map) {
        return appStoreDao.listAppStoresCount(map);
    }

    public AppStore getAppStoreById(Long id) {
        return appStoreDao.getAppStoreById(id);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void save(AppStore appStore) {
        appStoreDao.save(appStore);
        appStore.setUrl(MessageFormat.format(appStore.getUrl(),appStore.getId()));
        appStoreDao.update(appStore);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void update(AppStore appStore) {
        appStoreDao.update(appStore);

    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
        appStoreDao.delete(id);
    }
}
