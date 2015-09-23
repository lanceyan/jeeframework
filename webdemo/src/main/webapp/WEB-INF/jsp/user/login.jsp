<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>登录页面 - webdemo</title>

		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="static/css/bootstrap.min.css" />
		<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="static/css/ace.min.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="static/css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="static/css/ace-rtl.min.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="static/css/ace-ie.min.css" />
		<![endif]-->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="static/js/html5shiv.js"></script>
		<script src="static/js/respond.min.js"></script>
		<![endif]-->
		<style>
			.loginerror { color: #990000; background: #fbe3e3; padding: 0 10px; overflow: hidden; display: none; }
			.loginerror { -moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px; }
			.loginerror p { margin: 10px 0; }
		</style>

	</head>

	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class="ace-icon fa fa-leaf green"></i>
									<span class="red">webdemo</span>
									<span class="white" id="id-text2">后台管理系统</span>
								</h1>
								<h4 class="blue" id="id-company-text">&copy; hyfaytech.com </h4>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="ace-icon fa fa-coffee green"></i>
												请输入登录信息
											</h4>
											<div class="loginerror" id="loginError"><p>${errorMsg }</p></div>
											<div class="space-6"></div>

											<form id="loginform" action="login.do" method="post">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="userName" name="userName" class="form-control" placeholder="请输入用户名" />
															<i class="ace-icon fa fa-bossUser"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="password" name="password"  class="form-control" placeholder="请输入密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
															<input type="checkbox" class="ace" id="remember" name="remember"    />
															<span class="lbl"> 记住我 </span>
														</label>

														<button type="button" class="width-35 pull-right btn btn-sm btn-primary" id="submitBtn">
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">登录</span>
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>


										</div><!-- /.widget-main -->


									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->
							</div><!-- /.position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
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

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
			 $(document).on('click', '.toolbar a[data-target]', function(e) {
				e.preventDefault();
				var target = $(this).data('target');
				$('.widget-box.visible').removeClass('visible');//hide others
				$(target).addClass('visible');//show target
			 });
			});
			
			
			
			//you don't need this, just used for changing background
			jQuery(function($) {
			 $('#btn-login-dark').on('click', function(e) {
				$('body').attr('class', 'login-layout');
				$('#id-text2').attr('class', 'white');
				$('#id-company-text').attr('class', 'blue');
				
				e.preventDefault();
			 });
			 $('#btn-login-light').on('click', function(e) {
				$('body').attr('class', 'login-layout light-login');
				$('#id-text2').attr('class', 'grey');
				$('#id-company-text').attr('class', 'blue');
				
				e.preventDefault();
			 });
			 $('#btn-login-blur').on('click', function(e) {
				$('body').attr('class', 'login-layout blur-login');
				$('#id-text2').attr('class', 'white');
				$('#id-company-text').attr('class', 'light-blue');
				
				e.preventDefault();
			 });
			 
			});
		</script>
	</body>
</html>

<script type="text/javascript">

	function checkLoginForm(){
		var u = $('#userName');
		if(u.val() == '') {
			$('#loginError  p').html('请输入用户名！');
			jQuery('#loginError').slideDown();
			u.focus();
			return false;
		}

		var p = $('#password');
		if(p.val() == '') {
			$('#loginError  p').html('请输入密码！');
			jQuery('#loginError').slideDown();
			p.focus();
			return false;
		}
		return true;
	}

	function submitLogin(){

		$('#loginform').submit();
		return true;
	}

	$(document).ready(function(){

		$('#loginform').submit(function(){

			return checkLoginForm();

		});

		$('#submitBtn').click(function(){
			submitLogin();
		});

		$('#userName').keypress(function(){
			jQuery('#loginError').slideUp();
		});

		<c:if test="${ requestScope.error != null}">
			var errMsg = '${error.message }';

			if(errMsg != '') {
				$('.loginerror  p').html(errMsg);
				jQuery('#loginError').slideDown();
				$('#userName').focus();
			}
		</c:if>
	});


</script>
