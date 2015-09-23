<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<html>
<head>
<%@ include file="/WEB-INF/jsp/layout/meta.jsp"%>
<title>springmvctest：页首</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" type="text/css" href="<s:url value="/styles/displaytag/css/screen.css"/>">
<%--  引用struts标签到如css样式 --%>
<link rel="stylesheet" href="<s:url value="/styles/default.css"/>"
	type="text/css" > 
</head>
<body>
<table width="100%" height="100%" border="0" cellspacing="0" border="0">
  <tr  height="30">
    <td class="header" valign="top" colspan="2">
    	<div align="right" class="logo">springmvctest：页首</div>
    </td>
  </tr>
  <tr>
    <td valign="top" align="center" class="menu"  width="10%" nowrap>
       <%@ include file="/WEB-INF/jsp/layout/menu.jsp" %>
    </td>
    <td valign="top" class="content" width="90%">
        欢迎
    </td>
  </tr>
  <tr valign="bottom" >
    <td class="footer" colspan="2">
        <%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
    </td>
  </tr>
</table>
</body>
</html>
