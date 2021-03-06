/**
 * Created by lihao on 1/7/15.
 */
Ext.define('FlexCenter.HistoryMessageManager', {
    extend:'Oz.desktop.Module',

    requires:[
        'FlexCenter.history.view.HistoryMessageView'
    ],

    id:'historyMessageManager',
    moduleName:'historyMessageManager',

    init:function () {
        this.launcher = {
            text:projectRes.searchTitle,
            iconCls:'search-shortcut-16',
            handler:this.createWindow,
            scope:this
        };
    },

    createWindow:function () {
        var desktop = this.app.getDesktop(), win;
        win = desktop.getWindow('myHistoryMessageManager');
        if (!win) {
            win = desktop.createWindow({
                id:'myHistoryMessageManager',
                title:projectRes.searchTitle,
                shim:false,
                border:false,
                maximized: true,
                maximizable:false,
                layout:'fit',
                items:[
                    {
                        xtype:'historyMessageView'
                        
                    }
                ]
            });
        }
        win.show();
    }
});