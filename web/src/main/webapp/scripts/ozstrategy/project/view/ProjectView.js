/**
 * Created by lihao on 1/2/15.
 */
Ext.define('FlexCenter.project.view.ProjectView', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.projectView',

    requires: [
        'FlexCenter.project.store.Project',
        'FlexCenter.project.view.ProjectForm'
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
    title: projectRes.projectTitle,
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
                header: projectRes.header.serialNumber,
                dataIndex: 'serialNumber'
            },

            {
                header: projectRes.header.projectName,
                dataIndex: 'name'
            },
            {
                header: projectRes.header.activationCode,
                dataIndex: 'activationCode'
            },
            {
                header: projectRes.header.userCount,
                dataIndex: 'userCount'
            },
            {
                header: projectRes.header.projectDes,
                dataIndex: 'description'
            },
            {
                header: globalRes.header.createDate,
                dataIndex: 'createDate'
            },
            {
                xtype:'actioncolumn',
                header:projectRes.header.inProject,
                width:40,
                items:[
                    {
                        iconCls:'project-in',
                        tooltip:projectRes.header.inProject,
                        handler:function(grid, rowIndex, colIndex){
                            var rec = grid.getStore().getAt(rowIndex);
                            projectId=rec.get('id');
                            projectName=rec.get('name');
                            porjectActivationCode=rec.get('activationCode');
                            var pmb = Ext.create('Ext.ProgressBar', {
                                text:projectRes.inProjectLoading
                            });
                            var runnerProgress = new Ext.util.TaskRunner();
                            pmb.wait({
                                interval: 100,
                                duration: 1000,
                                increment: 10,
                                text: projectRes.inProjectLoading,
                                scope: this,
                                fn: function(){
                                    var flexCenterApp = new FlexCenter.App();
                                    var desktop=flexCenterApp.getDesktop();
                                    var projectViewport=Ext.ComponentQuery.query('#projectViewport')[0];
                                    projectViewport.removeAll();
                                    projectViewport.add(desktop);
                                    win.close();
                                }
                            });
                            var win=Ext.widget('window', {
                                width: 600,
                                height:80,
                                closable:false,
                                modal: true,
                                bodyPadding:10,
                                title:projectRes.inProjectLoading,
                                onCancel:function(){
                                    win.close();
                                    runnerProgress.stopAll();
                                    runnerProgress.destroy();
                                },
                                items:[
                                    pmb
                                ]
                            });
                            win.show();
                            
                            
                        }
                    }
                ]
            },
            {
                xtype:'actioncolumn',
                header:globalRes.buttons.edit,
                width:40,
                items:[
                    {
                        iconCls:'table-edit',
                        tooltip:globalRes.buttons.edit,
                        handler:function(grid, rowIndex, colIndex){
                            var rec = grid.getStore().getAt(rowIndex);
                            me.editClick(rec);
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
                me.editClick(record);
            }
        };
        me.callParent(arguments);
    },
    onAddClick:function(){
        var me=this;
        var edit = Ext.widget('projectForm', {
            title:globalRes.buttons.add
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
                title:globalRes.buttons.edit
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
                title: globalRes.buttons.edit,
                width: 300,
                msg: projectRes.alertUpdateProject,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },
    onDeleteClick: function (record) {
        var me = this;
        if (record) {
            Ext.Msg.confirm(globalRes.buttons.remove, Ext.String.format(projectRes.alertDeleteProject, record.data.name), function (txt) {
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
