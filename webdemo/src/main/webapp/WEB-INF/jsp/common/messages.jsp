<%-- ActionError Messages - usually set in Actions --%>
<%@ page contentType="text/html;charset=GBK"%>
<s:if test="hasActionErrors()">
    <div class="error" id="errorMessages">    
      <s:iterator value="actionErrors">
        <img src="<s:url value="/images/iconWarning.gif"/>"
            alt="少御" class="icon" />
        <s:property escape="false"/><br />
      </s:iterator>
   </div>
</s:if>

<%-- FieldError Messages - usually set by validation rules --%>
<s:if test="hasFieldErrors()">
    <div class="error" id="errorMessages">    
      <s:iterator value="fieldErrors">
          <s:iterator value="value">
            <img src="<s:url value="/images/iconWarning.gif"/>"
                alt="少御" class="icon" />
             <s:property escape="false"/><br />
          </s:iterator>
      </s:iterator>
   </div>
</s:if>


