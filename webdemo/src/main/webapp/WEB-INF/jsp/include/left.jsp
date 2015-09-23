<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/include_taglibs.jsp" %>
<div id="sidebar" class="sidebar                  responsive">
    <script type="text/javascript">
        try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
    </script>

    <ul class="nav nav-list">
        <li class="<c:if test="${fn:startsWith(requestScope.currentUri, '/dashboard')}">active</c:if>">
            <a href="/dashboard.do">
                <i class="menu-icon fa fa-tachometer"></i>
                <span class="menu-text"> Dashboard </span>
            </a>

            <b class="arrow"></b>
        </li>

        <li class="<c:if test="${fn:startsWith(requestScope.currentUri, '/user')}">active</c:if>">
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-user"></i>
                <span class="menu-text"> 用户管理 </span>

                <b class="arrow fa fa-angle-down"></b>
            </a>

            <b class="arrow"></b>

            <ul class="submenu">

                <li class="<c:if test="${fn:startsWith(requestScope.currentUri, '/userList')}">active</c:if>">
                    <a href="userList.do">
                        <i class="menu-icon fa fa-caret-right"></i>
                        注册用户管理
                    </a>

                    <b class="arrow"></b>
                </li>
            </ul>
        </li>

    </ul><!-- /.nav-list -->

    <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
        <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
    </div>

    <script type="text/javascript">
        try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
    </script>
</div>