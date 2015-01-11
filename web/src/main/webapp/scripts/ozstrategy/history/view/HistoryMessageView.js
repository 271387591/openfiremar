/**
 * Created by lihao on 1/7/15.
 */
Ext.define('FlexCenter.history.view.HistoryMessageView', {
    requires: [
        'FlexCenter.history.store.HistoryMessage'
    ],
    extend: 'Ext.panel.Panel',
    alias: 'widget.historyMessageView',
    itemId: 'historyMessageView',
    autoScroll: true,
    layout:'border',
    getStore:function(){
        var store=Ext.StoreManager.lookup("HistoryMessageStore");
        if(!store){
            store=Ext.create("FlexCenter.history.store.HistoryMessage",{
                storeId:'HistoryMessageStore'
            });
        }
        
        store.load();
        return store;
    },
    initComponent: function () {
        var me = this;
        var store=me.getStore(),sm=Ext.create('Ext.selection.CheckboxModel',{
            mode:'SINGLE'
        });
        me.items=[
            {
                xtype:'form',
                region:'north',
                height:60,
                frame:false,
                border:false,
                layout:'column',
                defaults: {
                    layout: 'vbox'
                },
                items:[
                    {
                        columnWidth: 1/3,
                        bodyStyle:'padding:5px 0 5px 5px',
                        border:false,
                        items:[
                            {
                                fieldLabel:'关键字',
                                xtype : 'textfield',
                                itemId:'message',
                                name : 'message'
                            },
                            {
                                fieldLabel:'用户昵称',
                                xtype : 'textfield',
                                name : 'fromNick'
                            }
                        ]
                    },
                    {
                        columnWidth: 1/3,
                        border:false,
                        bodyStyle:'padding:5px 0 5px 5px',
                        items:[
                            {
                                fieldLabel:'开始时间',
                                xtype : 'datefield',
                                format : 'Y-m-d',
                                editable:false,
                                name : 'startTime'
                            },
                            {
                                fieldLabel:'结束时间',
                                xtype : 'datefield',
                                editable:false,
                                format : 'Y-m-d 23:59:59',
                                name : 'endTime'
                            }
                        ]
                    },
                    {
                        columnWidth: 1/3,
                        border:false,
                        bodyStyle:'padding:5px 0 5px 5px',
                        items:[
                            {
                                xtype : 'button',
                                text : globalRes.buttons.search,
                                iconCls : 'search',
                                handler : function() {
                                    var data = me.down('form').getForm().getValues();
                                    var store=me.down('grid').getStore();
                                    store.on('beforeload',function(s,e){
                                        e.params = data;
                                    });
                                    store.load();
                                }
                            },
                            {
                                xtype : 'button',
                                text : globalRes.buttons.clear,
                                margins:'5 0 0 0',
                                iconCls : 'clear',
                                handler : function() {
                                    me.down('form').getForm().reset();
                                    var store=me.down('grid').getStore();
                                    store.on('beforeload',function(s,e){
                                        e.params = {};
                                    });
                                    store.load();
                                    
                                }
                            }
                        ]
                    }
                ]
            },
            {
                xtype:'grid',
                region:'center',
                store:store,
                forceFit: true,
                autoScroll: true,
                border:true,
                dockedItems:[
                    {
                        xtype: 'pagingtoolbar',
                        store: store,
                        dock: 'bottom',
                        displayInfo: true
                    }
                ],
                tbar:[
                    {
                        frame: true,
                        iconCls: 'table-delete',
                        xtype: 'button',
                        text: '删除',
                        itemId: 'userEditBtn',
                        scope: this,
                        handler: this.onDeleteClick
                    }
                    
                ],
                columns:[
                    {
                        header: '序号',
                        xtype:'rownumberer',
                        width:40
                    },
                    {
                        header: '用户昵称',
                        width:200,
                        dataIndex:'fromNick'
                    },
                    {
                        header: '工程',
                        width:200,
                        dataIndex: 'toNick'
                    },
                    {
                        header: '聊天内容',
                        flex:1,
                        dataIndex: 'message',
                        renderer: function (v) {
                            var message=me.down('#message').getValue();
                            if(message){
                                var reg = new RegExp(message, "g");
                                return v.replace(reg,'<font color="red">'+message+'</font>')
                            }
                            return v
                        }
                    },
                    {
                        header: '发送时间',
                        width:120,
                        dataIndex: 'createDate'
                    }
                ],
                listeners:{
                    itemdblclick:function(grid, record, item, index, e, eOpts){
                    }

                }
            }
        ];
        me.callParent(arguments);
    },

    onDeleteClick:function(){
        var me = this;
        var win=Ext.widget('window',{
            title:'删除',
            width:400,
            layout: 'fit',
            autoShow: true,
            modal: true,
            border:false,
            autoScroll:true,
            autoHeight:true,
            items:[
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
                            text: globalRes.buttons.ok,
                            formBind: true,
                            handler: function () {
                                var form=win.down('form').getForm();
                                var data=form.getValues();
                                ajaxPostRequest('historyMessageController.do?method=delete',data,function(result){
                                    if(result.success){
                                        me.down('grid').getStore().load();
                                        Ext.Msg.alert(globalRes.title.prompt,globalRes.removeSuccess);
                                    }else{
                                        Ext.MessageBox.alert({
                                            title:globalRes.title.warning,
                                            icon: Ext.MessageBox.ERROR,
                                            msg:result.message,
                                            buttons:Ext.MessageBox.OK
                                        });
                                    }
                                });
                            }
                        },
                        {
                            xtype: 'button',
                            text: globalRes.buttons.cancel,
                            handler: function () {
                                win.close();
                            }
                        }
                    ],
                    items:[
                        {
                            fieldLabel:'开始时间',
                            xtype : 'datefield',
                            format : 'Y-m-d',
                            editable:false,
                            allowBlank: false,
                            name : 'startTime'
                        },
                        {
                            fieldLabel:'结束时间',
                            xtype : 'datefield',
                            editable:false,
                            format : 'Y-m-d 23:59:59',
                            allowBlank: false,
                            name : 'endTime'
                        }
                    ]
                }
            ]
        });
        win.show();
    }
});