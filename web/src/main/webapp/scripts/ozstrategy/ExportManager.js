/**
 * Created by lihao on 1/11/15.
 */
Ext.define('FlexCenter.ExportManager', {
    extend: 'Oz.desktop.Module',

    requires: [
        'Ext.grid.*',
        'Ext.data.*',
        'FlexCenter.export.view.MessageExportView',
        'Oz.access.RoleAccess'
    ],

    id: 'exportManager',
    moduleName: 'exportManager',

    init: function () {
        this.launcher = {
            text: '数据导出',
            iconCls: 'user-man16',
            handler: this.createWindow,
            scope: this
        };
    },

    createWindow: function () {
        var me=this;
        var desktop = this.app.getDesktop(), win,projectView;
        win = desktop.getWindow('myExportManager');
        projectView=Ext.widget('messageExportView',{
            itemId:'messageExportView'
        });
        if (!win) {
            win = desktop.createWindow({
                id: 'myExportManager',
                title: '数据导出',
                width: 900,
                height: 550,
                shim: false,
                resizable: false,
                maximizable: true,
                layout: 'fit',
                items: [
                    projectView
                ]
            });
        }
        win.show();
    }
});