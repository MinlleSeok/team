/* 전역변수로 사용할 필요 정보 받아오기  */
var userId = document.getElementById("userId").value;  //세션에서 받아온 사용자 id
var userPhoto = $("#userPhoto").val();
var userCity = $("#userCity").val();
var userDistrict = $("#userDistrict").val();
var userNickname = $("#userNickname").val();



/* 점3개메뉴 누르면 메뉴 펴지고 닫히기  */
function dotMenuToggle(timeLinuNum){
	
	var dotMenu= $("#dotMenu"+timeLinuNum);
	if(dotMenu.attr("class")==="dotMenu closed"){
		dotMenu.show(300);
		dotMenu.attr("class","dotMenu opened");
		
	}else{
		dotMenu.hide(300);
		dotMenu.attr("class","dotMenu closed");
	}

}

function photoToggle(timeLineNum, photoCount){
		
	var timePhoto = $(".timePhoto"+timeLineNum);
	var timePhotoHide = $(".timePhoto"+timeLineNum+":nth-child(n+2)");
	var photoToggle = $("#photoToggle"+timeLineNum);
	
	var closedClass = "imgCenter tpClosed timePhoto"+timeLineNum;
	var openedClass = "imgCenter tpOpened timePhoto"+timeLineNum;
	 
	
	if(timePhoto.attr("class")===closedClass){
		timePhoto.css('display','block');
		timePhoto.attr('class',openedClass);
		photoToggle.attr('class', 'tpHide');
		photoToggle.html("<span>다시접기</span>");
	}else{
		timePhotoHide.css('display','none');
		timePhoto.attr('class',closedClass);
		photoToggle.attr('class', '1');
		photoToggle.html("<span>+"+photoCount+"</span>");
	}
	
}


///////////////////////////////////////좋아요 부분 JS///////////////////////////////
/*좋아요 부분 출력하기*/
function printFavorite(timeLineNum, tlUserId){
	var favoriteWrap = $("#favoriteWrap"+timeLineNum);

	// 좋아요 부분 html 틀 넣기
	favoriteWrap.html(
			  "<span id='heartWrap"+timeLineNum+"'>"
			+ 	"<span id='heart"+timeLineNum+"' class='heartOff2' onclick=\"likeToggle("+timeLineNum+",'"+tlUserId+"');\"></span>"
			+ "</span>"																			
			+ "<span id='likesCountWrap"+timeLineNum+"' class='countNums'></span>"	
			+ "<span class='replyIcon2'></span>"		
			+ "<span id='replyCountWrap"+timeLineNum+"' class='countNums'></span>"
			+ "<span id='replyToggle"+timeLineNum+"' class='replyToggle mouseHand' onclick='replyToggle("+timeLineNum+");'>댓글더보기</span>"
	); //html 더하기 끝	
	printLikeCount(timeLineNum);
	printReplyCount(timeLineNum);
}


/*좋아요 누를때 하트 채우고 끄기 토글*/
function likeToggle(timeLineNum, tlUserId){
	
	var heartWrap = $("#heartWrap"+timeLineNum);	
	var heart = $("#heart"+timeLineNum);	
	
	//하트 클릭했는지 여부 체크
	$.ajax({
		method:"POST",
		cache:false,
		url:"likeClickCheckAction.ti?timeLineNum="+timeLineNum+"&likeId="+userId,				
	})
	
	.done(function(clickCheck){				
		// clickCheck = 0  : 좋아요 안누름  // 1=누름
		if(clickCheck==0){ 	// 기존에 안 누른 경우(add)
			
			heart.attr("class", "heartOn2");
			
			$.ajax({
				method:"POST",
				cache:false,
				url:"addLikeAction.ti?userId="+tlUserId+"&timeLineNum="+timeLineNum+"&likeId="+userId,				
			})
			
			.done(function(data){				
				printLikeCount(timeLineNum);
				//printReplyCount(timeLineNum);
			});
			
			
		}else{	// 기존에 누른 경우(del)
			
			heart.attr("class", "heartOff2");
			
			$.ajax({
				method:"POST",
				cache:false,
				url:"delLikeAction.ti?timeLineNum="+timeLineNum+"&likeId="+userId,				
			})
			
			.done(function(data){				
				printLikeCount(timeLineNum);
				//printReplyCount(timeLineNum);
			});
		}  // if-else종료
	});  //done종료
	
	
} //likeToggle 종료


function printLikeCount(timeLineNum){
	
	var heart = $("#heart"+timeLineNum);	
	var likesCountWrap=$("#likesCountWrap"+timeLineNum);
	
	//하트 클릭했는지 여부 체크
	$.ajax({
		method:"POST",
		cache:false,
		url:"likeClickCheckAction.ti?timeLineNum="+timeLineNum+"&likeId="+userId,				
	})
	
	.done(function(clickCheck){
		
		// clickCheck = 0  : 좋아요 안누름  // 1=누름
		if(clickCheck==0){ 	// 기존에 안 누른 경우(add)			
			heart.attr("class", "heartOff2");			
		}else{
			heart.attr("class", "heartOn2");			
		}		
	}); //done 끝
	
	
	$.ajax({
		method:"POST",
		cache:false,
		url:"getLikeAction.ti?timeLineNum="+timeLineNum,				
	})
	
	.done(function(likesCount){		
		likesCountWrap.html(likesCount);			
	});
} //printLikeStatus 종료



// 댓글 수 세기 함수
function printReplyCount(timeLineNum){
	
	var replyCountWrap=$("#replyCountWrap"+timeLineNum);
	
	
	$.ajax({
		method:"POST",
		cache:false,
		url:"getReplyCountAction.ti?timeLineNum="+timeLineNum,				
	})
	
	.done(function(replyCount){		
		replyCountWrap.html(replyCount);			
	});
} //printLikeStatus 종료




/////////////////////////////////////댓글쓰기 JS///////////////////////////////

/* 댓글 달기 누르면 메뉴 펴지고 닫히기  */
function reToggle(timeLineNum, replyNum, reOwnerNick){
	
	var re_ref = replyNum;
	var replyWrap = $("#replyWrap"+replyNum);
	var reReWriteWrap = $("#reReWriteWrap"+replyNum);
	
	var closedClass = "closed reToggle replyWrap togClass"+timeLineNum;
	var openedClass = "opened reToggle replyWrap togClass"+timeLineNum;
	
	if(replyWrap.attr("class")==closedClass){
		replyWrap.after(
				"<div id='reReWriteWrap"+replyNum+"' class='reReWriteWrap opened'>"
				+ "<span class='profilePhoto40' style='background-image: url(\""+userPhoto+"\");'></span>"
				+ 	"<div class='reReInputWrap'>"
				+		 "	<input id='reReplyContent"+replyNum+"' type='text' placeholder='댓글을 입력해 보세요.'>"
				+		 "	<span class='btn70_35r_r mouseHand' onclick=\writeReReply("+timeLineNum+","+replyNum+","+re_ref+",'"+reOwnerNick+"');\>게시</span>"
				+ 	"</div>"
				+ "</div>"
		);
		replyWrap.attr("class",openedClass);
	}else{
		reReWriteWrap.hide(200);
		replyWrap.attr("class",closedClass);
	}	
	
}



/* 대댓글 달기 누르면 메뉴 펴지고 닫히기  */
function reReToggle(timeLineNum, replyNum, re_ref, reOwnerNick){
	
	var reReplyWrap = $("#reReplyWrap"+replyNum);
	var reReWriteWrap = $("#reReWriteWrap"+replyNum);
	
	var closedClass = "closed reToggle reReplyWrap togClass"+timeLineNum;
	var openedClass = "opened reToggle reReplyWrap togClass"+timeLineNum;
	
	if(reReplyWrap.attr("class")==closedClass){
		
		reReplyWrap.after(
				"<div id='reReWriteWrap"+replyNum+"' class='reReWriteWrap opened'>"
				+ "<span class='profilePhoto40' style='background-image: url(\""+userPhoto+"\");'></span>"
				+ 	"<div class='reReInputWrap'>"
				+		 "	<input id='reReplyContent"+replyNum+"' type='text' placeholder='댓글을 입력해 보세요.'>"
				+		 "	<span class='btn70_35r_r mouseHand' onclick=\writeReReply("+timeLineNum+","+replyNum+","+re_ref+",'"+reOwnerNick+"');\>게시</span>"
				+ 	"</div>"
				+ "</div>"
		);
		reReplyWrap.attr("class",openedClass);
	}else{
		reReWriteWrap.hide(200);
		reReplyWrap.attr("class",closedClass);
	}	
	
}


//댓글쓰기 부분

function writeReply(timeLineNum){

	var replyInput = $("#replyContent"+timeLineNum);
	var replyContent = replyInput.val();
	var replyWrap = $("#replyWrap"+timeLineNum);
		
	$.ajax({
		method:"POST",
		cache:false,
		url:"insertTimeReplyAction.ti?timeLineNum="+timeLineNum+"&content="+replyContent+"&userId="+userId,				
	})
	
	.done(function(data){	
		printReplyList(timeLineNum);
		replyInput.val('');		
	});	
}


// 대댓글 쓰기 부분

function writeReReply(timeLineNum, replyNum, re_ref, reOwnerNick){
	
	var reReplyInput = $("#reReplyContent"+replyNum)
	var reReplyContent = reReplyInput.val();
	var reReplyWrap = $("#reReplyWrap"+replyNum);
	var reReWriteWrap = $("#reReWriteWrap"+replyNum);
	
	var closedClass = "closed reToggle reReplyWrap togClass"+timeLineNum;
	var openedClass = "opened reToggle reReplyWrap togClass"+timeLineNum;
	
	$.ajax({
		method:"POST",
		cache:false,
		url:"insertTimeReReplyAction.ti?timeLineNum="+timeLineNum+"&re_ref="+re_ref+"&content="+reReplyContent+"&userId="+userId+"&reOwnerNick="+reOwnerNick,			
	})
	
	.done(function(data){	
		printReplyList(timeLineNum); //댓글 출력
		reReWriteWrap.hide(); // 대댓글창 숨기기
		reReWriteWrap.attr("class",closedClass); //대댓글창 클래스 변경		
		reReplyInput.val(''); //대댓글창 지우기			
		replyOpen(timeLineNum) // 댓글 강제로 열기
	});
		
}

////////////////////댓글과 대댓글 뿌리기(Print)//////////////////////////////////////////

function printReplyList(timeLineNum){
	
	var allReWrap = $("#allReWrap"+timeLineNum);	
	var replyHtml = "";
	
	$.ajax({
		method:"POST",
		cache:false,
		url:"getReplyAction.ti",			
	})
	
	.done(function(timeReObj){	
		
		var allData = JSON.parse(timeReObj);
		
		for(var i=0;i<allData.replyList.length;i++){

			// 배열로 받아온 값을 우선 변수에 저장(태그 코딩시 보기 쉽게)
			var timeLineReplyNum = allData.replyList[i].timeLineReplyNum;
			var timeLineNumRe = allData.replyList[i].timeLineNum;
			var userId = allData.replyList[i].userId;
			var content = allData.replyList[i].content;
			var writeTime = allData.replyList[i].writeTimeStr;
			var re_ref = allData.replyList[i].re_ref;
			var re_lev = allData.replyList[i].re_lev;
			var reOwnerNick = allData.replyList[i].reOwnerNick;
			var userPhoto = allData.replyList[i].userPhoto;
			var userNickname = allData.replyList[i].userNickname;	
			
			if(timeLineNum==timeLineNumRe){	
				
				if(re_lev==0){ // 댓글 뿌리기					
			replyHtml+= "<div id='replyWrap"+timeLineReplyNum+"' class='closed reToggle replyWrap togClass"+timeLineNumRe+"' name='hideRe"+timeLineNumRe+"'> "
			+"				<div class='reply'>"
			+"					<span class='profilePhoto35 mouseHand' style='background-image: url(\""+userPhoto+"\");'></span>"
			+"					<div class='replyProWrap'>"
			+"						<span>"+userNickname+"</span>	"
			+"						<span>"+writeTime+"</span>"
			+"					</div>	"
			+"					<span>"+content	
			+"						<span class='writeReBtn mouseHand' onclick=\" reToggle("+timeLineNum+","+timeLineReplyNum+",'"+userNickname+"')\">답글달기</span>	"
			+"					</span>	"
			+"				</div>"
			+"			</div>"	
			
				}else{// 대댓글 뿌리기
					
			replyHtml+= "<div id='reReplyWrap"+timeLineReplyNum+"' class='closed reToggle reReplyWrap togClass"+timeLineNumRe+"' name='hideRe"+timeLineNumRe+"'> "
			+"				<span class='replyAngle'></span>"
			+"				<div class='reReply'>"
			+"					<span class='profilePhoto35 mouseHand' style='background-image: url(\""+userPhoto+"\");'></span>"
			+"					<div class='reReplyProWrap'>"
			+"						<span>"+userNickname+"</span>"	
			+"						<span>"+writeTime+"</span>"
			+"					</div>	"
			+"					<span><b class='toId'>@"+reOwnerNick+" &nbsp; </b>"+content				
			+"						<span class='writeReBtn mouseHand' onclick = \"reReToggle("+timeLineNum+", "+timeLineReplyNum+", "+re_ref+",'"+userNickname+"')\"> 답글달기</span>"	
			+"					</span>"
			+"				</div>"
			+"			</div>"		
					
				}	
			}  // if끝			
			allReWrap.html(replyHtml);			
			
		}  // 전체 for문 끝
	}); //ajax done()끝
}// 함수 끝



function replyToggle(timeLineNum){
		
	var timeReply = $(".togClass"+timeLineNum);
	var timeReplyHide = $(".togClass"+timeLineNum+":nth-child(n+3)");	
	var replyToggle = $("#replyToggle"+timeLineNum);	
		
	if(replyToggle.text()=="댓글더보기"){
		
		timeReply.css('display','block');		
		replyToggle.text("댓글접기");
		replyToggle.attr('class', 'replyToggle mouseHand opened');
		
	}else{
		
		timeReplyHide.css('display','none');
		replyToggle.text("댓글더보기");
		replyToggle.attr('class', 'replyToggle mouseHand');
	}	
}


function replyClose(timeLineNum){
		
	var timeReply = $(".togClass"+timeLineNum);
	var timeReplyHide = $(".togClass"+timeLineNum+":nth-child(n+3)");	
	var replyToggle = $("#replyToggle"+timeLineNum);	
			
	timeReplyHide.css('display','none');
	replyToggle.text("댓글더보기");
}

function replyOpen(timeLineNum){
	
	// 댓글이나 대댓글 달면 댓글창 강제로 열어주기
	var timeReply = $(".togClass"+timeLineNum);
	var timeReplyHide = $(".togClass"+timeLineNum+":nth-child(n+3)");	
	var replyToggle = $("#replyToggle"+timeLineNum);	
	
	timeReply.css('display','block');		
	replyToggle.text("댓글접기");			
}



/*
	
 번개 만들기 버튼 토글  	
function thunderCancel(){
	$("#thunderToggle").hide(300);
	$("#thunderOpen").show();
}
	
	
 번개 만들기 insert  	
function insertThunder(){
	var thunderName= $("#thunderName").val();
	var thunderPlace= $("#thunderPlace").val();
	var thunderDate= $("#thunderDate").val();
	var thunderHour= $("#thunderHour").val();
	var thunderMinute= $("#thunderMinute").val();
	var thunderPerson= $("#thunderPerson").val();
	
	if(thunderName.length== 0) {
		alert("제목을 입력해 주세요");
		return;
	} 
	
	받아온 정보를 json오브젝트 형태로 저장
	var thunderInfoObj={
		"thunderName" :  thunderName,	
		"thunderPlace" :  thunderPlace,
		"thunderDate" :  thunderDate,
		"thunderHour" :  thunderHour,
		"thunderMinute" :  thunderMinute,
		"thunderPerson" :  thunderPerson,
		"userId" : userId,
		"userPhoto" : userPhoto,
		"moimNum" : moimNum,
	}
	
	json 오브젝트를 스트링 형태로변환
	var thunderInfoParam = JSON.stringify(thunderInfoObj);
	$.ajaxSetup( {cache:false} );
	$.ajax({
		method:"POST",
		cache:false,
		url:"insertThunderAction.ne",
		data:{"thunderInfoParam":thunderInfoParam}
	})
	
	.done(function(data){
		printThunderList();
		$("#thunderToggle").hide(300);			
	});
} insertThunder()끝
	
	
	
//모임 접속시 또는 모임 생성 후 번개리스트 출력하기	
	
function printThunderList(){	
	var xhttp;
	
	if (window.XMLHttpRequest) {
		xhttp = new XMLHttpRequest();			
	} else {
		xhttp = new ActiveXObject("Microsoft.XMLHTTP");			
	}

	xhttp.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200){ 
	//////////////
			
		var allData = JSON.parse(this.responseText);
		var thunderMoim = document.getElementById("thunderMoim");
					
		thunderMoim.innerHTML = "";
		var i;
		for (i = 0; i < allData.allThunderList.length; i++){
			
			var thunderNum = allData.allThunderList[i].thunderNum;
			var thOwnerPhoto = allData.allThunderList[i].userPhoto;
			var thOwnerId = allData.allThunderList[i].userId;
			var thOwnerId = allData.allThunderList[i].userId;
								
			thunderMoim.innerHTML += "<div class='thunderMoim'>"
				+	"<div class='calendarBox'>"
				+		"<span>"+allData.allThunderList[i].parsingDay+"</span>"
				+		"<span>"+allData.allThunderList[i].parsingDate+"</span>"
				+	"</div>"
				+	"<span>"+allData.allThunderList[i].thunderName
				+		"<a class='replyIcon'></a>"
				+		"<a class='reCount'>"+allData.allThunderList[i].thunderPerson+"</a>"
				+	"</span>"								
				+	"<span class='positionIcon'></span>"
				+	"<span>"+allData.allThunderList[i].thunderPlace+"</span>"								
				+	"<span class='clockIcon'></span>"
				+	"<span>"+allData.allThunderList[i].thunderDate.substring(0,16)+"</span>"							
				+	"<span class='personIcon'></span>"
				+	"<span><b id='joinPerson"+thunderNum+"'>2</b>/<b>"+allData.allThunderList[i].thunderPerson+"</b></span>"								
				+"</div>"
				
				+"<div class='thunderUser' id='thunderUser"+thunderNum+"'>"	
				+ "<div class='profileWrap'>"
				+ 	"<p class='profilePhoto35' style='background-image:url("+allData.allThunderList[i].userPhoto+")'></p>"
				+	"<div id='profileWrap"+thunderNum+"'>"
						번개 신청자 이미지 들오오는 곳
				+	"</div>"
				+ "</div>"
				+"</div>"
				
				+"<div class='buttons clear'>"									
				+	"<span class='thunderDel mouseHand DpNone2' id='thunderDel"+thunderNum+"' onclick= thunderDel("+thunderNum+"); >정모삭제</span>"
				+	"<span id='joinWrap"+thunderNum+"'>"
				+ 		"<span class='btn80_40b thunderJoin' id='thunderJoin"+thunderNum+"' onclick= thunderJoin("+thunderNum+"); >참가하기</span>"
				+	"</span>"
				+"</div>";
			printThunderJoinUser(thunderNum, thOwnerId);	
			}  // for문 종료
		}		
	};	
	////////////////////		
	xhttp.open("POST", "getThunderListAction.ne?t="+ new Date().getTime(), true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
	xhttp.send("moimNum="+moimNum);		
	//////////////
}	

	
//모임 접속시 또는 모임 생성 후 번개리스트 출력하기	
	
function printThunderJoinUser(thunderNum, thOwnerId){	
	var xhttp;
	
	if (window.XMLHttpRequest) {
		xhttp = new XMLHttpRequest();			
	} else {
		xhttp = new ActiveXObject("Microsoft.XMLHTTP");			
	}

	xhttp.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200){ 
	//////////////
		
		var allData = JSON.parse(this.responseText);			
		var profileWrap = document.getElementById("profileWrap"+thunderNum);
		var joinWrap = document.getElementById("joinWrap"+thunderNum);
			
		profileWrap.innerHTML = "";
		var i;
		
		joinWrap.innerHTML= "<span class='btn80_40b thunderJoin DpNone1' id='thunderJoin"+thunderNum+"' onclick= thunderJoin("+thunderNum+"); >참가하기</span>"
				
				
		// 프로파일 사진 불러오기용 for문
		for (i = 0; i < allData.allUser.length; i++){					
			//신청자 이미지 프로파일 넣기
			profileWrap.innerHTML += "<p class='profilePhoto35' style='background-image:url("+allData.allUser[i].userPhoto+")'></p>";
		}//for문 종료
		
		
		// 버튼 불러오기용 for문
		for (i = 0; i < allData.allUser.length; i++){					
														
			// 상황별 신청버튼 바꾸기(번개장, 신청시, 미신청시)
			var checkId = allData.allUser[i].userId;
			
			if(userId==thOwnerId){ //번개장은 신청안됨	
				joinWrap.innerHTML= "<span class='btn80_40b thunderJoin' id='thunderJoin"+thunderNum+"' onclick= alert('개설자입니다'); >참가하기</span>"
				return;
			}else if(userId==checkId){// 신청상태
				joinWrap.innerHTML= "<span class='btn80_40b thunderJoin checked' id='thunderJoin"+thunderNum+"' onclick= thunderJoin("+thunderNum+");>신청됨</span>"
				return;
			}else{//미신청 상태						
				joinWrap.innerHTML= "<span class='btn80_40b thunderJoin DpNone1' id='thunderJoin"+thunderNum+"' onclick= thunderJoin("+thunderNum +"); >참가하기</span>"				
			}					
		} // 버튼용 for문 종료		
	} // onreadystatechange()함수 종료		
};	
////////////////////		
xhttp.open("POST", "getThunderUserAction.ne?thunderNum="+thunderNum+"&t="+new Date().getTime(), true);
xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
xhttp.send("moimNum="+moimNum);		
//////////////
}		

	
번개 참여 신청 및 취소 토글 		

function thunderJoin(thunderNum){		
	var thunderJoin = $("#thunderJoin"+thunderNum);
	var joinPerson = $("#joinPerson"+thunderNum);
	var a = parseInt(joinPerson.innerHtml)
	var thunderPerson=5;	
	
	alert(a);
	
	$.ajaxSetup( {cache:false} );
	if(joinPerson>=thunderPerson){			
		alert("모집이 마감되었습니다");
		return;
	}else{
		
		//버튼 클릭 시 chekced 클래스명 토글(있게, 없게 하기)
		if(thunderJoin.attr('class')==="btn80_40b thunderJoin checked"){
							
			$.ajax({
				method:"POST",
				cache:false,
				url:"deleteThunderUserAction.ne?userId="+userId+"&thunderNum="+thunderNum+"&moimNum="+moimNum,				
			})
			
			.done(function(data){	
				printThunderJoinUser(thunderNum);				
			});
			
			
		}else{
							
			$.ajax({
				method:"POST",
				cache:false,
				url:"insertThunderUserAction.ne?userId="+userId+"&thunderNum="+thunderNum+"&moimNum="+moimNum,				
			})
			
			.done(function(data){
				printThunderJoinUser(thunderNum);				
			});
			
		}	
	} // 인원초과 체크 if-else 종료
}
	
	
 번개 삭제
function thunderDel(thunderNum){
	
	if(confirm("정말로 삭제 하시겠습니까?")){		
		location.href="thunderDelAction.ne?thunderNum="+thunderNum+"&moimNum="+moimNum;
	}else{
		return;
	}
}	
	
	
	
	
*/	
	
	
	
	
	