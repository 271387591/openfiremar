/**
 * Created by lihao on 1/2/15.
 */
Ext.define('FlexCenter.ProjectManager', {
    extend: 'Oz.desktop.Module',

    requires: [
        'Ext.grid.*',
        'Ext.data.*',
        'FlexCenter.project.view.ProjectView',
        'FlexCenter.project.view.ProjectUserView',
        'Oz.access.RoleAccess'
    ],

    id: 'projectManager',
    moduleName: 'projectManager',

    init: function () {
        this.launcher = {
            text: '工程管理',
            iconCls: 'user-man16',
            handler: this.createWindow,
            scope: this
        };
    },

    createWindow: function () {
        var me=this;
        var desktop = this.app.getDesktop(), win,projectView;
        win = desktop.getWindow('myProjectManager');
        projectView=Ext.widget('projectView',{
            itemId:'projectView',
            title:'工程列表'
        });
        this.mon(projectView,'addTab',function(rec){
            me.addPanel('projectUserView','project_'+rec.get('id'),{
                title:rec.get('name'),
                closable:true,
                itemId:'project_'+rec.get('id'),
                
                record:rec
            })
            
        });
        if (!win) {
            win = desktop.createWindow({
                id: 'myProjectManager',
                title: '工程管理',
                width: 900,
                height: 500,
                shim: false,
                border: false,
                resizable: false,
                maximizable: true,
                layout: 'fit',
                items: [
                    {
                        xtype: 'tabpanel',
                        plugins: Ext.create('Ext.ux.TabCloseMenu', {
                            closeTabText: '关闭当前',
                            closeOthersTabsText: '关闭其他',
                            closeAllTabsText: '关闭所有'
                        }),
                        itemId:'projectTab',
                        items: [
                            projectView
                        ]
                    }
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
    },
    addPanel: function (widget, itemId, config) {
        var me = this;
        var projectTab=Ext.ComponentQuery.query('#projectTab')[0];
        var panel = Ext.ComponentQuery.query('#' + itemId)[0];
        if (!panel) {
            if (config) {
                panel = Ext.widget(widget, config);
            } else {
                panel = Ext.widget(widget);
            }
            projectTab.add(panel);
        }
        projectTab.setActiveTab(panel);
    }


});