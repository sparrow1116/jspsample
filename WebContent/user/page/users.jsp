<%@ include file="/common/pre-body.jsp" %>
<%
	String userSrl = (String)session.getAttribute("UserSrl");
	if(userSrl == null){
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
								<i class="icon-user"></i>User
							</h2>
						</div>
						<div class="box-content">
							<form class="form-horizontal">
								<fieldset>
									<div id="tabs">
										<div id="UserListContentsLayer"></div>
									</div>
									<br/>
									<div>
										<input type="button" class="btn btn-primary"
											onclick='window.location.href="/user/page/user_add.jsp"'
											value="Add User" />
									</div>
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
	 	$(document).ready(function () {
				$("#tabs").tabs({
					selected : 0
				});
				jTableLoadCommonListContentsLayer();
		}); 
			
		function jTableLoadCommonListContentsLayer() {
	
			var jTableConfig = {
	       		title:  'User List',
	       		paging: true,
	        	pageSize: 20,
	        	sorting: true,
	       	 	selecting: true,     //Enable selecting
	        	multiselect: true,     //Allow multiple selecting
	        	selectOnRowClick: false,
	        	tablestyle: 'width: 100%',
	        	actions: {
	           		 listAction: '/user/ajax/user_list_ajax.jsp?'              
	        },
	        fields: {
	            UserSrl: {
	        		key: true,
					create: false,
					edit: false,
					list: false
	            },
	            UserID: {
	            	title: 'User ID',
	                width: '25%',
	                headerStyle: 'text-align:center;',
		            style: 'text-align:center;',
		            display:function(data) {
		            	if(data.record.Status == "Disable"){
		            		var html = '';	            	
		            		html += '<a onClick="alertwhenDelete();">' + data.record.UserID + '</a>';		
		            		return html;
		            	}else{
		            		var html = '';	            	
		            		html += '<a href="/user/page/user_edit.jsp?UserID=' + data.record.UserID + '">' + data.record.UserID + '</a>';		
		            		return html;
		            	}
	            
	            	},
	            },
	            Role:{
	                title: 'Role',
	                width: '20%',
	                headerStyle: 'text-align:center;',
		            style: 'text-align:center;',
	            },
	            LastLoginDate:{
	                title: 'Login Dates',
	                width: '30%',
	                headerStyle: 'text-align:center;',
	                style: 'text-align:center;',
	                sorting: false,                           
	            },
	            Status:{
	            	title: 'Status',
	            	width: '25%',
	            	headerStyle: 'text-align:center;',
		            style: 'text-align:center;',
		            display:function (data){
		            	var html ='';
		            	html += "<a href='javascript:modifyStatus(\""+ data.record.Status + "\", \""+ data.record.UserSrl + "\")'>" + data.record.Status + '</a>';;
		            	return html;
		            }
	            }
	            
	        }
	    };
	    
	    $('#UserListContentsLayer').jtable(jTableConfig).jtable('load');
		}

		function alertwhenDelete() {
			alert("The User is Disable, please modify the Status first");
		}

		function modifyStatus(status, userSrl) {
			$.ajax({
				url : '/user/ajax/user_modify_status_ajax.jsp',
				type : 'post',
				dataType : 'json',
				data : {
					"status" : status,
					"userSrl" : userSrl
				},
				error : function() {
					alert('Error');
				},

				success : function(data) {
					if (data.Result == 'OK') {
						jTableLoadCommonListContentsLayer();				

					} else if (data.Message) {
						alert(data.Message);
					} else {
						alert("Add User Fail");
					}
				}
			});
		}
	</script>


</body>
</html>
