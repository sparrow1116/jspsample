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
						<h2><i class="icon-edit"></i>Policy -- Basic Information</h2>
					</div>
					<div class="box-content">
						<form class="form-horizontal">
							<fieldset>
								<div class="control-group">
									<label for="focusInput" class="control-label">Name</label>
									<div class="controls">
									  <input type="text" id="policyName" class="input-xlarge"
									  <c:if test='${param.action ne "new"}'>
									  	disabled="disabled"
									  </c:if>
									   value='${requestScope.policyName}'>
									</div>
							  	</div>
								<div class="control-group">
									<label for="focusInput" class="control-label">Description</label>
									<div class="controls">
									  <input type="text" id="policyDescription" class="input-xlarge" value='${requestScope.policyDescription}'>
									</div>
							  	</div>
							  <div class="form-actions">						
								<input type="button" class="btn btn-primary" onclick='editPolicy()' value="Next"/>
								<input type="button" class="btn btn-primary" onclick='window.history.back()' value="Cancel"/>
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
		
		function editPolicy(){
			var name = $('#policyName').val();
			var description = $('#policyDescription').val();
								
			if(name == null || name == ""){
				alert("Please input the right name");
				return;
			}
			
			$.ajax({
		        url:'/policy/ajax/policy_edit_ajax.jsp',
				type : 'post',
				dataType : 'json',
				data : {
					"name" : name,
					"description" : description,
				},
				error : function() {
					alert('Error');
				},
		
				success : function(data) {
					if (data.Result == 'OK') {
						location.href = '/policy/page/policy_edit_script.jsp'; 											
					} else if (data.Message) {
						alert(data.Message);
					} else {
						alert("Add policy Fail");
					}
				}
			});
		}
	</script>
</body>
</html>