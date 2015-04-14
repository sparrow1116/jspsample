<div class="navbar">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse"> <span
				class="icon-bar"></span> <span class="icon-bar"></span> <span
				class="icon-bar"></span>
			</a> <a class="brand" href="/job/page/reports.jsp"> <span>GUI
					Web Scraper System</span></a>

			<!-- user dropdown starts -->
			<div class="btn-group pull-right">

				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-user"></i><span class="hidden-phone">${sessionScope.UserID}</span>
					<span class="caret"></span>
				</a>

				<ul class="dropdown-menu">
					<li><a href="/user/page/me_edit.jsp">Account</a></li>
					<li><a href="/user/page/logout.jsp">Logout</a></li>
				</ul>
			</div>
			<!-- user dropdown ends -->

			<!-- <div class="top-nav nav-collapse">
					<ul class="nav">
						<li><a href="#">Visit Site</a></li>
						<li>
							<form class="navbar-search pull-left">
								<input placeholder="Search" class="search-query span2" name="query" type="text">
							</form>
						</li>
					</ul>
				</div>/.nav-collapse -->
		</div>
	</div>
</div>
<script>
	$(function() {
		$('.btn-group').click(function() {
			if ($('.dropdown-menu').is(":visible")) {
				$('.dropdown-menu').hide();
			} else {
				$('.dropdown-menu').show();
			}
		})
	})
</script>