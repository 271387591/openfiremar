if (top != self) {
  top.location = self.location;
}
function ActionColumnHideAccess(config){
    var checkAccess=function(featureName){
        if(featureName == '#allowAll#'){
            return true;
        }
        var parts = featureName.split('|');
        for(var i = 0; i < parts.length; i ++){
            if(accessRes[parts[i]] == true){
                return true;
            }
        }
        return false;
    }
    var getClass=function(){
        if((checkAccess(config.featureName) || config.byPass) && config.extra){
            return config.cls;
        }
        return 'x-hide-display'
    }
    return getClass
}
function ActionColumnDisabledAccess(config){
    var checkAccess=function(featureName){
        if(featureName == '#allowAll#'){
            return true;
        }
        var parts = featureName.split('|');
        for(var i = 0; i < parts.length; i ++){
            if(accessRes[parts[i]] == true){
                return true;
            }
        }
        return false;
    }
    var isDisabled=function(){
        if(checkAccess(config.featureName) || config.byPass){
            return false;
        }
        return true;

    }
    return isDisabled
}

function ajaxPostRequest(url,params,callback,noMask){

    var box = Ext.MessageBox;
    box.show({
        msg: globalRes.loading,
        progressText: globalRes.loading,
        width:300,
        wait:true,
        waitConfig: {interval:200},
        iconCls:'loading-ux', //custom class in msg-box.html
        animateTarget: 'mb7'
    });
    
    //var myMask = new Ext.LoadMask(Ext.getBody(), {msg:globalRes.loading});
    //if(!mask){
    //    myMask.show();
    //}
    Ext.Ajax.request({
        url: url || '',
        params: params || {},
        method: 'POST',
        success: function (response, options) {
            //if (myMask != undefined){ myMask.destroy();}
            if (box != undefined){ box.close();}
            var result = Ext.decode(response.responseText,true);
            if(callback){
                callback(result);
            }
        },
        failure: function (response, options) {
            //if (myMask != undefined){ myMask.hide();}
            if (box != undefined){ box.close();}
            Ext.MessageBox.alert(globalRes.title.fail, Ext.String.format(globalRes.remoteTimeout,response.status));
        }
    });
}

// ajax回调函数处理系统退出
Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this);
function checkUserSessionStatus(conn,response,options){
//  debugger;
    //Ext重新封装了response对象
    try{
        if(response && response.getAllResponseHeaders().sessionstatus){
            setTimeout(function (){
                Ext.MessageBox.alert(globalRes.title.fail,globalRes.msg.logoutTimeout,function (){
                    location.href = basePath;
                });
            },500);
        }
    }catch (e){
        
    }
    
}
function setCookie(name,value,expires,path,domain,secure) {
    document.cookie = name + "=" + escape (value) +
        ((expires) ? "; expires=" + expires.toGMTString() : "") +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
}
function getCookie(name) {
    var prefix = name + "=";
    var start = document.cookie.indexOf(prefix);

    if (start==-1) {
        return null;
    }

    var end = document.cookie.indexOf(";", start+prefix.length);
    if (end==-1) {
        end=document.cookie.length;
    }

    var value=document.cookie.substring(start+prefix.length, end);
    return unescape(value);
}
Ext.applyIf(Array.prototype, {
    /**
     * Checks whether or not the specified object exists in the array.
     * @param {Object} o The object to check for
     * @param {Number} from (Optional) The index at which to begin the search
     * @return {Number} The index of o in the array (or -1 if it is not found)
     */
    indexOf : function(o, from){
        var len = this.length;
        from = from || 0;
        from += (from < 0) ? len : 0;
        for (; from < len; ++from){
            if(this[from] === o){
                return from;
            }
        }
        return -1;
    },

    /**
     * Removes the specified object from the array.  If the object is not found nothing happens.
     * @param {Object} o The object to remove
     * @return {Array} this array
     */
    remove : function(o){
        var index = this.indexOf(o);
        if(index != -1){
            this.splice(index, 1);
        }
        return this;
    }
});

