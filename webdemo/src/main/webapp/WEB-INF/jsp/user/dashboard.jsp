<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>控制面板 - webdemo</title>

		<meta name="description" content="overview &amp; stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="static/css/bootstrap.min.css" />
		<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />

		<!-- page specific plugin styles -->

		<!-- ace styles -->
		<link rel="stylesheet" href="static/css/ace.min.css?t=201508131539" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="static/css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		<link rel="stylesheet" href="static/css/ace-rtl.min.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="static/css/ace-ie.min.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="static/js/ace-extra.min.js"></script>

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="static/js/html5shiv.js"></script>
		<script src="static/js/respond.min.js"></script>
		<![endif]-->

		<style>
			.col-center-block {
				float: none;
				display: block;
				margin-left: auto;
				margin-right: auto;
			}
		</style>
	</head>

	<body class="skin-1">
	<jsp:include page="../include/nav.jsp"></jsp:include>

		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<jsp:include page="../include/left.jsp"></jsp:include>

			<div class="main-content">
				<div class="breadcrumbs" id="breadcrumbs">
					<script type="text/javascript">
						try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
					</script>

					<ul class="breadcrumb">
						<li>
							<i class="ace-icon fa fa-home home-icon"></i>
							<a href="#">Home</a>
						</li>
						<li class="active">Dashboard</li>
					</ul><!-- /.breadcrumb -->


				</div>

				<div class="page-content">

					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<div class="alert alert-block alert-success">
								<button type="button" class="close" data-dismiss="alert">
									<i class="ace-icon fa fa-times"></i>
								</button>

								<i class="ace-icon fa fa-check green"></i>

								欢迎访问webdemo后台管理系统

							</div>

							<div class="hr   hr-dotted"></div>

							<div class="row">
								<div class="widget-box ">
									<div class="widget-header widget-header-flat">
										<h4 class="widget-title lighter">
											<i class="ace-icon fa fa-star orange"></i>
											总数统计
										</h4>

										<div class="widget-toolbar">
											<a href="#" data-action="collapse">
												<i class="ace-icon fa fa-chevron-up"></i>
											</a>
										</div>
									</div>

									<div class="widget-body">
										<div class="widget-main no-padding">
											<div class="col-sm-7 infobox-container col-center-block">
												<div class="infobox infobox-green">
													<div class="infobox-icon">
														<i class="ace-icon fa fa-comments"></i>
													</div>

													<div class="infobox-data">
														<span class="infobox-data-number">${result.userCount}</span>
														<div class="infobox-content">用户</div>
													</div>
												</div>

												<div class="infobox infobox-blue">
													<div class="infobox-icon">
														<i class="ace-icon fa fa-twitter"></i>
													</div>

													<div class="infobox-data">
														<span class="infobox-data-number">${result.userCount}</span>
														<div class="infobox-content">频道</div>
													</div>
												</div>

												<div class="infobox infobox-pink">
													<div class="infobox-icon">
														<i class="ace-icon fa fa-shopping-cart"></i>
													</div>

													<div class="infobox-data">
														<span class="infobox-data-number">${result.userCount}</span>
														<div class="infobox-content">内容</div>
													</div>
												</div>
											</div>

										</div><!-- /.widget-main -->
									</div><!-- /.widget-body -->
								</div>



								<div class="vspace-12-sm"></div>


							</div><!-- /.row -->

							<div class="hr hr32 hr-dotted"></div>
							<!-- PAGE CONTENT ENDS -->
						</div><!-- /.col -->
					</div><!-- /.row -->
				</div><!-- /.page-content -->
			</div><!-- /.main-content -->

			<jsp:include page="../include/footer.jsp"></jsp:include>

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<%--<!--[if !IE]> -->--%>
		<%--<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>--%>

		<%--<!-- <![endif]-->--%>

		<%--<!--[if IE]>--%>
<%--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>--%>
<%--<![endif]-->--%>

		<%--<!--[if !IE]> -->--%>
		<script type="text/javascript">
			window.jQuery || document.write("<script src='static/js/jquery.min.js'>"+"<"+"/script>");
		</script>

		<%--<!-- <![endif]-->--%>

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='static/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='static/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

		<!-- page specific plugin scripts -->

		<!--[if lte IE 8]>
		  <script src="static/js/excanvas.min.js"></script>
		<![endif]-->

		<script src="static/js/jquery-ui.custom.min.js"></script>
		<script src="static/js/jquery.ui.touch-punch.min.js"></script>

		<!-- ace scripts -->
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {

			
			

			})
		</script>
	</body>
</html>
