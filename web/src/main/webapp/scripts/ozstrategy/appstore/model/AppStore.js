/**
 * Created by lihao on 1/6/15.
 */
Ext.define('FlexCenter.appstore.model.AppStore', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'url',
        'version',
        'description',
        'platform',
        {
            name: 'createDate', convert: function (v) {
            return Ext.util.Format.date(new Date(v), 'Y-m-d H:i:s');
        }
        }
    ]
});