<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="utf-8" />
	<title>注册用户列表</title>

	<meta name="description" content="Dynamic tables and grids using jqGrid plugin" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

	<!-- bootstrap & fontawesome -->
	<link rel="stylesheet" href="static/css/bootstrap.min.css" />
	<link rel="stylesheet" href="static/css/font-awesome/4.4.0/css/font-awesome.min.css" />

	<!-- page specific plugin styles -->
	<link rel="stylesheet" href="static/css/jquery-ui.min.css" />
	<link rel="stylesheet" href="static/css/datepicker.css" />
	<link rel="stylesheet" href="static/css/ui.jqgrid.css" />

	<!-- ace styles -->
	<link rel="stylesheet" href="static/css/ace.min.css" />

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

				<li>
					<a href="#">用户管理</a>
				</li>
				<li class="active">注册用户管理</li>
			</ul><!-- /.breadcrumb -->


		</div>

		<div class="page-content">

			<div class="hr   hr-dotted"></div>

			<form  id="frmSearch"  class="form-inline ">
				<input id="nickName" name="nickName" type="text" class="input-medium" placeholder="请输入用户昵称">
				<input id="createTime" name="createTime" type="text" class="input-medium" placeholder="注册日期">

				<button type="button" class="btn btn-info btn-sm  " onclick="search()">
					<i class="ace-icon fa fa-key bigger-110"></i>查询
				</button>
			</form>

			<div class="hr   hr-dotted clear"></div>

			<div class="row">
				<div class="col-xs-12">
					<!-- PAGE CONTENT BEGINS -->
					<table id="grid-table"></table>

					<div id="grid-pager"></div>

					<script type="text/javascript">
						var $path_base = "..";//this will be used for editurl parameter
					</script>

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

<!--[if !IE]> -->
<script type="text/javascript">
	window.jQuery || document.write("<script src='static/js/jquery.min.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]><!---->
<script type="text/javascript">
	window.jQuery || document.write("<script src='static/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<%--<![endif]-->--%>
<script type="text/javascript">
	if('ontouchstart' in document.documentElement) document.write("<script src='static/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<script src="static/js/jquery-ui.min.js"></script>
<script src="static/js/jquery-ui.custom.min.js"></script>

<!-- page specific plugin scripts -->
<script src="static/js/date-time/bootstrap-datepicker.min.js"></script>
<script src="static/js/jqGrid/jquery.jqGrid.js"></script>
<script src="static/js/jqGrid/i18n/grid.locale-en.js"></script>

<!-- ace scripts -->
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">

	var jsonCondition = {};

	function search() {
		var jsonArray =  jQuery("#frmSearch").serializeArray();
		for(var i=0;i<jsonArray.length;i++){
			var myobj = jsonArray[i];
			jsonCondition[myobj.name] = myobj.value;
		}

		jQuery("#grid-table").jqGrid('setGridParam',{
			page:1
		}).trigger("reloadGrid");
	}

	function mergeObject(src, dest) {
		var i;
		for(i in src) {
			dest[i]=src[i];
		}
		return dest;
	}
	function ajaxFailed(){

	}

	jQuery(function($) {


		$("#createTime").datepicker({format:'yyyy-mm-dd' , autoclose:true});


		var grid_selector = "#grid-table";
		var pager_selector = "#grid-pager";

		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
		})
		//resize on sidebar collapse/expand
		var parent_column = $(grid_selector).closest('[class*="col-"]');
		$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
			if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
				$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
			}
		})

		function imageFormat( cellvalue, options, rowObject ){
			return '<img src="'+cellvalue+'" width="50px" height="50px" />';
		}

//		$("#find_btn").click(function(){
//			var title = escape($("#title").val());
//			var sn = escape($("#sn").val());
//			$("#list").jqGrid('setGridParam',{
//				url:"do.php?action=list",
//				postData:{'title':title,'sn':sn}, //发送数据
//				page:1
//			}).trigger("reloadGrid"); //重新载入
//		});
		<%--gridComplete: function() {--%>
			<%--var ids = jQuery("#grid").jqGrid('getDataIDs');--%>
			<%--for (var i in ids) {--%>
				<%--var cl = ids[i];--%>
				<%--a = "<a title='编辑' href='<%=projectName %>/Web/login/doLogout.action' ><img src='../common/images/edit.png'  alt='编辑' style='height:22px;width:20px;' border='0'></a>";--%>
				<%--jQuery("#grid").jqGrid('setRowData', ids[i], {cz: a});--%>
			<%--}--%>
		<%--}--%>

		jQuery(grid_selector).jqGrid({

			datatype : function(postdata) {
				postdata = mergeObject(jsonCondition, postdata);

				jQuery.ajax({
					type : 'POST',
					contentType : 'application/json',
					url :  '/userListAjax.do',
					data : JSON.stringify(postdata),
					dataType : 'json',
					success : function(resp) {
						if (resp.code == 0) {
							var thegrid = jQuery(grid_selector)[0];
							thegrid.addJSONData(resp.result);
							styleCheckbox(thegrid);
							updateActionIcons(thegrid);
							updatePagerIcons(thegrid);
							enableTooltips(thegrid);

						} else {
//							alert(resp.errors[0].message);
							alert("no data");
						}
					},
					error : ajaxFailed
				});
			},
			loadui: 'block',
			loadtext: '加载中。。。',
			emptyrecords:'没有查询到数据',
			height: "60%",
			colNames:[  'uid','昵称','头像','来源', '国家', '省份','城市','性别','注册时间','描述', '操作'],
			colModel:[
				{name:'uid',index:'uid', width:40, sorttype:"int", editable: false},
				{name:'nickName',index:'nickName',sortable:false,  width:80,editable: true,editoptions:{size:"20",maxlength:"50"}, formatter:'showlink', formatoptions:{baseLinkUrl:'userList.do', addParam: '&action=edit', idName:'uid'}},
				{name:'avatar',index:'avatar',align:"center",sortable:false,  width:50,editable: false,formatter: imageFormat},
				{name:'source',index:'source', sortable:false, width:50,editable: false},
				{name:'country',index:'country',sortable:false,  width:50,editable: true},
				{name:'province',index:'province', sortable:false, width:50,editable: true},
				{name:'city',index:'city', width:50,sortable:false, editable: true},
				{name:'sex',index:'sex', width:40, sortable:false, editable: true,edittype:"select",editoptions:{value:"1:男;2:女"}},
				{name:'createTime',index:'createTime',width:90, editable:true, sorttype:"date",unformat: pickDate },
				{name:'description',index:'description', width:150, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"3",cols:"50"}},
				{name:'myac',index:'myac', width:80, fixed:true, sortable:false, resize:false,
					formatter:'actions',
					formatoptions:{
						keys:true,
						delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
						//editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
					}
				}
			],

			viewrecords : true,
			rowNum:10,
			rowList:[10,20,30],
			pager : pager_selector,
			altRows: true,
			//toppager: true,

			multiselect: true,
			//multikey: "ctrlKey",
			multiboxonly: true,
			editurl: "/userListAjax.do",//nothing is saved
			caption: "用户列表"
			,autowidth: true,
			sortname: 'uid',
			sortorder: 'desc',
			sortable:true


		});

		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		jQuery(  " .ui-pg-selbox").closest("td").before("<td dir='ltr'>每页条数</td>");


		//enable datepicker
		function pickDate( cellvalue, options, cell ) {
			setTimeout(function(){
				$(cell) .find('input[type=text]')
						.datepicker({format:'yyyy-mm-dd' , autoclose:true});
			}, 0);
		}


		//navButtons
		jQuery(grid_selector).jqGrid('navGrid',pager_selector,
				{ 	//navbar options
					edit: false,
//					editicon : 'ace-icon fa fa-pencil blue',
					add: false,
//					addicon : 'ace-icon fa fa-plus-circle purple',
					edit: false,
					del: true,
//					delicon : 'ace-icon fa fa-trash-o red',
					search: false,
//					searchicon : 'ace-icon fa fa-search orange',
					refresh: false,
//					refreshicon : 'ace-icon fa fa-refresh green',
					view: false,
//					viewicon : 'ace-icon fa fa-search-plus grey',
				},
				{},
				{},
				{
					//delete record form
					recreateForm: true,
					beforeShowForm : function(e) {
						var form = $(e[0]);
						if(form.data('styled')) return false;

						form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
						style_delete_form(form);

						form.data('styled', true);
					},
					onClick : function(e) {
						alert(1);
					},
					onclickSubmit:function(params, postdata){
						var formData = {};
						var rowid = $('#grid-table').getGridParam('selrow');

						// when the onclickSubmit event fires and calls this function,
						// a string containing a jqgrid colmodel column name needs to be
						// made available in order to modify that cell's value contained
						// in the postdata array prior to posting it to the server.

						columnName =  "uid";

						var value = $('#grid-table').jqGrid('getCell', rowid, columnName );

						formData[ columnName ] = value;

						return formData;
					}
				}
		);

//		jQuery(grid_selector).jqGrid('delGridRow',  {
//					reloadAfterSubmit:true,
//
//				}
//		);



		function style_delete_form(form) {
			var buttons = form.next().find('.EditButton .fm-button');
			buttons.addClass('btn btn-sm btn-white btn-round').find('[class*="-icon"]').hide();//ui-icon, s-icon
			buttons.eq(0).addClass('btn-danger').prepend('<i class="ace-icon fa fa-trash-o"></i>');
			buttons.eq(1).addClass('btn-default').prepend('<i class="ace-icon fa fa-times"></i>')
		}

		function beforeDeleteCallback(e) {
			var form = $(e[0]);
			if(form.data('styled')) return false;

			form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
			style_delete_form(form);

			form.data('styled', true);
		}

//		function beforeEditCallback(e) {
//			var form = $(e[0]);
//			form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
//			style_edit_form(form);
//		}



		//it causes some flicker when reloading or navigating grid
		//it may be possible to have some custom formatter to do this as the grid is being created to prevent this
		//or go back to default browser checkbox styles for the grid
		function styleCheckbox(table) {
			/**
			 $(table).find('input:checkbox').addClass('ace')
			 .wrap('<label />')
			 .after('<span class="lbl align-top" />')


			 $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
			 .find('input.cbox[type=checkbox]').addClass('ace')
			 .wrap('<label />').after('<span class="lbl align-top" />');
			 */
		}


		//unlike navButtons icons, action icons in rows seem to be hard-coded
		//you can change them like this in here if you want
		function updateActionIcons(table) {
			/**
			 var replacement =
			 {
                'ui-ace-icon fa fa-pencil' : 'ace-icon fa fa-pencil blue',
                'ui-ace-icon fa fa-trash-o' : 'ace-icon fa fa-trash-o red',
                'ui-icon-disk' : 'ace-icon fa fa-check green',
                'ui-icon-cancel' : 'ace-icon fa fa-times red'
            };
			 $(table).find('.ui-pg-div span.ui-icon').each(function(){
						var icon = $(this);
						var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
						if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
					})
			 */
		}

		//replace icons with FontAwesome icons like above
		function updatePagerIcons(table) {
			var replacement =
			{
				'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
				'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
				'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
				'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
		}

		function enableTooltips(table) {
			$('.navtable .ui-pg-button').tooltip({container:'body'});
			$(table).find('.ui-pg-div').tooltip({container:'body'});
		}
		//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');

	});
</script>
</body>
</html>
