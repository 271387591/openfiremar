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
        url: 'historyMessageController.do?method=listStore',
        
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
        },
        listeners:{
            exception: function(proxy, response, operation) {
                var result = Ext.decode(response.responseText,true);
                Ext.MessageBox.show({
                        title: '提示',
                        msg: result.message,
                        icon: Ext.MessageBox.WARNING,
                        buttons: Ext.Msg.OK
                    }
                );
            }
        }
    }
});