<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<c:forEach items="${array}" var="a">
		<div class="commColumn" onclick="location.href='board.bo?moimNum=${a.num}';">
			<div class="commCard">
					<h4>
						<b>${a.name}</b>
					</h4>
					<p>${a.category}</p>
			</div>
		</div>
	</c:forEach>
