/**
 * Created by lihao on 1/25/15.
 */
Ext.define('FlexCenter.ProjectHome', {
    extend: 'Ext.panel.Panel',
    requires: [
        'FlexCenter.project.view.ProjectView',
        'FlexCenter.UserHeader'
    ],
    alias: 'widget.projectHome',
    initComponent:function(){
        var me=this;
        Ext.apply(me,{
            itemId:'projectHome',
            border:false,
            layout: 'border',
            items:[
                {
                    region: 'north',
                    xtype: 'userHeader'
                },
                {
                    region:'center',
                    xtype:'projectView',
                    margin:'0 0 1 0'
                }
            ]
        });
        me.callParent(arguments);
    }
});