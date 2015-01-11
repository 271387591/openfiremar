/**
 * Created by lihao on 1/2/15.
 */
Ext.define('FlexCenter.project.view.ProjectView', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.projectView',

    requires: [
        'FlexCenter.project.store.Project',
        'FlexCenter.project.view.ProjectForm',
        'FlexCenter.project.view.ProjectUserView'
    ],

    getProjectStore: function () {
        var store = store = Ext.create('FlexCenter.project.store.Project', {
            storeId: 'ProjectStore'
        });
        store.load();
        return store;
    },
    border: false,
    forceFit: true,
    autoScroll: true,

    initComponent: function () {
        var me=this;
        
        me.store=me.getProjectStore();
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
                handler: this.onAddClick
            }
            //{
            //    frame: true,
            //    iconCls: 'user-edit',
            //    xtype: 'button',
            //    text: globalRes.buttons.edit,
            //    itemId: 'userEditBtn',
            //    scope: this,
            //    handler: this.editClick
            //},
            //{
            //    frame: true,
            //    iconCls: 'user-edit',
            //    xtype: 'button',
            //    text: '进入',
            //    scope: this,
            //    handler: function(){
            //        var selection = me.getView().getSelectionModel().getSelection()[0];
            //        me.fireEvent('addTab', selection);
            //    }
            //},
            
        ];
        me.features=[{
            ftype: 'search',
            disableIndexes: ['id', 'serialNumber', 'activationCode', 'peoples', 'createDate','users','description'],
            paramNames: {
                fields: 'fields',
                query: 'keyword'
            },
            searchMode: 'remote'
        }];
        me.columns=[
            {xtype: 'rownumberer'},
            {
                header: '工程编号',
                dataIndex: 'serialNumber'
            },

            {
                header: '工程名称',
                dataIndex: 'name'
            },
            {
                header: '激活码',
                dataIndex: 'activationCode'
            },
            {
                header: '工程人数',
                dataIndex: 'users',
                renderer: function (v) {
                    if(v){
                        return v.length;
                    }
                    return 0;
                }
            },
            {
                header: '工程简介',
                dataIndex: 'description'
            },
            {
                header: globalRes.header.createDate,
                dataIndex: 'createDate'
            },
            {
                xtype:'actioncolumn',
                header:'进入',
                width:40,
                items:[
                    {
                        iconCls:'project-in',
                        tooltip:'进入',
                        handler:function(grid, rowIndex, colIndex){
                            var rec = grid.getStore().getAt(rowIndex);
                            me.fireEvent('addTab', rec);
                        }
                    }
                ]
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
                            me.editClick(rec);
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
                me.editClick(record);
            }
        };
        me.callParent(arguments);
    },
    onAddClick:function(){
        var me=this;
        var edit = Ext.widget('projectForm', {
            title:'添加工程'
        });
        edit.setActiveRecord(null);
        edit.show();
        me.mon(edit, 'create', function (win, data) {
            Ext.Ajax.request({
                url: 'projectController.do?method=saveProject',
                params: data,
                method: 'POST',
                success: function (response, options) {
                    var result = Ext.decode(response.responseText);
                    if (result.success) {
                        me.getStore().load();
                        me.editWin = win;
                        Ext.Msg.alert(globalRes.title.prompt, globalRes.addSuccess, function () {
                            me.editWin.close();
                        });
                    } else {
                        if (result.message) {
                            Ext.MessageBox.show({
                                title: globalRes.title.prompt,
                                width: 300,
                                msg: result.message,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.ERROR
                            });
                        }
                    }
                },
                failure: function (response, options) {
                    Ext.MessageBox.alert(globalRes.title.fail, Ext.String.format(globalRes.remoteTimeout, response.status));
                }
            });
        }, this);
        
        
        
        //ajaxPostRequest('userController.do?method=listAvailableUsers',{},function(result){
        //    if(result.success){
        //        var data=result.data;
        //        var availableUserStore=Ext.create('FlexCenter.user.store.Users',{
        //            data:data,
        //            proxy: {
        //                type: 'memory',
        //                reader: {
        //                    type: 'json',
        //                    root: 'data',
        //                    totalProperty: 'total',
        //                    messageProperty: 'message'
        //                }
        //            }
        //        });
        //    }
        //});
    },
    editClick: function (selection) {
        var me = this;
        if (selection) {
            var edit = Ext.widget('projectForm', {
                title:'编辑工程'
            });
            edit.setActiveRecord(selection);
            edit.show();
            me.mon(edit, 'update', function (win, data) {
                Ext.Ajax.request({
                    url: 'projectController.do?method=updateProject',
                    params: data,
                    method: 'POST',
                    success: function (response, options) {
                        var result = Ext.decode(response.responseText);
                        if (result.success) {
                            me.getStore().load();
                            me.editWin = win;
                            Ext.Msg.alert(globalRes.title.prompt, globalRes.updateSuccess, function () {
                                me.editWin.close();
                            });
                        } else {
                            if (result.message) {
                                Ext.MessageBox.show({
                                    title: globalRes.title.prompt,
                                    width: 300,
                                    msg: result.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.INFO
                                });
                            }
                        }
                    },
                    failure: function (response, options) {
                        Ext.MessageBox.alert(globalRes.title.fail, Ext.String.format(globalRes.remoteTimeout, response.status));
                    }
                });
            });
        }
        else {
            Ext.MessageBox.show({
                title: '编辑工程',
                width: 300,
                msg: '请选择要编辑的工程',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },
    onDeleteClick: function (record) {
        var me = this;
        if (record) {
            Ext.Msg.confirm('删除', Ext.String.format('确定要删除工程：{0}', record.data.name), function (txt) {
                if (txt === 'yes') {
                    Ext.Ajax.request({
                        url: 'projectController.do?method=deleteProject',
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
