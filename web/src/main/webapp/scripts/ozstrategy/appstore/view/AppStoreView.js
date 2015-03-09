/**
 * Created by lihao on 1/6/15.
 */
Ext.define('FlexCenter.appstore.view.AppStoreView', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.appStoreView',

    requires: [
        'FlexCenter.appstore.store.AppStore',
        'FlexCenter.appstore.view.AppStoreForm'
    ],

    getAppStoreStore: function () {
        var store = store = Ext.create('FlexCenter.appstore.store.AppStore', {
            storeId: 'AppStoreStore'
        });
        store.load();
        return store;
    },
    border: false,
    forceFit: true,
    autoScroll: true,

    initComponent: function () {
        var me=this;
        me.store=me.getAppStoreStore();
        me.dockedItems = [{
            xtype: 'pagingtoolbar',
            store: me.store,
            dock: 'bottom',
            displayInfo: true
        }];
        me.tbar=[
            {
                frame: true,
                iconCls: 'table-add',
                xtype: 'button',
                text: globalRes.buttons.add,
                scope: this,
                handler: function(){
                    me.onAddClick(null);
                }
            }
        ];
        me.columns=[
            {xtype: 'rownumberer'},
            {
                header: appStoreRes.header.platform,
                dataIndex: 'platform'
            },
            {
                header: appStoreRes.header.version,
                dataIndex: 'version'
            },
            {
                header: '最新版本',
                dataIndex: 'currentVersion',
                renderer: function (v) {
                    return v?'是':'否';
                }
            },
            
            {
                header: appStoreRes.header.url,
                dataIndex: 'url',
                renderer: function (v) {
                    return '<a href="'+v+'" target="_blank">'+v+'</a>';
                }
            },
            {
                header: appStoreRes.header.description,
                dataIndex: 'description'
            },
            {
                header: globalRes.header.createDate,
                dataIndex: 'createDate'
            },
            {
                xtype:'actioncolumn',
                header:globalRes.buttons.edit,
                hidden:true,
                width:40,
                items:[
                    {
                        iconCls:'table-edit',
                        tooltip:globalRes.buttons.edit,
                        handler:function(grid, rowIndex, colIndex){
                            var rec = grid.getStore().getAt(rowIndex);
                            me.onAddClick(rec);
                        }
                    }
                ]
            },
            {
                xtype:'actioncolumn',
                header:globalRes.buttons.remove,
                width:40,
                items:[
                    {
                        iconCls:'table-delete',
                        tooltip:globalRes.buttons.remove,
                        handler:function(grid, rowIndex, colIndex){
                            var rec = grid.getStore().getAt(rowIndex);
                            me.onDeleteClick(rec);
                        }
                    }
                ]
            }
        ];
        me.listeners={
            itemdblclick: function (view, record, item, index, e, eOpts) {
                me.onAddClick(record);
            }
        };
        me.callParent(arguments);
    },
    onAddClick:function(rec){
        var me=this;
        var edit = Ext.widget('appStoreForm', {
            title:(rec?globalRes.buttons.edit:globalRes.buttons.add)
        });
        edit.setActiveRecord(rec);
        edit.show();
        me.mon(edit, 'create', function (win, form) {
            var data=form.getValues();
            form.submit({
                url: 'appStoreController.do?method=save',
                waitMsg: globalRes.buttons.fileLoading,
                params:data,
                success: function(fp, o) {
                    Ext.Msg.alert(globalRes.title.prompt, globalRes.addSuccess,function(){
                        win.close();
                    });
                    me.getStore().load();
                },
                failure:function(){
                    Ext.Msg.alert(globalRes.title.prompt, globalRes.addFail);
                }
            });
            
            
        }, this);
    },
    
    onDeleteClick: function (record) {
        var me = this;
        if (record) {
            Ext.Msg.confirm(globalRes.buttons.remove, Ext.String.format(appStoreRes.removeAlert, record.data.version), function (txt) {
                if (txt === 'yes') {
                    Ext.Ajax.request({
                        url: 'appStoreController.do?method=delete',
                        params: {id: record.data.id},
                        method: 'POST',
                        success: function (response, options) {
                            var result = Ext.decode(response.responseText);
                            if (result.success) {
                                Ext.Msg.alert(globalRes.title.prompt, globalRes.removeSuccess);
                                me.getStore().load();
                            } else {
                                Ext.MessageBox.show({
                                    title: globalRes.title.prompt,
                                    width: 300,
                                    msg: result.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.ERROR
                                });

                            }
                        },
                        failure: function (response, options) {
                            Ext.MessageBox.alert(globalRes.title.fail, Ext.String.format(globalRes.remoteTimeout, response.status));
                        }
                    });
                }
            });
        } else {
            Ext.MessageBox.show({
                title: userRoleRes.removeRole,
                width: 300,
                msg: userRoleRes.editRole,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    }
});