/**
 *	数据模式下的网络行为js
 */
/*添加时间的函数*/
function addDay(dayNumber, date) {
        date = date ? date : new Date();
        var ms = dayNumber * (1000 * 60 * 60 * 24)
        var newDate = new Date(date.getTime() + ms);
        return newDate;
    }
/*将字符串转为时间格式*/
function getDate(strDate) {    
    var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/,    
     function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');    
return date;    
}
var pageSize = 18;	//分页每页显示记录条数
var laypage;	//定义分页的全局变量
/*点击导航切换*/
$("ul.nav_ul li").click(function(){
var $this = $(this), curIndex = $this.index();
/*		$this.addClass("selected").siblings().removeClass("selected");	//修改样式
	//更换图片
	$this.children("img").attr("src", "../../image/datamode/nav_selected.png");
	$this.siblings().children("img").attr("src", "../../image/datamode/nav.png");
	//显示对应表格
	$(".center_table>div").eq(curIndex).css("display", "block").siblings("div").not(".foot_area").css("display", "none");
	//显示对应排行榜
	$(".rank>.table_all").eq(curIndex).css("display", "block").siblings("div").not(".title").css("display", "none");
	*/
	//加载数据前清空所有的查询条件
	$(".clear_btn").click();
	switch(curIndex){
	case 0: 
		pagi_deviceNumber();	//网络设备监测
		$(".rank .title span").text("设备使用率Top10");
		top10_deviceNumber(); //网络设备使用率top10
		break;
	case 1: 
		pagi_stunetArea();	//学生上网行为和成绩
		$(".rank .title span").text("学生平均上网时长Top10");
		top10_stuNet(); //学生平均上网时长top10
		break;
	case 2: 
		pagi_stuNetKey();	//学生沉迷上网原因
		$(".rank .title span").text("学生上网原因Top10");
		top10_stuKeyWord(); //学生沉迷网络原因top10
		break;
	default: 
		break;
	}	
});

//var college_major = null, major_college = {};	//学院专业对应关系
/*页面渲染完成执行*/
$(document).ready(function(){
	var w = $(".center_table").width();
	if(w < 1196){
		$("#table2 .search_area>div").css("top", "20px");
	}
	//赋值laypage
	layui.use('laypage', function(){
		laypage = layui.laypage;
		
		//默认点击左边导航条第一个
		$("ul.nav_ul li").eq(0).click();
	});
	//通知通告
	top5_notice();
});
/**
 * 分页显示网络设备信息
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_deviceNumber(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var deviceNumber = conditions.eq(0).children("input").val(),	//设备编号
		installLocation= conditions.eq(1).children("input").val(),	//设备安置地点
		startDate = conditions.eq(2).children("input.start").val(),	//监测开始时间
		endDate = conditions.eq(2).children("input.end").val();	//监测结束时间
	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'post',
		url: '/netWork/networkDeviceNum',
		data: {
			deviceNumber: (deviceNumber ==null) ? "" : deviceNumber,
			installLocation: (installLocation ==null) ? "" :installLocation,
			startDate: startDate,
			endDate: endDate,
			pageNum: curr ? curr : 1,
			pageSize: pageSize
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			$(".loading").hide();	//隐藏加载层
			if(page){
				var html = "";
				if(page.records){
					$.each(page.records, function(i, item){
						/*
						 * 根据不同类型，字体显示不同颜色
						 */
						var poorhtml = "";
						//使用率的html
						var  ratehtml="";
						//使用率的增长图标
						var increasehtml="";
						//年限
						var limitedTime=item.limitedTime;
						//终止日期
						var endate=addDay(Number(limitedTime.substring(0,1))*365,getDate(item.installDate));
						var nowedate=new Date();
						//相差时间
						var diffdate=parseInt((endate.getTime() - nowedate) / (1000 * 60 * 60 * 24));
						if(diffdate<90){
							poorhtml="style='color:red'";
						}
						var rate=Number(item.netUsage.substring(0,2));
						if(rate>80){
							ratehtml="style='color:red'";
						}
						if(item.increase==1){
							increasehtml="<img src='../../image/datamode/increase.png'>";
						}
						if(item.increase==0){
							increasehtml="<img src='../../image/datamode/unchange.png'>";
						}
						if(item.increase==-1){
							increasehtml="<img src='../../image/datamode/decrease.png'>";
						}
						html += "<tr data-id='"+item.uuid+"'>" +
								"<td width='10%' >" + item.deviceNumber+ "</td>" +
								"<td width='10%' >" + item.clientCount + "</td>" +
								"<td width='7%' title='" + item.installDate +"'>" + item.installDate + "</td>" +
								"<td width='7%' title='" + item.limitedTime + "'"+poorhtml+">" + item.limitedTime+ "</td>" +
								"<td width='10%' title='" + item.broadband + "'>" + item.broadband + "</td>" +
								"<td width='10%' "+ratehtml+">" + item.netUsage +increasehtml +"</td>" +
								"<td width='10%'>" + item.flowConsumped + "</td>" +
								"<td width='11%'>" + item.installLocation + "</td>" +
								"<td width='10%'style='text-decoration:underline;cursor:pointer;' " +
									"onclick='show_principalDetail(this)'>" + item.principalName + "</td>" +
								"<td width='10%'>" + item.countMonth + "</td>" +
								"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='8'>无数据</td></tr>";
				}
				
				$("#table1 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
	
				layui.use('laypage', function(){
					var laypage = layui.laypage;
					laypage.render({
						elem: $("#pagination"),	//容器
						count: totalCount,	//数据总数
						limit: pageSize,	//每页显示条数
						curr: curPage,	//当前页
						groups: 3,	//连续出现的页码个数
						prev: '<',
						next: '>',
						first: '首页',
						last: '尾页',
						jump: function(obj, first){	//当前分页所有选项值、是否首次
							if(!first){	//首次不执行
								pagi_deviceNumber(obj.curr);	//获取点击页的数据
							}
							if(totalCount){
								var firstR = 0, lastR = 0;
								if(obj.curr == totalPage){
									lastR = totalCount;
									firstR = pageSize * (obj.curr - 1) + 1;
								}else{
									firstR = (obj.curr - 1) * pageSize + 1;
									lastR = obj.curr * pageSize;
								}
								$("#pageText").html("共 <span class='num'>"+totalPage+"</span> 页 ，" +
										"<span class='num'>"+totalCount+"</span> 条，" +
										" 当前第 <span class='num'>"+curPage+"</span>" +
										" 页，第 <span class='num'>"+firstR+"</span> 条至" +
										"第 <span class='num'>"+lastR+"</span> 条");
							}else{
								$("#pageText").html("");
							}
						}
					});
				});
			}
		}
	});
}

/**
 * 分页显示学生网络行为和成绩
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_stunetArea(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table2 .search_condition");
	var stuname = conditions.eq(0).children("input").val(),	//姓名
		college = conditions.eq(1).children("input").val(),	//学院名称
		stuClass = conditions.eq(2).children("input").val(),	//专业名称
		startDate = conditions.eq(3).children("input.start").val(),	//统计开始日期
		endDate = conditions.eq(3).children("input.end").val();	//统计结束日期
	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'post',
		url: '/netWork/getBehaviorAndGrade',
		data: {
			stuname: (stuname== null) ? "" : stuname,
			college: (college== "全部") ? "" : college,
			stuClass: (stuClass== "全部") ? "" : stuClass,
			startDate: startDate,
			endDate: endDate,
			pageNum: curr ? curr : 1,
			pageSize: pageSize
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			$(".loading").hide();	//隐藏加载层
			if(page){
				var html = "";
				if(page.records){
					$.each(page.records, function(i, item){
				html += "<tr data-id='"+item.uuid+"'>" +
						        "<td width='14%' title='" + item.studentID+ "'>" + item.studentID + "</td>" +
								"<td width='5%' style='text-decoration:underline;cursor:pointer;'onclick='show_stuNetDetail(this)'>" + item.stuname + "</td>" +
								"<td width='10%'>" + item.college + "</td>" +
								"<td width='10%' title='" + item.stuclass + "'>" + item.stuclass + "</td>" +
								"<td width='8%'>" + item.hour+ "</td>" +
								"<td width='8%'>" + item.times+ "</td>" +
								"<td width='10%' >" + item.upload + "</td>" +
								"<td width='10%'>" + item.download + "</td>" +
								"<td width='5%'>"+item.grade+"</td>"+
								"<td width='10%'>" + item.date + "</td>" +
								"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='9'>无数据</td></tr>";
				}
				
				$("#table2 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
				
				layui.use('laypage', function(){
					var laypage = layui.laypage;
					laypage.render({
						elem: $("#pagination"),	//容器
						count: totalCount,	//数据总数
						limit: pageSize,	//每页显示条数
						curr: curPage,	//当前页
						groups: 3,	//连续出现的页码个数
						prev: '<',
						next: '>',
						/*first: '首页',
						last: '尾页',*/
						jump: function(obj, first){	//当前分页所有选项值、是否首次
							if(!first){	//首次不执行
                                pagi_stunetArea(obj.curr);	//获取点击页的数据
							}
							if(totalCount){
								var firstR = 0, lastR = 0;
								if(obj.curr == totalPage){
									lastR = totalCount;
									firstR = pageSize * (obj.curr - 1) + 1;
								}else{
									firstR = (obj.curr - 1) * pageSize + 1;
									lastR = obj.curr * pageSize;
								}
								$("#pageText").html("共 <span class='num'>"+totalPage+"</span> 页 ，" +
										"<span class='num'>"+totalCount+"</span> 条，" +
										" 当前第 <span class='num'>"+curPage+"</span>" +
										" 页，第 <span class='num'>"+firstR+"</span> 条至" +
										"第 <span class='num'>"+lastR+"</span> 条");
							}else{
								$("#pageText").html("");
							}
						}
					});
				});
			}
		}
	});	
}
/**
 * 学生沉迷网络原因
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_stuNetKey(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table3 .search_condition");
	var studentName = conditions.eq(0).children("input").val(),	//学院名称
	collegeName = conditions.eq(1).children("input").val(),	//学院
	major = conditions.eq(2).children("input").val(),	//专业
	startDate = conditions.eq(3).children("input.start").val(),	//统计开始日期
	endDate = conditions.eq(3).children("input.end").val();	//统计结束日期
	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'post',
		url: '/netWork/getStuNetWords',
		data: {
			studentName: (studentName == null) ? "" : studentName,
			collegeName: (collegeName == "全部") ? "" : collegeName,
			major: (major  == "全部") ? "" : major,
			startDate: startDate,
			endDate: endDate,
			pageNum: curr ? curr : 1,
			pageSize: pageSize
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			$(".loading").hide();	//隐藏加载层
			if(page){
				var html = "";
				if(page.records){
					$.each(page.records, function(i, item){
						html += "<tr data-uuid='"+item.uuid+"'>" +
							    "<td width='10%' >" + item.studentID + "</td>" +
								"<td width='10%' >" + item.studentName + "</td>" +
								"<td width='10%' >" + item.collegeName + "</td>" +
								"<td width='11%' >" + item.major + "</td>" +
								"<td width='14%'>" + item.avghour+"h" + "</td>" +
								"<td width='11%'>" + item.keyWords+ "</td>" +
								"<td width='15%'>" +item.date + "</td>" +
								"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='16'>无数据</td></tr>";
				}
				
				$("#table3 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
				
				layui.use('laypage', function(){
					var laypage = layui.laypage;
					laypage.render({
						elem: $("#pagination"),	//容器
						count: totalCount,	//数据总数
						limit: pageSize,	//每页显示条数
						curr: curPage,	//当前页
						groups: 3,	//连续出现的页码个数
						prev: '<',
						next: '>',
						/*first: '首页',
						last: '尾页',*/
						jump: function(obj, first){	//当前分页所有选项值、是否首次
							if(!first){	//首次不执行
								pagi_stuNetKey(obj.curr);	//获取点击页的数据
							}
							if(totalCount){
								var firstR = 0, lastR = 0;
								if(obj.curr == totalPage){
									lastR = totalCount;
									firstR = pageSize * (obj.curr - 1) + 1;
								}else{
									firstR = (obj.curr - 1) * pageSize + 1;
									lastR = obj.curr * pageSize;
								}
								$("#pageText").html("共 <span class='num'>"+totalPage+"</span> 页 ，" +
										"<span class='num'>"+totalCount+"</span> 条，" +
										" 当前第 <span class='num'>"+curPage+"</span>" +
										" 页，第 <span class='num'>"+firstR+"</span> 条至" +
										"第 <span class='num'>"+lastR+"</span> 条");
							}else{
								$("#pageText").html("");
							}
						}
					});
				});
			}
		}
	});	
}

//导出的标识参数，常量
var data_d = {
	'pageNum' : 0,
	'pageSize': 0
};

/**
 * 导出网络设备信息
 */
function export_deviceNumber(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var deviceNumber = conditions.eq(0).children("input").val(),	//设备编号
	installLocation= conditions.eq(1).children("input").val(),	//设备安置地点
	startDate = conditions.eq(2).children("input.start").val(),	//监测开始时间
	endDate = conditions.eq(2).children("input.end").val();	//监测结束时间
	var data_c = {
					deviceNumber: (deviceNumber ==null) ? "" : deviceNumber,
					installLocation: (installLocation ==null) ? "" :installLocation,
					startDate: startDate,
					endDate: endDate,
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'post',
		url: '/netWork/networkDeviceNum',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["设备编号","终端连接数","安装时间","年限","宽带速率","宽带使用率","宽带消耗",
				             "设备地点","设备负责人","检测时间"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.deviceNumber);datarow.push(o.clientCount);datarow.push(o.installDate);
					datarow.push(o.limitedTime);datarow.push(o.broadband);datarow.push(o.netUsage);
					datarow.push(o.flowConsumped);datarow.push(o.installLocation);datarow.push(o.principalName);
					datarow.push(o.countMonth);
					datarows.push(datarow);
				});
				var fileName = '网络设备监测';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出学生上网行为和成绩
 */
function export_stunetArea(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table2 .search_condition");
	stuname = conditions.eq(0).children("input").val(),	//姓名
	college = conditions.eq(1).children("input").val(),	//学院名称
	stuClass = conditions.eq(2).children("input").val(),	//专业名称
	startDate = conditions.eq(3).children("input.start").val(),	//统计开始日期
	endDate = conditions.eq(3).children("input.end").val();	//统计结束日期
	var data_c = {
					stuname: (stuname== null) ? "" : stuname,
					college: (college== "全部") ? "" : college,
					stuClass: (stuClass== "全部") ? "" : stuClass,
					startDate: startDate,
					endDate: endDate,
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'post',
		url: '/netWork/getBehaviorAndGrade',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","学院","专业","上网时长","上网次数",
				             "上传流量总量","下载流量总量","上次考试平均成绩","统计时间"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);datarow.push(o.stuname);datarow.push(o.college);
					datarow.push(o.stuClass);datarow.push(o.hour);datarow.push(o.times);
					datarow.push(o.upload);datarow.push(o.download);datarow.push(o.grade);
					datarow.push(o.date);
					
					datarows.push(datarow);
				});
				var fileName = '学生上网行为和成绩';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}


/**
 * 导出学生沉迷网络原因
 */
function export_keyword(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table3 .search_condition");
	var studentName = conditions.eq(0).children("input").val(),	//学院名称
	collegeName = conditions.eq(1).children("input").val(),	//学院
	major = conditions.eq(2).children("input").val(),	//专业
	startDate = conditions.eq(3).children("input.start").val(),	//统计开始日期
	endDate = conditions.eq(3).children("input.end").val();	//统计结束日期
	
	var data_c = {
			        studentName: (studentName == null) ? "" : studentName,
					collegeName: (collegeName == "全部") ? "" : collegeName,
					major: (major  == "全部") ? "" : major,
					startDate: startDate,
					endDate: endDate,
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	$.ajax({
		type: 'post',
		url: '/netWork/getStuNetWords',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","学院","专业","平均每天上网时长","沉迷网络原因","统计时间"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);
					datarow.push(o.studentName);
					datarow.push(o.collegeName);
					datarow.push(o.major);
					datarow.push(o.avghour);
					datarow.push(o.keyWords);
					datarow.push(o.date);

					
					datarows.push(datarow);
				});
				var fileName = '学生沉迷上网原因';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}
/* 导出excel文件的通用方法
 * title : 表头
 * data : 数据
 * fileName : 生成的excel文件名
 */
function exportToExcel(title, data, fileName){
	if(data != null){
		//导出到excel
          var option={};//一张sheet
          option.fileName = fileName+"_"+new Date().getTime();
          option.datas=[
            {
              sheetData:data,//数据源
              sheetName:'sheet1',//sheet名
              sheetHeader:title,//表头
            }
          ];
          var toExcel=new ExportJsonExcel(option);
          toExcel.saveExcel();
	}else{
		alert("无数据导出");
	}
}


/**
 * 时间戳转换成yyyy-MM-dd HH:mm:ss格式
 * @param timeStamp	时间戳
 * @param istime	是否需要时分秒，为true是包含时分秒，为空或false不包含时分秒
 * @returns {String}
 * @author leoya
 */
function formatDateTime(timeStamp, istime) {   
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

function uploadfile(){
    $.ajax({
        url: '/campusbd/api/preciseFunding/upload',//上传地址
        type: 'POST',
        data: new FormData($('#uploadForm')[0]),//表单数据
        processData: false,
        contentType: false,
        success:function(data){
        },
        contentType: false, //必须false才会自动加上正确的Content-Type
        processData: false //必须false才会避开jQuery对 formdata 的默认处理
    
    });
}
/*多选框点击事件*/
function check(element){
	if(element.className=="check"){	//选择
		$(element).html('<img src="../../image/checked.png">');
		$(element).removeClass('check').addClass('checked');			
	}
	else{							//取消选择
		$(element).html('<img src="../../image/check.png">');
		$(element).removeClass('checked').addClass('check');
	}		
}
//设备信息表格点击负责人姓名显示详细信息
function show_principalDetail(obj){
	var $td = $(obj), uuid = $td.parent().attr("data-id"), 
	principalName= $td.text();	//负责人姓名
	parent.layer.open({
		title:  "个人详情：" + principalName,
		area: ['21%', '30%'],
		skin: 'my-default',
		shade: 0.7,
		shadeClose: true,
		btn: [],
		success: function(layero, index){
			var $layero = $(layero),
				$title = $layero.find(".layui-layer-title"),
				_title = $title.text();
			$layero.prepend("<img src='image/datamode/layer.png'>");
			$title.html("<div class='mylay-title'>" +
					"<span>" + _title + "</span>" +
					"<hr class='mylay-title-left-hr'>" +
					"<hr class='mylay-title-right-hr'>" +
					"<div class='mylay-title-point'></div></div>");
			$layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
			$layero.find('.layui-layer-setwin').css('left','44%');
			$layero.find('.layui-layer-setwin').css('height','7%');
			$layero.find('.layui-layer-content').css('height','160px');
			$.ajax({
				type: 'post',
				url: '/netWork/getEmployee',
				data: {
					principalName: principalName
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data){
						//负责人姓名
						var employeeName=data.employeename;
						//负责人所属部门
						var department=data.department;
						//负责人职务
						var position=data.position;
						//负责人电话
						var phoneNumber=data.phonenumber;
						//负责人
						var email=data.email;
						$layero.find('.layui-layer-content').html("<div style='color: white;text-align: left;'>"+"<span style='color:#5ec5fb'>负责人姓名:</span> <span style='margin-left:6px'>"+employeeName+"</span></div>"
								+"<div style='color: white;text-align: left';>"+"<span style='color:#5ec5fb'>所属部门:</span> <span style='margin-left:18px'>"+department+"</span></div>"
								+"<div style='color: white;text-align: left';>"+"<span style='color:#5ec5fb'>职务:</span><span style='margin-left:50px'>"+position+"</span></div>"
								+"<div style='color: white;text-align: left';>"+"<span style='color:#5ec5fb'>联系电话: </span><span style='margin-left:18px'>"+phoneNumber+"</span></div>"
								+"<div style='color: white;text-align: left';>"+"<span style='color:#5ec5fb'>电子邮箱: </span><span style='margin-left:18px'>"+email+"</span></div>"
										);
					}
				}
			});
		}
	})
}

/*点击学生查看学生上网内容*/
function show_stuNetDetail(obj){
	var $td = $(obj), uuid = $td.parent().attr("data-id");
	parent.layer.open({
		/*type: 2,*/
		title: '学生上网内容',
		area: ['30%', '45%'],
		skin: 'my-default',
		shade: 0.7,
		shadeClose: true,
		btn:[],
		/*content: ,*/
		success: function(layero, index){
			var $layero = $(layero), $title = $layero.find(".layui-layer-title"),
				_title = $title.text();
			$layero.prepend("<img src='image/datamode/layer.png'>");
			$title.html("<div class='mylay-title'>" +
					"<span>" + _title + "</span>" +
					"<hr class='mylay-title-left-hr'>" +
					"<hr class='mylay-title-right-hr'>" +
				"<div class='mylay-title-point'></div></div>");
			$layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
			var $content = $layero.find(".layui-layer-content");
			$.ajax({
		    	type: 'post',
		    	url: '/netWork/findStuBehaviorAndGrade',
		    	data: {
					uuid : uuid
				},
		    	dataType: 'json',
		    	success: function(result){
		    		var data = result.object;
		    		var mychart = echarts.init($content[0]);
		    		var option = {
		    				 tooltip : {
		    				        trigger: 'item',
		    				        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    				    },
		    				    series : [
		    				        {
		    				            name: '上网内容',
		    				            type: 'pie',
		    				            radius : '80%',
		    				            center: ['50%', '50%'],
		    				            data:[
		    				                {value:data.studyhour, name:'学习'},
		    				                {value:data.gamehour, name:'游戏'},
		    				                {value:data.videohour, name:'视频'},
		    				                {value:data.bookhour, name:'小说'},
		    				                {value:data.musichour, name:'音乐'},
		    				                {value:data.shoppinghour, name:'购物'},
		    				                {value:data.otherhour, name:'其它'}
		    				            ],
		    				            itemStyle: {
		    				                emphasis: {
		    				                    shadowBlur: 10,
		    				                    shadowOffsetX: 0,
		    				                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		    				                }
		    				            }
		    				        }
		    				    ]
		    		};
		    		mychart.setOption(option);
		        		}
			});
		}
	});
}

/**
 * 显示网络设备使用率Top10排行榜
 */
function top10_deviceNumber(){
	$.ajax({
		type: 'get',
		url: '/netWork/getTopDevice',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.uuid+"'>" +
					"<td width='20%'>" + (i+1) + "</td>" +
					"<td width='40%'>" + item.deviceNumber + "</td>" +
					"<td width='40%'>" + item.netUsage+ "</td>" +
					"</tr>";
				})
			}
			else{	//数据为空
				html = "<tr><td colspan='8'>无数据</td></tr>";
			}
			$('#top10_1 tbody').html(html);			
        }
	});
}

/**
 * 显示学生平均上网时长Top10排行榜
 */
function top10_stuNet(){
	$.ajax({
		type: 'get',
		url: '/netWork/getTopStuNet',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.studentID+"'>" +
					"<td width='20%'>" + (i+1) + "</td>" +
					"<td width='35%' >" + item.stuname + "</td>" +
					"<td width='45%' >" + item.hour + "</td>" +
					"</tr>";
				})
			}
			else{	//数据为空
				html = "<tr><td colspan='8'>无数据</td></tr>";
			}
			$('#top10_2 tbody').html(html);			
        }
	});
}

/**
 * 显示上网原因Top10排行榜
 */
function top10_stuKeyWord(){	
	$.ajax({
		type: 'get',
		url: '/netWork/getKeyWords',
		dataType: 'json',
		success: function(result){
			var page = result.object;
			var html = "";
			if(page){
				//if(page.list){
					$.each(page, function(i, item){
						html += "<tr>" +
						"<td width='20%'>" + (i+1) + "</td>" +
						"<td width='40%'>" + item[0] + "</td>" +
						"<td width='40%'>" + item[1] + "</td>" +
						"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='8'>无数据</td></tr>";
				}
				$('#top10_3 tbody').html(html);
			//}			
		}
	});
}

/**
 * 显示最近5条通知通告
 */
function top5_notice(){
	$.ajax({
		type: 'post',
		url: '/campusbd/api/preciseFunding/findTopNotice',
		dataType: 'json',
		data:{
			belong:'精准资助'
		},
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<li>"+
					"<span class='notice_title'>"+item.noticeTitle+"</span>"+
					"<span class='notice_time'>"+item.noticeTime+"</span>"+
					"<div class='notice_content'>"+item.noticeContent+"</div>"+
				"</li>";
				})
			}
			else{	//数据为空
				html = "<div style='color: #82ABC1;text-align:center;'>没有最新的通知通告</div>";
			}
			$('#notice ul').html(html);			
        }
	});
}