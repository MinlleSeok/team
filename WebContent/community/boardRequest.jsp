<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="commSec">	
	<form action="request.bo" method="post">
		<div class="insertBtn">
			<label for="name">게시판명</label>
			<input type="text" name="name" id="name" class="insertBox" placeholder="게시판명을 입력해 주세요." required autofocus>
		</div>
		<div class="insertBtn">
			<label for="category">카테고리</label>
			<select name="category" id="category">
				<option value="game">게임</option>
				<option value="sport">스포츠</option>
				<option value="issue">이슈</option>
				<option value="love">연애</option>
			</select>
		</div>
		<button class="clickBtn requestClick" type="submit">게시판 만들기</button>	
	</form>
</div>
