/*(function() {
			(function() {
				var screenWidth = window.screen.width, screenHeight = window.screen.height;
				 // 初始化页面渲染样式修改
				$("body").css({
					"width" : screenWidth + "px",
					"height" : screenHeight + "px",
					"min-height" : screenWidth / (1920 / 1080) + "px",
					"max-height" : screenWidth / (1920 / 1080) + "px",
					"background" : "url(image/login/login_bg.png)",
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
					$("#middle form").css("height","70%");
					$("#middle label").map(function(){
						$(this).css("font-size","24px");
					});
					$(".remember b,.msg").map(function(){
						$(this).css("font-size","16px");
					});
					$("#middle input[type=text],#middle input[type=password]").map(function(){
						$(this).css("font-size","22px");
						$(this).css("height","70%");
					});
					$("#middle>form>p>a").css("bottom","40%");
					$("#middle>form>p>a").css("height","14%");
					$("#bottom>div").css("padding-top","3%");
					$("#bottom>div").css("font-size","22px");
				}
			})();
		})();*/

/*谷歌浏览器解决input淡黄色填充问题*/
/*$(function() {
	   if (navigator.userAgent.toLowerCase().indexOf("chrome") >= 0) {
	      $(window).load(function(){
	         $('input:not(input[type=submit])').each(function(){
	         var outHtml = this.outerHTML;
	         $(this).append(outHtml);
	         });
	      });
	   }
	});*/

/*加载layer*/
$(document).ready(function(){
	layui.use('layer', function(){
		window.layer = layui.layer;
	});
	
	//记住密码
	if($.cookie("rmbMe")=="true"){
		$("#rememberMe").prop("checked",true);
		$("#username").val($.cookie("username"));
		$("#password").val($.cookie("password"));
	}
});

//取消记住密码
function remember(element){
      if(!element.checked){
        $.cookie("rmbMe",null,{path:"/"});
		$.cookie("username",null,{path:"/"});
		$.cookie("password",null,{path:"/"});
      }
};
//点击登录跳转到登录页面，陈仁群修改
$("#btn_login").click(function(){	
	var peopleName = $("#username").val();
	var peoplePassword = $("#password").val();

	if(peopleName==null||peopleName==""){	
		$('.msg').html("*请输入用户名");
	}
	else if(peoplePassword==null||peoplePassword==""){
		$('.msg').html("*请输入密码");
	}
	else{
		$.ajax({
			type: "post",
			url: "/campusbd/api/user/login",
			async: false,
			dataType: "json",
			data:{
				username:peopleName,
				password:peoplePassword
			},
			success:function(data) {
				if(data.object=="sucess"){
					if($("#rememberMe").prop("checked")){
						$.cookie('rmbMe', 'true', { expires: 7 , path: '/' });
						$.cookie('username', peopleName , { expires: 7 , path: '/' });
						$.cookie('password', peoplePassword , { expires: 7 , path: '/' });
					}
					else{
		        		$.cookie("rmbMe",null,{path:"/"});
						$.cookie("username",null,{path:"/"});
						$.cookie("password",null,{path:"/"});
					}
					window.location.href="choose.html";
				}
				else{
					$('.msg').html("*用户名或密码错误");
				}
			}
		})	
	}
})

/*回车登录*/
$(document).keydown(function(e){ 
	var curKey = e.which; 
	if(curKey == 13){
		$("#btn_login").click();
	} 
}); 