/**
 * Created by lihao on 1/6/15.
 */
Ext.define('FlexCenter.appstore.view.AppStoreForm', {
    extend: 'Ext.Window',
    alias: 'widget.appStoreForm',

    requires: [
    ],

    layout: 'fit',
    autoShow: true,
    modal: true,
    width: 600,
    border:false,
    minWidth: 600,
    maxHeight:600,
    autoScroll:true,
    activeRecord:null,
    initComponent: function () {
        var me = this;
        me.items=[
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
                            var form=me.down('form').getForm();
                            if(me.activeRecord==null){
                                me.fireEvent('create', me, form);
                            }else{
                                me.fireEvent('update', me, form);
                            }
                            
                        }
                    },
                    {
                        xtype: 'button',
                        text: globalRes.buttons.cancel,
                        handler: function () {
                            me.close();
                        }
                    }
                ],
                items: [
                    {
                        xtype: 'fieldset',
                        checkboxToggle: false,
                        title: appStoreRes.header.title,
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
                                fieldLabel: '<font color="red">*</font>'+appStoreRes.header.version,
                                name: 'version',
                                maxLength: 50,
                                minLength: 1,
                                blankText: globalRes.tooltip.notEmpty,
                                allowBlank: false
                            },
                            {
                                xtype: 'filefield',
                                name: 'fileName',
                                fieldLabel: '<font color="red">*</font>'+appStoreRes.header.fileName,
                                msgTarget: 'side',
                                allowBlank: false,
                                //listeners:{
                                //    change:function(f,value){
                                //        if(value.indexOf(".apk")==-1){
                                //            f.markInvalid('文件格式不正确')
                                //            
                                //        }
                                //        
                                //    }
                                //},
                                buttonText: globalRes.buttons.fileText
                            },
                            {
                                fieldLabel:'<font color="red">*</font>'+appStoreRes.header.platform,
                                xtype:'combo',
                                name:'platform',
                                mode:'local',
                                editable:false,
                                triggerAction:'all',
                                store:[
                                    ['Android', 'Android'],
                                    ['Iphone', 'Iphone']
                                ],
                                value:'Android'
                            },
                            {
                                fieldLabel:'<font color="red">*</font>最新版本',
                                xtype:'combo',
                                name:'currentVersion',
                                mode:'local',
                                editable:false,
                                triggerAction:'all',
                                store:[
                                    ['true', '是'],
                                    ['false', '否']
                                ],
                                value:'true'
                            },
                            
                            {
                                xtype:'textareafield',
                                grow: true,
                                fieldLabel: appStoreRes.header.description,
                                anchor: '100%',
                                name: 'description'
                            }
                        ]
                    }
                ]
            }
        ];
        this.addEvents('create');
        this.addEvents('update');
        me.callParent(arguments);
    },
    setActiveRecord: function (record) {
        this.activeRecord = record;
        if(record){
            this.down('form').getForm().loadRecord(record);
        }

    }
});