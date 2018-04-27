<%--
  Created by IntelliJ IDEA.
  User: F�lix
  Date: 25/04/2018
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>


<display:table id="row" name="volumes" requestURI="${requestURI}" pagesize="5">
    <c:set var = "contiene" value = "false"/>
    <jstl:forEach items="${row.customers}" var="cliente">
        <jstl:if test="${cliente eq customer }">
            <c:set var = "contiene" value = "true"/>
        </jstl:if>
    </jstl:forEach>


    <acme:column code="volume.title" value="${row.title}"/>
    <acme:column code="volume.description" value="${row.description}"/>
    <acme:column code="volume.anyo" value="${row.anyo}" />



    <display:column>
        <jstl:if test="${contiene eq true }">
            <acme:button url="volume/customer/unsubscribe.do?volumeId=${row.id}" code="newsPaper.unsubscribe"/>
        </jstl:if>
    </display:column>

    <display:column>
        <jstl:if test="${contiene ne true}">
            <acme:button url="volume/customer/subscribe.do?volumeId=${row.id}" code="newsPaper.subscribe"/>
        </jstl:if>
    </display:column>


    <display:column>
        <acme:button url="newsPaper/listNewsPapersVNP.do?volumeId=${row.id}" code="volume.newsPapers.list"/>
    </display:column>

</display:table>


<input type="button" value="<spring:message code="customer.cancel" /> " onclick="goBack()">
<script>
    function goBack() {
        window.history.back()
    }
</script>
