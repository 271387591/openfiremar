/**
 * Created by lihao on 1/7/15.
 */
Ext.define('FlexCenter.history.view.HistoryMessageView', {
    requires: [
        'FlexCenter.history.store.HistoryMessage',
        'Ext.ux.form.field.DateTimeField'
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
        store.getProxy().extraParams={projectId:projectId};
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
                                fieldLabel:globalRes.keyword,
                                xtype : 'textfield',
                                itemId:'message',
                                name : 'message'
                            },
                            {
                                fieldLabel:projectRes.header.nickName,
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
                                fieldLabel:globalRes.buttons.startTime,
                                xtype : 'datefield',
                                editable:false,
                                name : 'startTime',
                                format:'Y-m-d 00:00:00',
                                width:260,
                                maxValue:new Date()
                            },
                            {
                                fieldLabel:globalRes.buttons.endTime,
                                xtype : 'datefield',
                                editable:false,
                                name : 'endTime',
                                width:260,
                                format:'Y-m-d 23:59:59',
                                maxValue:new Date()
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
                                    store.loadPage(1);
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
                                    store.loadPage(1);
                                    
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
                        text: globalRes.buttons.remove,
                        scope: this,
                        handler: this.onDeleteByIdClick
                    },
                    {
                        frame: true,
                        iconCls: 'table-delete',
                        xtype: 'button',
                        text: '按时间段删除',
                        itemId: 'userEditBtn',
                        scope: this,
                        handler: this.onDeleteClick
                    }
                ],
                columns:[
                    {
                        header: projectRes.header.nickName,
                        width:200,
                        dataIndex:'fromNick'
                    },
                    {
                        header: projectRes.header.message,
                        flex:1,
                        dataIndex: 'message',
                        renderer: function (v,metaData,rec) {
                            var message=me.down('#message').getValue();
                            if(message){
                                var reg = new RegExp(message, "g");
                                return v.replace(reg,'<font color="red">'+message+'</font>')
                            }
                            if(rec.get('type')!=0){
                                return '<a href="'+v+'" target="_blank">'+v+'</a>'
                            }
                            
                            return v
                        }
                    },
                    {
                        header: projectRes.header.sendDate,
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
    onDeleteByIdClick: function () {
        var me = this;
        var record = me.down('grid').getSelectionModel().getSelection()[0];
        if (record) {
            Ext.Msg.confirm(globalRes.buttons.remove, '你确定要删除', function (txt) {
                if (txt === 'yes') {
                    Ext.Ajax.request({
                        url: 'historyMessageController.do?method=deleteById',
                        params: {id: record.data.id,projectId:projectId},
                        method: 'POST',
                        success: function (response, options) {
                            var result = Ext.decode(response.responseText);
                            if (result.success) {
                                Ext.Msg.alert(globalRes.title.prompt, globalRes.removeSuccess);
                                me.down('grid').getStore().load();
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
                title: globalRes.buttons.remove,
                width: 300,
                msg: '请选择要删除的数据',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
        }
    },

    onDeleteClick:function(){
        var me = this;
        var win=Ext.widget('window',{
            title:globalRes.buttons.remove,
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
                                data.projectId=projectId;
                                ajaxPostRequest('historyMessageController.do?method=delete',data,function(result){});
                                var bar=me.createDeleteBar(win);
                                bar.show();
                                Ext.Function.defer(function(){
                                    bar.fireEvent('startBar');
                                }, 1000);
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
                            name:'projectId',
                            value:projectId,
                            hidden:true
                        },
                        {
                            fieldLabel:globalRes.buttons.startTime,
                            xtype : 'datefield',
                            editable:false,
                            allowBlank: false,
                            format:'Y-m-d 00:00:00',
                            maxValue:new Date(),
                            minValue:function(){
                                var newValue=new Date(); 
                                var year=newValue.getFullYear();
                                var moth=newValue.getMonth()-3;
                                var day=newValue.getDate();
                                return new Date(year,moth,day);
                            }(),
                            itemId:'startTime',
                            name : 'startTime'
                        },
                        {
                            fieldLabel:globalRes.buttons.endTime,
                            xtype : 'datefield',
                            editable:false,
                            allowBlank: false,
                            name : 'endTime',
                            format:'Y-m-d 23:59:59',
                            //maxValue:new Date(),
                            listeners:{
                                change:function( f, newValue, oldValue, eOpts ){
                                    var s=win.down('#startTime');
                                    var sv= s.getValue();
                                    if(sv>newValue){
                                        s.setValue(newValue);
                                    }
                                    s.setMaxValue(newValue);
                                    if(newValue){
                                        var year=newValue.getFullYear();
                                        var moth=newValue.getMonth()-3;
                                        var day=newValue.getDate();
                                        var min=new Date(year,moth,day);
                                        s.setMinValue(min);
                                    }
                                    
                                }
                            }
                        }
                    ]
                }
            ]
        });
        win.show();
    },
    createDeleteBar:function(pWin){
        var me=this;
        var pmb = Ext.create('Ext.ProgressBar', {
            text:projectRes.removing
        });
        var runnerProgress = new Ext.util.TaskRunner();
        pmb.wait({
            interval: 500,
            //duration: 50000,
            increment: 15,
            text: projectRes.removing,
            scope: this,
            fn: function(){
                pmb.updateText(globalRes.title.success);
            }
        });
        var win=Ext.widget('window', {
            width: 600,
            height:100,
            closable:false,
            modal: true,
            bodyPadding:10,
            title:projectRes.removing,
            buttons:[
                {
                    text:globalRes.buttons.close,
                    disabled:true,
                    itemId:'closeBtn',
                    handler:function(){
                        win.close();
                        if(pWin){
                            pWin.close();
                        }
                        me.down('grid').getStore().load();
                    }

                }
            ],
            onCancel:function(){
                win.close();
                runnerProgress.stopAll();
                runnerProgress.destroy();
            },
            items:[
                pmb
            ]
        });
        me.mon(win,'startBar',function(){
            var fn = function() {
                var running=true;
                Ext.Ajax.request({
                    url: 'historyMessageController.do?method=pullNotification',
                    params: {},
                    method: 'POST',
                    success: function (response, options) {
                        var result = Ext.decode(response.responseText,true);
                        if(result.finished){
                            if(result.state){
                                runnerProgress.stopAll();
                                runnerProgress.destroy();
                                pmb.reset();
                                pmb.updateText(globalRes.removeSuccess);
                                win.down('#closeBtn').setDisabled(false);

                            }else{
                                runnerProgress.stopAll();
                                runnerProgress.destroy();
                                pmb.reset();
                                pmb.updateText(globalRes.removeFail);
                                win.down('#closeBtn').setDisabled(false);
                            }
                        }
                    },
                    failure: function (response, options) {
                        Ext.MessageBox.alert(globalRes.title.fail, Ext.String.format(globalRes.remoteTimeout,response.status));
                    }
                });
                return running;
            };
            var task={
                run: fn,
                interval: 1000
            };
            runnerProgress.start(task);
        });
        return win;

    }
});