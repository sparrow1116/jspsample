
var currentDom = null;
//the param is for the node in iframe,when the node is click,it's value is true
var isFromContent = false;
var endLoadDomTime = '';
var loadDomTimeSpan = '';
window.addEventListener('DOMContentLoaded', function(){
    endLoadDomTime = new Date().getTime();
    loadDomTimeSpan = endLoadDomTime - parent.beginLoadDomTime;
    console.log("loadDomTimeSpan:" + loadDomTimeSpan);
    function getXpath(e){
        var contentForIframe1 = jq('#contentForIframe1');
        var ctype = contentForIframe1.attr('data');
        e = e || window.event;
        var target = e.target || e.srcElement;
        if (ctype == "Left") {
            if (target.tagName == "A") {
                jq(target).attr('target', "_self");
                var url = jq(target).attr('href');
                if (url.indexOf('http://') != 0) {
                    url = jq('base').attr('href') + url;
                }
                jq('#txtUrl', parent.document).val(url);
                isFromContent = true;
                jq('#btnStart', parent.document).click();
            }
            return true;
        }
        else {
            jq("#txtValue", parent.document).val('');
            jq("#txtValue", parent.document).focus();
            
            var isAnnotate = jq("#txtIsAnnotate", parent.document).val();
            
            if (isAnnotate == "Yes") {
                currentDom = jq(target);
                var s = new xpath(currentDom, 'xpath', true, 2, true);
                
                jq('#txtParentXpath', parent.document).val(xpath.parentXpath);
                if (xpath.isExist(target) == true) {
                    jq(".dim", parent.document).show();
                    jq('.data_acquisition_alert', parent.document).show();
                    if (xpath.g_xpath.length > 0) {
                        jq('#txtXpath', parent.document).val(xpath.g_xpath[xpath.g_xpath.length - 1]);
                        jq('#txtXpath', parent.document).attr('title', xpath.g_xpath[xpath.g_xpath.length - 1]);
                    }
                    var parentNode = s.getDomByXpath(xpath.parentXpath);
                    
                    var selectedNode = parentNode.find(currentDom[0].tagName);
                    var isMoreOne = 0;
                    console.log('xpath.isCtrl:' + xpath.isCtrl);
                    console.log('selectedNode.length:' + selectedNode.length);
                    if (xpath.isCtrl == true && selectedNode.length > 0) {
                        new xpath(jq(e.target), 'xpath', true, 2, true);
                        for (var i = 0, len = selectedNode.length; i < len; i++) {
                            new xpath(jq(selectedNode[i]), 'xpath', true, 2, true);
                        }
                    }
                }
                
                return false;
            }
            else {
                if (target.tagName == "A") {
                    jq(target).attr('target', "_self");
                    var url = jq(target).attr('href');
                    if (url.indexOf('http://') != 0) {
                        url = jq('base').attr('href') + url;
                    }
                    jq('#txtUrl', parent.document).val(url);
                    isFromContent = true;
                    jq('#btnStart', parent.document).click();
                }
                return true;
            }
        }
    }
    var jqIsLoadInterval = setInterval(function(){
        if (jqIsLoad) {
            clearInterval(jqIsLoadInterval);
            jq('.dim', parent.document).hide();
            jq('.loading', parent.document).hide();
            jq(document).mousedown(function(e){
                if (e.which == 1) {
                    return getXpath(e);
                }
                
            });
            
            jq(document).mouseup(function(e){
                if (e.which == 1) {
                    return false;
                }
            });
            jq(document).click(function(e){
                if (e.which == 1) {
                    return false;
                }
            });
        }
        else {
            console.log('loading');
        }
    }, 10);
    
    
})
function HighLightDom(key, currentXpath){
    //alert('currentXpath:' + currentXpath);
    var dom = xpath.getDomByXpath(currentXpath)
    new xpath(jq(dom), 'xpath', true, 2, true);
    xpath.keyList.push(key);
    xpath.valueList.push(currentXpath);
}
