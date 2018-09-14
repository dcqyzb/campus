/**
 * 首页js
 */

$(document).ready(function(){
	//默认点击导航第一个
	$(".nav_tabs span").eq(0).click();

	//设置当前用户名
	$.ajax({
		type: "get",
		url: "/campusbd/api/user/getuser",
		async: false,
		dataType: "json",
		success:function(data) {
			if(data.resultCode==true){
				var name=data.object.userName;
				$('#username').text(name);
			}
		}
	})	
	
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
});

//绑定图表模式下点击标题跳转事件
$("#contentFrame").bind("load.test", function(){
	var $title = $("#contentFrame").contents().find(".level_b .title span");
	if($title.length > 0){
		$title.on("click",function(e){
			var _index = $(this).attr("data-index");
			if(_index){
				$(".mode span").eq(1).click();
				$("#contentFrame").bind("load.test2", function(){
					$("#contentFrame").contents().find("ul.nav_ul li").eq(_index).click();
				});
			}
		});
	}else{
		$("#contentFrame").unbind("load.test2");
	}
});

//导航展开
function showNav(){
	toggleChildAnimation(false);	//关闭动画	
	$(".nav_expand").slideDown("slow");
	/* 展开导航中文字弯曲 */
	$(".nav_tabs span.first").arctext({radius: 200, dir: -1});
	$(".nav_tabs span.second").arctext({radius: 200, dir: -1});
	$(".nav_tabs span.third").arctext({radius: 200, dir: -1});
	$(".nav_tabs span.fourth").arctext({radius: 200, dir: -1});
	$(".nav_tabs span.fifth").arctext({radius: 200, dir: -1});
	$(".nav_tabs span.sixth").arctext({radius: 200, dir: -1});
}
//导航隐藏
function hideNav(){
	$(".nav_expand").slideUp("slow", function(){
		//恢复子页面的动画效果
		toggleChildAnimation(true);});
}
//导航展开点击某一栏
$(".nav_tabs span").click(function(){
	var text = $(this).text();
	var href = $(this).attr("data-href");
	$(this).addClass("selected").parent().siblings().children("span").removeClass("selected");
	$(this).next("img").fadeIn(300).parent().siblings().children("img").fadeOut();
	$(".nav_expand_span_center").text(text);
	$(".nav_choose_span").fadeOut(500, function(){
		$(".nav_choose_span").text(text);
	}).fadeIn(400);
	$(".nav_expand").delay(300).slideUp("slow");
	$("#contentFrame").attr("src", href);
	//$("#contentFrame").load();
	$(".mode span").eq(0).click();	//默认选择图标模式
});
function changeNav(flag){
	var $spans = $(".nav_tabs span.nav_span");
	var $span_selected = $(".nav_tabs span.nav_span.selected");
	var _cur = $span_selected.parent().index();	//当前选择的下标
	var _next = (_cur == 5)?0:(_cur+1);
	var _prev = (_cur == 0)?5:(_cur-1);
	if(flag == 1){
		$spans.eq(_next).click();
	}else{
		$spans.eq(_prev).click();
	}
}
//点击其它地方隐藏导航
$(document).bind("click", function(e){
	var _class = $(e.target).attr("class");
	if(_class && $(e.target).attr("class").indexOf("nav_span") == -1
			&& $(e.target).attr("class").indexOf("down_icon") == -1 ){
		var isVisible = $(".nav_expand:visible").length;	//判断导航是否是显示状态
		if(isVisible){
			$(".nav_expand").slideUp("slow", function(){
				//恢复子页面的动画效果
				toggleChildAnimation(true);
			});
		}
	}
});

/*
 * 学院选择
 */
function selectAcademy(){
	toggleChildAnimation(false);	//关闭动画
	$(".academy ul").slideToggle("fast", function(){toggleChildAnimation(true);});
}
function changeAcademy(obj){	//点击下拉框某一学院
	toggleChildAnimation(false);
	$(obj).addClass("selected").siblings().removeClass("selected");
	$("#academy_selected").text($(obj).text());
	$(obj).parent().slideToggle("fast", function(){toggleChildAnimation(true);});
}

/**
 * 点击导航切换子页面的动画效果
 * @param flag	为true表示开启，为false表示关闭
 */
function toggleChildAnimation(flag){
	//获取子页面有动画的对象，EffectChart表示子页面所有的有动画的图对象
	var charts = $("#contentFrame")[0].contentWindow.EffectChart;
	if(charts){
		for(var i=0, len=charts.length; i<len; i++){
			var obj = charts[i];
			//判断该对象是否是dom元素，为echarts实例才执行
			if(!isDom(obj)){
				var series = obj.getOption().series, newSeries = [];			
				if(!flag){	//关闭动画
					$.each(series, function(i, item){
						var type = item.type;
						//当系列series为effectScatter或lines,即有动画效果，重新设置动画
						if(type == "effectScatter"){
							newSeries.push({
								rippleEffect: {period: 0}	//关闭动画
							});
						}else if(type == "lines"){
							newSeries.push({
								effect: {period: 0}
							});
						}else{
							newSeries.push({});
						}
					});
				}else{	//重新开启动画
					$.each(series, function(i, item){
						var type = item.type;
						//当系列series为effectScatter或lines,即有动画效果，重新设置动画
						if(type == "effectScatter"){
							newSeries.push({
								rippleEffect: {period: 4}
							});
						}else if(type == "lines"){
							newSeries.push({
								effect: {period: 6}
							});
						}else{
							newSeries.push({});
						}
					});
				}
				obj.setOption({series: newSeries});	//重新渲染
			}
		}
	}
}

//判断对象是否是dom元素，是返回true，不是返回false
var isDom = ( typeof HTMLElement === 'object' ) ?
    function(obj){
        return obj instanceof HTMLElement;
    } :
    function(obj){
        return obj && typeof obj === 'object' 
        	&& obj.nodeType === 1 
        	&& typeof obj.nodeName === 'string';
    };

//模式切换
$(".mode span").click(function(){
	var $this = $(this), $navSpan = $(".nav_tabs span.selected"), 
		$frame = $("#contentFrame"),	//iframe	
		_modeIndex = $this.index(),	//当前模式下标
		_navIndex = $navSpan.parent().index(),	//当前导航下标
		_href = $navSpan.attr("data-href");
	$this.addClass("selected").siblings().removeClass("selected");
	if(_modeIndex == 0){	//选择图表模式
		$frame.siblings("img").fadeIn();
		$frame.css("width", "90%");
		$frame.attr("src", _href);
		//$frame.load();
	}else{	//选择数据模式
		$frame.siblings("img").fadeOut();
		var base_href = "page/datamode/", 
			data_href = base_href + _href.split(".")[0].split("/")[1] + "_data.html";
		$frame.css("width", "99%");
		$frame.attr("src", data_href);
		//$frame.load();
	}
});
//用户退出
$("#logout").click(function(){
	$.ajax(
			{
				type: "get",
				url:"/campusbd/api/user/logout",
				dataType: "json",
				success: function() {
					window.location.href="/campusbd/login.html";
				}
				}
			)
			
});


