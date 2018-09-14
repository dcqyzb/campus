/**
 * 页面加载时一些共用的方法，如页面高度样式调整、时间输入框加载插件、页面渲染完成后获取学院专业等
 */

(function(){
	/*
	 * 调整页面高度样式
	 */
	var $parentIframe = $(window.parent.document.getElementById("contentFrame"));
	$("body").css("height", $parentIframe.height());
	
	var screenWidth = window.screen.width;
	if( screenWidth < 1900){
		$(".table_area").css("height", $(".center_table>img").height() - 150);
		$(".table_all").map(function(){
			$(this).css("height", $(this).parent().height() - 50);
		});
		//$(".table_content").css("height", $(".table_area").height() - 50);
		$(".table_content").map(function(){
			$(this).css("height", $(this).parent().height() - 50);
		});
		
		$(".nav_ul a").css("font-size", (screenWidth/277 + 10) + "px");
		$(".search_condition span").css("font-size", "16px");
		$(".search_condition input").css("font-size", "14px");
		$(".table_head thead").css("font-size", "16px");
		$(".table_content tbody").css("font-size", "14px");
		$(".right .title").css("font-size", "16px");
	}
	
	/*
	 * 时间输入框的插件加载
	 */
	//加载'年'输入框
	$(".input_year").datetimepicker({
		language: 'zh-CN',
		format: 'yyyy',
		startView: 'decade',
		minView: 'decade',
		todayHighlight: true,
		autoclose: true,
		endDate: new Date()
	}).on("changeDate", function(ev){
		var target = ev.target;
		var value = target.value;
		var $this = $(target), $other = $this.siblings("input");
		if($this.attr("class").indexOf("start") != -1){
			$other.datetimepicker('setStartDate', value);
		}else{
			$other.datetimepicker('setEndDate', value);
		}
	});
	//加载'月'输入框
	$(".input_month").datetimepicker({
		language: 'zh-CN',
		format: 'yyyy-mm',
		startView: 'year',
		minView: 'year',
		todayHighlight: true,
		autoclose: true,
		endDate: new Date()
	}).on("changeDate", function(ev){
		var target = ev.target;
		var value = target.value;
		var $this = $(target), $other = $this.siblings("input");
		if($this.attr("class").indexOf("start") != -1){
			$other.datetimepicker('setStartDate', value);
		}else{
			$other.datetimepicker('setEndDate', value);
		}
	});
	//加载'日'输入框
	$(".input_date").datetimepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		startView: 'month',
		minView: 'month',
		todayHighlight: true,
		autoclose: true,
		endDate: new Date()
	}).on("changeDate", function(ev){
		var target = ev.target;
		var value = target.value;
		var $this = $(target), $other = $this.siblings("input");
		if($this.attr("class").indexOf("start") != -1){
			$other.datetimepicker('setStartDate', value);
		}else{
			$other.datetimepicker('setEndDate', value);
		}
	});
	
})();

/**
 * 页面渲染完成后获取学院专业
 */
var college_major = null, major_college = {};	//学院专业对应关系
$(document).ready(function(){
	/*
	 * 获取学院专业
	 */
	$.ajax({
		type: 'post',
		url: '/poverty/getCollegeMajor',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data){
				college_major = data;
				var college_li = "<li class='selected'>全部</li>",
					major_li = "<li class='selected'>全部</li>";
				$.each(data, function(college, majors){
					college_li += "<li title='"+college+"'>" + college +"</li>";
					$.each(majors, function(i, major){
						major_college[major] = college;	//专业-学院对应关系
						major_li += "<li title='"+major+"'>" + major +"</li>";
					});
				});
				$("ul.college").html(college_li);
				$("ul.major").html(major_li);	
			}
		}
	});
});
