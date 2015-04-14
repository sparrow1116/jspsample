<!-- left menu starts -->

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="span2 main-menu-span">
	<div class="well nav-collapse sidebar-nav">
		<ul class="nav nav-tabs nav-stacked main-menu">
			<li class="nav-header hidden-tablet">Main</li>
			<li><a class="ajax-link" href="/job/page/reports.jsp" id="mainMenuReport"><i class="icon-home"></i><span class="hidden-tablet"> Jobs</span></a></li>
			<li><a class="ajax-link" href="#" id="mainMenuReport"><i class="icon-star"></i><span class="hidden-tablet"> AutoScrapping</span></a></li>
			<li><a class="ajax-link" href="#" id="mainMenuReport"><i class="icon-wrench"></i><span class="hidden-tablet"> Reports</span></a></li>
			<!-- <li><a class="ajax-link" href="/job/page/reports.jsp" id="mainMenuScan"><i class="icon-eye-open"></i><span class="hidden-tablet"> Reports</span></a></li>
			<li><a class="ajax-link" href="/job/page/reports.jsp" id="mainMenuPolicy"><i class="icon-star"></i><span class="hidden-tablet"> Setting</span></a></li>
			<li><a class="ajax-link" href="/job/page/reports.jsp" id="mainMenuScript"><i class="icon-align-justify"></i><span class="hidden-tablet"> --</span></a></li>
			<li><a class="ajax-link" href="/job/page/reports.jsp" id="mainMenuTool"><i class="icon-wrench"></i><span class="hidden-tablet">--</span></a></li> -->
			<c:if test="${sessionScope.Role=='admin'}">
				<li><a class="ajax-link" href="/user/page/users.jsp"><i class="icon-user"></i><span class="hidden-tablet"> Users</span></a></li>
			</c:if>
		</ul>
	</div><!--/.well -->
</div><!--/span-->
<!-- left menu ends -->