/**
 * Created by lihao on 1/7/15.
 */
Ext.define('FlexCenter.history.store.HistoryMessage', {
    extend: 'Ext.data.Store',
    alias: 'store.appStores',
    requires: [
        'FlexCenter.history.model.HistoryMessage'
    ],
    model: 'FlexCenter.history.model.HistoryMessage',
    proxy: {
        type: 'ajax',
        url: 'historyMessageController.do?method=list',
        
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total',
            messageProperty: 'message'
        },
        extraParams:{
            search:'true'
        },
        writer: {
            writeAllFields: false,
            root: 'data'
        }
    }
});