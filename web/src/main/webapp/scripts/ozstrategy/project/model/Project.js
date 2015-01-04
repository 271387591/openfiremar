/**
 * Created by lihao on 1/1/15.
 */
Ext.define('FlexCenter.project.model.Project', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'serialNumber',
        'name',
        'description',
        'activationCode',
        'mucRoomId',
        'users',
        {
            name: 'createDate', convert: function (v) {
            return Ext.util.Format.date(new Date(v), 'Y-m-d H:i:s');
        }
        }
    ]
    //hasMany: {model: 'FlexCenter.project.model.ProjectUser', name: 'users'}
});