/**
 * Created by lihao on 1/1/15.
 */
Ext.define('FlexCenter.project.store.Project', {
    extend: 'Ext.data.Store',
    alias: 'store.projects',
    requires: [
        'FlexCenter.project.model.Project'
    ],
    model: 'FlexCenter.project.model.Project',
    proxy: {
        type: 'ajax',
        url: 'projectController.do?method=listProjects',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total',
            messageProperty: 'message'
        },
        writer: {
            writeAllFields: false,
            root: 'data'
        }
    }
});