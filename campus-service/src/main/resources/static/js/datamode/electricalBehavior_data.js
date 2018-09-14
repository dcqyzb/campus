/**
 *	数据模式下的用电行为js
 */

var pageSize = 18;	//分页每页显示记录条数

/*js加载执行*/
(function(){
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
})();

/*点击导航切换*/
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
	
	//加载数据前清空所有的查询条件
	$(".clear_btn").click();
	
	switch(curIndex){
	case 0: 
		pagi_collegeElec();	//学院用电统计
		$(".rank .title span").text("学院用电Top10");
		top10_collegeElec();
		break;
	case 1: 
		pagi_dormElec("","steal");
		$(".rank .title span").text("宿舍窃电嫌疑Top10");
		top10_dorm("steal");
		break;
	case 2: 
		pagi_dormElec("","highPower");
		$(".rank .title span").text("宿舍违章嫌疑Top10");
		top10_dorm("highPower");
		break;
	case 3: 
		pagi_dormElec("","unclose");
		$(".rank .title span").text("宿舍未关电器Top10");
		top10_dorm("unclose");
		break;
	default: 
		break;
	}	
});

/*页面渲染完成执行*/
$(document).ready(function(){
	var w = $(".center_table").width();
	if(w < 1085){
		$("#table2 .search_area>div").css("top", "20px");
		$("#table2 .search_area>div:not(div[class=search_condition])").css("top", "25px");
		$("#table3 .search_area>div").css("top", "20px");
		$("#table3 .search_area>div:not(div[class=search_condition])").css("top", "25px");
		$("#table4 .search_area>div").css("top", "20px");
		$("#table4 .search_area>div:not(div[class=search_condition])").css("top", "25px");
	}
	/*
	 * 获取学院专业
	 */
	$.ajax({
		type: 'get',
		url: '/poverty/getCollegeMajor',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data){
				college_major = data;
				var college_li = "<li class='selected'>全部</li>";
				$.each(data, function(college, majors){
					college_li += "<li title='"+college+"'>" + college +"</li>";
				});
				$("ul.college").html(college_li);
			}
		}
	});

	//通知通告
	top5_notice();
	//默认点击左边导航条第一个
	$("ul.nav_ul li").eq(0).click();
});

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

//点击空白隐藏下拉框
$(document).bind("click", function(e){
	var className = e.target.className;
	if(!className || (className && className.indexOf("select-") == -1)){
		$(".select-ul").hide();
	}
});

/*查询条件清空*/
function clearCondition(obj){
	if(!obj) return;
	var $this = $(obj),
		$select_input = $this.siblings().find("input.select-input"),
		$select_ul = $this.siblings().find("ul.select-ul"),
		$date_input = $this.siblings().find(".input_year,.input_month,.input_date");
	//输入框清空
	$this.siblings().find("input").val("");
	
	$select_input.val("全部");
	$select_ul.map(function(){
		$(this).find("li").eq(0).addClass("selected").siblings().removeClass("selected");
	});
	$date_input.map(function(){
		$(this).val("");
		if($(this).attr("class").indexOf("start") != -1
				|| $(this).attr("class").indexOf("end") != -1){
			$(this).datetimepicker("setStartDate", null);
			$(this).datetimepicker("setEndDate", new Date());
		}
	});
}

/**
 * 分页显示学院用电
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_collegeElec(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		startMonth = conditions.eq(1).children("input.start").val(),	//选择的起始年份
		endMonth = conditions.eq(1).children("input.end").val();	//选择的结束年份

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/getCollegeElecByCondition',
		data: {
			collegeName: (collegeName == "全部") ? "" : collegeName,
			startMonth: startMonth,
			endMonth: endMonth,
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
						var n = ((curr ? curr : 1) - 1) * page.size + i + 1;
						//学院月用电量
						var electricity = item.electricity+"";
						electricity = electricity.substring(0,electricity.indexOf("."));
						//学院总人数，自取整数部分
						var peopleNum = item.manNum+item.womanNum;
						//男女比
						var rate = (item.manNum/item.womanNum)+"";
						rate = rate.substring(0,3)+" : 1";
						//人均月用电量，自取整数部分
						var avgElec = item.avgElec+"";
						avgElec = avgElec.substring(0,avgElec.indexOf("."));
						//同比涨幅显示小数点后两位
						var increase = item.increase*100+"";
						increase = increase.substring(0,increase.indexOf("."))+"%";
						//涨跌幅显示
						var img = (item.increase > 0.1) ? 
								"<img width='14' src='../../image/datamode/increase.png'>" :
								((item.increase < -0.1) ? "<img width='14' src='../../image/datamode/decrease.png'>" :
									"<img width='14' src='../../image/datamode/unchange.png'>");
						//修改三类宿舍个数表格样式，点击个数显示宿舍详情
						var green_html = "<td width='10%'>&nbsp;" + item.greenDormNum + "&nbsp;</td>";
						if(item.greenDormNum > 0){
							green_html = "<td width='10%' class='clickable' onclick='show_greenDormDetail(this)'>&nbsp;" 
								+ item.greenDormNum + "&nbsp;</td>";
						}
						var steal_html = "<td width='10%'>&nbsp;" + item.stealDormNum + "&nbsp;</td>";
						if(item.stealDormNum > 0){
							steal_html = "<td width='10%' class='clickable' onclick='show_stealDormDetail(this)'>&nbsp;" 
								+ item.stealDormNum + "&nbsp;</td>";
						}
						var highPower_html = "<td width='10%' >&nbsp;" + item.highPowerDormNum + "&nbsp;</td>";
						if(item.highPowerDormNum > 0){
							highPower_html = "<td width='10%' class='clickable' onclick='show_highPowerDormDetail(this)'>&nbsp;" 
								+ item.highPowerDormNum + "&nbsp;</td>";
						}
						html += "<tr data-collegeName='"+item.collegeName+"' data-month='"+item.date+"'>" +
							"<td width='15%' title='" + item.collegeName + "'>" + item.collegeName + "</td>" +
							"<td width='10%'>" + item.date + "</td>" +
							"<td width='10%'>" + electricity + "</td>" +
							"<td width='10%'>" + peopleNum + "</td>" +
							"<td width='10%' title='同比涨幅"+increase+"'>" + avgElec +img + "</td>" +
							"<td width='10%'>" + rate + "</td>" +
							green_html +
							steal_html +
							highPower_html +
						"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='9'>无数据</td></tr>";
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
						/*first: '首页',
						last: '尾页',*/
						jump: function(obj, first){	//当前分页所有选项值、是否首次
							if(!first){	//首次不执行
								pagi_collegeElec(obj.curr);	//获取点击页的数据
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
 * 分页显示宿舍用电
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_dormElec(curr,belong){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = "";
	switch(belong){
	case 'steal':
		conditions = $("#table2 .search_condition");
		break;
	case 'highPower':
		conditions = $("#table3 .search_condition");
		break;
	case 'unclose':	
		conditions = $("#table4 .search_condition");
		break;
	default: 
		break;	
	}
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		dormName=conditions.eq(1).children("input").val(),	//学院名称
		roomName=conditions.eq(2).children("input").val(),	//学院名称
		startMonth = conditions.eq(3).children(".start").val(),	//选择的起始年份
		endMonth = conditions.eq(3).children(".end").val();	//选择的结束年份

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/findElectricByConition',
		data: {
			collegeName: (collegeName == "全部") ? "" : collegeName,
			dormName: (dormName == "全部") ? "" : dormName.replace(/栋/,""),
			roomName: (roomName == "全部") ? "" : roomName,
			startMonth: startMonth,
			endMonth: endMonth,
			belong:belong,
			pageNum: curr ? curr : 1,
			pageSize: pageSize
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			$(".loading").hide();	//隐藏加载层
			if(page){
				var html = "";
				var stuNum=0,dormName="";
				switch(belong){
				case 'steal':					
					if(page.records){
						$.each(page.records, function(i, item){
							stuNum=item.students.split(',').length;
							dormName=item.dormID.split('-')[0]+"栋";
							//宿舍月用电量
							var electricity = item.electricity+"";
							electricity = electricity.substring(0,electricity.indexOf("."));
							//同比涨幅显示小数点后两位
							var increase = item.increase*100+"";
							increase = increase.substring(0,increase.indexOf("."))+"%";
							//涨跌幅显示
							var img = (item.increase > 0.1) ? 
									"<img width='14' src='../../image/datamode/increase.png'>" :
									((item.increase < -0.1) ? "<img width='14' src='../../image/datamode/decrease.png'>" :
										"<img width='14' src='../../image/datamode/unchange.png'>");
							/*
							 * 根据窃电嫌疑大小，字体显示不同颜色
							 */
							var stealSusphtml = "";
							var stealSusp=((item.stealSusp*100)+"0000").substring(0,5);
							if(stealSusp>80){
								stealSusphtml = "<td width='10%' class='poor-level1' title='" + stealSusp + "%'>严重</td>";
							}
							else if(stealSusp>50){
								stealSusphtml = "<td width='10%' class='poor-level2' title='" + stealSusp + "%'>一般</td>";
							}
							else{
								stealSusphtml = "<td width='10%' class='poor-level3' title='" + stealSusp + "%'>无</td>";
							}
							/*
							 * 窃电次数为0，不添加点击事件
							 */
							var stealSum_html = "<td width='15%' title='" + item.stealSum + "' class='clickable' onclick='show_stealHistory("+
											'"'+item.dormID+'"'+","+'"'+item.date+'"'+")'>" + item.stealSum + "次</td>";
							if(item.stealSum == 0){
								stealSum_html = "<td width='15%'>" + item.stealSum + "次</td>";
							}

							html += "<tr data-id='"+item.uuid+"'>" +
									"<td width='15%' title='" + item.dormID + "' class='clickable'" + 
									"onclick='show_studentsDetail("+'"'+item.dormID+'"'+","+'"'+item.date+'"'+")'>"+
									item.dormID + "</td>" +
									"<td width='10%'>" + dormName+ "</td>" +
									"<td width='10%'>" + item.date + "</td>" +
									"<td width='15%' title='同比涨幅"+increase+"'>" + electricity +img+ "</td>" +
									stealSusphtml +
									"<td width='10%'>" + item.collegeName + "</td>" +
									"<td width='10%'>" + item.type + "</td>" +
									"<td width='5%'>" + stuNum + "人</td>" +
									stealSum_html +
									"</tr>";
						});
					}else{	//数据为空
						html = "<tr><td colspan='9'>无数据</td></tr>";
					}
					
					$("#table2 tbody").html(html);
					break;
				case 'highPower':
					if(page.records){
						$.each(page.records, function(i, item){										
							stuNum=item.students.split(',').length;
							dormName=item.dormID.split('-')[0]+"栋";
							//宿舍月用电量
							var electricity = item.electricity+"";
							electricity = electricity.substring(0,electricity.indexOf("."));
							//同比涨幅显示小数点后两位
							var increase = item.increase*100+"";
							increase = increase.substring(0,increase.indexOf("."))+"%";
							//涨跌幅显示
							var img = (item.increase > 0.1) ? 
									"<img width='14' src='../../image/datamode/increase.png'>" :
									((item.increase < -0.1) ? "<img width='14' src='../../image/datamode/decrease.png'>" :
										"<img width='14' src='../../image/datamode/unchange.png'>");
							/*
							 * 根据违章嫌疑大小，字体显示不同颜色
							 */
							var highPowerSusphtml = "";
							var highPowerSusp=((item.highPowerSusp*100)+"0000").substring(0,5);
							if(highPowerSusp>80){
								highPowerSusphtml = "<td width='10%' class='poor-level1' title='" + highPowerSusp + "%'>严重</td>";
							}
							else if(highPowerSusp>50){
								highPowerSusphtml = "<td width='10%' class='poor-level2' title='" + highPowerSusp + "%'>一般</td>";
							}
							else{
								highPowerSusphtml = "<td width='10%' class='poor-level3' title='" + highPowerSusp + "%'>无</td>";
							}
							/*
							 * 违章次数为0，不添加点击事件
							 */
							var highPowerSum_html = "<td width='15%' title='" + item.highPowerSum + "' class='clickable' onclick='show_highPowerHistory("+
											'"'+item.dormID+'"'+","+'"'+item.date+'"'+")'>" + item.highPowerSum + "次</td>";
							if(item.highPowerSum == 0){
								highPowerSum_html = "<td width='15%'>" + item.highPowerSum + "次</td>";
							}
							
							html += "<tr data-id='"+item.uuid+"'>" +
									"<td width='15%' title='" + item.dormID + "' class='clickable'" +
									"onclick='show_studentsDetail("+'"'+item.dormID+'"'+","+'"'+item.date+'"'+")'>"+
									item.dormID + "</td>" +
									"<td width='10%'>" + dormName+ "</td>" +
									"<td width='10%'>" + item.date + "</td>" +
									"<td width='15%' title='同比涨幅"+increase+"'>" + electricity +img+ "</td>" +
									highPowerSusphtml +
									"<td width='10%'>" + item.collegeName + "</td>" +
									"<td width='10%'>" + item.type + "</td>" +
									"<td width='5%'>" + stuNum + "人</td>" +
									highPowerSum_html +
									"</tr>";
						});
					}else{	//数据为空
						html = "<tr><td colspan='9'>无数据</td></tr>";
					}
					
					$("#table3 tbody").html(html);
					break;
				case 'unclose':
					if(page.records){
						$.each(page.records, function(i, item){										
							stuNum=item.students.split(',').length;
							dormName=item.dormID.split('-')[0]+"栋";
							//宿舍月用电量
							var electricity = item.electricity+"";
							electricity = electricity.substring(0,electricity.indexOf("."));
							//同比涨幅显示小数点后两位
							var increase = item.increase*100+"";
							increase = increase.substring(0,increase.indexOf("."))+"%";
							//涨跌幅显示
							var img = (item.increase > 0.1) ? 
									"<img width='14' src='../../image/datamode/increase.png'>" :
									((item.increase < -0.1) ? "<img width='14' src='../../image/datamode/decrease.png'>" :
										"<img width='14' src='../../image/datamode/unchange.png'>");
							/*
							 * 未关电器次数为0，不添加点击事件
							 */
							var uncloseNum_html = "<td width='15%' title='" + item.uncloseNum + "' class='clickable' onclick='show_uncloseDetail("+
											'"'+item.dormID+'"'+","+'"'+item.date+'"'+","+'"'+item.uncloseNum+'"'+")'>" + item.uncloseNum + "次</td>";
							if(item.uncloseNum == 0){
								uncloseNum_html = "<td width='15%'>" + item.uncloseNum + "次</td>";
							}
							html += "<tr data-id='"+item.uuid+"'>" +
									"<td width='15%' title='" + item.dormID + "' class='clickable'" +
										"onclick='show_studentsDetail("+'"'+item.dormID+'"'+","+'"'+item.date+'"'+")'>" +
										item.dormID + "</td>" +
									"<td width='10%'>" + dormName+ "</td>" +
									"<td width='15%'>" + item.date + "</td>" +
									"<td width='15%' title='同比涨幅"+increase+"'>" + electricity +img+ "</td>" +
									"<td width='10%'>" + item.collegeName + "</td>" +
									"<td width='10%'>" + item.type + "</td>" +
									"<td width='10%'>" + stuNum + "人</td>" +							
									uncloseNum_html +
									"</tr>";
						});
					}else{	//数据为空
						html = "<tr><td colspan='9'>无数据</td></tr>";
					}
					
					$("#table4 tbody").html(html);
					break;
				default: 
					break;
				}
				
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
								pagi_dormElec(obj.curr,belong);	//获取点击页的数据
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
 * 点击节能宿舍数，显示学院某月节能宿舍
 * @param obj
 */
function show_greenDormDetail(obj){
	var map = $(obj).parent()[0].dataset;
	var collegeName = map.collegename;
	var date = map.month;
	parent.layer.open({
		title:  collegeName+" "+date+" 节能宿舍" ,
		area: ['540px', '258px'],
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
			
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getCollegeDormGreen',
				dataType: 'json',
				data:{
					collegeName:collegeName,
					date:date
				},
				success: function(result){
					var data = result.object;
					if(data){
						var html="";
						var thead = "<thead><tr><th>宿舍号</th>"
							  +"<th>日期</th>"
							  +"<th>人数</th>"
							  +"<th>类型</th>"
							  +"<th style='width:25%;'>人均用电（度）</th>"
							  +"</tr></thead>";
						$.each(data, function(i, item){
							html+="<tr><td>"+item.dormID+"</td>"
									 +"<td>"+item.date+"</td>"
									 +"<td>"+item.peopleNum+"</td>"
									 +"<td>"+item.type+"</td>"
									 +"<td>"+item.avgElec+"</td>";
						
						});
						$layero.find(".layui-layer-content").html("<table>"+thead+html+"</table>");
					}
				}
			});
		}
	});
}
/**
 * 点击窃电宿舍数，显示学院某月窃电宿舍
 * @param obj
 */
function show_stealDormDetail(obj){
	var map = $(obj).parent()[0].dataset;
	var collegeName = map.collegename;
	var date = map.month;
	parent.layer.open({
		title:  collegeName+" "+date+" 窃电宿舍" ,
		area: ['540px', '258px'],
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
			
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getCollegeDormSteal',
				dataType: 'json',
				data:{
					collegeName:collegeName,
					date:date
				},
				success: function(result){
					var data = result.object;
					if(data){
						var html="";
						var thead = "<thead><tr><th>宿舍号</th>"
							  +"<th>日期</th>"
							  +"<th>人数</th>"
							  +"<th style='width:23%;'>用电量（度）</th>" 
							  +"<th style='width:23%;'>窃电量（度）</th>" 
							  +"</tr></thead>";
						$.each(data, function(i, item){
							html+="<tr><td>"+item.dormID+"</td>"
									 +"<td>"+item.date+"</td>"
									 +"<td>"+item.peopleNum+"</td>"
									 +"<td>"+item.electricity+"</td>"
									 +"<td>"+item.stealElec+"</td>";
						
						});
						$layero.find(".layui-layer-content").html("<table>"+thead+html+"</table>");
					}
				}
			});
		}
	});

}
/**
 * 点击违章宿舍数，显示学院某月违章宿舍
 * @param obj
 */
function show_highPowerDormDetail(obj){
	var map = $(obj).parent()[0].dataset;
	var collegeName = map.collegename;
	var date = map.month;
	parent.layer.open({
		title:  collegeName+" "+date+" 违章宿舍" ,
		area: ['540px', '258px'],
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
			
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getCollegeDormHighPower',
				dataType: 'json',
				data:{
					collegeName:collegeName,
					date:date
				},
				success: function(result){
					var data = result.object;
					if(data){
						var html="";
						var thead = "<thead><tr><th>宿舍号</th>"
							  +"<th>日期</th>"
							  +"<th>人数</th>"
							  +"<th>类型</th>"
							  +"<th style='width:22%;'>用电量（度）</th>" 
							  +"<th>违章电器</th>" 
							  +"</tr></thead>";
						$.each(data, function(i, item){
							html+="<tr><td>"+item.dormID+"</td>"
									 +"<td>"+item.date+"</td>"
									 +"<td>"+item.peopleNum+"</td>"
									 +"<td>"+item.type+"</td>"
									 +"<td>"+item.electricity+"</td>"
									 +"<td>"+item.highPower+"</td>";
						
						});
						$layero.find(".layui-layer-content").html("<table>"+thead+html+"</table>");
					
					}
				}
			});
		}
	});

}

/**
 * 点击历史窃电次数，显示历史窃电详情
 * @param dormId 宿舍号
 * @param date 时间
 */
function show_stealHistory(dormId,date){
	parent.layer.open({
		title:  "宿舍历史窃电详情：" + dormId,
		type: 1,
		area: ['540px', '322px'],
		skin: 'my-default',
		shade: 0.7,
		shadeClose:true,
		success: function(layero, index){
			var $layero = $(layero),
			$content = $layero.find(".layui-layer-content"),
			$title = $layero.find(".layui-layer-title"),
			_title = $title.text();
			$layero.prepend("<img src='image/datamode/layer.png'>");
			$title.html("<div class='mylay-title'>" +
					"<span>" + _title + "</span>" +
					"<hr class='mylay-title-left-hr'>" +
					"<hr class='mylay-title-right-hr'>" +
			"<div class='mylay-title-point'></div></div>");
			$layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
			$content.css("height","81%");
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getDormStealHistory',
				data: {
					dormId:dormId,
					date:date
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//弹窗中内容
						var dormName = dormId.split('-')[0]+"栋";
						var houseName =  dormId.split('-')[1];
						var top="<div class='dormInfo' style='margin-top:0px;'><span>宿舍："+dormName+houseName+"</span>" +
									"&nbsp;&nbsp;&nbsp;<span>"+date+"窃电次数："+data.length+"次</span>"+
								"</div>";
						$content.html(top+
									  '<div class="history">'+
									  '<div class="history_main">'+
									  '<div class="history_line"></div><ul></ul></div></div>'
									 );

						$.each(data, function(i, item){
							$content.find(".history_main ul").append("<li>"+
									item.date+"&nbsp;&nbsp;&nbsp;月用电量："+
									item.electricity+"度"+"&nbsp;&nbsp;&nbsp;窃电量："+
									item.stealElec+"度"+
									'</li>');
						});
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>无历史窃电记录！</div>");
					}
				}
				});
			}
	});
}

/**
 * 点击历史违章次数，显示历史违章详情
 * @param dormId 宿舍号
 * @param date 时间
 */
function show_highPowerHistory(dormId,date){
	parent.layer.open({
		title:  "宿舍历史违章详情：" + dormId,
		type: 1,
		area: ['540px', '322px'],
		skin: 'my-default',
		shade: 0.7,
		shadeClose:true,
		success: function(layero, index){
			var $layero = $(layero),
			$content = $layero.find(".layui-layer-content"),
			$title = $layero.find(".layui-layer-title"),
			_title = $title.text();
			$layero.prepend("<img src='image/datamode/layer.png'>");
			$title.html("<div class='mylay-title'>" +
					"<span>" + _title + "</span>" +
					"<hr class='mylay-title-left-hr'>" +
					"<hr class='mylay-title-right-hr'>" +
			"<div class='mylay-title-point'></div></div>");
			$layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
			$content.css("height","81%");
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getDormHighPowerHistory',
				data: {
					dormId:dormId,
					date:date
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//弹窗中内容
						var dormName = dormId.split('-')[0]+"栋";
						var houseName =  dormId.split('-')[1];
						var top="<div class='dormInfo' style='margin-top:0px;'><span>宿舍："+dormName+houseName+"</span>" +
									"&nbsp;&nbsp;&nbsp;<span>"+date+"违章次数："+data.length+"次</span>"+
								"</div>";
						$content.html(top+
									  '<div class="history">'+
									  '<div class="history_main">'+
									  '<div class="history_line"></div><ul></ul></div></div>'
									 );

						$.each(data, function(i, item){
							$content.find(".history_main ul").append("<li>"+
									item.date+"&nbsp;&nbsp;&nbsp;违章电器："+
									item.highPower+
									'</li>');
						});
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>无历史违章记录！</div>");
					}
				}
				});
			}
	});
}

/**
 * 点击未关电器次数，显示当月未关电器详情
 * @param dormId 宿舍号
 * @param date 时间
 * @param totalNum 未关电器总次数
 */
function show_uncloseDetail(dormId,date,totalNum){
	parent.layer.open({
		title:  "宿舍当月未关电器详情：" + dormId,
		type: 1,
		area: ['540px', '322px'],
		skin: 'my-default',
		shade: 0.7,
		shadeClose:true,
		success: function(layero, index){
			var $layero = $(layero),
			$content = $layero.find(".layui-layer-content"),
			$title = $layero.find(".layui-layer-title"),
			_title = $title.text();
			$layero.prepend("<img src='image/datamode/layer.png'>");
			$title.html("<div class='mylay-title'>" +
					"<span>" + _title + "</span>" +
					"<hr class='mylay-title-left-hr'>" +
					"<hr class='mylay-title-right-hr'>" +
			"<div class='mylay-title-point'></div></div>");
			$layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
			$content.css("height","81%");
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getUncloseDetail',
				data: {
					dormID:dormId,
					date:date
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//弹窗中内容
						var dormName = dormId.split('-')[0]+"栋";
						var houseName =  dormId.split('-')[1];
						var top="<div class='dormInfo' style='margin-top:0px;'><span>宿舍："+dormName+houseName+"</span>" +
									"&nbsp;&nbsp;&nbsp;<span>"+date+"未关电器次数："+totalNum+"次</span>"+
								"</div>";
						$content.html(top+
								      '<div class="history">'+
									  '<div class="history_main">'+
									  '<div class="history_line"></div><ul></ul></div></div>'
									 );
						$.each(data, function(i, item){
							var period = "";
							//具体时间段
							var times = item.value.split(",");
							//遍历
							for(var m = 0,len=times.length;m<len;m++){
								if(times[m]){
									period += "<div class='period'>"+
													times[m]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可能存在未关电器"+
												"</div>";
								}
							}
							$content.find(".history_main ul").append("<li>"+
										item.name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次数："+(times.length-1)+
										period+
										"</li>");
						});
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>当月无未关电器记录!</div>");
						}
					}
				});
			}	
	});
}

/**
 * 点击宿舍号，显示宿舍学生的详情
 * @param dormId 宿舍号
 * @param date 时间
 */
function show_studentsDetail(dormId,date){
	parent.layer.open({
		title:  "宿舍人员" ,
		area: ['742px', '419px'],
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
			$.ajax({
				type: 'get',
				url: '/electricalBehavior/getDormStudents',
				data: {
					dormID:dormId,
					date:date
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//弹窗中内容
						var dormName = dormId.split('-')[0]+"栋";
						var houseName =  dormId.split('-')[1];
						var top="<div class='dormInfo'><span>宿舍："+dormName+houseName+"</span>" +
									"&nbsp;&nbsp;&nbsp;<span>人数："+data.length+"人</span>"+
								"</div>";
						var html ="<tr>";
						for(var i = 0;i<6;i++){
							if(i<data.length){
								html+="<td>" +
									"<div class='left'>" +
										"<img src='image/datamode/bed.png'><br/>"+(i+1)+"号床" +
									"</div>" +
									"<div class='right'>" +
										"<ul><li>学号：" +data[i].studentID+"</li>"+
											"<li>姓名：" +data[i].studentName+"</li>"+
											"<li>学院：" +data[i].collegeName+"</li>"+
											"<li title='"+data[i].stuClass+"' style='overflow: hidden;text-overflow: ellipsis;'>专业：" +data[i].stuClass+"</li>"+
											"<li>电话：" +data[i].phone+"</li>"+
										"</ul>" +
									"</div>"
								"</td>";
							}
							else{
								html+="<td>" +
								"<div class='left'>" +
									"<img src='image/datamode/bed.png'><br/>"+(i+1)+"号床" +
								"</div>" +
								"<div class='right'>" +
									
								"</div>"
							"</td>";
							}
							if(i==2){
								html+="</tr><tr>";
							}
						}
						html+="</tr>";
						$layero.find(".layui-layer-content").html(top+"<table class='studentTable'>"+html+"</table>");	
						//隐藏滚动条
						$layero.find(".layui-layer-content").css("overflow-y","hidden");
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>无学生信息!</div>");
					}
				}	
			});
		}
	});
}

//导出的标识参数，常量
var data_d = {
	'pageNum' : 0,
	'pageSize': 0
};

/**
 * 导出学院用电
 */
function export_collegeElec(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		startMonth = conditions.eq(1).children("input.start").val(),	//选择的起始年份
		endMonth = conditions.eq(1).children("input.end").val();	//选择的结束年份
	
	var data_c = {
			'collegeName': (collegeName == "全部") ? "" : collegeName,
			'startMonth': startMonth,
			'endMonth': endMonth
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/getCollegeElecByCondition',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学院","日期","用电量（度）","用电人数","人均用电量（度）","同比涨幅","男女比例","节能宿舍数",
				             "窃电宿舍数","违章宿舍数"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//学院总人数，自取整数部分
					var peopleNum = o.manNum+o.womanNum;
					//男女比
					var rate = (o.manNum/o.womanNum)+"";
					rate = rate.substring(0,3)+" : 1";
					//按表头字段顺序填入数据
					datarow.push(o.collegeName);
					datarow.push(o.date);
					datarow.push(o.electricity);
					datarow.push(peopleNum);
					datarow.push(o.avgElec);
					datarow.push(o.increase);
					datarow.push(rate);
					datarow.push(o.greenDormNum);
					datarow.push(o.stealDormNum);
					datarow.push(o.highPowerDormNum);
					
					datarows.push(datarow);
				});
				var fileName = '学院用电统计';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});

}

/**
 * 导出宿舍用电
 */
function export_dormElec(belong){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = "";
	switch(belong){
	case 'steal':
		conditions = $("#table2 .search_condition");
		break;
	case 'highPower':
		conditions = $("#table3 .search_condition");
		break;
	case 'unclose':	
		conditions = $("#table4 .search_condition");
		break;
	default: 
		break;	
	}
	
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
	dormName=conditions.eq(1).children("input").val(),	//学院名称
	roomName=conditions.eq(2).children("input").val(),	//学院名称
	startMonth = conditions.eq(3).children(".start").val(),	//选择的起始年份
	endMonth = conditions.eq(3).children(".end").val();	//选择的结束年份
	
	var data_c = {
			collegeName: (collegeName == "全部") ? "" : collegeName,
			dormName: (dormName == "全部") ? "" : dormName.replace(/栋/,""),
			roomName: (roomName == "全部") ? "" : roomName,
			startMonth: startMonth,
			endMonth: endMonth,
			belong:belong
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/findElectricByConition',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){				
				switch(belong){
				case 'steal':					
					var title = ["宿舍号","所属楼栋","时间","月用电量（度）","窃电嫌疑","学院","寝室类型","人数",
					             "历史窃电次数"];  //表头								
					var datarows = [];      //数据
					$.each(data.records, function(i, o){
						var datarow = [];   //每一行的数据
						
						var dormName=o.dormID.split('-')[0]+"栋";
						var steal = "";
						var stealSusp=((o.stealSusp*100)+"0000").substring(0,5);
						if(stealSusp>80){
							steal = "严重（"+ stealSusp + "%）";
						}
						else if(stealSusp>50){
							steal = "一般（"+ stealSusp + "%）";
						}
						else{
							steal = "无（"+ stealSusp + "%）";
						}
						var stuNum=o.students.split(',').length;
						
						//按表头字段顺序填入数据
						datarow.push(o.dormID);
						datarow.push(dormName);
						datarow.push(o.date);
						datarow.push(o.electricity);
						datarow.push(steal);
						datarow.push(o.collegeName);
						datarow.push(o.type);
						datarow.push(stuNum);
						datarow.push(o.stealSum);
						
						datarows.push(datarow);
					});
					var fileName = '窃电行为分析';//生成的文件名
					break;
				case 'highPower':
					var title = ["宿舍号","所属楼栋","时间","月用电量（度）","违章嫌疑","学院","寝室类型","人数",
					             "历史违章次数"];  //表头
					var datarows = [];      //数据
					$.each(data.records, function(i, o){
						var datarow = [];   //每一行的数据
						
						var dormName=o.dormID.split('-')[0]+"栋";
						var highPower = "";
						var highPowerSusp=((o.highPowerSusp*100)+"0000").substring(0,5);
						if(highPowerSusp>80){
							highPower = "严重（"+ highPowerSusp + "%）";
						}
						else if(highPowerSusp>50){
							highPower = "一般（"+ highPowerSusp + "%）";
						}
						else{
							highPower = "无（"+ highPowerSusp + "%）";
						}
						var stuNum=o.students.split(',').length;
						
						//按表头字段顺序填入数据
						datarow.push(o.dormID);
						datarow.push(dormName);
						datarow.push(o.date);
						datarow.push(o.electricity);
						datarow.push(highPower);
						datarow.push(o.collegeName);
						datarow.push(o.type);
						datarow.push(stuNum);
						datarow.push(o.highPowerSum);
						
						datarows.push(datarow);
					});
					var fileName = '违章电器情况';//生成的文件名
					break;
				case 'unclose':
					var title = ["宿舍号","所属楼栋","时间","月用电量（度）","所属学院","寝室类型","寝室人数",
					             "未关电器次数"];  //表头
					var datarows = [];      //数据
					$.each(data.records, function(i, o){
						var datarow = [];   //每一行的数据
						
						var dormName=o.dormID.split('-')[0]+"栋";
						var stuNum=o.students.split(',').length;

						//按表头字段顺序填入数据
						datarow.push(o.dormID);
						datarow.push(dormName);
						datarow.push(o.date);
						datarow.push(o.electricity);
						datarow.push(o.collegeName);
						datarow.push(o.type);
						datarow.push(stuNum);
						datarow.push(o.uncloseNum);
						
						datarows.push(datarow);
					});
					var fileName = '未关电器情况';//生成的文件名
					break;
				default: 
					break;
				}
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
 * 显示学院人均用电Top10排行榜
 */
function top10_collegeElec(){	
	
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/getCollegeElecByCondition',
		data: {
			collegeName: "",
			startMonth: "",
			endMonth: "",
			pageNum: 1,
			pageSize: 10
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			if(page){
				var html = "";
				if(page.records){
					var html = "";
					if(page.records){
						$.each(page.records, function(i, item){
							//人均月用电量，自取整数部分
							var avgElec = item.avgElec+"";
							avgElec = avgElec.substring(0,avgElec.indexOf("."));
							html += "<tr>" +
							"<td width='15%'>" + (i+1) + "</td>" +
							"<td width='40%'>" + item.collegeName + "</td>" +
							"<td width='45%'>" + avgElec + "</td>" +
							"</tr>";
						});
					}else{	//数据为空
						html = "<tr><td colspan='8'>无数据</td></tr>";
					}
					$('#top10_1 tbody').html(html);
				}
			}
		}
	});
}

/**
 * 显示宿舍用电Top10排行榜（窃电，违章，未关）
 */
function top10_dorm(belong){		
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/findElectricByConition',
		data: {
			collegeName: "",
			dormName: "",
			roomName: "",
			startMonth: "",
			endMonth: "",
			belong:belong,
			pageNum: 1,
			pageSize: 10
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			if(page){
				var html = "";
				if(page.records){
					var html = "";					
					switch(belong){
					case 'steal':					
						if(page.records){
							$.each(page.records, function(i, item){
								html += "<tr>" +
								"<td width='20%'>" + (i+1) + "</td>" +
								"<td width='40%'>" + item.dormID + "</td>" +
								"<td width='40%'>" + ((item.stealSusp*100)+"0000").substring(0,5) + "%</td>" +
								"</tr>";
							});
						}else{	//数据为空
							html = "<tr><td colspan='8'>无数据</td></tr>";
						}
						$('#top10_2 tbody').html(html);
						break;
					case 'highPower':
						if(page.records){
							$.each(page.records, function(i, item){
								html += "<tr>" +
								"<td width='20%'>" + (i+1) + "</td>" +
								"<td width='40%'>" + item.dormID + "</td>" +
								"<td width='40%'>" + ((item.highPowerSusp*100)+"0000").substring(0,5) + "%</td>" +
								"</tr>";
							});
						}else{	//数据为空
							html = "<tr><td colspan='8'>无数据</td></tr>";
						}
						$('#top10_3 tbody').html(html);
						break;
					case 'unclose':
						if(page.records){
							$.each(page.records, function(i, item){
								html += "<tr>" +
								"<td width='20%'>" + (i+1) + "</td>" +
								"<td width='40%'>" + item.dormID + "</td>" +
								"<td width='40%'>" + item.uncloseNum + "次</td>" +
								"</tr>";
							});
						}else{	//数据为空
							html = "<tr><td colspan='8'>无数据</td></tr>";
						}
						$('#top10_4 tbody').html(html);
						break;
					default: 
						break;
					}					
				}
			}
		}
	});
}

/**
 * 显示最近5条通知通告
 */
function top5_notice(){
	$.ajax({
		type: 'get',
		url: '/poverty/findTopNotice',
		dataType: 'json',
		data:{
			belong:'用电行为'
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