/**
 * Created by IntelliJ IDEA.
 * User: kangpan
 * Date: 1/25/13
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */


Ext.define('FlexCenter.user.view.UserView', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.userView',

    requires: [
        'Ext.toolbar.Paging',
        'Ext.toolbar.TextItem',
        'FlexCenter.user.store.Users',
        'FlexCenter.project.store.Project',
        'Ext.ux.grid.feature.Detail',
        'FlexCenter.user.view.UserForm'
    ],

    getStore: function () {
        var store  = Ext.create('FlexCenter.user.store.Users', {
            storeId: 'userStore',
            pageSize: 25
        });
        store.getProxy().extraParams={projectId:projectId};
        return store;
    },
    border: false,
    forceFit: true,
    autoScroll: true,

    initComponent: function () {
        var me = this;
        var userStore = me.getStore();
        userStore.load();
        me.tbar=[
            {
                frame: true,
                iconCls: 'user-add',
                xtype: 'button',
                text: globalRes.buttons.add,
                scope: this,
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'addUser',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                handler: this.onAddClick
            },
            {
                frame: true,
                iconCls: 'user-edit',
                xtype: 'button',
                text: globalRes.buttons.edit,
                itemId: 'userEditBtn',
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'updateUser',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                scope: this,
                handler: this.onEditClick
            },
            {
                frame: true,
                iconCls: 'user-delete',
                xtype: 'button',
                text: '删除',
                itemId: 'userDeleteBtn',
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'updateUser',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                scope: this,
                handler: this.onDeleteClick
            },
            
            {
                iconCls: 'user-edit',
                text: userRoleRes.passwordTilte,
                scope: this,
                itemId: 'userPasswordTilteBtn',
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'updateUserPassword',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                handler: this.onChangePasswordClick
            },
            
            {
                iconCls: 'btn-authorization',
                text: userRoleRes.authenticationUser,
                scope: this,
                itemId: 'authorizationBtn',
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'updateUserPassword',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                handler: this.authorization

            },
            {
                iconCls: 'user-delete',
                itemId: 'lockUserBtn',
                text: userRoleRes.lockUser,
                scope: this,
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'lockUser',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                handler: this.onLockUserClick
            },
            {
                iconCls: 'user-edit',
                itemId: 'unLockUserBtn',
                text: userRoleRes.unLockUser,
                plugins: Ext.create('Oz.access.RoleAccess', {
                    featureName: 'unLockUser',
                    mode: 'hide',
                    byPass: globalRes.isAdmin
                }),
                scope: this,
                handler: this.onUnLockUserClick
            }
        ];
        me.plugins=[
            Ext.create('Oz.access.RoleAccess', {
                featureName: 'updateUser',
                mode: 'hide',
                byPass: globalRes.isAdmin
            })
        ];
        me.features=[{
            ftype: 'search',
            disableIndexes: ['id', 'defaultRoleDisplayName', 'accountLocked', 'enabled', 'createDate','authentication'],
            paramNames: {
                fields: 'fields',
                query: 'keyword'
            },
            searchMode: 'remote'
        }, {
            id: 'detail',
            ftype: 'detail',
            tplDetail: [
                '<tpl for=".">',
                userRoleRes.header.nickName + ' : <b>{nickName}</b><br/>',
                userRoleRes.userRoles + ' : <b><tpl for="simpleRoles"><tpl if="xindex != 1">, </tpl>{#}. {displayName}</tpl></b><br/>',
                globalRes.header.createDate + ' : <b>{createDate}</b><br/>',
                '</tpl>'
            ]
        }];
        me.columns=[
            {xtype: 'rownumberer'},
            {
                header: userRoleRes.header.nickName,
                dataIndex: 'nickName'
            },

            {
                header: userRoleRes.header.username,
                dataIndex: 'username'
            },
            {
                header: userRoleRes.header.accountLocked,
                dataIndex: 'accountLocked',
                renderer: function (v) {
                    return v ? globalRes.yes : globalRes.no;
                }
            },
            {
                header: userRoleRes.header.authentication,
                dataIndex: 'authentication',
                renderer: function (v) {
                    return v ? globalRes.yes : globalRes.no;
                }
            },
            {
                header: globalRes.header.createDate,
                dataIndex: 'createDate'
            }
        ];
        me.viewConfig= {
            getRowClass: function (record) {
                if (!record.get('enabled')) {
                    return 'error-row';
                }
                if (record.get('accountLocked')) {
                    return 'locked-row';
                }
            }
        };
        me.listeners={
            itemdblclick: function (view, record, item, index, e, eOpts) {
                me.onEditClick();
            },
            itemclick:function (view, record, item, index, e, eOpts) {
                var auth=record.get('authentication');
                if(auth){
                    me.down('#authorizationBtn').setDisabled(true);
                }else{
                    me.down('#authorizationBtn').setDisabled(false);
                }
                var accountLocked=record.get('accountLocked');
                if(accountLocked){
                    me.down('#lockUserBtn').setDisabled(true);
                    me.down('#unLockUserBtn').setDisabled(false);
                }else{
                    me.down('#unLockUserBtn').setDisabled(true);
                    me.down('#lockUserBtn').setDisabled(false);
                }
                
            }
        };
        me.store= userStore;
        me.dockedItems = [{
            xtype: 'pagingtoolbar',
            store: userStore,
            dock: 'bottom',
            displayInfo: true
        }];
        this.callParent();
    },
    onDeleteClick:function(){
        var me=this;
        var selection = me.getSelectionModel().getSelection()[0];
        if (selection) {
            var userId = selection.data.id;
            var nickName = selection.data.nickName;
            Ext.MessageBox.show({
                title: '删除用户',
                width: 400,
                msg: '你确定要删除用户['+nickName+']？',
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        Ext.Ajax.request({
                            url: 'userController.do?method=deleteUser',
                            params: {id: userId},
                            method: 'POST',
                            success: function (response, options) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.Msg.alert(globalRes.title.prompt, globalRes.removeSuccess);
                                    me.store.load();
                                }
                                else {
                                    Ext.MessageBox.show({
                                        title: title,
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
                }
            });
        }
        else {
            Ext.MessageBox.show({
                title: '删除用户',
                width: 300,
                msg: '请选择要删除的用户',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
        
        
    },
    onAddClick: function () {
        var me = this;
        ajaxPostRequest('userRoleController.do?method=readAvailableRoles', {projectId:projectId}, function (result) {
            if (result.success) {
                var availableRoleStore = Ext.create('FlexCenter.user.store.Roles', {
                    storeId: 'availableRoleStore',
                    data: result.data,
                    proxy: {
                        type: 'memory',
                        reader: {
                            type: 'json',
                            root: 'data',
                            totalProperty: 'total',
                            messageProperty: 'message'
                        }
                    }
                });
                me.addClick(availableRoleStore);
            }
        });
    },
    addClick: function (availableRoleStore) {

        var me = this;
        var edit = Ext.widget('userForm', {
            isAdmin: true,
            availableRoleStore: availableRoleStore
        }).show();
        edit.setActiveRecord(null);
        this.mon(edit, 'create', function (win, data) {
            Ext.Ajax.request({
                url: 'userController.do?method=saveUser',
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
    },

    onDblClick: function (view, record, item, index, e) {
        var edit, me = this;
        edit = Ext.widget('userForm', {
            isEdit: false
        }).show();
        edit.setActiveRecord(record);
        this.mon(edit, 'update', function (win, form, active) {
            var fields = active.fields,
                values = form.getValues(),
                name,
                obj = {};

            fields.each(function (f) {
                name = f.name;
                if (name in values) {
                    obj[name] = values[name];
                }
            });
            active.beginEdit();
            active.set(obj);
            active.endEdit();
            me.editWin = win;
            me.editWin.close();
        });
    },
    onEditClick: function () {
        var me = this;
        var selection = me.getSelectionModel().getSelection()[0];
        if(selection){
            selection=me.store.getById(selection.get('id'));
        }else{
            Ext.MessageBox.show({
                title: userRoleRes.editUser,
                width: 300,
                msg: userRoleRes.msg.editUser,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
        
        ajaxPostRequest('userRoleController.do?method=readAvailableRoles', {projectId:projectId}, function (result) {
            if (result.success) {
                var availableRoleStore = Ext.create('FlexCenter.user.store.Roles', {
                    storeId: 'availableRoleStore',
                    data: result,
                    proxy: {
                        type: 'memory',
                        reader: {
                            type: 'json',
                            root: 'data',
                            totalProperty: 'total',
                            messageProperty: 'message'
                        }
                    }
                });
                me.editClick(availableRoleStore,selection);
            }
        });
    },

    editClick: function (availableRoleStore,selection) {
        var me = this;
        
        if (selection) {
            var edit;
            edit = Ext.widget('userForm', {
                isEdit: true,
                availableRoleStore: availableRoleStore
            }).show();
            edit.setActiveRecord(selection);
            this.mon(edit, 'update', function (win, data) {
                Ext.Ajax.request({
                    url: 'userController.do?method=updateUser',
                    params: data,
                    method: 'POST',
                    success: function (response, options) {
                        var result = Ext.decode(response.responseText);
                        if (result.success) {
                            me.editWin = win;
                            Ext.Msg.alert(globalRes.title.prompt, globalRes.updateSuccess, function () {
                                me.editWin.close();
                                me.store.loadPage(1);
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
                title: userRoleRes.editUser,
                width: 300,
                msg: userRoleRes.msg.editUser,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },
    authorization:function(){
        var me=this;
        var record = me.getView().getSelectionModel().getSelection()[0];
        if (record) {
            var userId = record.data.id;
            var nickName = record.data.nickName;
            Ext.MessageBox.show({
                title: '认证用户',
                width: 400,
                msg: '你确定要认证用户['+nickName+']？',
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        Ext.Ajax.request({
                            url: 'userController.do?method=authorizationUser',
                            params: {id: userId},
                            method: 'POST',
                            success: function (response, options) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.Msg.alert(globalRes.title.prompt, globalRes.updateSuccess);
                                    //Ext.Msg.alert(globalRes.title.prompt, globalRes.addSuccess);
                                    me.store.load();
//                                    Ext.MessageBox.show({
//                                        title: title,
//                                        width: 300,
//                                        msg: disable?userRoleRes.msg.disableUserSuccess:userRoleRes.msg.unDisableUserSuccess,
//                                        buttons: Ext.MessageBox.OK,
//                                        icon: Ext.MessageBox.INFO
//                                    });

                                }
                                else {
                                    Ext.MessageBox.show({
                                        title: title,
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
                }
            });
        } else {
            Ext.MessageBox.show({
                title: '认证用户',
                width: 300,
                msg: userRoleRes.msg.editUser,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
        
    },
    onChangePasswordClick: function () {
        var me = this;
        var record = me.getView().getSelectionModel().getSelection()[0];
        if (record) {
            var win = Ext.create('Ext.Window', {
                width: 500,
                modal: true,
                border: false,
                title: userRoleRes.passwordTilte + '[' + record.data.nickName + "]",
                items: Ext.create('Ext.form.Panel', {
                    bodyPadding: 5,
                    layout: 'anchor',
                    defaults: {
                        anchor: '100%'
                    },
                    defaultType: 'textfield',
                    items: [{
                        fieldLabel: userRoleRes.newPassword,
                        minLength: 6,
                        minLengthText: userRoleRes.passwordError,
                        maxLength: 16,
                        maxLengthText: userRoleRes.passwordError,
                        name: 'newPassword',
                        inputType: 'password',
                        allowBlank: false,
                        showCapsWarning: true
                    }, {
                        fieldLabel: userRoleRes.passwordAffirm,
                        minLength: 6,
                        minLengthText: userRoleRes.passwordError,
                        maxLength: 16,
                        maxLengthText: userRoleRes.passwordError,
                        name: 'confirmPassword',
                        inputType: 'password',
                        allowBlank: false,
                        validator: function (v) {
                            var form = this.ownerCt.getForm();
                            var newPass = form.findField('newPassword');
                            if (v == newPass.getValue()) {
                                return true;
                            }
                            else {
                                return userRoleRes.passwordHitNotAllow;
                            }
                        }
                    }]
                }),
                buttons: [{
                    text: globalRes.buttons.submit,
                    handler: function () {
                        var userId = record.data.id;
                        var newPassword = this.up('window').down('form').getForm().findField('newPassword').getValue();
                        me.changePassword(userId, newPassword, this);
                    }
                }, {
                    text: globalRes.buttons.back,
                    handler: function () {
                        this.up('window').close();
                    }
                }]
            });
            win.show();
        } else {
            Ext.MessageBox.show({
                title: userRoleRes.passwordTilte,
                width: 300,
                msg: userRoleRes.msg.passwordEdit,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },

    changePassword: function (userId, newPassword, openWin) {
        Ext.Ajax.request({
            url: 'userController.do?method=updatePasswordByAdmin',
            params: {id: userId, newPassword: newPassword},
            method: 'POST',
            success: function (response, options) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    Ext.Msg.alert(globalRes.title.prompt, globalRes.updateSuccess, function () {
                        openWin.up('window').close();
                    });
                } else {
                    Ext.MessageBox.show({
                        title: userRoleRes.passwordTilte,
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
    },
    disableUnUser: function (disable) {
        var me = this;
        var record = me.getView().getSelectionModel().getSelection()[0];
        if (record) {
            var title = disable ? userRoleRes.disableUser : userRoleRes.unDisableUser;
            var url = disable ? 'disableUser' : 'unDisableUser';
            var userId = record.data.id;
            var username = record.data.fullName;
            Ext.MessageBox.show({
                title: title,
                width: 400,
                msg: title + '[' + username + ']',
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        Ext.Ajax.request({
                            url: 'userController.do?method=' + url,
                            params: {id: userId},
                            method: 'POST',
                            success: function (response, options) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.Msg.alert(globalRes.title.prompt, globalRes.addSuccess);
                                    me.getStore().load();
//                                    Ext.MessageBox.show({
//                                        title: title,
//                                        width: 300,
//                                        msg: disable?userRoleRes.msg.disableUserSuccess:userRoleRes.msg.unDisableUserSuccess,
//                                        buttons: Ext.MessageBox.OK,
//                                        icon: Ext.MessageBox.INFO
//                                    });

                                }
                                else {
                                    Ext.MessageBox.show({
                                        title: title,
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
                }
            });
        } else {
            Ext.MessageBox.show({
                title: disable ? userRoleRes.disableUser : userRoleRes.unDisableUser,
                width: 300,
                msg: userRoleRes.msg.editUser,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },


    onDisableUserClick: function () {
        this.disableUnUser(true);
    },
    onUnDisableUserClick: function () {
        this.disableUnUser(false);
    },

    lockOrUnlockUser: function (lock) {
        var me = this;
        var record = me.getView().getSelectionModel().getSelection()[0];
        if (record) {
            var title = lock ? userRoleRes.lockUser : userRoleRes.unLockUser;
            var url = lock ? 'lockUser' : 'unLockUser';
            var userId = record.data.id;
            var username = record.data.nickName;

            Ext.MessageBox.show({
                title: title,
                width: 400,
                msg: title + '[' + username + ']？',
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            url: 'userController.do?method=' + url,
                            params: {id: userId},
                            method: 'POST',
                            success: function (response, options) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.Msg.alert(globalRes.title.prompt, globalRes.updateSuccess);
                                    me.store.load();
                                } else {
                                    Ext.MessageBox.show({
                                        title: title,
                                        width: 350,
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
                }
            });
        } else {
            Ext.MessageBox.show({
                title: lock ? userRoleRes.lockUser : userRoleRes.unLockUser,
                width: 350,
                msg: userRoleRes.msg.editUser,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },
    onLockUserClick: function () {
        this.lockOrUnlockUser(true);
    },
    onUnLockUserClick: function () {
        this.lockOrUnlockUser(false);
    }
});