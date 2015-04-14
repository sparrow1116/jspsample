
<%@ include file="/job/controller/main_ajax.jsp"%>
<%
	String userSrl = (String) session.getAttribute("UserSrl");
	if (userSrl == null) {
		response.sendRedirect("/login.jsp");
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="JavaScript" src="/js/jqueryForIframe.js"></script>
<script language="JavaScript" src="/js/mainPage.js"></script>
<script language="JavaScript" src="/js/dragPanel.js"></script>
<script language="JavaScript" src="/js/jbox.js"></script>
<link href="/css/data_acquisition_alert.css" rel='stylesheet'>
<link href="/css/bootstrap-cerulean.css" rel='stylesheet'>
<link href="/css/main.css" rel='stylesheet'>
<link href="/css/404.css" rel='stylesheet'>
<link href="/css/jbox.css" rel='stylesheet'>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Web GUI Scraper</title>

</head>
<body>

	<div class="dim" id='dim'></div>
	<div class="loading" id='loading'></div>

	<div class="job-frame" id="job">
		<div class="job-sidebar data_acquisition_alert_lbHeader" id="sidebar">
		<img class="arrow" src="/img/arrow_l_right_y.png"></img>
		</div>
		<div class='job-title'>Job Name:</div>
		<input class="job-name" id="name" value="new_job"></input>
		<div class="divider divider-first"></div>

		<div class="items-block">
			<div class="item-title">Select Items:</div>
			<table class="items-table" id="items">
				<!-- <tr class="table-row">
					<td class="table-column-1">keyname1</td>
					<td class="table-column-2"><button
							style="font-size: 20px; border-radius: 8px;">edit</button></td>
					<td class="table-column-3"><button
							style="font-size: 20px; border-radius: 8px;">delete</button></td>
				</tr> -->
			</table>
		</div>

		<div class="none-disp">
			<div
				style="font-size: 22px; font-weight: bold; margin-left: 22%; margin-top: 13%;">Nothing
				had selected</div>
			<div style="font-size: 20px; margin-left: 8%; margin-top: 9%;">please
				select something in the crawler page</div>
		</div>
		<div class="output-block">
			<div class="select-key"></div>
			<div class="select-key-value"></div>
			<button class="save-project" id="save">Save Project</button>
		</div>
	</div>



	<div class="data_acquisition_alert opaque white">
		<div id="uLightBox" class="shadow top bottom">
			<div id="lbHeader" class="data_acquisition_alert_lbHeader top">
				<header>Add Key</header>
			</div>
			<div id="lbContent" class="data_acquisition_alert_lbContent">
				<table width=95% cellpadding=0 cellspacing=0 border=0>
					<tr>
						<td align=right valign=middle width=20%>Key</td>
						<td width="5px"></td>
						<td><input style='height: 20px;' id='txtValue' /></td>
					</tr>
					<tr>
						<td height='10px'></td>
					</tr>
					<tr>
						<td align=right valign=middle width=20%>Interesting<br />
							Area XPath
						</td>
						<td></td>
						<td><textarea id='txtParentXpath' readonly='readonly'></textarea></td>
					</tr>
					<tr>
						<td height='10px'></td>
					</tr>
					<tr>
						<td align=right valign=middle width=20%>Selected<br />Area
							XPath
						</td>
						<td></td>
						<td><textarea id='txtXpath' readonly='readonly'></textarea></td>
					</tr>
				</table>

			</div>
			<div id="lbFooter" class="data_acquisition_alert_lbFooter bottom">
				<input type="button" class="flat floatRight" value="Ok"> <input
					type="button" class="flat floatRight" value="Cancel">
			</div>
		</div>
	</div>
	<!-- <div class="topbar"> -->
	<div class="navbar">
		<div class="navbar-inner">
			<div class="container-fluid">
				<!-- <input id='txtUrl' class='topbar_input' value='http://www.imdb.com'> -->
				<div class="inputURL">

					<input type="text" class="loginName"
						name="loginName" id="txtUrl" placeholder="input your url" />
					<ul class="on_changes">
						<li>http://www.imdb.com/title/tt3682448/?ref_=nm_flmg_dr_3</li>
						<li>http://www.imdb.com/title/tt0249968/?ref_=nm_flmg_wr_18</li>
						<li>http://www.imdb.com/title/tt0385880/?ref_=nm_flmg_prd_50</li>
						<li>http://www.imdb.com/title/tt0739529/?ref_=nm_flmg_act_31</li>
						<li>http://www.imdb.com/title/tt3316960/?ref_=nm_flmg_act_6</li>
					</ul>
				</div>
				<input id='btnStart' class='topbar_button btn btn-primary'
					type='button' value='Start'>
				<textarea id='txtXpath'
					style="width: 100%; height: 100px; display: none;">
					
			</textarea>
				<input id='btnBackJobList' class='topbar-button btn btn-primary'
					type='button' value="JobList">
			</div>
		</div>
	</div>
	<iframe seamless id='ifm1' class='leftIframe'
		src='/job/page/welcome.html?type=1'></iframe>
	<!-- <div class='split'></div> -->
	<iframe seamless id='ifm2' class='rightIframe'
		src='/job/page/welcome.html?type=2'></iframe>

	<input type="hidden" id='txtIsAnnotate' value='No'>

	<script language="JavaScript" src="/js/inputmail.js"></script>
	<script>
		jq("#txtUrl").changeTips({
			divTip : ".on_changes"
		});
	</script>
</body>
</html>