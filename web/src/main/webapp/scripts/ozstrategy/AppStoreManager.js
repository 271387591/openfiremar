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
            text: '应用版本管理',
            iconCls: 'user-man16',
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
                title: '应用版本管理',
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