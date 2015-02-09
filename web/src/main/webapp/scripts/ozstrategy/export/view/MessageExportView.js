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
        store.getProxy().extraParams={projectId:projectId};
        return store;
    },
    initComponent: function () {
        var me = this;
        var store=me.getStore();
        store.load();
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
                                title:projectRes.exportPic,
                                autoScroll: true,
                                items:[
                                    {
                                        columnWidth:.5,
                                        border:false,
                                        items:[
                                            {
                                                fieldLabel:globalRes.buttons.startTime,
                                                xtype : 'datefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                itemId:'picStartTime',
                                                format:'Y-m-d',
                                                maxValue:new Date(),
                                                minValue:function(){
                                                    var newValue=new Date();
                                                    var year=newValue.getFullYear();
                                                    var moth=newValue.getMonth()-3;
                                                    var day=newValue.getDate();
                                                    return new Date(year,moth,day);
                                                }(),
                                                name : 'startTime'
                                            },
                                            {
                                                fieldLabel:globalRes.buttons.endTime,
                                                xtype : 'datefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                format:'Y-m-d',
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
                                                text: projectRes.buttons.exportBtn,
                                                handler: function () {
                                                    var form=me.down('#picForm').getForm();
                                                    var data=form.getValues();
                                                    data.projectId=projectId;
                                                    data.type='MessagePicture';
                                                    ajaxPostRequest("messageExportController.do?method=export",data,function(result){
                                                        if(result.success){
                                                            var win=me.createExportWin();
                                                            win.show();
                                                            win.fireEvent('startBar');
                                                        }else{
                                                            Ext.Msg.alert(globalRes.title.fail,result.message);
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
                        itemId:'vcForm',
                        border:false,
                        items:[
                            {
                                xtype:'fieldset',
                                layout:'column',
                                title:projectRes.exportVoi,
                                autoScroll: true,
                                items:[
                                    {
                                        columnWidth:.5,
                                        border:false,
                                        items:[
                                            {
                                                fieldLabel:globalRes.buttons.startTime,
                                                xtype : 'datefield',
                                                format:'Y-m-d 00:00:00',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                maxValue:new Date(),
                                                itemId:'vcStartTime',
                                                minValue:function(){
                                                    var newValue=new Date();
                                                    var year=newValue.getFullYear();
                                                    var moth=newValue.getMonth()-3;
                                                    var day=newValue.getDate();
                                                    return new Date(year,moth,day);
                                                }(),
                                                name : 'startTime'
                                            },
                                            {
                                                fieldLabel:globalRes.buttons.endTime,
                                                xtype : 'datefield',
                                                editable:false,
                                                allowBlank: false,
                                                width:300,
                                                name : 'endTime',
                                                format:'Y-m-d 23:59:59',
                                                maxValue:new Date(),
                                                itemId : 'vcEndTime',
                                                listeners:{
                                                    change:function( f, newValue, oldValue, eOpts ){
                                                        var s=me.down('#vcStartTime');
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
                                                formBind: true,
                                                text: projectRes.buttons.exportBtn,
                                                handler: function () {
                                                    var form=me.down('#vcForm').getForm();
                                                    var data=form.getValues();
                                                    data.projectId=projectId;
                                                    data.type='Voice';
                                                    ajaxPostRequest("messageExportController.do?method=export",data,function(result){
                                                        if(result.success){
                                                            var win=me.createExportWin();
                                                            win.show();
                                                            win.fireEvent('startBar');
                                                        }else{
                                                            Ext.Msg.alert(globalRes.title.fail,result.message);
                                                        }
                                                    });

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
                title:projectRes.exportGrid,
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
                        header: projectRes.header.executeDate,
                        flex:1,
                        dataIndex:'executeDate'
                    },
                    {
                        header: projectRes.header.type,
                        dataIndex: 'type',
                        flex:1,
                        renderer: function (v) {
                            if(v=='MessagePicture'){
                                return projectRes.exportPic;
                            }
                            return projectRes.exportVoi;
                        }
                    },
                    {
                        header: projectRes.header.hasFile,
                        dataIndex: 'hasFile',
                        flex:1,
                        renderer: function (v) {
                            if(v){
                                return globalRes.title.success;
                            }
                            return globalRes.title.fail;
                        }
                    },
                    {
                        header: projectRes.header.exportor,
                        flex:1,
                        dataIndex: 'exportor',
                        renderer: function (v) {
                            if(!v){
                                return projectRes.header.exportorDefault;
                            }
                            return v;
                        }
                    },
                    {
                        header:globalRes.buttons.download,
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

                                    return globalRes.buttons.download;
                                },
                                handler:function(grid, rowIndex, colIndex,item,e,rec){
                                    var multiFile=rec.get('multiFile');
                                    var id=rec.get('id');
                                    if(multiFile){
                                        window.open('messageExportController.do/multiDownload?id='+id)
                                        console.log('sdf')
                                        
                                    }else{
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
            text:projectRes.exporting
        });
        var runnerProgress = new Ext.util.TaskRunner();
        pmb.wait({
            interval: 500,
            //duration: 50000,
            increment: 15,
            text: projectRes.exporting,
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
            title:projectRes.buttons.exportBtn,
            buttons:[
                {
                    text:globalRes.buttons.close,
                    disabled:true,
                    itemId:'closeBtn',
                    handler:function(){
                        win.close();
                        var form=me.down('#picForm').getForm();
                        form.reset();
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
                                pmb.updateText(projectRes.exportSuccess);
                                win.down('#closeBtn').setDisabled(false);

                            }else{
                                runnerProgress.stopAll();
                                runnerProgress.destroy();
                                pmb.reset();
                                pmb.updateText(projectRes.exportFail);
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