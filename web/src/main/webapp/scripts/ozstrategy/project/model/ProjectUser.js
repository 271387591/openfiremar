/**
 * Created by lihao on 1/1/15.
 */
Ext.define('FlexCenter.project.model.ProjectUser', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'username',
        'projectName',
        'projectId',
        'userId',
        'manager'
    ]
});