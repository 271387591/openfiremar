/**
 * Created by lihao on 1/6/15.
 */
Ext.define('FlexCenter.appstore.store.AppStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appStores',
    requires: [
        'FlexCenter.appstore.model.AppStore'
    ],
    model: 'FlexCenter.appstore.model.AppStore',
    proxy: {
        type: 'ajax',
        url: 'appStoreController.do?method=listAppStores',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total',
            messageProperty: 'message'
        },
        writer: {
            writeAllFields: false,
            root: 'data'
        }
    }
});