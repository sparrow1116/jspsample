<%@ include file="/common/pre-body.jsp"%>
<%-- <%@ include file="/job/controller/job_init_controller.jsp"%> --%>
<%
	String userSrl = (String) session.getAttribute("UserSrl");
	if (userSrl == null) {
		response.sendRedirect("/login.jsp");
		return;
	}
%>
<body>
	<%@ include file="/ui/navbar.jsp"%>
	<div class="container-fluid">
		<div class="row-fluid">
			<%@ include file="/ui/left_menu.jsp"%>
			<%@ include file="/ui/no_script.jsp"%>

			<div id="content" class="span10">
				<!-- content starts -->
				<%@ include file="/ui/breadcrumb.jsp"%>
				<div class="row-fluid sortable">
					<div class="box span12">
						<div data-original-title="" class="box-header well">
							<h2>
								<i class="icon-file"></i>Jobs
							</h2>
							<div class="pull-right">
								<!-- <input type="button" class="btn btn-primary" onclick='window.location.href="/job/page/job_edit.jsp?action=new"' value="New Job"/> -->
								<input type="button" class="btn btn-primary" id="newJob"
									value="Create New Job" />
							</div>
						</div>
						<div class="box-content">
							<form class="form-horizontal">
								<fieldset>
									<div id="tabs">
										<div id="ReportContentsLayer"></div>
									</div>
									<br /> <br />
								</fieldset>
							</form>
						</div>
					</div>

				</div>
				<!--/row-->

			</div>
			<!--/#content.span10-->

		</div>
		<!--/fluid-row-->

		<hr>

		<%@ include file="/ui/hide_model.jsp"%>
		<%@ include file="/ui/footer.jsp"%>

	</div>
	<!--/.fluid-container-->

	<%@ include file="/common/include-js.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {

			$('#newJob').click(function() {
				window.location.href = "/job/page/mainPage.jsp";
			});
			$('#ReportContentsLayer').jtable(jScanTableConfig).jtable('load');
		});

		function downloadReport(id) {
			window
					.open(
							'/report/page/report_download.jsp?reportID=' + id,
							'About',
							'width=650,height=500,left = 600, top =300, depended = yes, titlebar = no, toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
		}

		var jScanTableConfig = {
			title : 'Report',
			paging : true,
			pageSize : 20,
			sorting : false,
			selecting : true, //Enable selecting
			multiselect : true, //Allow multiple selecting
			selectOnRowClick : false,
			tablestyle : 'width: 100%',
			actions : {
				listAction : '/job/ajax/report_list_ajax.jsp'
			},
			fields : {
				id : {
					key : true,
					title : 'JobID',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;',
				},
				jobname : {
					key : true,
					title : 'JobName',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;',
					display : function(data) {
						var html = "";

						html += "<a  href='mainPage.jsp?jobId="
								+ data.record.id + "'> " + data.record.jobname
								+ '</a>';

						return html;
					}
				},
				swrapper : {
					key : true,
					columnResizable : false,
					title : 'Scraper Profile',
					width : '16%',
					headerStyle : 'text-align:center;',
					style : 'text-align:left;word-break:break-all ',
					display : function(data) {
						var html = data.record.swrapper;
						list = html.split(';');
						if (list.length > 0) {
							var ol = "<ol>";
							for ( var i = 0, len = list.length; i < len; i++) {
								ol += "<li>" + list[i] + "</li>";
							}
							ol += "</ol>";
							html = ol;
						}
						return html;
					}
				},
				cwrapper : {
					key : true,
					title : 'Classification Profile',
					width : '16%',

					headerStyle : 'text-align:center;',
					style : 'text-align:left;word-break:break-all',
				},
				cprocess : {
					key : true,
					columnResizable : false,
					title : 'Classfication Info',
					width : '20%',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;word-break:break-all ',
					display : function(data) {
						var jobId = data.record.id;
						var info = data.record.cinfo;
						var infoHtml=updateProcess.setInfo(info);
						var html = "<span id='cprocess_"+jobId+"'>"
								+ data.record.cprocess  + "%<br />"
								+ infoHtml + "</span>";
						return html;
					}
				},
				sprocess : {
					key : true,
					title : 'Scraper Info',
					width : '12%',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;word-break:break-all',
					display : function(data) {
						var jobId = data.record.id;
						var html = "<span id='sprocess_"+jobId+"'>"
								+ data.record.sprocess + "%</span>";
						return html;
					}
				},
				submittime : {
					title : 'SubmitTime',
					width : '8%',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;'
				},
				finishtime : {
					title : 'FinishTime',
					width : '8%',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;'
				},
				status : {
					title : 'Status',
					width : '8%',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;'
				},
				Action : {
					title : 'Action',
					width : '10%',
					headerStyle : 'text-align:center;',
					style : 'text-align:center;',
					display : function(data) {
						var html = "";
						var status = data.record.status;
						if (status == "submitted") {
							html += "<a class='btn btn-primary' href='javascript:start(\""
									+ data.record.id
									+ "\")'> "
									+ "Start"
									+ '</a>';
						} else if (status == "finished") {
							html += "<a class='btn btn-success' href='javascript:downloadReport(\""
									+ data.record.id
									+ "\")'> "
									+ "Download"
									+ '</a>';
						}

						/*html += "<a class='btn' href='javascript:viewConfig(\""
							+ data.record.ReportID + "\")'> " + "View Config" + '</a>'; */

						return html;
					}
				}
			}
		};

		function downloadReport(id) {

			window
					.open(
							'/job/page/report_download.jsp?id=' + id,
							'Download',
							'width=350,height=320,left = 600, top =300, depended = yes, titlebar = no, toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
		}

		function start(id) {
			new updateProcess(id).sInterval();
			$.ajax({
				url : '/job/ajax/job_start.jsp',
				type : 'post',
				dataType : 'json',
				data : {
					"jobid" : id
				},
				error : function() {
					alert('Error occurs at server.');
				},

				success : function(data) {

					if (data.Result == 'OK') {
						//alert("Process Start!!");
						//location.href = '/job/page/reports.jsp'; 				
					} else if (data.Message) {
						alert(data.Message);
					} else {
						alert("Failes");
					}
				}
			});
		}
		var updateProcess = function(jobId) {
			this.isFinish = true;
			this.jobId = jobId;
			this.currentObj = null;
			this.sProcessDomId = "#sprocess_" + jobId;
			this.cProcessDomId = "#cprocess_" + jobId;
			this.sProcess = 0;
			this.cProcess = 0;
			this.info = "";
		}
		/*get process begin*/

		updateProcess.prototype.update = function() {
			this.isFinish = false;
			var currentObj = this;
			$.ajax({
				url : '/job/ajax/loadProcess.jsp',
				type : 'post',
				dataType : 'json',
				data : "jobId=" + this.jobId,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('Error occurs at server.'
							+ XMLHttpRequest.responseText);
				},

				success : function(data) {
					data = eval(data);
					currentObj.isFinish = true;
					currentObj.sProcess = data.sProcess;
					currentObj.cProcess = data.cProcess;
					currentObj.info = data.info;

				}
			});
		}
		updateProcess.prototype.sInterval = function() {
			var currentObj = this;
			setInterval(
					function() {
						if (currentObj.isFinish) {
							if (parseInt(currentObj.sProcess) >= 100
									&& parseInt(currentObj.cProcess) >= 100) {
								clearInterval(currentObj.sInterval);
								location.href = '/job/page/reports.jsp';
							} else {
								currentObj.update();
							}
							$(currentObj.sProcessDomId).html(
									currentObj.sProcess + '%');
							$(currentObj.cProcessDomId)
									.html(
											updateProcess.setInfo(currentObj.info)
													+ currentObj.cProcess + '%');

						}
					}, 1000);
		}
		updateProcess.setInfo=function(info){
			var infoList=[];
			var infoHtml="";
			if (info != null && info.length > 0) {
				var infoList = info.split('|');
			}
			if(infoList.length>0){
				infoHtml="<table id='table_info' style='background:Transparent;' class='cinfolist'>";
				for(var i=0;i<infoList.length;i++){
					infoHtml+="<tr>";
					if(infoList[i].indexOf(':')>-1){
						var tempList=infoList[i].split(":");
						infoHtml+="<td align=left><strong>"+tempList[0]+"</strong></td>";
						infoHtml+="<td align=right>"+tempList[1]+"</td>";
					}else{
						infoHtml+="<td align=left colspan=2>"+infoList[i]+"</td>";
					}
					infoHtml+="</tr>";
				}
				infoHtml+="</table>";
			}
			return infoHtml;
		}

		/*get process begin*/
	</script>
</body>
</html>