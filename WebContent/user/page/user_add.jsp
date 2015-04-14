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
						<h2><i class="icon-plus"></i>Add user</h2>
					</div>
					<div class="box-content">
						<form class="form-horizontal">
					        <fieldset>
					        	<div class="control-group">
									<label for="focusedInput" class="control-label">UserID</label>
									<div class="controls">
									  <input value="" id="userID" class="input-xlarge focused" type="text" maxlength="20"> (UseID can only consist of numbers and letters)
									</div>									
							  	</div>
							  	<div class="control-group">
									<label for="focusedInput" class="control-label">Password</label>
									<div class="controls">
									   <input type="password" class="input-xlarge focused" id = "password" maxlength = "20"> (Password can only consist of numbers and letters)				  
									</div>									
							  	</div>
							  	<div class="control-group">
									<label for="focusedInput" class="control-label">Confirm Password</label>
									<div class="controls">									   
					       				 <input type="password" class="input-xlarge focused" id = "passwordConfirm" maxlength = "20"> (Password can only consist of numbers and letters)				  
									</div>									
							  	</div>
							  	<div class="controls">
								  <label class="checkbox">
										<input type="checkbox" id = "isAdmin" name = "isAdmin" value="Administrator" style="opacity: 0;">Administrator
								  </label>
								</div>
								<div class="form-actions"> 
					      			<button class="btn btn-large btn-primary" type="button"  onClick ="addUser()" >Confirm</button>
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
		
		
		function addUser(){		
			var userID ="";
			var password ="";
			var role ="";
			
			if($('input:checkbox[name="isAdmin"]').attr("checked")) {
				role = "admin";			
			}else{
				role ="user";
			}
			
			userID = $('#userID').val();
			password = $('#password').val();
			
			var reg=/[^A-Za-z0-9]/g;
											
			if($('#passwordConfirm').val() !=$('#password').val() ){
				alert("The password is different");
			}else if(userID == null){
				alert("Please input the right userName");
			}else if(password == null|| password == ""){
				alert("Please input the password");
				
			}else if(reg.test(userID)){
				alert("UseID can only consist of numbers and letters");
			}else if(reg.test(password)){
				alert("Password can only consist of numbers and letters");	
			}
			else{
				addUserToDB(userID, password, role);
			}
		}
		
		
		function addUserToDB(userID, password, role){
		
		$.ajax({
		        url:'../ajax/user_add_save_ajax.jsp',
				type : 'post',
				dataType : 'json',
				data : {
					"userID" : userID,
					"password" : password,
					"role" : role
				},
				error : function() {
					alert('Error');
				},
		
				success : function(data) {
					if (data.Result == 'OK') {
						alert("Add User Success");
						location.href = 'users.jsp'; 	
										
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
