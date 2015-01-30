/**
 * Created by lihao on 1/11/15.
 */
Ext.define('FlexCenter.export.model.MessageExport', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'exportor',
        'type',
        'hasFile',
        'multiFile',
        {
            name: 'executeDate', convert: function (v) {
            return Ext.util.Format.date(new Date(v), 'Y-m-d H:i:s');
        }
        },
        {
            name: 'createDate', convert: function (v) {
            return Ext.util.Format.date(new Date(v), 'Y-m-d H:i:s');
        }
        }
    ]
});