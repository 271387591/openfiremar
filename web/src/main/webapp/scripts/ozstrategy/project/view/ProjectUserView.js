/**
 * Created by lihao on 1/3/15.
 */
Ext.define('FlexCenter.project.view.ProjectUserView', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.projectUserView',

    requires: [
        'FlexCenter.user.store.Users'
    ],

    getUserStore: function () {
        var me=this;
        var store = Ext.StoreManager.lookup('projectUserStore');
        if (!store) {
            store = Ext.create('FlexCenter.user.store.Users', {
                storeId: 'projectUserStore',
                proxy:{
                    type: 'ajax',
                    url: 'userController.do?method=listProjectUsers',
                    reader: {
                        type: 'json',
                        root : 'data',
                        totalProperty  : 'total',
                        messageProperty: 'message'
                    }
                }
            });
        }
        store.on('beforeload',function(s,e){
            e.params = {
                projectId:me.record.get('id')
            };
        });
        store.load();
        return store;
    },
    border: false,
    forceFit: true,
    autoScroll: true,
    layout:'border',
    initComponent: function () {
        var me = this;
        var userStore=me.getUserStore();
        me.items=[
            {
                xtype:'form',
                region:'north',
                //autoHeight:true,
                defaults: {
                    anchor: '100%'
                },
                bodyPadding: 10,
                border:false,
                frame:true,
                items:[
                    {
                        xtype: 'container',
                        layout:'hbox',
                        items:[{
                            xtype: 'container',
                            flex: 1,
                            border:false,
                            layout: 'anchor',
                            defaultType: 'displayfield',
                            items: [{
                                fieldLabel: '工程编号',
                                name: 'serialNumber',
                                value:me.record.get('serialNumber'),
                                anchor:'95%'
                            }, {
                                fieldLabel: '工程名称',
                                name: 'name',
                                value:me.record.get('name'),
                                anchor:'95%'
                            }]
                        },{
                            xtype: 'container',
                            flex: 1,
                            layout: 'anchor',
                            defaultType: 'displayfield',
                            items: [{
                                fieldLabel: '激活码',
                                name: 'activationCode',
                                value:me.record.get('activationCode'),
                                anchor:'95%'
                            },{
                                fieldLabel: '工程人数',
                                value:me.record.get('users').length,
                                anchor:'95%'
                            }]
                        }]
                    },
                    {
                        xtype:'displayfield',
                        fieldLabel: '描述',
                        name: 'description',
                        value:me.record.get('description'),
                        anchor:'95%'
                    }
                ]
            },
            {
                xtype:'grid',
                region:'center',
                store: userStore,
                forceFit: true,
                autoScroll: true,
                title:'工程人员',
                dockedItems:[
                    {
                        xtype: 'pagingtoolbar',
                        store: userStore,
                        dock: 'bottom',
                        displayInfo: true
                    }
                ],
                tbar:[
                    {
                        frame: true,
                        iconCls: 'user-add',
                        xtype: 'button',
                        text: '添加人员',
                        scope: this,
                        handler: this.onAddClick
                    },
                    {
                        frame: true,
                        iconCls: 'user-edit',
                        xtype: 'button',
                        text: '删除人员',
                        itemId: 'userEditBtn',
                        scope: this,
                        handler: this.onEditClick
                    }
                ],
                columns:[
                    {xtype: 'rownumberer'},
                    {
                        header: '用户昵称',
                        dataIndex: 'nickName'
                    },
            
                    {
                        header: userRoleRes.header.username,
                        dataIndex: 'username'
                    },
                    {
                        header: userRoleRes.header.defaultRoleName,
                        dataIndex: 'defaultRoleDisplayName'
                    },
                    {
                        header: userRoleRes.header.accountLocked,
                        dataIndex: 'accountLocked',
                        renderer: function (v) {
                            return v ? globalRes.yes : globalRes.no;
                        }
                    },
                    {
                        header: '认证状态',
                        dataIndex: 'authentication',
                        renderer: function (v) {
                            return v ? globalRes.yes : globalRes.no;
                        }
                    },
                    {
                        header: globalRes.header.createDate,
                        dataIndex: 'createDate'
                    }
                ]
            }
        ];
        me.addEvents('addTab');
        me.callParent();
    },
    setRecord:function(record){
        var me=this;
        if(record){
            me.down('form').getForm().loadRecord(record);
        }
    }
});