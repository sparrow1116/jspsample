var xpath = function(dom, type, fullPath, expansion, isCtrl){
    this.type = type;
    this.dom = dom;
    this.fullPath = fullPath;
    this.expansion = expansion;
    this.isCtrl = isCtrl;
    this.path = "";
    this.bgColor = 'orange';
    this.highLight(this.dom);
    var selector = this.path.replaceAll('/', '>').replaceAll('\\[', ':eq(').replaceAll('\\]', ')').replaceAll('\\:eq\\(\\@', '[').replaceAll('\'\\)', '\']');
    this.hhParent();
    return selector;
    
}
xpath.g_xpath = [];
xpath.isAnnotate = false;
xpath.selectedNode = [];
xpath.keyList = [];
xpath.valueList = [];
xpath.parentXpath = "";
xpath.currentXpath = "";
xpath.isExist = function(currentDom){
    if (xpath.selectedNode.length > 0) {
        for (var i = 0, len = xpath.selectedNode.length; i < len; i++) {
            if (xpath.selectedNode[i] == currentDom) {
                return true;
            }
        }
    }
    return false;
}
//Get the min length xpath
xpath.getMinLength = function(){
    var length = 10000;
    for (var i = 0, len1 = xpath.g_xpath.length; i < len1; i++) {
        if (length > xpath.g_xpath[i].length) {
            length = xpath.g_xpath[i].length;
        }
    }
    return length;
    
}
//Get the parent's xpath 
xpath.getParentXpath = function(){
    var minLength = xpath.getMinLength();
    var currentString = '';
    if(xpath.g_xpath.length==1){
        currentString=xpath.g_xpath[0];
    }else if(xpath.g_xpath.length>1){
    for (var i = 1; i < minLength; i++) {
        var currentString = xpath.g_xpath[0].substring(0, i);
        for (var j = 0; j < xpath.g_xpath.length; j++) {
            if (xpath.g_xpath[j].indexOf(currentString) == -1) {
                currentString = currentString.substring(0, currentString.lastIndexOf('/'));
                return currentString;
                
            }
        }
    }
    
    currentString = currentString.substring(0, currentString.lastIndexOf('/'));
    }
    return currentString;
}
String.prototype.replaceAll = function(s1, s2){
    return this.replace(new RegExp(s1, "gm"), s2);
}
xpath.prototype.getPath = function(e, path){
    var tn = e.get(0).tagName;
    if (this.isNullOrEmpty(e) || this.isNullOrEmpty(tn)) {
        return path;
    }
    var attr = this.getAttr(e);
    tn = tn.toLowerCase() + attr;
    path = this.isNullOrEmpty(path) ? tn : tn + "/" + path;
    var parentE = e.parent();
    if (this.isNullOrEmpty(parentE) || (!this.fullPath && attr.substring(0, 5) == '[@id=')) {
        return path;
    }
    return this.getPath(parentE, path);
}
xpath.prototype.highLight = function(options){

    if (jq('#samsung_style1').length <= 0) {
        jq('head').append("<style id='samsung_style1'>.selectedNode{background-color: " + this.bgColor + " !important;}.parent{background:green}</style>");
    }
    this.path = this.getPath(this.dom, '');
    
    if (!eval(this.dom.attr('isSelect'))) {
        var pos = this.dom.offset(), em = this.expansion;
        var width = this.dom.width();
        var height = this.dom.height();
        
        this.dom.addClass('selectedNode');
        this.dom.attr('isSelect', true);
        xpath.g_xpath.push(this.path);
        xpath.selectedNode.push(this.dom[0]);
        xpath.currentXpath = this.path;
        //xpath.keyList.push($());
    }
    else {
    
        this.dom.attr('isSelect', false);
        xpath.g_xpath.splice(jq.inArray(this.path, xpath.g_xpath), 1);
        xpath.selectedNode.splice(jq.inArray(this.dom[0], xpath.selectedNode), 1);
        xpath.keyList.splice(jq.inArray(jq("#txtValue",parent.document).val(), xpath.keyList), 1);
        xpath.valueList.splice(jq.inArray(jq("#txtXpath",parent.document).val(), xpath.valueList), 1);
        this.dom.removeClass('selectedNode');
    }
}
xpath.prototype.getAttr = function(e){
    var tn = e.get(0).tagName;
    if (e.siblings(tn).size() > 0) {
        var i = e.prevAll(tn).size();
        if (this.type == 'xpath') {
            i++;
        }
        return '[' + i + ']';
    }
    else {
        return '';
    }
}
xpath.prototype.hhParent = function(){
    this.shortXpath = "";
    if (xpath.g_xpath && xpath.g_xpath.length > 0) {
        // txtXpath.attr('value', "Node Xpath:");
        
        for (var i = 0, len = xpath.g_xpath.length; i < len; i++) {
            if (this.shortXpath == "") {
                this.shortXpath = xpath.g_xpath[i];
            }
            else 
                if (this.shortXpath.length > xpath.g_xpath[i].length) {
                    this.shortXpath = xpath.g_xpath[i];
                }
            //txtXpath.attr('value', txtXpath.attr('value') + '\n' + g_xpath[i]);
        }
        // this.shortXpath = this.shortXpath.substring(0, this.shortXpath.lastIndexOf('/'))
        //txtXpath.attr('value', txtXpath.attr('value') + '\n\nparentXpath:\n' + shortXpath);
        xpath.parentXpath = xpath.getParentXpath();
        var s = this.getDomByXpath(xpath.parentXpath);
        jq('.parent').removeClass('parent');
        s.addClass('parent');
        
    }
    else {
        // txtXpath.attr('value', "");
        jq('.parent').removeClass('parent');
        xpath.parentXpath = "";
    }
    
}
/**
 根据xpath获取元素
 特性：
 - 转换xpath为csspath进行jQuery元素获取
 - 仅支持自然表述（不支持非、或元素选取）
 @param xpath {String} 目标元素xpath
 @returns {jQuery Object} 元素/元素集合
 */
xpath.prototype.getDomByXpath = function(xpath){

    // 开始转换 xpath 为 css path
    // 转换 // 为 " "
    xpath = xpath.replace(/\/\//g, "");
    // 转换 / 为 >
    xpath = xpath.replace(/\//g, ">");
    // 转换 [elem] 为 :eq(elem) ： 规则 -1
    xpath = xpath.replace(/\[([^@].*?)\]/ig, function(matchStr, xPathIndex){
        var cssPathIndex = parseInt(xPathIndex) - 1;
        return ":eq(" + cssPathIndex + ")";
    });
    // 1.2 版本后需要删除@
    xpath = xpath.replace(/\@/g, "");
    
    // 返回jQuery元素
    return jq(xpath);
};
/**
 根据xpath获取元素
 特性：
 - 转换xpath为csspath进行jQuery元素获取
 - 仅支持自然表述（不支持非、或元素选取）
 @param xpath {String} 目标元素xpath
 @returns {jQuery Object} 元素/元素集合
 */
xpath.getDomByXpath = function(xpathObj){

    // 开始转换 xpath 为 css path
    // 转换 // 为 " "
    xpathObj = xpathObj.replace(/\/\//g, "");
    // 转换 / 为 >
    xpathObj = xpathObj.replace(/\//g, ">");
    // 转换 [elem] 为 :eq(elem) ： 规则 -1
    xpathObj = xpathObj.replace(/\[([^@].*?)\]/ig, function(matchStr, xPathIndex){
        var cssPathIndex = parseInt(xPathIndex) - 1;
        return ":eq(" + cssPathIndex + ")";
    });
    // 1.2 版本后需要删除@
    xpathObj = xpathObj.replace(/\@/g, "");
    
    // 返回jQuery元素
    return jq(xpathObj);
};
xpath.prototype.isNullOrEmpty = function(o){
    return null == o || 'null' == o || '' == o || undefined == o;
}
xpath.prototype.getGuid = function(){
    var S4 = function(){
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    };
    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}

