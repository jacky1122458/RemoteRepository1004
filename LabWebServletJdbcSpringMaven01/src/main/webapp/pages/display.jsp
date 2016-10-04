<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
#showPhoto {
	position:absolute;
	z-index:1;
	left:600px;
	top:75px;
	width:300px;
	height:300px;
}
</style>
<script type="text/javascript">
function doMouseOver(path) {
	var showPhotoNode = document.getElementById("showPhoto");
	var imgNode = document.createElement("img");	//<img>
	imgNode.src = path								//<img src="">
	showPhotoNode.appendChild(imgNode);
}
function doMouseOut() {
	var showPhotoNode = document.getElementById("showPhoto");
	while(showPhotoNode.hasChildNodes()) {
		showPhotoNode.removeChild(showPhotoNode.childNodes[0]);
	}
}
</script>
<title>Display</title>
</head>
<body>

<h3>Select Product Table Result : ${fn:length(select)} row(s) selected</h3>
<c:if test="${not empty select}">
<table>
	<thead>
	<tr>
		<th>ID</th>
		<th>Name</th>
		<th>Price</th>
		<th>Make</th>
		<th>Expire</th>
		<th>Photo</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="bean" items="${select}">
		<c:url value="/pages/product.jsp" var="path">
			<c:param name="id" value="${bean.id}" />
			<c:param name="name" value="${bean.name}" />
			<c:param name="price" value="${bean.price}" />
			<c:param name="make" value="${bean.make}" />
			<c:param name="expire" value="${bean.expire}" />
		</c:url>
		<c:url value="/pages/photo.view" var="link">
			<c:param name="photoid" value="${bean.id}" />
		</c:url>
	<tr>
		<td><a href="${path}">${bean.id}</a></td>
		<td>${bean.name}</td>
		<td>${bean.price}</td>
		<td><label onmouseover="doMouseOver('${link}')" onmouseout="doMouseOut()">${bean.make}</label></td>
		<td>${bean.expire}</td>
		<td><a href="${link}"><img src="../img/click.png"/></a></td>
	</tr>
	</c:forEach>
	
	</tbody>
</table>
</c:if>
<h3><a href="<c:url value="/pages/product.jsp" />">Product Table</a></h3>

<div id="showPhoto"></div>


</body>
</html>