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
                header: '平台',
                dataIndex: 'platform'
            },
            {
                header: '版本号',
                dataIndex: 'version'
            },
            {
                header: '下载地址',
                dataIndex: 'url',
                renderer: function (v) {
                    return '<a href="'+v+'" target="_blank">'+v+'</a>';
                }
            },
            {
                header: '版本简介',
                dataIndex: 'description'
            },
            {
                header: globalRes.header.createDate,
                dataIndex: 'createDate'
            },
            {
                xtype:'actioncolumn',
                header:'编辑',
                width:40,
                items:[
                    {
                        iconCls:'table-edit',
                        tooltip:'编辑',
                        handler:function(grid, rowIndex, colIndex){
                            var rec = grid.getStore().getAt(rowIndex);
                            me.onAddClick(rec);
                        }
                    }
                ]
            },
            {
                xtype:'actioncolumn',
                header:'删除',
                width:40,
                items:[
                    {
                        iconCls:'table-delete',
                        tooltip:'删除',
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
            title:(rec?'编辑':'添加')
        });
        edit.setActiveRecord(rec);
        edit.show();
        me.mon(edit, 'create', function (win, form) {
            var data=form.getValues();
            form.submit({
                url: 'appStoreController.do?method=save',
                waitMsg: '正在上传...',
                params:data,
                success: function(fp, o) {
                    Ext.Msg.alert('提示', '保存成功！',function(){
                        win.close();
                    });
                    me.getStore().load();
                },
                failure:function(){
                    Ext.Msg.alert('提示', '保存失败！');
                }
            });
            
            
        }, this);
    },
    
    onDeleteClick: function (record) {
        var me = this;
        if (record) {
            Ext.Msg.confirm('删除', Ext.String.format('确定要删除此版本：{0}', record.data.version), function (txt) {
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