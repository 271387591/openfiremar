/**
 * Created by lihao on 1/7/15.
 */
Ext.define('FlexCenter.history.model.HistoryMessage', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'fromId',
        'fromNick',
        'toId',
        'toNick',
        'type',
        'message',
        
        {
            name: 'createDate', convert: function (v) {
            return Ext.util.Format.date(new Date(v), 'Y-m-d H:i:s');
        }
        }
    ]
});