<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>
<%@ page isErrorPage="true" %>
<%@ page contentType="text/html;charset=GBK"%>
<%--============================================================================
说明：当处理请求发生错误时显示的错误页面
============================================================================--%>
<html>
<head>
<%@ include file="/WEB-INF/jsp/layout/meta.jsp"%>
<title>springmvctest：错误提示页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <link rel="stylesheet" href="/styles/default.css" type="text/css" >

</head>
<body>
<script language="javascript">
function showhide(layerId){
     obj = document.getElementById(layerId);
     if(obj)
     {   
	     var isVisible = obj.style.display == "none";
	     obj.style.display = isVisible ? "" : "none";
     }
}
</script>
<div align="center" class="error">
  <p/>
  <p/>
  <p/>
  <p/>
  系统错误，请稍后重试，或者联系系统管理员：
  <s:property value="exception"/>
  <br>
  查看<a href="javascript:showhide('detail')">详细情况</a><p/>
  <div id="detail" align="left" style="display:none;border:1px dashed gray;padding:10px;margin:10px">
    <pre>
    <s:property value="exceptionStack"/>
    </pre>
  </div>
</div>

</body>
</html>
