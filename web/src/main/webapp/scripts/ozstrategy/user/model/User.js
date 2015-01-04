Ext.define('FlexCenter.user.model.User', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'unitName',
        'username',
        'roleName',
        'roleDisplayName',
        'firstName',
        'lastName',
        'password',
        'passwordAffirm',
        'mobile',
        'email',
        'fullName',
        'defaultRoleName',
        'defaultRoleDisplayName',
        'defaultRoleId',
        'gender',
        'nickName',
        'userNo',
        'authentication',
        'enabled',
        'accountLocked',
        'projectId',
        'projectName',
        'activationCode',
        {
            name: 'createDate', convert: function (v) {
            return Ext.util.Format.date(new Date(v), 'Y-m-d H:i:s');
        }
        },
        'simpleRoles',
        'projects'
    ],
    hasMany: {model: 'FlexCenter.user.model.SimpleRole', name: 'simpleRoles'},
    hasMany: {model: 'FlexCenter.project.model.ProjectUser', name: 'projects'}
});