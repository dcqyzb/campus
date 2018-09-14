/**
 *	各页面数据模式下的一些工具方法，如ul模拟下拉框的点击、学院与专业联动
 */

/*点击导航切换，改变样式，不涉及到数据加载，加载数据需到各页面js另外写方法*/
$("ul.nav_ul li").click(function(){
	var $this = $(this), curIndex = $this.index();
	$this.addClass("selected").siblings().removeClass("selected");	//修改样式
	//更换图片
	$this.children("img").attr("src", "../../image/datamode/nav_selected.png");
	$this.siblings().children("img").attr("src", "../../image/datamode/nav.png");
	//显示对应表格
	$(".center_table>div").eq(curIndex).css("display", "block").siblings("div").not(".foot_area").css("display", "none");
	//显示对应排行榜
	$(".rank>.table_all").eq(curIndex).css("display", "block").siblings("div").not(".title").css("display", "none");
});

/**
 * 以下部分为input-ul模拟实现下拉框的效果，为解决原始下拉框样式不美观问题
 */
/*模拟下拉框实现——输入框点击*/
$(".select-input").click(function(){
	var $this = $(this), 	//点击的输入框
		$ul = $this.siblings("ul"),	//获取同级的ul
		isVisible = $this.siblings("ul:visible").length;	//判断ul是否是显示状态
	if(!isVisible){	//ul不是显示状态
		$ul.show();
	}else{
		$ul.hide();
	}
});
/*模拟下拉框实现——li点击，动态绑定*/
$(".select-ul").on("click", "li", function(){
	var $this = $(this),	//点击的li项
		$input = $this.parent().siblings("input");	//获取同级输入框
	$this.addClass("selected").siblings().removeClass("selected");	//改变点击样式
	$input.val($this.text());	//选中的值赋值到输入框
	$input.attr("title", $this.text());
	$this.parent().hide();	//隐藏ul
});

/*学院专业联动——点击某学院，专业切换为该学院下所有专业，动态绑定*/
$(".select-ul.college").on("click", "li", function(){
	var $this = $(this),	//点击某学院的项
		$major_ul = $this.parent().parent().siblings().find("ul.major"),	//找到同级的专业ul
		$major_input = $major_ul.siblings("input"),	//专业的输入框
		_text = $this.text();	//学院名称
	_text = (_text == "全部") ? "" : _text;
	
	$major_input.val("全部");	//清除输入框
	var major_li = "<li class='selected'>全部</li>";	//专业的html
	if(_text){	//点击学院不为空
		//获取该学院下所有专业
		$.each(college_major[_text], function(i, major){
			major_li += "<li title='"+major+"'>" + major +"</li>";
		});
	}else{	//点击学院为全部，获取所有专业
		$.each(college_major, function(college, majors){
			$.each(majors, function(i, major){
				major_li += "<li title='"+major+"'>" + major +"</li>";
			});
		});
	}
	$major_ul.html(major_li);
});
/*学院专业联动——点击某专业，学院框显示对应学院，动态绑定*/
$(".select-ul.major").on("click", "li", function(){
	var $this = $(this),	//点击某专业的项
		$college_ul = $this.parent().parent().siblings().find("ul.college"),	//找到同级的学院ul
		$college_input = $college_ul.siblings("input"),	//学院对应的输入框
		_text = $this.text();	//专业名称
	
	var collegeName = (_text && _text !="全部") ? major_college[_text] : "";	//该专业对应学院名称
	collegeName = collegeName ? collegeName : "全部";
	$college_input.val(collegeName);	//将该专业对应学院名称赋值到学院的输入框
	//修改学院选中项
	$college_ul.children("li").removeClass("selected");
	$.each($college_ul.children("li"), function(i, li){
		var name = $(li).text();
		if(name == collegeName){
			$(li).addClass("selected");
		}
	});
});

//点击空白隐藏下拉框
$(document).bind("click", function(e){
	var className = e.target.className;
	if(!className || (className && className.indexOf("select-") == -1)){
		$(".select-ul").hide();
	}
});

/**
 * 查询条件清空
 * @param obj	清空按钮
 */
var clearCondition = function(obj){
	if(!obj) return;
	var $this = $(obj),
		$select_input = $this.siblings().find("input.select-input"),	//获取下拉框形式的条件输入框
		$select_ul = $this.siblings().find("ul.select-ul"),	//下拉框列表
		$date_input = $this.siblings().find("input").not(".select-input");	//获取其它输入框
	//下拉框清空
	$select_input.val("全部");
	$select_ul.map(function(){
		$(this).find("li").eq(0).addClass("selected").siblings().removeClass("selected");
		$(this).find("li").eq(0).click();
	});	
	$date_input.map(function(){
		$(this).val("");
		//时间输入框清空
		if($(this).attr("class") && 
			($(this).attr("class").indexOf("start") != -1
				|| $(this).attr("class").indexOf("end") != -1)){
			$(this).datetimepicker("setStartDate", null);
			$(this).datetimepicker("setEndDate", new Date());
		}
	});
	
	//如果有正负增长按钮，清空'消费对比'正负增长点击状态
	$(".operate.posi_btn span").removeClass("checked");
	$(".operate.nega_btn span").removeClass("checked");
}


/**
 * 多选框点击事件
 */
var check = function(element){
	if(element.className=="check"){	//选择
		$(element).html('<img src="../../image/checked.png">');
		$(element).removeClass('check').addClass('checked');			
	}
	else{							//取消选择
		$(element).html('<img src="../../image/check.png">');
		$(element).removeClass('checked').addClass('check');
	}		
};

/**
 * 时间戳转换成yyyy-MM-dd HH:mm:ss格式
 * @param timeStamp	时间戳
 * @param istime	是否需要时分秒，为true是包含时分秒，为空或false不包含时分秒
 * @returns {String}
 * @author leoya
 */
var formatDateTime = function(timeStamp, istime) {   
    var date = new Date();  
    date.setTime(timeStamp);  
    var y = date.getFullYear();      
    var m = date.getMonth() + 1;      
    m = m < 10 ? ('0' + m) : m;      
    var d = date.getDate();      
    d = d < 10 ? ('0' + d) : d;      
    var h = date.getHours();    
    h = h < 10 ? ('0' + h) : h;   
    var minute = date.getMinutes();    
    var second = date.getSeconds();    
    minute = minute < 10 ? ('0' + minute) : minute;      
    second = second < 10 ? ('0' + second) : second;
    if(istime){
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;    	
    }else{
    	return y + '-' + m + '-' + d;
    }    
};    


