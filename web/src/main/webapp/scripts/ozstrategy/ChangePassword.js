/**
 * Created with IntelliJ IDEA.
 * User: kangpan
 * Date: 29/1/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('FlexCenter.ChangePassword', {
      extend: 'Ext.window.Window',
      alias : 'widget.changepassword',

      requires: [
        'Ext.form.Panel'
      ],

      config: {
        title: undefined,
        doReset: null,
        avoidClose : false
      },
      resizable: false,
      border: false,
//      iconCls: 'icon-user',

      initComponent: function() {
        var me = this;
        Ext.apply(this, {
          layout: 'fit',
          title: userRoleRes.passwordTilte,
          modal: true,
          width: 380,
          autoHeight: true,
          resizable: false,
          maximizable:false,

          items: {
            xtype: 'form',
            itemId: 'passwordForm',
            defaultType: 'textfield',
            autoHeight: true,
            frame: true,
            border: false,
            bodyStyle: 'padding:5px 20px 0px 5px',
            labelWidth: 105, // label settings here cascade unless overridden
            defaults: {               // defaults are applied to items, not the container
              anchor: '100%'
            },
            monitorValid: true,
            items: [
              {
                xtype: 'hidden',
                itemId:'userId',
                name: 'id'
              },
              {
                fieldLabel: userRoleRes.oldPassword,
                name: 'oldPassword',
                itemId:'oldPassword',
                blankText:'必填',
                tabIndex:1,
                inputType: 'password',
                allowBlank: false
              },
              {
                fieldLabel: userRoleRes.newPassword,
                name: 'newPassword',
                itemId:'newPassword',
                blankText:'必填',
                inputType: 'password',
                tabIndex:2,
                allowBlank: false,
                showCapsWarning: true
              },
              {
                fieldLabel: userRoleRes.passwordAffirm,
                name: 'confirmPassword',
                blankText:'必填',
                inputType: 'password',
                tabIndex:3,
                allowBlank: false,
                validator: function(v) {
                  var form = this.ownerCt.getForm();
                  var newPass = form.findField('newPassword');
                  if (v == newPass.getValue()) {
                    return true;
                  }
                  else {
                    return userRoleRes.passwordHitNotAllow;
                  }
                }
              }
            ],
            buttons: [
              {
                text: globalRes.buttons.save,
                formBind: true,
                handler: function() {
                  var win = this.up('window');
                  win.onChangePassword();
                }
              },
              {
                text: globalRes.buttons.cancel,
                hidden : me.avoidClose,
                handler: function() {
                  var win = this.up('window');
                  win.onCancel();
                }
              },
              {
                text:globalRes.buttons.logout,
                iconCls:'logout',
                hidden : !me.avoidClose,
                handler: me.onLogout,
                scope: me
              }
            ]
          }
        });

        this.addEvents('doChangePassword');

        this.callParent(arguments);
      },


      getFormPanel: function(){
        return this.down('form');
      },

      setPasswordInfo: function(userId,passwordHint){
        this.down('#userId').setValue(userId);
      },

      onChangePassword : function(){
        var form = this.getFormPanel().getForm();
        var userId =  this.down('#userId').getValue();
        var oldPass =  this.down('#oldPassword').getValue();
        var newPass =  this.down('#newPassword').getValue();
        if (form.isValid()) {
          this.fireEvent('doChangePassword',this,userId, oldPass, newPass);
        }
      },
      onCancel: function(){
        this.close();
      }
    }
);




