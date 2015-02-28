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
    public List<AppStore> listAppStores(Map<String, Object> map, Integer start, Integer limit) throws Exception {
        return appStoreDao.listAppStores(map,new RowBounds(start,limit));
    }

    public Integer listAppStoresCount(Map<String, Object> map) throws Exception {
        return appStoreDao.listAppStoresCount(map);
    }

    public AppStore getAppStoreById(Long id) throws Exception {
        return appStoreDao.getAppStoreById(id);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void save(AppStore appStore) throws Exception {
        appStoreDao.save(appStore);
        appStore.setUrl(MessageFormat.format(appStore.getUrl(),appStore.getId()));
        appStoreDao.update(appStore);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void update(AppStore appStore) throws Exception {
        appStoreDao.update(appStore);

    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) throws Exception {
        appStoreDao.delete(id);
    }

    public AppStore getCurrent(String platForm) {
        return appStoreDao.getCurrent(platForm);
    }
}
