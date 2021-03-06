Ext.define('FlexCenter.user.view.UserForm', {
    extend: 'Ext.window.Window',
    alias: 'widget.userForm',

    requires: [
        'Ext.form.Panel',
        'Ext.data.Store',
        'Ext.ux.grid.feature.Search',
        'Ext.ux.Utils',
        'Ext.ux.form.MultiSelect'
    ],

    config: {
        title: undefined,
        activeRecord: null
    },
    mySelf: false,
    resizable: false,
    getProject:function(){
        var me=this;
        var availableProjectStore = Ext.create('FlexCenter.project.store.Project', {
            storeId: 'availableProjectStore'
        });
        availableProjectStore.load();
        return availableProjectStore;
        
    },
    initComponent: function () {
        var me = this;
        var availableProjectStore=me.getProject(); 
        Ext.apply(this, {
            layout: 'fit',
            autoShow: true,
            modal: true,
            width: 800,
            autoHeight: true,
            border: false,
            minWidth: 600,
            maxHeight: 600,
            autoScroll: true,
            items: [
                {
                    xtype: 'form',
                    frame: true,
                    bodyPadding: 5,
                    layout: 'anchor',
                    defaults: {
                        anchor: '100%'
                    },
                    autoScroll: true,
                    buttons: [
                        {
                            xtype: 'button',
                            itemId: 'save',
                            text: globalRes.buttons.save,
                            formBind: true,
                            handler: function () {
                                var win = this.up('window');
                                win.onSave();
                            }
                        },
                        {
                            xtype: 'button',
                            text: globalRes.buttons.cancel,
                            handler: function () {
                                var win = this.up('window');
                                win.onCancel();
                            }
                        }
                    ],
                    items: [
                        {
                            xtype: 'fieldset',
                            checkboxToggle: false,
                            title: userRoleRes.userInfo,
                            autoHeight: true,
                            defaultType: 'textfield',
                            defaults: {               // defaults are applied to items, not the container
                                anchor: '100%'
                            },
                            collapsed: false,
                            items: [
                                {
                                    xtype: 'hidden',
                                    name: 'id'
                                },
                                {
                                    xtype: 'hidden',
                                    name:'projectId',
                                    value:projectId
                                },
                                {
                                    xtype: 'hidden',
                                    name:'activationCode',
                                    value:porjectActivationCode
                                },
                                {
                                    fieldLabel: '<font color="red">*</font>' + userRoleRes.header.username,
                                    name: 'username',
                                    maxLength: 50,
                                    minLength: 1,
                                    blankText: globalRes.tooltip.notEmpty,
                                    tabIndex: 2,
                                    readOnly: me.isEdit,
                                    readOnlyCls: 'x-item-disabled',
                                    regex: /^[a-zA-Z0-9_]{3,16}$/,   
                                    regexText: userRoleRes.usernameRegexText,
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '<font color="red">*</font>' + userRoleRes.header.nickName,
                                    name: 'nickName',
                                    maxLength: 50,
                                    minLength: 1,
                                    blankText: globalRes.tooltip.notEmpty,
                                    allowBlank: false
                                },
                                //{
                                //    fieldLabel: '<font color="red">*</font>' + '用户编号',
                                //    name: 'userNo',
                                //    maxLength: 50,
                                //    minLength: 1,
                                //    hidden:true,
                                //    blankText: globalRes.tooltip.notEmpty
                                //},
                                
                                {
                                    fieldLabel: '<font color="red">*</font>' + userRoleRes.header.password,
                                    itemId: 'password',
                                    name: 'password',
                                    blankText: globalRes.tooltip.notEmpty,
                                    tabIndex: 5,
                                    minLength: 6,
                                    minLengthText: userRoleRes.passwordError,
                                    maxLength: 16,
                                    maxLengthText: userRoleRes.passwordError,
                                    inputType: 'password',
                                    allowBlank: false
                                }, {
                                    fieldLabel: '<font color="red">*</font>' + userRoleRes.passwordAffirm,
                                    itemId: 'passwordAffirm',
                                    name: 'passwordAffirm',
                                    blankText: globalRes.tooltip.notEmpty,
                                    tabIndex: 5,
                                    minLength: 6,
                                    minLengthText: userRoleRes.passwordError,
                                    maxLength: 16,
                                    maxLengthText: userRoleRes.passwordError,
                                    inputType: 'password',
                                    validator: function (v) {
                                        var newPass = me.down('#password');
                                        if (v == newPass.getValue()) {
                                            return true;
                                        }
                                        else {
                                            return userRoleRes.passwordHitNotAllow;
                                        }
                                    },
                                    allowBlank: false
                                }
                            ]
                        },
                        {
                            xtype: 'fieldset',
                            title: userRoleRes.selectRole,
                            checkboxToggle: false,
                            height: 300,
                            autoHeight: true,
                            defaults: {               // defaults are applied to items, not the container
                                anchor: '100%'
                            },
                            items: [
                                {
                                    xtype: 'multiselect',
                                    border: false,
                                    name: 'simpleRoles',
                                    itemId: 'roles',
                                    hideLabel: true,
                                    filterMode: 'local',
                                    allowBlank: false,
                                    store: me.availableRoleStore,
                                    availableTitle: userRoleRes.availableRoles,
                                    selectedTitle: userRoleRes.assignedRoles,
                                    availableIdProperty: 'id',
                                    selectedIdProperty: 'id',
                                    availableCfg: {
                                        features: [
                                            {
                                                id: 'searchRole',
                                                ftype: 'search',
                                                disableIndexes: ['id'],
                                                searchMode: 'local'
                                            }]
                                    },
                                    availableViewCfg: {
                                        getRowClass: function (record) {
                                            if (record.get('organizationRole')) {
                                                return 'blue-grid-row';
                                            }
                                            return '';
                                        }
                                    },
                                    selectedCfg: {
                                        
                                        features: [
                                            {
                                                id: 'searchSelectedRole',
                                                ftype: 'search',
                                                disableIndexes: ['id'],
                                                searchMode: 'local'
                                            }]
                                    },
                                    selectedViewCfg: {
                                        listeners: {
                                            'refresh': function (view, eOpts) {
                                                var selModel = view.ownerCt.getSelectionModel();
                                                var store = view.ownerCt.store;
                                                //me.onGridViewRefresh(store, selModel);
                                            }
                                        },
                                        getRowClass: function (record) {
                                            // return a custom css class based on the record or index
                                            if (record.get('name')) {
                                                return 'blue-grid-row';
                                            }
                                            return '';
                                        }
                                    },
                                    columns: [
                                        {
                                            header: 'ID',
                                            hidden: true,
                                            sortable: false,
                                            dataIndex: 'id'
                                        }, {
                                            header: userRoleRes.header.roleName,
                                            flex: 1,
                                            sortable: true,
                                            dataIndex: 'name'
                                        }, {
                                            header: userRoleRes.header.displayName,
                                            flex: 1,
                                            sortable: true,
                                            dataIndex: 'displayName'
                                        }
                                    ]
                                }
                            ]
                        }
                        
                    ]
                }
            ]
        });

        this.addEvents('create');
        this.addEvents('update');

        this.callParent(arguments);
    },
    onGridViewRefresh: function (store, selModel) {
        var defaultRoleId = this.down('#defaultRoleId').getValue();

        if (defaultRoleId) {
            var select = store.findBy(function (rec, id) {
                return id == defaultRoleId;
            })
            if (select != -1) {
                selModel.select(select);
            }
        }
    },
    onCheckedChange: function (records) {
        if (records && records.length > 0) {
            this.down('#defaultRoleId').setValue(records[0].get('id'));
        } else {
            this.down('#defaultRoleId').setValue(null);
        }
    },

    getFormPanel: function () {
        return this.down('form');
    },

    setActiveRecord: function (record) {
        this.activeRecord = record;
        if (record) {
            this.setTitle(userRoleRes.editUser);
            this.down('#password').disable();
            this.down('#passwordAffirm').disable();
            this.down('#password').hide();
            this.down('#passwordAffirm').hide();
            this.getFormPanel().loadRecord(record);
        } else {
            this.setTitle(userRoleRes.addUser);
            this.down('#password').show();
            this.down('#passwordAffirm').show();
            if (this.down('#password').isDisabled())
                this.down('#password').enable();
            if (this.down('#passwordAffirm').isDisabled())
                this.down('#passwordAffirm').enable();

            this.getFormPanel().getForm().reset();
        }
    },

    onSave: function () {
        var me = this;
        var active = this.activeRecord, form = this.getFormPanel().getForm(),
            datas = form.getValues(), simpleRoles = datas.simpleRoles, roleids = [];
        Ext.each(simpleRoles, function (data) {
            roleids.push(data.id);
        });
        //if (roleids.length == 0) {
        //    Ext.MessageBox.alert(globalRes.title.prompt, userRoleRes.msg.addUserHasRole);
        //    return;
        //}
        datas.roleIds = roleids.join(',');
        if (form.isValid()) {
            if (!active) {
                this.fireEvent('create', this, datas);
            }
            else {
                this.fireEvent('update', this, datas);
            }
        }
    },

    onCancel: function () {
        this.close();
    }
})