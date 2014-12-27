/**
 * Created by lihao on 10/20/14.
 */
Ext.define('Oz.app.TabMenu', {
    extend: 'Ext.menu.Menu',
    plain: true,
    componentCls: 'tab-menu',

    initComponent: function() {
        this.addEvents(
            /**
             * @event
             * Fired when one of the tab-related menu items is clicked.
             * @param {Ext.menu.Item} item
             */
            "tabItemClick",
            /**
             * @event
             * Fired when "close all tabs" item is clicked.
             */
            "closeAllTabs"
        );

        this.items = [{
            text: '关闭其他',
            iconCls: 'menu-close',
            cls: 'close-all',
            tabUrl:this.tabUrl,
            handler: this.onCloseAllTabs,
            scope: this
        }];

        this.callParent();
    },
    onCloseAllTabs:function(url){
        this.fireEvent("closeAllTabs",url);
    },

    /**
     * Adds new menu item to represent a tab.
     *
     * @param {Object} tab Tab config object with `text`, `iconCls` and 'href` field.
     * @param {String} cls additional CSS class name
     */
    addTab: function(tab, cls) {
        // Insert before 'close all tabs' item
        this.insert(this.items.length - 1, {
            text: tab.text,
            iconCls: tab.iconCls,
            origIcon: tab.iconCls,
            href: tab.href,
            cls: cls,
            handler: this.onTabItemClick,
            scope: this
        });
    },

    onTabItemClick: function(item) {
        this.fireEvent("tabItemClick", item);
    },

    /**
     * Adds CSS class to menu item representing a tab.
     *
     * @param {Object} tab Index of menu item
     * @param {String} cls CSS class name
     */
    addTabCls: function(tab, cls) {
        this.items.each(function(item) {
            if (item.href === tab.href) {
                item.addCls(cls);
            }
        });
    }
});