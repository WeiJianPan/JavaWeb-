<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>订单管理</title>

	<%-- 静态包含 base标签、css样式、jQuery文件 --%>
	<%@ include file="/pages/common/head.jsp"%>


</head>
<body>
	
	<div id="header">
			<img class="logo_img" alt="" src="../../static/img/logo.gif" >
			<span class="wel_word">订单管理系统</span>

		<%-- 静态包含 manager管理模块的菜单  --%>
		<%@include file="/pages/common/manager_menu.jsp"%>

	</div>

	<div id="main">
		<table>
			<tr>
				<td>日期</td>
				<td>金额</td>
				<td>状态</td>
				<td>详情</td>
				<td>发货</td>
			</tr>
			<c:forEach items="${requestScope.orders}" var="orders">
				<tr>
						<%--格式化日期输出--%>
					<td><fmt:formatDate value="${orders.createTime}" pattern="yyyy.MM.dd"/></td>
					<td>${orders.price}</td>
					<td>
						<c:if test="${orders.status eq 0}">未发货</c:if>
						<c:if test="${orders.status eq 1}">已发货</c:if>
						<c:if test="${orders.status eq 2}">已完成</c:if>
					</td>
					<td><a href="orderServlet?action=showOrderDetail&orderId=${orders.orderId}">查看详情</a></td>
					<td>
						<c:if test="${orders.status eq 0}">
							<a class="DeliverGoods"
							   href="orderServlet?action=changeOrderStatus&orderId=${orders.orderId}">发货</a>
						</c:if>

						<c:if test="${orders.status != 0}">
							<div>已发出</div>
						</c:if>
					</td>
				</tr>
			</c:forEach>

		</table>
	</div>


	<%--静态包含页脚内容--%>
	<%@include file="/pages/common/footer.jsp"%>


</body>
</html>