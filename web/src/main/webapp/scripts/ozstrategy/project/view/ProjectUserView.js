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
        var sm = Ext.create('Ext.selection.CheckboxModel');
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
                selModel:sm,
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
                        handler: this.addUser
                    },
                    {
                        frame: true,
                        iconCls: 'user-edit',
                        xtype: 'button',
                        text: '移除人员',
                        itemId: 'userEditBtn',
                        scope: this,
                        handler: this.removeUser
                    }
                ],
                columns:[
                    {
                        header: '用户昵称',
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
                        header: '认证状态',
                        dataIndex: 'authentication',
                        renderer: function (v) {
                            return v ? globalRes.yes : globalRes.no;
                        }
                    },
                    {
                        header: '是否为管理员',
                        dataIndex: 'projectId',
                        renderer: function (v,m,rec) {
                            var projectId=me.record.get('id');
                            var projects=rec.get('projects');
                            for(var i=0;i<projects.length;i++){
                                if((projects[i].projectId==projectId) && (projects[i].manager==true)){
                                    return '<font color="red">是</font>';
                                }
                            }
                            return '否';
                        }
                    },
                    {
                        header: globalRes.header.createDate,
                        dataIndex: 'createDate'
                    },
                    {
                        xtype:'actioncolumn',
                        header:'设置管理人员',
                        width:80,
                        dataIndex: 'projects',
                        items:[
                            {
                                getClass: function(v, meta, record) {
                                    var projectId=me.record.get('id');
                                    for(var i=0;i<v.length;i++){
                                        if((v[i].projectId==projectId) && (v[i].manager==true)){
                                            return 'user-delete';
                                        }
                                    }
                                    return 'user-add';
                                },
                                getTip:function(v,metadata,record,rowIndex,colIndex,store){
                                    var projectId=me.record.get('id');
                                    for(var i=0;i<v.length;i++){
                                        if((v[i].projectId==projectId) && (v[i].manager==true)){
                                            return '取消管理员';
                                        }
                                    }
                                    return '设为管理人员';
                                },
                                handler:function(grid, rowIndex, colIndex){
                                    var rec = grid.getStore().getAt(rowIndex);
                                    var projectId=me.record.get('id');
                                    var v=rec.get('projects');
                                    for(var i=0;i<v.length;i++){
                                        if((v[i].projectId==projectId) && (v[i].manager==true)){
                                            Ext.Msg.confirm('提示', Ext.String.format('您确定要取消{0}管理员身份？', rec.get('nickName')), function (txt) {
                                                if(txt=='yes'){
                                                    me.updateManager(rec.get('id'),'false');
                                                }
                                            });
                                            return;
                                        }
                                    }
                                    Ext.Msg.confirm('提示', Ext.String.format('您确定要将{0}设为管理员？', rec.get('nickName')), function (txt) {
                                        if(txt=='yes'){
                                            me.updateManager(rec.get('id'),'true');
                                        }
                                    });
                                    
                                }
                            }
                        ]
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
    },
    addUser:function(){
        var me=this;
        ajaxPostRequest('userController.do?method=listAvailableUsers',{},function(result){
            if(result.success){
                var data=result.data;
                var availableUserStore=Ext.create('FlexCenter.user.store.Users',{
                    data:data,
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
                var win=Ext.widget('window',{
                    title:'添加人员',
                    layout:'fit',
                    height:410,
                    width:600,
                    modal:true,
                    items:[
                        {
                            xtype:'form',
                            border:false,
                            height:390,
                            listeners:{
                                afterrender:function(f){
                                    var form= f.getForm();
                                    var store=me.down('grid').getStore();
                                    var users=[];
                                    store.each(function(rec){
                                        var obj={};
                                        obj.id=rec.get('id');
                                        obj.nickName=rec.get('nickName');
                                        obj.username=rec.get('username');
                                        users.push(obj);
                                    });
                                    me.record.set('users',users);
                                    form.loadRecord(me.record);
                                }
                            },
                            defaults: {               
                                anchor: '100%'
                            },
                            buttons:[
                                {
                                    text:'保存',
                                    handler:function(){
                                        var value=win.down('form').getForm().getValues();
                                        me.insertUser(value,win);
                                    }
                                },{
                                    text:'关闭',
                                    handler:function(){
                                        win.close();
                                    }
                                }
                            ],
                            items:[
                                {
                                    xtype: 'multiselect',
                                    border: false,
                                    name: 'users',
                                    itemId: 'users',
                                    hideLabel: true,
                                    filterMode: 'local',
                                    allowBlank: false,
                                    store: availableUserStore,
                                    availableTitle: '可选人员',
                                    selectedTitle: '已选人员',
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
                                            }
                                        },
                                        getRowClass: function (record) {
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
                                            header: '用户名',
                                            flex: 1,
                                            sortable: true,
                                            dataIndex: 'username'
                                        }, {
                                            header: '昵称',
                                            flex: 1,
                                            sortable: true,
                                            dataIndex: 'nickName'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                });
                win.show();
            }
        });
    },
    insertUser:function(value,win){
        var me=this;
        var users=value.users;
        var userIds=[],projectId=me.record.get('id');
        for(var i=0;i<users.length;i++){
            userIds.push(users[i].id);
        }
        var data={
            userIds:userIds.join(','),
            projectId:projectId
        };
        ajaxPostRequest('projectController.do?method=saveProjectUser',data,function(result){
            if(result.success){
                Ext.Msg.alert('提示','保存成功',function(txt){
                    if(win){
                        win.close();
                    }
                });
                me.down('grid').getStore().load();
                var projectView = Ext.ComponentQuery.query('#projectView')[0];
                projectView.getStore().load();
                
            }else{
                Ext.Msg.alert('提示','保存失败');
            }
        });
    },
    updateManager:function(userId,manager){
        var me=this;
        var projectId=me.record.get('id');
        
        var data={
            userId:userId,
            projectId:projectId,
            manager:manager
        };
        ajaxPostRequest('projectController.do?method=updateManager',data,function(result){
            if(result.success){
                Ext.Msg.alert('提示','设置成功');
                me.down('grid').getStore().load();
                var projectView = Ext.ComponentQuery.query('#projectView')[0];
                projectView.getStore().load();
            }else{
                Ext.Msg.alert('提示','保存失败');
            }
        });
    },
    removeUser:function(){
        var me=this;
        var record = me.down('grid').getSelectionModel().getSelection();
        if(record.length<1){
            Ext.Msg.alert('提示','请选择人员');
            return;
        }
        var userIds=[],nickNames=[];
        for(var i=0;i<record.length;i++){
            userIds.push(record[i].get('id'));
            nickNames.push(record[i].get('nickName'));
        }
        var projectId=me.record.get('id');
        var data={
            userIds:userIds.join(','),
            projectId:projectId
        };
        Ext.Msg.confirm('提示', Ext.String.format('您确定要移除:{0}？', nickNames.join(',')), function (txt) {
            if(txt=='yes'){
                ajaxPostRequest('projectController.do?method=removeUser',data,function(result){
                    if(result.success){
                        Ext.Msg.alert('提示','移除成功');
                        me.down('grid').getStore().load();
                        var projectView = Ext.ComponentQuery.query('#projectView')[0];
                        projectView.getStore().load();
                        var projectView = Ext.ComponentQuery.query('#projectView')[0];
                        projectView.getStore().load();
                    }else{
                        Ext.Msg.alert('提示','移除失败');
                    }
                });
            }
        });
        
        
        
    }
    
});