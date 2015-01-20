/**
 * Created by lihao on 1/11/15.
 */
Ext.define('FlexCenter.export.view.MessageExportView', {
    requires: [
        'FlexCenter.export.store.MessageExport',
        'Ext.ux.form.field.DateTimeField',
        'Ext.ux.form.Downloader'
    ],
    extend: 'Ext.panel.Panel',
    alias: 'widget.messageExportView',
    itemId: 'messageExportView',
    layout:'border',
    border:false,
    
    getStore:function(){
        var store=Ext.StoreManager.lookup("MessageExportStore");
        if(!store){
            store=Ext.create("FlexCenter.export.store.MessageExport",{
                storeId:'MessageExportStore'
            });
        }
        store.load();
        return store;
    },
    initComponent: function () {
        var me = this;
        var store=me.getStore();
        me.items=[
            {
                xtype:'panel',
                border:false,
                region:'north',
                autoScroll: true,
                items:[
                    {
                        xtype:'form',
                        frame: false,
                        bodyPadding: 5,
                        border:false,
                        itemId:'picForm',
                        items:[
                            {
                                xtype:'fieldset',
                                layout:'column',
                                title:'文字、图片',
                                autoScroll: true,
                                items:[
                                    {
                                        columnWidth:.5,
                                        border:false,
                                        items:[
                                            {
                                                fieldLabel:'开始时间',
                                                xtype : 'datefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                itemId:'picStartTime',
                                                format:'Y-m-d 00:00:00',
                                                maxValue:new Date(),
                                                name : 'startTime'
                                            },
                                            {
                                                fieldLabel:'结束时间',
                                                xtype : 'datefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                format:'Y-m-d 23:59:59',
                                                maxValue:new Date(),
                                                itemId : 'picEndTime',
                                                name : 'endTime',
                                                listeners:{
                                                    change:function( f, newValue, oldValue, eOpts ){
                                                        var s=me.down('#picStartTime');
                                                        var sv= s.getValue();
                                                        if(sv>newValue){
                                                            s.setValue(newValue);
                                                        }
                                                        s.setMaxValue(newValue);
                                                    }
                                                }
                                            }
                                        ]
                                    },
                                    {
                                        columnWidth:.5,
                                        border:false,
                                        items:[
                                            {
                                                xtype: 'button',
                                                width:120,
                                                margin:1,
                                                formBind: true,
                                                text: '导出',
                                                handler: function () {
                                                    var picStartTime=me.down('#picStartTime').getValue();
                                                    var picEndTime=me.down('#picEndTime').getValue();
                                                    var form=me.down('#picForm').getForm();
                                                    var data=form.getValues();
                                                    ajaxPostRequest("messageExportController.do?method=export",data,function(result){
                                                        if(result.success){
                                                            var win=me.createExportWin();
                                                            win.show();
                                                            win.fireEvent('startBar');
                                                        }else{
                                                            Ext.Msg.alert('错误',result.message);
                                                        }
                                                    });
                                                }
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    
                    {
                        xtype:'form',
                        frame: false,
                        bodyPadding: 5,
                        border:false,
                        items:[
                            {
                                xtype:'fieldset',
                                layout:'column',
                                title:'语音信息',
                                autoScroll: true,
                                items:[
                                    {
                                        columnWidth:.5,
                                        border:false,
                                        items:[
                                            {
                                                fieldLabel:'开始时间',
                                                xtype : 'datetimefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                name : 'startTime'
                                            },
                                            {
                                                fieldLabel:'结束时间',
                                                xtype : 'datetimefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                name : 'endTime'
                                            }
                                        ]
                                    },
                                    {
                                        columnWidth:.5,
                                        border:false,
                                        items:[
                                            {
                                                xtype: 'button',
                                                itemId: 'save',
                                                width:120,
                                                margin:1,
                                                text: '导出',
                                                handler: function () {

                                                }
                                            }
                                        ]
                                    }
                                    
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                xtype:'grid',
                autoScroll: true,
                forceFit: true,
                border:true,
                region:'center',
                store:store,
                title:'导出历史',
                viewConfig:{
                    getRowClass: function (record) {
                        if (!record.get('hasFile')) {
                            return 'disabled-row';
                        }
                    }
                },
                dockedItems:[
                    {
                        xtype: 'pagingtoolbar',
                        store: store,
                        dock: 'bottom',
                        displayInfo: true
                    }
                ],
                columns:[
                    {
                        header: '执行时间',
                        flex:1,
                        dataIndex:'executeDate'
                    },
                    {
                        header: '导出类容',
                        dataIndex: 'type',
                        flex:1,
                        renderer: function (v) {
                            if(v=='MessagePicture'){
                                return '文字，图片';
                            }
                            return '语音';
                        }
                    },
                    {
                        header: '状态',
                        dataIndex: 'hasFile',
                        flex:1,
                        renderer: function (v) {
                            if(v){
                                return '成功';
                            }
                            return '失败';
                        }
                    },
                    {
                        header: '操作者(用户名)',
                        flex:1,
                        dataIndex: 'exportor'
                    },
                    {
                        header:'下载',
                        width:50,
                        xtype:'actioncolumn',
                        dataIndex:'hasFile',
                        items:[
                            {
                                getClass: function(v, meta, record) {
                                    if(v){
                                        return 'file-download';
                                    }
                                    
                                },
                                isDisabled:function(view,rowIndex,colIndex,item ,record){
                                    if(record.get('hasFile')){
                                        return false;
                                    }
                                    return true;
                                },
                                getTip:function(v,metadata,record,rowIndex,colIndex,store){

                                    return '下载';
                                },
                                handler:function(grid, rowIndex, colIndex,item,e,rec){
                                    Ext.ux.form.Downloader.get({
                                        url: 'messageExportController.do?method=download',
                                        params:{
                                            id:rec.get('id')
                                        },
                                        success:function(response,options){

                                        },
                                        failure: function(response,options){
                                            var str=response.responseXML.body.innerText;
                                            var result = Ext.decode(str,true);
                                            Ext.Msg.alert('Message','dowload file fail..');
                                        }
                                    });
                                }
                            }
                        ]

                    }
                    
                ]
                
            }
        ]
        me.callParent(arguments);
    },
    createExportWin:function(){
        var me=this;
        var pmb = Ext.create('Ext.ProgressBar', {
            text:'正在导出...'
        });
        var runnerProgress = new Ext.util.TaskRunner();
        pmb.wait({
            interval: 500,
            //duration: 50000,
            increment: 15,
            text: '正在导出...',
            scope: this,
            fn: function(){
                pmb.updateText('成功!');
            }
        });
        var win=Ext.widget('window', {
            width: 600,
            height:100,
            closable:false,
            modal: true,
            bodyPadding:10,
            title:'导出',
            buttons:[
                {
                    text:'关闭',
                    disabled:true,
                    itemId:'closeBtn',
                    handler:function(){
                        win.close();
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
                    url: 'messageExportController.do?method=pullNotification',
                    params: {},
                    method: 'POST',
                    success: function (response, options) {
                        var result = Ext.decode(response.responseText,true);
                        if(result.finished){
                            if(result.state){
                                runnerProgress.stopAll();
                                runnerProgress.destroy();
                                pmb.reset();
                                pmb.updateText('导出成功,请点击下方的下载按钮下载文件。');
                                win.down('#closeBtn').setDisabled(false);

                            }else{
                                runnerProgress.stopAll();
                                runnerProgress.destroy();
                                pmb.reset();
                                pmb.updateText('导出失败,请重试。');
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
        
    },
    onAddClick:function(){
        var me=this;
        var win=Ext.widget('window',{
            title:'数据导出',
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
                            xtype : 'datetimefield',
                            editable:false,
                            allowBlank: false,
                            name : 'startTime'
                        },
                        {
                            fieldLabel:'结束时间',
                            xtype : 'datetimefield',
                            editable:false,
                            allowBlank: false,
                            name : 'endTime'
                        }
                    ]
                }
            ]
        });
        win.show();
        
    },

    onDeleteClick:function(){
        var me = this;
        Ext.ux.form.Downloader.get({
            url: 'messageExportController.do?method=export',
            params:{id:1},
            handleException: function(response,options){
                var me = this,result = Ext.decode(response.responseXML.body.innerText,true);
                if(result){
                    Ext.Msg.alert('Message',result['message']);
                    barwin.close();
                }else{
                    Ext.Msg.alert('Message',' An unknown Error occurred while downloading.');
                    barwin.close();
                }
            }
        });
        return;
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
                            xtype : 'datetimefield',
                            editable:false,
                            allowBlank: false,
                            name : 'startTime'
                        },
                        {
                            fieldLabel:'结束时间',
                            xtype : 'datetimefield',
                            editable:false,
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