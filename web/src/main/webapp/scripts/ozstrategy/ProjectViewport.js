/**
 * Created by lihao on 1/24/15.
 */
Ext.define('FlexCenter.ProjectViewport', {
    extend: 'Ext.container.Viewport',
    requires: [
        'FlexCenter.ProjectHome'
    ],
    mixins: {
        observable: 'Ext.util.Observable'
    },
    
    initComponent:function(){
        var me=this;
        var projectView=Ext.widget('projectView',{
            region:'center',
            margin:'0 0 1 0'
        });
        //Ext.EventManager.on(projectView, 'inProject', function(){
        //    console.log();
        //});
        //this.mon(projectView,'inProject',function(rec){
        //    console.log(rec);
        //
        //});
        Ext.apply(me,{
            layout:'fit',
            itemId:'projectViewport',
            items:[
                {
                    xtype:'projectHome'
                }
            ]
        });
        me.callParent(arguments);
    }
});
