/**
 * Created by lihao on 1/2/15.
 */
Ext.define('FlexCenter.project.view.ProjectForm', {
    extend: 'Ext.Window',
    alias: 'widget.projectForm',

    requires: [
        'FlexCenter.project.store.Project'
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
                            var data=form.getValues();
                            if(me.activeRecord==null){
                                me.fireEvent('create', me, data);
                            }else{
                                me.fireEvent('update', me, data);
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
                        title: projectRes.projectInfo,
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
                                name: 'managerIds',
                                itemId:'managerIds'
                            },
                            {
                                fieldLabel: '<font color="red">*</font>'+projectRes.header.serialNumber,
                                name: 'serialNumber',
                                maxLength: 50,
                                minLength: 1,
                                blankText: globalRes.tooltip.notEmpty,
                                allowBlank: false
                            },
                            {
                                fieldLabel: '<font color="red">*</font>'+projectRes.header.projectName,
                                name: 'name',
                                maxLength: 50,
                                minLength: 1,
                                blankText: globalRes.tooltip.notEmpty,
                                allowBlank: false
                            },
                            {
                                fieldLabel: '<font color="red">*</font>'+projectRes.header.activationCode,
                                name: 'activationCode',
                                maxLength: 6,
                                minLength: 1,
                                blankText: globalRes.tooltip.notEmpty,
                                allowBlank: false
                            },
                            {
                                xtype:'textareafield',
                                grow: true,
                                fieldLabel: projectRes.header.projectDes,
                                anchor: '100%',
                                name: 'description'
                            }
                        ]
                    }
                    //{
                    //    xtype: 'fieldset',
                    //    title: '选择人员',
                    //    checkboxToggle: false,
                    //    height: 300,
                    //    autoHeight: true,
                    //    defaults: {               // defaults are applied to items, not the container
                    //        anchor: '100%'
                    //    },
                    //    items: [
                    //        {
                    //            xtype: 'multiselect',
                    //            border: false,
                    //            name: 'users',
                    //            itemId: 'users',
                    //            hideLabel: true,
                    //            filterMode: 'local',
                    //            allowBlank: false,
                    //            store: me.availableUserStore,
                    //            availableTitle: '可选人员',
                    //            selectedTitle: '已选人员(选则管理人员)',
                    //            availableIdProperty: 'id',
                    //            selectedIdProperty: 'id',
                    //            availableCfg: {
                    //            features: [
                    //                {
                    //                    id: 'searchRole',
                    //                    ftype: 'search',
                    //                    disableIndexes: ['id'],
                    //                    searchMode: 'local'
                    //                }]
                    //            },
                    //            availableViewCfg: {
                    //                getRowClass: function (record) {
                    //                    if (record.get('organizationRole')) {
                    //                        return 'blue-grid-row';
                    //                    }
                    //                    return '';
                    //                }
                    //            },
                    //            selectedCfg: {
                    //                selModel: {
                    //                    selType: 'checkboxmodel',
                    //                    //mode: 'SINGLE',
                    //                    allowDeselect: false,
                    //                    injectCheckbox: 'first',
                    //                    listeners: {
                    //                        'selectionchange': function (rowModel, records, eOpts) {
                    //                            me.onCheckedChange(records);
                    //                        }
                    //                    }
                    //                },
                    //                features: [
                    //                    {
                    //                        id: 'searchSelectedRole',
                    //                        ftype: 'search',
                    //                        disableIndexes: ['id'],
                    //                        searchMode: 'local'
                    //                    }]
                    //            },
                    //            selectedViewCfg: {
                    //                listeners: {
                    //                    'refresh': function (view, eOpts) {
                    //                        var selModel = view.ownerCt.getSelectionModel();
                    //                        var store = view.ownerCt.store;
                    //                        me.onGridViewRefresh(store, selModel);
                    //                    }
                    //                },
                    //                getRowClass: function (record) {
                    //                    // return a custom css class based on the record or index
                    //                    if (record.get('name')) {
                    //                        return 'blue-grid-row';
                    //                    }
                    //                    return '';
                    //                }
                    //            },
                    //            columns: [
                    //                {
                    //                    header: 'ID',
                    //                    hidden: true,
                    //                    sortable: false,
                    //                    dataIndex: 'id'
                    //                }, {
                    //                    header: '用户名',
                    //                    flex: 1,
                    //                    sortable: true,
                    //                    dataIndex: 'username'
                    //                }, {
                    //                    header: '昵称',
                    //                    flex: 1,
                    //                    sortable: true,
                    //                    dataIndex: 'nickName'
                    //                }
                    //            ]
                    //        }
                    //    ]
                    //}
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
            this.down('#managerIds').setValue(record.get('managerIds'));
            this.down('form').getForm().loadRecord(record);
        }
        
    },
    onGridViewRefresh: function (store, selModel) {
        var me=this;
        var managerIds = me.down('#managerIds').getValue();
        console.log(managerIds)
        if (managerIds) {
            managerIds=managerIds.split(',');
            for(var i=0;i<managerIds.length;i++){
                var select = store.findBy(function (rec, id) {
                    return id == managerIds[i];
                })
                if (select != -1) {
                    selModel.select(select);
                }
            }
        }
    },
    onCheckedChange: function (records) {
        if (records && records.length > 0) {
            var managerIds=[];
            for(var i=0;i<records.length;i++){
                managerIds.push(records[i].get('id'))
            }
            this.down('#managerIds').setValue(managerIds.join(','));
        } else {
            this.down('#managerIds').setValue(null);
        }
    }
    
});