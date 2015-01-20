/**
 * Created by lihao on 1/11/15.
 */
Ext.define('FlexCenter.export.store.MessageExport', {
    extend: 'Ext.data.Store',
    alias: 'store.appStores',
    requires: [
        'FlexCenter.export.model.MessageExport'
    ],
    model: 'FlexCenter.export.model.MessageExport',
    proxy: {
        type: 'ajax',
        url: 'messageExportController.do?method=list',
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