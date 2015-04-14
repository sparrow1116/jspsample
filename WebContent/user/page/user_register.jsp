
<%@ include file="/common/pre-body.jsp"%>

<body>
	<div class="container-fluid">
		<div class="row-fluid">

			<div class="row-fluid">
				<div class="span12 center login-header">
					<h2>Welcome to Web GUI Scraper</h2>
				</div>
				<!--/span-->
			</div>
			<!--/row-->

			<div class="row-fluid">
				<div class="well span5 center login-box">
					<div class="alert alert-info">Register User</div>
					<form class="form-horizontal" action="index.html" method="post">
						<fieldset>
							<div class="input-prepend" title="userID" data-rel="tooltip">
								<span class="add-on"><i class="icon-user"></i></span> <input
									autofocus class="input-large span10" name="userID" id="userID"
									type="text" />
							</div>
							<div class="clearfix"></div>

							<div class="input-prepend" title="Password" data-rel="tooltip">
								<span class="add-on"><i class="icon-lock"></i></span> <input
									class="input-large span10" name="password" id="password"
									type="password" />
							</div>
							<div class="clearfix"></div>

							<div class="input-prepend" title="Password" data-rel="tooltip">
								<span class="add-on"><i class="icon-lock"></i></span> <input
									class="input-large span10" name="passwordConfirm"
									id="passwordConfirm" type="password" />
							</div>
							<div class="clearfix"></div>

							<!-- 	<div class="input-prepend">
							<label class="remember" for="remember"><input type="checkbox" id="remember" />Remember me</label>
							</div> -->

							<p class="center span5">

								<button class="btn  btn-primary" type="button"
									onClick="addUser()">Confirm</button>
								<button class="btn  btn-success" type="button"
									onclick="window.history.back()">Cancel</button>

								<!-- <a class="btn btn-success" href="/user/user_register.jsp">
										<i class="icon-plus-sign icon-white"></i>  
									Register                                            
								</a> -->
							</p>
						</fieldset>
					</form>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
		</div>
		<!--/fluid-row-->

	</div>
	<!--/.fluid-container-->

	<%@ include file="/common/include-js.jsp"%>

	<script type="text/javascript">
		$(document).ready(function() {

		});

		function addUser() {

			var userID = "";
			var password = "";

			userID = $('#userID').val();
			password = $('#password').val();
			var reg = /[^A-Za-z0-9]/g;

			if ($('#passwordConfirm').val() != $('#password').val()) {

			} else if (userID == null) {

				jBox('Please input the right userName.', {
					title : "Message",
				});
			} else if (password == null || password == "") {
				jBox('Please input the password.', {
					title : "Message",
				});

			} else if (reg.test(userID)) {
				jBox('UseID can only consist of numbers and letters.', {
					title : "Message",
				});
			} else if (reg.test(password)) {
				jBox('Password can only consist of numbers and letters.', {
					title : "Message",
				});
			} else {
				addUserToDB(userID, password);
			}
		}

		function addUserToDB(userID, password) {

			$.ajax({
				url : '../ajax/user_register_save_ajax.jsp',
				type : 'post',
				dataType : 'json',
				data : {
					"userID" : userID,
					"password" : password

				},
				error : function() {
					alert('Error');
				},

				success : function(data) {
					if (data.Result == 'OK') {
						jBox('Add User Success.', {
							title : "Message",
							submit : function() {
								location.href = '/job/page/reports.jsp';
							}
						});

					} else if (data.Message) {
						jBox(data.Message, {
							title : "Message",
						});
					} else {
						jBox('Add User Fail.', {
							title : "Message",
						});
					}
				}
			});
		}
		function checkIsDuplicated() {

		}
	</script>


</body>
</html>
