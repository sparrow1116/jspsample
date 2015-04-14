// JavaScript Document
(function(jq){
	jq.fn.extend({
		"changeTips":function(value){
			value = jq.extend({
				divTip:""
			},value)
			
			var jqthis = jq(this);
			var indexLi = -1;
			
			//点击document隐藏下拉层
			jq(document).click(function(event){
				if(jq(event.target).attr("class") == value.divTip || jq(event.target).is("li")){
					var liVal = jq(event.target).text();
					jqthis.val(liVal);
					blus();
				}else{
					blus();
				}
			})
			
			//隐藏下拉层
			function blus(){
				indexLi = -1;
				jq(value.divTip).hide();
			}
			
			//键盘上下执行的函数
			function keychang(up){
				if(up == "up"){
					if(indexLi == 0){
						indexLi = jq(value.divTip).children().length-1;
					}else{
						indexLi--;
					}
				}else{
					if(indexLi ==  jq(value.divTip).children().length-1){
						indexLi = 0;
					}else{
						indexLi++;
					}
				}
				jq(value.divTip).children().eq(indexLi).addClass("active").siblings().removeClass();	
			}
			
			//值发生改变时
			function valChange(){
				var tex = jqthis.val();//输入框的值
				var fronts = "";//存放含有“@”之前的字符串
				
				jq(value.divTip).show();
//					children().
//					each(function(index) {
//						var valAttr = jq(this).attr("email");
//						if(index==1){jq(this).text(tex).addClass("active").siblings().removeClass();}
//						//索引值大于1的LI元素进处处理
//						if(index>1){
//							//当输入的值有“@”的时候
//							if(af.test(tex)){
//								//如果含有“@”就截取输入框这个符号之前的字符串
//								fronts = tex.substring(tex.indexOf("@"),0);
//								jq(this).text(fronts+valAttr);
//								//判断输入的值“@”之后的值，是否含有和LI的email属性
//								if(regMail.test(jq(this).attr("email"))){
//									jq(this).show();
//								}else{
//									if(index>1){
//										jq(this).hide();
//									}	
//								}
//								
//							}
//							//当输入的值没有“@”的时候
//							else{
//								jq(this).text(tex+valAttr);
//							}
//						}
//	                })
					
			}
			
			
			//输入框值发生改变的时候执行函数，这里的事件用判断处理浏览器兼容性;
			
			jq(this).bind("click",function(e){
				valChange();
				e.stopPropagation();
//				if(jqthis.val() == 'w' || jqthis.val() == 'h'){
//					valChange();
//				}
			});
			jq(this).bind("input",function(e){
				blus();
			})
		
			

			//鼠标点击和悬停LI
			jq(value.divTip).children().
			hover(function(){
				indexLi = jq(this).index();//获取当前鼠标悬停时的LI索引值;
				if(jq(this).index()>=0){
					jq(this).addClass("active").siblings().removeClass();
				}	
			})
					
		
			//按键盘的上下移动LI的背景色
			jqthis.keydown(function(event){
				if(event.which == 38){//向上
					keychang("up")
				}else if(event.which == 40){//向下
					keychang()
				}else if(event.which == 13){ //回车
					if(indexLi>=0){
						var liVal = jq(value.divTip).children().eq(indexLi).text();
						jqthis.val(liVal);
					}
					blus();
				}
			})				
		}	
	})	
})(jQuery)