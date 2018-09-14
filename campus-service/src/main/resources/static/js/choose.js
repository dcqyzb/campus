/*(function() {
			(function() {
				var screenWidth = window.screen.width, screenHeight = window.screen.height;
				//初始化页面渲染样式修改
				$("body").css({
					"width" : screenWidth + "px",
					"height" : screenHeight + "px",
					"min-height" : screenWidth / (1920 / 1080) + "px",
					"max-height" : screenWidth / (1920 / 1080) + "px",
					"background-size" : screenWidth + "px "+screenHeight + "px"
				});
				
				//根据屏幕分辨率大小设置样式
				if( screenWidth > 1440){
					$("#top").css("padding-top","4%");
					$("#top>hr").map(function(){
						$(this).css("margin-top", "1.65%");
					});
					$("#header").css("background","url(image/login/header.png) no-repeat");
					$("#header>h1").css("font-size","53px");
					$("#header>h1").css("padding-top","0");
					$("#header>span").css("font-size","22px");
					$(".block").map(function(){
						$(this).css("font-size", "30px");
					});
					$(".block img").map(function(){
						$(this).css("width", "60px");
					});
					$("#bottom>div").css("padding-top","3%");
					$("#bottom>div").css("font-size","22px");
				}
			})();
		})();*/

/*加载layer*/
$(document).ready(function(){
	layui.use('layer', function(){
		window.layer = layui.layer;
	});
	
	/*判断用户是否登录，否则转到登录页面*/
	$.ajax({
		type: 'get',
		url: '/campusbd/api/user/checklogin',
		dataType: 'json',
		success: function(result){
			if(result=="sucess"){
			}else{
				window.location.href="login.html";
			}
		}
	});
	
	/*判断是否有可视化管理平台点击权限*/
	$.ajax({
		type: 'get',
		url: '/campusbd/api/user/show',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data=="sucess"){
				$('.block img[alt="可视化管理平台"]').parents(".block").parent().addClass("selected");
			}else{
			}
		}
	});
	
	/*判断是否数据管理平台点击权限*/
	$.ajax({
		type: 'get',
		url: '/campusbd/api/user/Mange',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data=="sucess"){
				$('.block img[alt="数据管理平台"]').parents(".block").parent().addClass("selected");
			}else{
			}
		}
	});
});

/*块级元素点击事件*/
function changeBg(element){
	var isSelect=$(element).parent().attr('class');
	if(isSelect!=null&&isSelect!=""){ //可选
		var title=$(element).find('img').attr('alt');
		//判断是否有权限,可视化管理平台
		if(title=="可视化管理平台"){
			window.location.href="index.html";
		}
		else{//判断是否有权限,数据管理平台
			window.location.href="index.html";
		}
	}else{	//不可选			
	}
}
