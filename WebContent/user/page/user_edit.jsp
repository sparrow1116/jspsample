<%@ include file="/common/pre-body.jsp" %>   

<body>
	<%@ include file="/ui/navbar.jsp" %>
	<div class="container-fluid">
		<div class="row-fluid">
			<%@ include file="/ui/left_menu.jsp" %>
			<%@ include file="/ui/no_script.jsp" %>
			
			<div id="content" class="span10">
			<!-- content starts -->
			<%@ include file="/ui/breadcrumb.jsp" %>
			<div class="row-fluid sortable">		
				<div class="box span12">
					<div data-original-title="" class="box-header well">
						<h2><i class="icon-edit"></i>Edit user</h2>
					</div>
					<div class="box-content">
						<form class="form-horizontal">
					        <fieldset>
					        	<div class="control-group">
									<label for="focusedInput" class="control-label">UserID</label>
									<div class="controls">
									  <input value='${param.UserID}' id="userID" disabled="disabled" class="input-xlarge focused" type="text" value='${param.UserID}'>
									</div>									
							  	</div>
							  	<div class="control-group">
									<label for="focusedInput" class="control-label">Password</label>
									<div class="controls">
									   <input type="password" class="input-xlarge focused" id = "password"> (Password can only consist of numbers and letters)					  
									</div>									
							  	</div>
							  	<div class="control-group">
									<label for="focusedInput" class="control-label">Confirm Password</label>
									<div class="controls">									   
					       				 <input type="password" class="input-xlarge focused" id = "confirmPassword"> (Password can only consist of numbers and letters)			  
									</div>									
							  	</div>
							  	<div class="controls">
								  <label class="checkbox">
										<input type="checkbox" id = "isAdmin" name = "isAdmin" value="Administrator" style="opacity: 0;">Administrator
								  </label>
								</div>
								<div class="form-actions"> 
					      			<button class="btn btn-large btn-primary" type="button"  onClick ="editUser()" >Confirm</button>
					     			<button class="btn btn-large btn-primary" type="button" onclick="window.history.back()">Cancel</button>
					     		</div>
							  </fieldset>
						  </form>
						 
					</div>
				</div>
			
			</div><!--/row-->
		
			</div><!--/#content.span10-->
			
		</div><!--/fluid-row-->
				
		<hr>
		
		<%@ include file="/ui/hide_model.jsp" %>
		<%@ include file="/ui/footer.jsp" %>
		
	</div><!--/.fluid-container-->
	
	<%@ include file="/common/include-js.jsp" %>   
	<script type="text/javascript">
		
		$(document).ready(function () {
		});
				
		function editUser(){
			
			var userID = "";
			var password = "";
			var confirmPassword = "";
			var admin = "user";
			var reg=/[^A-Za-z0-9]/g;
			
			userID = $("#userID").val();
			password = $('#password').val();
			confirmPassword = $('#confirmPassword').val();
			if($('input:checkbox[name="isAdmin"]').attr("checked")) admin = "admin";
								
			if(password != confirmPassword) {
				alert("Two passwords is not equal.");
			} else if (password == "" || confirmPassword == "") {
				alert("Please input the password");	
			}else if(reg.test(password)){
				alert("Password can only consist of numbers and letters");	
			} else {
				$.ajax({
			        url:'../ajax/user_edit_ajax.jsp',
					type : 'post',
					dataType : 'json',
					data : {
						"userID" : userID,
						"password" : password,
						"admin" : admin
					},
					error : function() {
						alert('Error occurs at server.');
					},		
					success : function(data) {
						
						if (data.Result == 'OK') {
							alert("Edit User Success");
							location.href = 'users.jsp'; 												
						} else if (data.Message) {
							alert(data.Message);
						} else {
							alert("Edit User Fail");
						}
					}
				});
			}
		}
		
	</script>
	
		
</body>
</html>
