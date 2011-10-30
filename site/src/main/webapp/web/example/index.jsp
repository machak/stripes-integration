<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="StripesResources"/>
<%--@elvariable id="text" type="org.onehippo.forge.stripes.beans.TextDocument"--%>
<%--@elvariable id="document" type="org.onehippo.forge.stripes.beans.NewsDocument"--%>
<%--@elvariable id="user" type="org.onehippo.forge.stripes.beans.User"--%>
<h1>stripes jsp</h1>
${document.title}
<div>
  <hst:html hippohtml="${document.html}" />
</div>

User:${user}


<s:form action="/web/example/test.action" event="saveUser">
    <s:errors field="user.name" />
    <s:text name="user.name" />
  <s:submit name="saveUser" />
</s:form>


<div>
  <a href="https://svn.onehippo.org/repos/closed/hippo/friday/stripes-integration/">source</a>
</div>