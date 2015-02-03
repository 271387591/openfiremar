/**
 * Created by lihao on 1/6/15.
 */
Ext.define('FlexCenter.AppStoreManager', {
    extend: 'Oz.desktop.Module',

    requires: [
        'Ext.grid.*',
        'Ext.data.*',
        'FlexCenter.appstore.view.AppStoreView',
        'Oz.access.RoleAccess'
    ],

    id: 'appStoreManager',
    moduleName: 'appStoreManager',

    init: function () {
        this.launcher = {
            text: appStoreRes.title,
            iconCls: 'appStore-shortcut-16',
            handler: this.createWindow,
            scope: this
        };
    },

    createWindow: function () {
        var me=this;
        var desktop = this.app.getDesktop(), win,projectView;
        win = desktop.getWindow('myAppStoreManager');
        projectView=Ext.widget('appStoreView',{
            itemId:'appStoreView'
        });
        if (!win) {
            win = desktop.createWindow({
                id: 'myAppStoreManager',
                title: appStoreRes.title,
                width: 900,
                height: 500,
                shim: false,
                //border: false,
                resizable: false,
                maximizable: true,
                layout: 'fit',
                items: [
                    projectView
                ],
                buttons: [
                    {
                        text: globalRes.buttons.close,
                        handler: function () {
                            win.close();
                        }
                    }
                ]
            });
        }
        win.show();
    }
});