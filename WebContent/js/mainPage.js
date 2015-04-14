var isloaded = false;
//the variable is for load the dom tree,when request the url is successful,it will be set
var beginLoadDomTime = '';

jq(function(){
    jq.jBox.defaults.zIndex = 10000;
    var fileName = "";
    var editKey = null;
    
    var updataMode = -1;
    
    function getQueryString(name){
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) 
            return unescape(r[2]);
        return null;
    }
    
    var jobId = getQueryString('jobId');
    
    jq('#btnBackJobList').click(function(){
        window.location.href = "/job/page/reports.jsp";
    });
    
    var jobInfo;
    if (parseInt(jobId) > 0) {
        jq.ajax({
            url: '/job/ajax/report_list_ajax.jsp',
            type: 'post',
            dataType: 'json',
            data: {
                "jobID": jobId
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
                console.log('Error occurs at server.' +
                XMLHttpRequest.responseText);
            },
            
            success: function(data){
                var obj = data.Records[0];
                jobInfo = obj;
                updataMode = jobId;
                //obj.OrderIndex
                //obj.cwrapper parent
                //obj.datasource url
                //obj.id	
                //obj.jobname
                //obj.outputpath
                //obj.status
                //obj.submittime
                //obj.swrapper // key values
                jq('#txtUrl').val(obj.datasource);
                loadUrlByJobData();
                
                jq("#name").val(obj.jobname);
                var tb_items = jq("#items")[0].childNodes;
                for (var i = 0; i < tb_items.length; i++) {
                    tb_items[i].remove();
                }
                var keyValueList = obj.swrapper.split(";");
                for (var i = 0; i < keyValueList.length; i++) {
                    var keyValue = keyValueList[i].split(":");
                    updateItems(keyValue[0], keyValue[1]);
                }
                jq(".none-disp").hide();
                jq(".output-block").show();
                
            }
        });
    }
    
    
    var minusSize = {
        width: 20,
        height: 45
    };
    jq(".output-block").hide();
    //	jq("#job").hide();
    var jobFrameState = false;
    var animProcessing = false;
    function showJobFrame(state){
        animProcessing = true;
        if (state) {
            jq("#job").animate({
                left: '0%'
            }, 1000, 'swing', function(){
                animProcessing = false
            });
            jq(".arrow").attr('src', '/img/arrow_l_left_y.png');
        }
        else {
            jq(".arrow").attr('src', '/img/arrow_l_right_y.png');
            jq("#job").animate({
                left: '-28%'
            }, 1000, 'swing', function(){
                animProcessing = false
            });
        }
    }
    
    jq("#sidebar").click(function(){
        if (!animProcessing) {
            jobFrameState = !jobFrameState;
            showJobFrame(jobFrameState);
        }
        
    });
    
    function loadUrlByJobData(){
        LoadUrl("Left", LoadUrl, 'right');
    }
    
    //start button begin
    jq("#btnStart").click(function(){
        if (jq('#ifm1')[0].contentWindow.isFromContent) {
            LoadUrl("Left");
        }
        else 
            if (jq("#btnStart").val() == "Annotate this page") {
                LoadUrl("Right");
                jq('#txtIsAnnotate').val('Yes');
                
            }
            else {
                jq('#txtIsAnnotate').val('No');
                if (jq("#btnStart").val() == 'Start' ||
                jq("#btnStart").val() == 'Continue Browser') {
                    LoadUrl("Left");
                }
                else {
                
                    jq('#txtIsAnnotate').val('Yes');
                    jq("#btnStart").val('Continue Browser');
                }
            }
        
    });
    //start button end
    
    jq('#txtIsAnnotate').click(function(){
        LoadUrl();
    });
    jq("#ifm2").load(function(){
        if (jobInfo && jobInfo.swrapper) {
            var swrapperList = jobInfo.swrapper.split(';');
            if (swrapperList.length > 0) {
                var tempXpathObj = jq('#ifm2')[0].contentWindow.xpath;
                for (var i = 0; i < swrapperList.length; i++) {
                    var tempXpath = swrapperList[i].split(':');
                    jq('#ifm2')[0].contentWindow.HighLightDom(tempXpath[0], tempXpath[1]);
                }
            }
        }
        jq("#txtIsAnnotate").val("Yes");
    })
    //Load Url beign
    function LoadUrl(Ctype, callback, param){
        var theURL = jq("#txtUrl").val();
        theURL = theURL.trim();
        if (IsURL(theURL) == true) {
        
            jq('.dim').show();
            jq('.loading').show();
            if (theURL.indexOf("http:") < 0 &&
            theURL.indexOf("https:") < 0 &&
            theURL.indexOf("mms:") < 0 &&
            theURL.indexOf("rtsp:") < 0 &&
            theURL.indexOf("ftp:") < 0) {
                theURL = "http://" + theURL;
            }
            console.log("come in start URL:" + theURL);
            isloaded = false;
            
            jq.ajax({
                url: '/job/controller/main_ajax.jsp',
                type: 'post',
                dataType: 'json',
                data: {
                    "url": theURL,
                    "type": Ctype
                },
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    console.log('Error occurs at server.' +
                    XMLHttpRequest.responseText);
                    if (Ctype == "Left") {
                        jq("#ifm1").attr("srcdoc", 'The server is error,please try it again');
                    }
                    else 
                        if (Ctype == "Right") {
                            jq("#ifm2").attr("srcdoc", 'The server is error,please try it again');
                        }
                    jq('.dim').hide();
                    jq('.loading').hide();
                    jq("#btnStart").val('Start');
                },
                
                success: function(data){
                    beginLoadDomTime = new Date().getTime();
                    var data = eval(data);
                    fileName = data.filePath;
                    if (data.requestTimeSpan.length > 0) {
                        console.log('requestTimeSpan:' + data.requestTimeSpan);
                    }
                    if (Ctype == "Left") {
                        jq("#btnStart").val("Annotate this page");
                        jq("#ifm1").attr("srcdoc", data.HTML);
                        if (data.HTML == 'connect time out') {
                            var info = "<html lang=\"en\"><head><link href=\"/css/404.css\" rel=\"stylesheet\" media=\"screen\"></head><body><div class=\"content\">" +
                            "<h2>Connect time out</h2>" +
                            "<p class=\"text\">" +
                            "The connect is time out,please check your url and try it again" +
                            "</p>" +
                            "</div></body></html>";
                            jq("#ifm1").attr("srcdoc", info);
                            jq("#btnStart").val('Start');
                        }
                    }
                    else {
                        jq("#ifm2").attr("srcdoc", data.HTML);
                        jq("#btnStart").val('Continue Browser');
                        if (data.HTML == 'connect time out') {
                            jq("#ifm2").removeAttr('srcdoc');
                            jq("#ifm2").attr("src", "/error/404.html");
                        }
                        
                    }
                    jq('.dim').hide();
                    jq('.loading').hide();
                    isloaded = true;
                    if (typeof(callback) == 'function') {
                        callback(param);
                    }
                    
                }
            });
        }
        else {
            jBox('please input correct url.', {
                title: "Message",
            });
            
        }
    }
    //Load Url end
    
    //calc the loading position begin
    var position = {
        left: jq('.dim').width() / 2 - jq('.loading').width() / 2,
        top: jq('.dim').height() / 2 - jq('.loading').height() / 2
    };
    jq('.loading').css({
        'top': '' + position.top + 'px ',
        'left': position.left + 'px'
    });
    //calc the loading position end
    //document key down,up begin
    jq(document).keydown(function(event){
        if (event.keyCode == 13) {
            jq('#btnStart').click();
        }
        jq('#ifm2')[0].contentWindow.xpath.isCtrl = event.ctrlKey;
        console.log('event.keyCode:' + event.keyCode);
    })
    
    jq(document).keyup(function(event){
        jq('#ifm2')[0].contentWindow.xpath.isCtrl = false;
    })
    //document key down,up end
    
    //set the iframe begin	
//    jq('iframe').each(function(){
//        jq(this).width(jq('.dim').width() / 2 - minusSize.width);
//        jq(this).height(jq('.dim').height() - minusSize.height);
//        jq(this).css('marginTop', minusSize.height);
//    })
//    jq('.split').height(jq('.dim').height() - 7);
//    jq('.split').css('left', jq('.dim').width() / 2 - minusSize.width / 2);
    //set the iframe end
    
    //Judge Url begin 
    function IsURL(str_url){
        var strRegex = "^((https|http|ftp|rtsp|mms)://)?[a-z0-9A-Z]{0,10}\.[a-z0-9A-Z][a-z0-9A-Z]{0,61}?[a-z0-9A-Z]\.com|net|cn|cc (:s[0-9]{1-4})?/$";
        var re = new RegExp(strRegex);
        str_url = str_url.trim();
        if (re.test(str_url)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    //Judge Url end
    
//    jq(window).resize(function(){
//        jq('iframe').each(function(){
//            jq(this).width(jq('.dim').width() / 2 - minusSize.width);
//            jq(this).height(jq('.dim').height() - minusSize.height);
//            jq(this).css('marginTop', minusSize.height);
//        })
//        jq('.split').height(jq('.dim').height() - 7);
//        jq('.split').css('left', jq('.dim').width() / 2 - minusSize.width / 2);
//        
//    });
    
    
    
    
    function keyVSvalue(data, state){
        var keys = jq('#ifm2')[0].contentWindow.xpath.keyList;
        var values = jq('#ifm2')[0].contentWindow.xpath.valueList;
        if (state) {
            for (var i = 0; i < keys.length; i++) {
                if (data == keys[i]) {
                    return values[i];
                }
            }
            return null;
        }
        else {
            for (var i = 0; i < values.length; i++) {
                if (data == values[i]) {
                    return keys[i];
                }
            }
            return null;
        }
    }
    
    function updateItems(key, value){
    
        jq(".select-key")[0].innerHTML = key + ':';
        jq(".select-key-value")[0].innerHTML = value;
        
        if (editKey == null) {
            var rowData = document.createElement("tr");
            rowData.className = "table-row";
            rowData.innerHTML = "<td class='table-column-1'>" + key + "</td>" +
            "<td class='table-column-2'><button style='width:95%;height:100%;border-radius:8px;'>Edit</button></td>" +
            "<td class='table-column-3'><button style='width:95%;height:100%;border-radius:8px;'>Delete</button></td>";
            jq('#items').append(rowData);
        }
        else {
            var items = jq('.table-column-1');
            for (var i = 0; i < items.length; i++) {
                if (items[i].innerHTML == editKey) {
                    jq('.table-column-1')[i].innerHTML = key ;
                    break;
                }
            }
        }
    }
    
    jq('#items').click(function(e){
        if (e.target.className == "table-column-1") {
            if (jq('#ifm2')[0].contentWindow.xpath.keyList) {
                var key = e.target.innerHTML;
                if (keyVSvalue(key, true)) {
                    jq('.select-key')[0].innerHTML = key + ':';
                    jq('.select-key-value')[0].innerHTML = keyVSvalue(key, true);
                }
            }
            
        }
        else 
            if (e.target.parentNode.className == "table-column-2") {
            
            	var parentXpath = jq('#ifm2')[0].contentWindow.xpath.parentXpath;
                var key = e.target.parentNode.parentNode.childNodes[0].innerHTML;
                jq(".dim").show();
                jq('.data_acquisition_alert').show();
                jq("#txtValue").val(key);
                jq("#txtParentXpath").val(parentXpath);
                jq("#txtXpath").val(keyVSvalue(key, true));
                editKey = key;
            }
            else 
                if (e.target.parentNode.className == "table-column-3") {
                    e.target.parentNode.parentNode.remove();
                    var xpathObj=jq('#ifm2')[0].contentWindow.xpath;
                    var key = e.target.parentNode.parentNode.childNodes[0].innerHTML;
                    var value = keyVSvalue(key, true);
                    
                    var keys = xpathObj.keyList;
                    var values = xpathObj.valueList;
                    
                   // var newKeys = null;
                  //  var newValues = null;
//                    if (keys.length >= 1) {
//                        keys.splice(jq.inArray(key, keys), 1);
//                        values.splice(jq.inArray(value, values), 1);
//                    }
                    
                    jq("#txtValue").val(key);
                    jq("#txtXpath").val(value);
                    
                    
                    var currentDeleteDom=xpathObj.getDomByXpath(value);
                    new xpathObj(jq(currentDeleteDom), 'xpath', true, 2, true);
                    
                    jq('#ifm2')[0].contentWindow.xpath.keyList = keys;
                    jq('#ifm2')[0].contentWindow.xpath.valueList = values;
                    
                }
        
    });
    
    jq('#save').click(function(){
    
        if (jq('#ifm2')[0].contentWindow.xpath.keyList) {
        
            var projectName = jq("#name").val();
            var url = jq('#txtUrl').val();
            var keys = jq('#ifm2')[0].contentWindow.xpath.keyList.join('---');
            var values = jq('#ifm2')[0].contentWindow.xpath.valueList.join('---');
            
            var parentXpath = jq('#ifm2')[0].contentWindow.xpath.parentXpath;
            
            jq.ajax({
                url: '/job/ajax/report_list_ajax.jsp',
                type: 'post',
                dataType: 'json',
                data: {
                    "projectName": projectName,
                    "jobID": jobId
                },
                error: function(){
                    jBox('Error occurs at server.', {
                        title: "Message",
                    });
                },
                
                success: function(data){
                    var result = data.Result;
                    
                    if (result == "Error") {
                        jBox("You had a project with the same name" +
                            "please change your pojectName.", {
                            title:'Message' ,
                        });
                        
                        return;
                    }
                    else 
                        if (result == "OK") {
                            jq.ajax({
                                url: '/job/ajax/profile.jsp',
                                type: 'post',
                                dataType: 'json',
                                data: {
                                    "updataMode": updataMode,
                                    "name": projectName,
                                    "url": url,
                                    "fileName": fileName,
                                    "key": keys,
                                    "value": values,
                                    "type": "insert",
                                    "parentXpath": parentXpath
                                },
                                error: function(){
                                    jBox('Error occurs at server.', {
                                        title: 'Message',
                                    });
                                    
                                },
                                
                                success: function(data){
                                    if (data.Result == "OK") {
                                        updataMode = -1;
                                        location.href = "/job/page/reports.jsp";
                                    }
                                    else {
                                        jBox('Fail to save the data.', {
                                            title: 'Message',
                                        });
                                    }
                                    
                                }
                            });
                        }
                }
            });
            
        }
        
    });
    
    /*key value panel begin*/
    jq('.data_acquisition_alert_lbFooter input').each(function(){
        jq(this).click(function(){
        
            var key = jq("#txtValue").val();
            var value = jq("#txtXpath").val();
            
            if (jq(this).index() == 1) {
            	if(editKey == null){
            		var xpath = jq('#ifm2')[0].contentWindow.xpath;
                    var currentDom = xpath.getDomByXpath(value);
                    new xpath(jq(currentDom), 'xpath', true, 2, true);
            	}
            }
            else {
                if (key == "" || key == "undefined") {
                    jBox('please input the key', {
                        title: "Message",
                    });
                    jq("#txtValue").focus();
                    return false;
                }
                else {
                    if (editKey == null) {
                        jq('#ifm2')[0].contentWindow.xpath.keyList.push(key);
                        jq('#ifm2')[0].contentWindow.xpath.valueList.push(value);
                    }
                    else {
                        var index = -1;
                        for (var i = 0; i < jq('#ifm2')[0].contentWindow.xpath.valueList.length; i++) {
                            if (value == jq('#ifm2')[0].contentWindow.xpath.valueList[i]) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            jq('#ifm2')[0].contentWindow.xpath.keyList[index] = key;
                        }
                    }
                    
                    if (!(jq(".none-disp").is(":hidden"))) {
                        jq(".none-disp").hide();
                        jq(".output-block").show();
                    }
                    updateItems(key, value);
                }
            }
            jq('.data_acquisition_alert').hide();
            jq('.dim').hide();
            editKey = null;
        })
    })
    
});
