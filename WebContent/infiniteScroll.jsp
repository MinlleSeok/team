<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String path=request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style type="text/css">
	.ssss{width:400px; height: 500px; background: red; margin: 20px auto;}		
</style>

<!-- JQuery 연동 -->
<script src="https://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>

	<div class="divWrap">
		<div class="ssss">무한스크롤 연습1</div>
		<div class="ssss">무한스크롤 연습2</div>		
	</div>
	
<script>
	$(function(){
		
		
		// 시작
		var count = 2;
		
		for(var i=3;i<=5;i++){
			
			count=i;
			$(".divWrap").append("<div class='ssss'>무한스크롤 연습"+count+"</div>");
			
		}
		
		if(count==5){
			$(window).bind("scroll", iScroll);			
		}
		
		// 스크롤 함수	
		function iScroll(){
			var docHeight = $(document).height();
			var scrollTop = $(window).scrollTop();
			var winHeight = $(window).height();
			var scrollHeight = scrollTop + winHeight;
			
			console.log("docHeight  :"+ docHeight);
			console.log("scrollTop  :"+ scrollTop);
			console.log("winHeight  :"+ winHeight);
			console.log("scrollHeight  :"+ scrollHeight);
			
			if(docHeight==scrollHeight){
				
				for(var i=1;i<=5;i++){					
					count++;
					
					$(".divWrap").append("<div class='ssss'>무한스크롤 연습"+count+"</div>");		
				}			
			}
			
		}
		
		
		
		
		
		
	});


</script>	
	
	
	
	


	
</body>
</html>

	
	
	<!-- <script>
    var page = 2;

    $(window).scroll(function() {
        if ($(window).scrollTop() == $(document).height() - $(window).height()) {
          ++page;
          $("body").append('<div class="ssss">' + page + '</div>');

        }
    });
  </script> -->
	