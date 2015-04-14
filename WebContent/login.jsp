
<%@ include file="/common/pre-body.jsp"%>

<body>
	<div class="container-fluid">
		<div class="row-fluid">

			<div class="row-fluid">
				<div class="span12 center login-header">
					<h2>Welcome to GUI Web Scrapper</h2>
				</div>
			</div>

			<div class="row-fluid">
				<div class="well span5 center login-box">
					<div class="alert alert-info">Please login with your User ID
						and Password.</div>
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

							<p class="center span5">
								<button type=button class="btn btn-primary" onclick="login()">Login</button>
								<button type=button class="btn btn-success"
									onclick="location.href='/user/page/user_register.jsp';">Register</button>
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
		$(function() {
			$("body").keydown(function(e) {
				if (e.keyCode == 13) {
					login();
				}
			})
		})

		function login() {

			var userID = "";
			var password = "";

			userID = $('#userID').val();
			password = $('#password').val();

			if (userID == null) {
				alert("Please input the right userName");
			} else if (password == null) {
				alert("Please input the password");

			} else {
				loginUser(userID, password);
			}
		}

		function loginUser(userID, password) {

			$.ajax({
				url : '/user/ajax/login_check_ajax.jsp',
				type : 'post',
				dataType : 'json',
				data : {
					"userID" : userID,
					"password" : password
				},
				error : function() {
					alert('Error occurs at server.');
				},

				success : function(data) {

					if (data.Result == 'OK') {
						location.href = '/job/page/reports.jsp';
					} else if (data.Message) {
						alert(data.Message);
					} else {
						alert("Log Fails");
					}
				}
			});
		}
	</script>


</body>
</html>
