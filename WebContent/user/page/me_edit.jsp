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
						<h2><i class="icon-edit"></i>Change Password</h2>
					</div>
					<div class="box-content">
						<form class="form-horizontal">
							<fieldset>
								<div class="control-group">
									<label for="focusedInput" class="control-label">Password</label>
									<div class="controls">
									  <input value="" id="password" class="input-xlarge focused" type="password"> (Password can only consist of numbers and letters)
									</div>									
							  	</div>
							  	<div class="control-group">
									<label for="focusedInput" class="control-label">Confirm Password</label>
									<div class="controls">
									  <input value="" id="confirmPassword" class="input-xlarge focused" type="password"> (Password can only consist of numbers and letters)
									</div>
							  	</div>
								  <div class="form-actions"> 
								  	<button class="btn btn-primary" type="button" onclick ="editSelf()" >Confirm</button>
							  		<button class="btn btn-primary" type="button" onclick="window.history.back()">Cancel</button>
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
				
		function editSelf(){
			
			var password = "";
			var confirmPassword = "";
			
			password = $('#password').val();
			confirmPassword = $('#confirmPassword').val();
			
			var reg=/[^A-Za-z0-9]/g;
								
			if(password != confirmPassword) {
				alert("Two passwords is not equal.");
			} else if (password == "" || confirmPassword == "") {
				alert("Please input the password");	
			} else if(reg.test(password)){
				alert("Password can only consist of numbers and letters");	
			}  else {
				$.ajax({
			        url:'/user/ajax/me_edit_ajax.jsp',
					type : 'post',
					dataType : 'json',
					data : {
						"password" : password
					},
					error : function() {
						alert('Error occurs at server.');
					},		
					success : function(data) {
						
						if (data.Result == 'OK') {
							alert("Change password Success");
							window.history.back();												
						} else if (data.Message) {
							alert(data.Message);
						} else {
							alert("Change password Fail");
						}
					}
				});
			}
		}
		
	</script>
	
		
</body>
</html>
