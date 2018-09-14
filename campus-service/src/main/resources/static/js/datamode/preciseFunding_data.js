/**
 *	数据模式下的精准资助js
 */

var pageSize = 18;	//分页每页显示记录条数
var laypage;	//定义分页的全局变量

/*点击导航切换*/
$("ul.nav_ul li").click(function(){
	var $this = $(this), curIndex = $this.index();
	//加载数据前清空所有的查询条件
	$(".clear_btn").click();
	switch(curIndex){
	case 0: 
		pagi_poorNumber();	//贫困生人数分布
		$(".rank .title span").text("学院贫困生人数Top10");
		top10_poorNumber(); //贫困生人数分布top10
		break;
	case 1: 
		pagi_poorArea();	//贫困生生源地分布
		$(".rank .title span").text("地区贫困生人数Top10");
		top10_poorArea(); //贫困生生源地分布top10
		break;
	case 2: 
		pagi_average();	//学院平均消费
		$(".rank .title span").text("学院平均消费Top10");
		top10_average(); //学院平均消费top10
		break;
	case 3: 
		pagi_forecast();	//下阶段贫困生预测
		$(".rank .title span").text("贫困生预测Top10");
		top10_forecast(); //贫困生预测top10
		break;
	case 4: 
		pagi_abnormal();	//异常消费
		$(".rank .title span").text("学院异常消费人数Top10");
		top10_abnormal(); //异常消费top10
		break;
	case 5: 
		
		pagi_comparision();	//消费对比
		$(".rank .title span").text("贫困生消费涨跌幅Top10");
		top10_comparision("正"); //消费对比top10
		break;
	default: 
		
		break;
	}	
});

/*页面渲染完成执行*/
$(document).ready(function(){
	/*
	 * 调整查询条件样式，当查询条件太多导致换行时调整其上高度
	 */
	var w = $(".center_table").width();
	if(w < 1144){
		$("#table1 .search_area>div").css("top", "20px");
	}
	if(w < 1085){
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
 * 分页显示贫困生人数分布
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_poorNumber(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		major = conditions.eq(1).children("input").val(),	//专业
		poorType = conditions.eq(2).children("input").val(),	//贫困等级
		startYear = conditions.eq(3).children("input.start").val(),	//评定开始年份
		endYear = conditions.eq(3).children("input.end").val();	//评定结束年份
	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/poverty/poorStudentNum',
		data: {
			collegeName: (collegeName == "全部") ? "" : collegeName,
			major: (major == "全部") ? "" : major,
			poorRank: (poorType == "全部") ? "" : poorType,
			startYear: startYear,
			endYear: endYear,
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
						 * 根据贫困不同类型，字体显示不同颜色
						 */
						var poorhtml = "";
						switch(item.poorRank){
						case '特困':
							poorhtml = "<td width='10%' class='poor-level1'>" + item.poorRank + "</td>";
							break;
						case '困难':
							poorhtml = "<td width='10%' class='poor-level2'>" + item.poorRank + "</td>";
							break;
						case '一般':
							poorhtml = "<td width='10%' class='poor-level3'>" + item.poorRank + "</td>";
							break;
						default: 
							break;
						}
										
						html += "<tr data-id='"+item.uuid+"'>" +
								"<td width='15%' title='" + item.studentID + "'>" + item.studentID + "</td>" +
								"<td width='10%' class='clickable' " +
									"onclick='show_poorDetail(this)'>" + item.stuName + "</td>" +
								"<td width='7%'>" + item.stuSex + "</td>" +
								"<td width='15%' title='" + item.college + "'>" + item.college + "</td>" +
								"<td width='23%' title='" + item.major + "'>" + item.major + "</td>" +
								poorhtml +
								"<td width='10%'>" + item.poorYear + "</td>" +
								"<td width='10%'>" + item.schoolYear + "</td>" +
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
							pagi_poorNumber(obj.curr);	//获取点击页的数据
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
			}
		}
	});
}

/**
 * 分页显示贫困生生源地分布
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_poorArea(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table2 .search_condition");
	var area = conditions.eq(1).children("input").val(),	//户口所在地
		collegeName = conditions.eq(0).children("input").val(),	//学院名称
		poorType = conditions.eq(2).children("input").val(),	//贫困等级
		startYear = conditions.eq(3).children("input.start").val(),	//评定年份开始日期
		endYear = conditions.eq(3).children("input.end").val();	//评定年份结束日期

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/poverty/poorAreaPage',
		data: {
			birthPlace: (area == "全部") ? "" : area,
			college: (collegeName == "全部") ? "" : collegeName,
			startYear: startYear,
			endYear: endYear,
			poorRank: (poorType == "全部") ? "" : poorType,
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
						 * 根据贫困不同类型，字体显示不同颜色
						 */
						var poorhtml = "";
						switch(item.poorRank){
						case '特困':
							poorhtml = "<td width='10%' class='poor-level1'>" + item.poorRank + "</td>";
							break;
						case '困难':
							poorhtml = "<td width='10%' class='poor-level2'>" + item.poorRank + "</td>";
							break;
						case '一般':
							poorhtml = "<td width='10%' class='poor-level3'>" + item.poorRank + "</td>";
							break;
						default: 
							break;
						}
										
						html += "<tr data-id='"+item.uuid+"'>" +
								"<td width='15%' title='" + item.studentID + "'>" + item.studentID + "</td>" +
								"<td width='10%'>" + item.stuName + "</td>" +
								"<td width='5%'>" + item.stuSex + "</td>" +
								"<td width='20%' title='" + item.birthPlace + "'>" + item.birthPlace + "</td>" +
								"<td width='10%'>" + item.familyAcc + "</td>" +
								"<td width='10%' title='" + item.college + "'>" + item.college + "</td>" +
								poorhtml +
								"<td width='10%'>" + item.poorYear + "</td>" +
								"<td width='10%'>" + item.schoolYear + "</td>" +
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
							pagi_poorArea(obj.curr);	//获取点击页的数据
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
			}
		}
	});	
}

/**
 * 分页显示学院平均消费
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_average(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table3 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		startYear = conditions.eq(1).children("input.start").val(),	//选择的起始年份
		endYear = conditions.eq(1).children("input.end").val();	//选择的结束年份

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/poverty/collegeAveragePage',
		data: {
			collegeName: (collegeName == "全部") ? "" : collegeName,
			startYear: startYear,
			endYear: endYear,
			pageNum: curr ? curr : 1,
			pageSize: pageSize
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			$(".loading").hide();	//隐藏加载层
			if(page){
				var html = "";
				if(page.object){
					$.each(page.object, function(i, item){
						var n = ((curr ? curr : 1) - 1) * page.size + i + 1;
						//涨跌幅显示
						var img = (item.updown > 0) ? 
								"<img width='14' src='../../image/datamode/increase.png'>" :
								((item.updown < 0) ? "<img width='14' src='../../image/datamode/decrease.png'>" :
									"<img width='14' src='../../image/datamode/unchange.png'>");
						html += "<tr data-collegeName='"+item.collegeName+"' data-year='"+item.year+"'>" +
							"<td width='5%'>" + n + "</td>" +
							"<td width='16%' title='" + item.collegeName + "'>" + item.collegeName + "</td>" +
							"<td width='10%'>" + item.year + "</td>" +
							"<td width='10%'>" + item.collegePopu + "</td>" +
							"<td width='11%'>" + item.hydroValue + "</td>" +
							"<td width='11%'>" + item.repastValue + "</td>" +
							"<td width='11%'>" + item.enteValue + "</td>" +
							"<td width='11%'>" + item.otherValue + "</td>" +
							"<td width='15%' class='clickable' " +
								"onclick='show_collegeAveDetail(this)'>" + item.payValue + img + "</td>" +
						"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='9'>无数据</td></tr>";
				}
				
				$("#table3 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
				
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
							pagi_average(obj.curr);	//获取点击页的数据
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
			}
		}
	});	
}

/**
 * 分页显示下阶段贫困生预测情况
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_forecast(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table4 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		major = conditions.eq(1).children("input").val(),	//专业
		poorType = conditions.eq(2).children("input").val();	//贫困类型

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/poverty/poorForecastPage',
		data: {
			collegeName: (collegeName == "全部") ? "" : collegeName,
			major: (major == "全部") ? "" : major,
			poorRank: (poorType == "全部") ? "" : poorType,
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
						 * 根据贫困不同类型，字体显示不同颜色
						 */
						var poorhtml = "", heathyhtml = "";
						switch(item.poorRank){
						case '特困':
							poorhtml = "<td width='6%' class='poor-level1'>" + item.poorRank + "</td>";
							break;
						case '困难':
							poorhtml = "<td width='6%' class='poor-level2'>" + item.poorRank + "</td>";
							break;
						case '一般':
							poorhtml = "<td width='6%' class='poor-level3'>" + item.poorRank + "</td>";
							break;
						default: 
							break;
						}
						//健康状况
						if(item.heathy == "较差"){
							heathyhtml = "<td width='6%' class='poor-level1'>" + item.heathy + "</td>";
						}else{
							heathyhtml = "<td width='6%'>" + item.heathy + "</td>";
						}
						var isDisabled = (item.isDisabled == "是") ? item.isDisabled : "/",
							isSingleParent = (item.isSingleParent == "是" || item.isSingleParent == "有") ? 
									item.isSingleParent : "/",
							childForMartyrs = (item.childForMartyrs == "是") ? item.childForMartyrs : "/",
							isHasPatient = (item.isHasPatient == "是") ? item.isHasPatient : "/",
							isMakeupExam = (item.isMakeupExam == "是") ? item.isMakeupExam : "/";
										
						html += "<tr data-uuid='"+item.uuid+"'>" +
								"<td width='3%' title='多选框' class='check' onclick='check(this)'><img src='../../image/check.png' width='14'></td>" +
								"<td width='9%' title='" + item.studentID + "'>" + item.studentID + "</td>" +
								"<td width='6%' class='clickable' onclick='show_stuFundingDetail("+'"'+item.stuName+'"'+","+item.studentID+")'>" + item.stuName + "</td>" +
								"<td width='10%' title='" + item.collegeName + "'>" + item.collegeName + "</td>" +
								"<td width='10%' title='" + item.major + "'>" + item.major + "</td>" +
								poorhtml +
								"<td width='6%'>" + item.preSchoolResidence + "</td>" +
								heathyhtml +
								"<td width='5%'>" + isDisabled + "</td>" +
								"<td width='5%'>" + isSingleParent + "</td>" +
								"<td width='5%'>" + childForMartyrs + "</td>" +
								"<td width='4%'>" + item.familyPopNum + "</td>" +
								"<td width='7%' title='" + item.familyAnnualIncome + "元'>" + item.familyAnnualIncome + "</td>" +
								"<td width='6%'>" + isHasPatient + "</td>" +
								"<td width='6%' title='" + item.studentLoan + "元'>" + item.studentLoan + "</td>" +
								"<td width='6%'>" + isMakeupExam + "</td>" +
								"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='16'>无数据</td></tr>";
				}
				
				$("#table4 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
				
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
							pagi_forecast(obj.curr);	//获取点击页的数据
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
			}
		}
	});	
}

/**
 * 分页显示异常消费数据
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_abnormal(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table5 .search_condition");
	var collegeName = conditions.eq(1).children("input").val(),	//学院名称
		//type = conditions.eq(1).children("input").val(),	//消费类型
		startDate = conditions.eq(2).children("input.start").val(),	//起始日期
		endDate = conditions.eq(2).children("input.end").val(),	//结束日期
		stuname = conditions.eq(0).children("input").val();//学生姓名
	
	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/poverty/abnormalConsumptionPage',
		data: {
			college: (collegeName == "全部") ? "" : collegeName,
			startYear: startDate,
			endYear: endDate,
			type: "",
			stuname: stuname,
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
						html += "<tr><td width='12%' title='" + item.studentID + "'>" + item.studentID + "</td>" +
								"<td width='8%'>" + item.stuname + "</td>" +
								"<td width='8%' title='" + item.college + "'>" + item.college + "</td>" +
								"<td width='10%' title='" + item.stuclass + "'>" + item.stuclass + "</td>" +
								"<td width='8%'>" + item.catering + "</td>" +
								"<td width='8%'>" + item.entertainment + "</td>" +
								"<td width='8%'>" + item.hydropower + "</td>" +
								"<td width='8%'>" + item.otherconsumption + "</td>" +
								"<td width='10%'>" + item.value + "</td>" +
								"<td width='10%'>" + formatDateTime(item.yyyymmdd) + "</td>" +
								"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='9'>无数据</td></tr>";
				}
				
				$("#table5 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
				
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
							pagi_abnormal(obj.curr);	//获取点击页的数据
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
			}
		}
	});	
}

/**
 * 点击贫困生消费对比正增长
 */
function increase_comparision(obj){
	pagi_comparision(1,"正");
//	$(obj).find("span").addClass("checked");
//	$(obj).next().find("span").removeClass("checked");
}
/**
 * 点击贫困生消费对比负增长
 */
function decrease_comparision(obj){
	pagi_comparision(1,"负");
//	$(obj).find("span").addClass("checked");
//	$(obj).prev().find("span").removeClass("checked");
}

/**
 * 分页显示贫困生消费对比
 * @param curr	当前页，为空默认显示第1页
 * @param increase 只显示正增长/负增长（默认"",显示全部），"正"、“负”
 */
function pagi_comparision(curr,increase){
	//清空正负增长点击状态
//	$(".operate.posi_btn span").removeClass("checked");
//	$(".operate.nega_btn span").removeClass("checked");
	$(".operate.posi_btn").removeClass("checked");
	$(".operate.nega_btn").removeClass("checked");
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table6 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		major = conditions.eq(1).children("input").val(),	//专业
		poorType = conditions.eq(2).children("input").val();	//贫困等级

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'get',
		url: '/poverty/poorComparisonPage',
		data: {
			college: (collegeName == "全部") ? "" : collegeName,
			major: (major == "全部") ? "" : major,
			poorType: (poorType == "全部")? "" : poorType,
			increase: increase? increase :"",
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
						 * 根据贫困不同类型，字体显示不同颜色
						 */
						var poorhtml = "";
						switch(item.poorRank){
						case '特困':
							poorhtml = "<td width='15%' class='poor-level1'>" + item.poorRank + "</td>";
							break;
						case '困难':
							poorhtml = "<td width='15%' class='poor-level2'>" + item.poorRank + "</td>";
							break;
						case '一般':
							poorhtml = "<td width='15%' class='poor-level3'>" + item.poorRank + "</td>";
							break;
						default: 
							break;
						}
						//转换涨幅成百分数型
						var increase_str =Math.round(parseFloat(item.increase*100)*100)/100+"%";
						var increase_html = "";
						if(increase_str.indexOf("-") == 0){
							increase_html = "<td width='15%'>" + increase_str + "<img src='../../image/datamode/decrease.png' /></td>"
						}
						else{
							increase_html = "<td width='15%'>" + increase_str + "<img src='../../image/datamode/increase.png' /></td>"
						}
						html += "<tr><td width='15%' title='" + item.studentID + "'>" + item.studentID + "</td>" +
								"<td width='10%' class='clickable' " +
									"onclick='show_stuConsumpDetail("+item.studentID+")'>" + item.stuName + "</td>" +
								"<td width='10%'>" + item.stuSex + "</td>" +
								"<td width='15%' title='" + item.college + "'>" + item.college + "</td>" +
								"<td width='20%' title='" + item.major + "'>" + item.major + "</td>" +
								poorhtml +
								increase_html +
								"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='7'>无数据</td></tr>";
				}
				
				$("#table6 tbody").html(html);
				
				//分页信息
				var totalCount = page.total,
					totalPage = page.pages,
					curPage = page.current,
					pageSize = page.size;
				
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
							pagi_comparision(obj.curr);	//获取点击页的数据
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
 * 导出贫困生人数分布
 */
function export_poorNumber(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		major = conditions.eq(1).children("input").val(),	//专业
		poorType = conditions.eq(2).children("input").val(),	//贫困等级
		startYear = conditions.eq(3).children("input.start").val(),	//评定开始年份
		endYear = conditions.eq(3).children("input.end").val();	//评定结束年份
	var data_c = {
			'collegeName': (collegeName == "全部") ? "" : collegeName,
			'major': (major == "全部") ? "" : major,
			'poorRank': (poorType == "全部") ? "" : poorType,
			'startYear': startYear,
			'endYear': endYear
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'get',
		url: '/poverty/poorStudentNum',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","性别","学院","专业","贫困等级","评定年份",
				             "入学年份","生源地","户口","家庭人口","家庭年收入/元","烈士子女",
				             "单亲","孤残","健康状况","有危重病人","助学贷款/元","补考记录"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);datarow.push(o.stuName);datarow.push(o.stuSex);
					datarow.push(o.college);datarow.push(o.major);datarow.push(o.poorRank);
					datarow.push(o.poorYear);datarow.push(o.schoolYear);datarow.push(o.birthPlace);
					datarow.push(o.familyAcc);datarow.push(o.familyPopNum);datarow.push(o.familyAnnualIncome);
					datarow.push(o.childForMartyrs);datarow.push(o.isSingleParent);datarow.push(o.isDisabled);
					datarow.push(o.heathy);datarow.push(o.isHasPatient);datarow.push(o.studentLoan);
					datarow.push(o.isMakeupExam);
					
					datarows.push(datarow);
				});
				var fileName = '贫困生人数分布名单';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出贫困生生源地分布
 */
function export_poorArea(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table2 .search_condition");
	var area = conditions.eq(0).children("input").val(),	//户口所在地
		collegeName = conditions.eq(0).children("input").val(),	//学院名称
		poorType = conditions.eq(2).children("input").val(),	//贫困等级
		startYear = conditions.eq(3).children("input.start").val(),	//评定年份开始日期
		endYear = conditions.eq(3).children("input.end").val();	//评定年份结束日期
	
	var data_c = {
			'birthPlace': (area == "全部") ? "" : area,
			'college': (collegeName == "全部") ? "" : collegeName,
			'startYear': startYear,
			'endYear': endYear,
			'poorRank': (poorType == "全部") ? "" : poorType
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'get',
		url: '/poverty/poorAreaPage',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","性别","学院","专业","贫困等级","评定年份",
				             "入学年份","生源地","户口","家庭人口","家庭年收入/元","烈士子女",
				             "单亲","孤残","健康状况","有危重病人","助学贷款/元","补考记录"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);datarow.push(o.stuName);datarow.push(o.stuSex);
					datarow.push(o.college);datarow.push(o.major);datarow.push(o.poorRank);
					datarow.push(o.poorYear);datarow.push(o.schoolYear);datarow.push(o.birthPlace);
					datarow.push(o.familyAcc);datarow.push(o.familyPopNum);datarow.push(o.familyAnnualIncome);
					datarow.push(o.childForMartyrs);datarow.push(o.isSingleParent);datarow.push(o.isDisabled);
					datarow.push(o.heathy);datarow.push(o.isHasPatient);datarow.push(o.studentLoan);
					datarow.push(o.isMakeupExam);
					
					datarows.push(datarow);
				});
				var fileName = '贫困生生源地分布';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出学院平均消费
 */
function export_average(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table3 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		startYear = conditions.eq(1).children("input.start").val(),	//选择的起始年份
		endYear = conditions.eq(1).children("input.end").val();	//选择的结束年份
	
	var data_c = {
			'collegeName' : (collegeName == "全部") ? "" : collegeName,
			'startYear': startYear,
			'endYear': endYear
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'get',
		url: '/poverty/collegeAveragePage',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.object != null){
				var title = ["学院名称","年","月","学院人数","人均总消费/元","娱乐/元","餐饮/元","水电/元","其他/元","人均消费环比"];  //表头
				var datarows = [];      //数据
				$.each(data.object, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.collegeName);
					datarow.push(o.year);
					datarow.push(o.month);
					datarow.push(o.collegePopu);
					datarow.push(o.payValue);
					datarow.push(o.enteValue);
					datarow.push(o.repastValue);
					datarow.push(o.hydroValue);
					datarow.push(o.otherValue);
					var updown = o.updown;
					var updownDes = "持平";
					if(updown > 0){
						updownDes = "上涨";
					}else if(updown < 0){
						updownDes = "下降";
					} 
					datarow.push(updownDes);//月人均总消费环比上个月的变化
					
					datarows.push(datarow);
				});
				var fileName = '学院平均消费';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出贫困生预测数据
 */
function export_poorForecast(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table4 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		major = conditions.eq(1).children("input").val(),	//专业
		poorType = conditions.eq(2).children("input").val();	//贫困类型
	var data_c = {
			'collegeName' : (collegeName == "全部") ? "" : collegeName,
			'major' : (major == "全部") ? "" : major,
			'poorRank' : (poorType == "全部") ? "" : poorType
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'get',
		url: '/poverty/poorForecastPage',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","学院","专业","贫困等级","入学年份","户口","健康状况","是否孤残","单亲家庭",
				             "烈士子女","家庭人口","家庭年收入","有危重病人","助学贷款","是否补考"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);
					datarow.push(o.stuName);
					datarow.push(o.collegeName);
					datarow.push(o.major);
					datarow.push(o.poorRank);
					datarow.push(o.schoolYear);
					datarow.push(o.preSchoolResidence);
					datarow.push(o.heathy);
					datarow.push(o.isDisabled);
					datarow.push(o.isSingleParent);
					datarow.push(o.childForMartyrs);
					datarow.push(o.familyPopNum);
					datarow.push(o.familyAnnualIncome);
					datarow.push(o.isHasPatient);
					datarow.push(o.studentLoan);
					datarow.push(o.isMakeupExam);
					
					datarows.push(datarow);
				});
				var fileName = '贫困生预测名单';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出异常消费情况
 */
function export_abnormal(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table5 .search_condition");
	var collegeName = conditions.eq(1).children("input").val(),	//学院名称
		//type = conditions.eq(1).children("input").val(),	//消费类型
		startDate = conditions.eq(2).children("input.start").val(),	//起始日期
		endDate = conditions.eq(2).children("input.end").val(),	//结束日期
		stuname = conditions.eq(0).children("input").val();//学生姓名
	
	var data_c = {
			'college' : (collegeName == "全部") ? "" : collegeName,
			'startYear': startDate,
			'endYear': endDate,
			'type': "",
			'stuname': stuname,
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	$.ajax({
		type: 'get',
		url: '/poverty/abnormalConsumptionPage',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","学院","专业","消费时间","娱乐/元","餐饮/元","水电/元","其他/元","总计/元"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);
					datarow.push(o.stuname);
					datarow.push(o.college);
					datarow.push(o.stuclass);
					datarow.push(formatDateTime(o.yyyymmdd));
					datarow.push(o.entertainment);
					datarow.push(o.catering);
					datarow.push(o.hydropower);
					datarow.push(o.otherconsumption);
					datarow.push(o.value);
					
					datarows.push(datarow);
				});
				var fileName = '贫困生异常消费情况';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出贫困生消费对比
 */
function export_comparision(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table6 .search_condition");
	var collegeName = conditions.eq(0).children("input").val(),	//学院名称
		major = conditions.eq(1).children("input").val(),	//专业
		date = conditions.eq(2).children("input").val();	//日期
	var data_c = {
			'college' : (collegeName == "全部") ? "" : collegeName,
			'major' : (major == "全部") ? "" : major,
			'year' : date ? date.split("-")[0] : "",//年
			'month' : date ? date.split("-")[1] : "",//月
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	$.ajax({
		type: 'get',
		url: '/poverty/poorComparisonPage',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","性别","学院","专业","月消费总额/元","时间"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据
					//按表头字段顺序填入数据
					datarow.push(o.studentID);
					datarow.push(o.stuName);
					datarow.push(o.stuSex);
					datarow.push(o.college);
					datarow.push(o.major);
					datarow.push(o.moneyAmount);
					datarow.push(o.month);
					
					datarows.push(datarow);
				});
				var fileName = '贫困生消费对比';//生成的文件名
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

//预测贫困生对比
function compare_poorForecast(){
	var $checked = $("td[class='checked']"),
		len = $checked.length;
	if(len == 0){
		parent.layer.msg("请选择预测的贫困生，只能选择2-3个人！", {icon: 0, shade: 0.5, time: 1500});
	}else if(len == 1){
		parent.layer.msg("请选择2-3个人！", {icon: 0, shade: 0.5, time: 1500});
	}else if(len > 3){
		parent.layer.msg("最多只能选择3个人哦！", {icon: 0, shade: 0.5, time: 1500});
	}else{
		var checkedPersons = [];	//预测学生对比uuid
		$.each($checked, function(i, td){
			checkedPersons.push($(td).parent().attr("data-uuid"));
		});
		/*
		 * 显示对比弹窗
		 */
		parent.layer.open({
			title:  "数据对比",
			area: ['809px', '464px'],
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
					url: '/poverty/getForeastById',
					data: {
						uuids: checkedPersons
					},
					dataType: 'json',
					success: function(result){
						var data = result.object;
						if(data){
							var html = "", len = data.length;
							//表格各行的html
							var poorRank = "", preSchoolResidence = "", familyAnnualIncome = "",
								familyPopNum = "", heathy = "", isDisabled = "", isSingleParent = "",
								childForMartyrs = "", isHasPatient = "", isMakeupExam = "",
								studentLoan = "";
							
							for(var i=0; i<len; i++){
								//贫困等级颜色显示
								switch(data[i].poorRank){
								case '特困':
									poorRank += "<td style='color:#ed2e2e'>" + data[i].poorRank + "</td>";
									break;
								case '困难':
									poorRank += "<td style='color:#f09c17'>" + data[i].poorRank + "</td>";
									break;
								case '一般':
									poorRank += "<td style='color:#7ec433'>" + data[i].poorRank + "</td>";
									break;
								default: 
									break;
								}
								//健康状况
								if(data[i].heathy == "较差"){
									heathy += "<td style='color:#ed2e2e'>" + data[i].heathy + "</td>";
								}else{
									heathy += "<td >" + data[i].heathy + "</td>";
								}
								//poorRank += "<td>" + data[i].poorRank + "</td>";	//贫困等级
								preSchoolResidence += "<td>" + data[i].preSchoolResidence + "</td>";	//户口类型
								familyAnnualIncome += "<td>" + data[i].familyAnnualIncome + "</td>";	//家庭年收入
								familyPopNum += "<td>" + data[i].familyPopNum + "</td>";	//家庭人口数
								//heathy += "<td>" + data[i].heathy + "</td>";	//健康状况
								isDisabled += "<td>" + data[i].isDisabled + "</td>";	//是否孤残
								isSingleParent += "<td>" + data[i].isSingleParent + "</td>";	//单亲
								childForMartyrs += "<td>" + data[i].childForMartyrs + "</td>";	//烈士子女
								isHasPatient += "<td>" + data[i].isHasPatient + "</td>";	//危重病人
								isMakeupExam += "<td>" + data[i].isMakeupExam + "</td>";	//补考
								studentLoan += "<td>" + data[i].studentLoan + "</td>";	//助学贷款
							}
							if(len == 2){
								html += "<thead><tr>" +
										"<th width='30%'></th>" +
										"<th width='35%'>" + data[0].stuName + "(" + 
											data[0].major + ")</th>" +
										"<th width='35%'>" + data[1].stuName + "(" + 
											data[1].major + ")</th>" +
										"</tr></thead>";
							}else{
								html += "<thead><tr>" +
										"<th width='25%'></th>" +
										"<th width='25%'>" + data[0].stuName + "(" + 
											data[0].major + ")</th>" +
										"<th width='25%'>" + data[1].stuName + "(" + 
											data[1].major + ")</th>" +
										"<th width='25%'>" + data[2].stuName + "(" + 
											data[2].major + ")</th>" +
										"</tr></thead>";
							}
							html += "<tbody>";
							html += "<tr><td class='head'>贫困等级</td>" + poorRank + "</tr>";
							html += "<tr><td class='head'>户口类型</td>" + preSchoolResidence + "</tr>";
							html += "<tr><td class='head'>家庭年收入(元)</td>" + familyAnnualIncome + "</tr>";
							html += "<tr><td class='head'>家庭人口数</td>" + familyPopNum + "</tr>";
							html += "<tr><td class='head'>健康状况</td>" + heathy + "</tr>";
							html += "<tr><td class='head'>是否孤残</td>" + isDisabled + "</tr>";
							html += "<tr><td class='head'>是否单亲</td>" + isSingleParent + "</tr>";
							html += "<tr><td class='head'>烈士子女</td>" + childForMartyrs + "</tr>";
							html += "<tr><td class='head'>是否有危重病人</td>" + isHasPatient + "</tr>";
							html += "<tr><td class='head'>是否补考</td>" + isMakeupExam + "</tr>";
							html += "<tr><td class='head'>贷款金额</td>" + studentLoan + "</tr>";
							html += "</tbody>";
							
							$layero.find(".layui-layer-content").html("<table>"+html+"</table>");
						}else{
							//parent.layer.close(index);
						}
					}
				});
			}
		});
	}
}

//贫困生人数分布表格点击行显示详细信息
function show_poorDetail(obj){
	var $td = $(obj), uuid = $td.parent().attr("data-id"), 
		stuName = $td.text();	//学生姓名
	parent.layer.open({
		title:  "个人详情：" + stuName,
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
				url: '/poverty/getPoorById',
				data: {
					uuid: uuid
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data){
						/*
						 * 组成关键字数组
						 */
						var keywords = [data.birthPlace, data.college, data.familyAcc, 
						                "家庭年收入"+data.familyAnnualIncome, data.familyPopNum+"口人",
						                "健康状况"+data.heathy, data.major, data.poorRank,
						                data.schoolYear+"年入学", data.poorYear+"年评定",
						                data.stuSex];
						if(data.childForMartyrs == "是"){
							keywords.push("烈士子女");
						}
						if(data.isDisabled == "是"){
							keywords.push("孤残");
						}
						if(data.isHasPatient == "是"){
							keywords.push("家庭有危重病人");
						}
						if(data.isMakeupExam == "是"){
							keywords.push("有补考");
						}
						if(data.isSingleParent == "是"){
							keywords.push("单亲");
						}
						if(data.studentLoan > 0){
							keywords.push("助学贷款"+data.studentLoan+"元");
						}
						var d = [];	//词云数组，必须包含value属性
						$.each(keywords, function(i, n){
							d.push({name: n, value: 10});
						});
						
						//初始化echarts实例
						var canvas = $layero.find(".layui-layer-content");
						var mychart = echarts.init(canvas[0]);
						//各项的颜色
						var color = ['#95e5e4', '#d15f69', '#e59bac', '#e6470f', '#ef7512', '#f09511', '#f4c74a',
							        '#bcb75b', '#67d1d1', '#3aa289', '#62c899', '#1abb92', '#445fa6', '#4178d3',
							        '#2195d2', '#46afe6'];
						mychart.setOption({
					        series: [{
					            type: 'wordCloud',
					            shape: 'circle',
					            sizeRange: [16, 16],//字体大小范围
					            rotationRange: [-45, 45],//字体旋转角度区间
					            gridSize: 16, //字符间距
					            bottom: 10,
					            left: 'center',
					            top: 'center',
					            textStyle: {
					                normal: {
					                    color: function() {
					                    	return color[Math.round(Math.random() * 16)];
					                    }
					                },
					                emphasis: {
					                    shadowBlur: 20,
					                    shadowColor: '#333'
					                }
					            },
					            data: d
					        }]
					    });
					}
				}
			});
		}
	})
}

function show_collegeAveDetail(obj){
	var $tr = $(obj).parent(), 
		collegeName = $tr.attr("data-collegeName"), 	//学院名称
		year = $tr.attr("data-year");	//年份
	
	parent.layer.open({
		title:  year + " 年 " + collegeName + " 各月消费详情",
		area: ['742px', '387px'],
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
				url: '/poverty/getCollegeMonths',
				data: {
					collegeName : collegeName,
					year : year
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data){
						//初始化echarts实例
						var canvas = $layero.find(".layui-layer-content");
						var mychart = echarts.init(canvas[0]);
						var hydro_data = [],	//水电消费数据
							repast_data = [],	//餐饮消费数据
							enter_data = [],	//娱乐消费数据
							other_data = [];	//其他消费数据
						$.each(data, function(i, item){
							hydro_data.push([parseInt(item.month) + '月', item.hydroValue]);
							repast_data.push([parseInt(item.month) + '月', item.repastValue]);
							enter_data.push([parseInt(item.month) + '月', item.enteValue]);
							other_data.push([parseInt(item.month) + '月', item.other_value]);
						});
						mychart.setOption({
							color: ['#d6cc39', '#5cba64', '#d54733', '#60bad4'],
							tooltip: {  //提示框
					            trigger: 'axis',    //坐标轴触发
					            borderWidth: 1,
					            borderColor: 'gold',
					            position: function(point, params, dom, rect, size){
					            	var $dom = $(dom).eq(0), _domHtml = $dom.html();
					            	_domHtml += 
					            		"<hr style='width:8%;height:1px;left:-3px;top:-3px;'>" +
					                	"<hr style='width:1px;height:8%;left:-3px;top:-3px;'>" +
					                	"<hr style='width:8%;height:1px;right:-3px;top:-3px;'>" +
					                	"<hr style='width:1px;height:8%;right:-3px;top:-3px;'>" +
					                	"<hr style='width:8%;height:1px;left:-3px;bottom:-3px;'>" +
					                	"<hr style='width:1px;height:8%;left:-3px;bottom:-3px;'>" +
					                	"<hr style='width:8%;height:1px;right:-3px;bottom:-3px;'>" +
					                	"<hr style='width:1px;height:8%;right:-3px;bottom:-3px;'>";
					            	$dom.html(_domHtml).addClass("tip").css("borderRadius","0");
					            	return ['82%', '0'];
					            }
					        },
					        legend: {   //图例组件
					        	top: '0',
					            data: ['水电', '餐饮', '娱乐', '其他'],
					            itemWidth: 8,
					            textStyle: {
					                color: '#b0bfc2',
					                fontSize: 14
					            }
					        },
							grid : {
					        	left: '10%',
					        	right: '10%',
					        	top: '15%',
					        	bottom: '10%'
					        },
					        xAxis : [   //x轴
					            {
					                type : 'category',  //轴类型，类目型
					                name: '月份',       //显示的轴名称
					                nameTextStyle: {
					                	color: '#6f9ed6'
					                },
					                axisLine: {         //轴线
					                    lineStyle: {    //轴线样式
					                        color: '#2d556f'
					                    }
					                },
					                axisLabel: {        //轴的刻度标签设置
					                    color: '#6f9ed6'   //文本颜色
					                },
					                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
					            }
					        ],
					        yAxis : [   //y轴
					            {
					                type : 'value',     //轴类型，数值轴，连续数据
					                name: '消费（元）',    //轴名称
					                nameTextStyle: {
					                	color: '#6f9ed6'
					                },
					                axisLine: {
					                    lineStyle: {
					                        color: '#2d556f'
					                    }
					                },
					                axisLabel: {
					                    color: '#6f9ed6'
					                },
					                splitLine: {    //在表格区域的分割线
					                    show: false
					                }
					            }
					        ],
					        series: [
					            {
					                name: '水电',
					                type: 'bar',    //柱状图
					                stack: '1',
					                data: hydro_data
					            },
					            {
					                name: '餐饮',
					                type: 'bar',    //柱状图
					                stack: '1',
					                data: repast_data
					            },
					            {
					                name: '娱乐',
					                type: 'bar',    //柱状图
					                stack: '1',
					                data: enter_data
					            },
					            {
					                name: '其他',
					                type: 'bar',    //柱状图
					                stack: '1',
					                data: other_data
					            }
					        ]
						});
					}
				}
			});
		}
	})
}

/**
 * 点击学生姓名，显示预测贫困生的历史资助详情
 * @param stuName 姓名
 * @param studentID 学号
 */
function show_stuFundingDetail(stuName,studentID){
	$.ajax({
		type: 'get',
		url: '/poverty/getFundingRecords',
		data: {
			stuName:stuName,
			studentID:studentID
		},
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data && data != ""){
				parent.layer.open({
					title:  "个人资助项目详情：" + stuName,
					type: 1,
					area: ['651px', '400px'],
					skin: 'my-default',
					shade: 0.7,
					shadeClose: true,
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
						$content.html('<div class="demo">'
								+'<div class="container">'
								+'<div class="row">'
								+'<div class="col-md-12">'
								+'<div class="main-timeline"></div>'
								+'</div></div></div></div>');

						$.each(data, function(i, item){
							$content.find(".main-timeline").append('<div class="timeline">'
									+'<a href="#" class="timeline-content">'
									+'<span class="year">'+item.poorYear+'</span>'
									+'<div class="inner-content">'
									+'<h3 class="timeline-title">'+item.surName+'</h3>'
									+'<p class="description">'
									+item.surRank+', '+item.surType+', '+item.surAmount
									+'</p>'
									+'</div>'
									+'</a>'
									+'</div>');
						});
					}
				});
			}
			else{
				parent.layer.msg(
					  stuName+"无个人资助项目"
					);
			}
		}
	});
}

/*贫困生消费对比，点击学生查看具体月消费*/
function show_stuConsumpDetail(studentID){
	parent.layer.open({
		/*type: 2,*/
		title: '贫困生月消费',
		area: ['600px', '322px'],
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
		    	type: 'get',
		    	url: '/poverty/findStuMonthConsumpRecord',
		    	data: {
					studentID : studentID
				},
		    	dataType: 'json',
		    	success: function(result){
		    		var data = result.object;
		    		if(data){
		    			//初始化echarts实例
		    		    var mychart = echarts.init($content[0]);
		    		    var titles = [], series = [];
		        		$.each(data, function(i, item){
		        			titles[i] = item.name.split(",")[0];
		        			series.push({
		        				name: item.name.split(",")[0],
		        				type: 'line',
		                        data: item.value
		        			});
		        		});
	    			    var option = {
	    			        color: ['#ffff00', '#e47833', '#00ff00','#ff00ff','#00ffff'],
	    			        legend : {
		    					x: 'right',
		    					y: 'bottom',
		    					orient: 'horizontal',
		    					padding: [0,20,0,0],
		    					textStyle: {color:'#fff'},
		    					data: titles
			    			},
	    			        tooltip: {  //提示框
	    			            trigger: 'axis',    //坐标轴触发
	    			            borderWidth: 1,
		    			        borderColor: 'gold',
		    			        position: function(point, params, dom, rect, size){
		    			        	var $dom = $(dom).eq(0), _domHtml = $dom.html();
		    			        	_domHtml += 
		    			        		"<hr style='width:8%;height:1px;left:-3px;top:-3px;'>";
		    			        	$dom.html(_domHtml).addClass("tip").css("borderRadius","0");
		    			        }
	    			        },
	    			        grid: {
	    			            left: '5%',
	    			            right: '5%',
	    			            top: '5%',
	    			            bottom: 30,
	    			            containLabel: true
	    			        },
	    			        xAxis : [   //x轴
	    			            {
	    			                type : 'category',  //轴类型，类目型
	    			                boundaryGap: false,
	    			                axisLine: {         //轴线
	    			                    lineStyle: {    //轴线样式
	    			                        color: '#2d556f'
	    			                    }
	    			                },
	    			                axisLabel: {        //轴的刻度标签设置
	    			                    color: '#6f9ed6'   //文本颜色
	    			                },
	    			                splitLine: {
	    			                	show: true,
	    			                	lineStyle: {
	    			                		color: '#2d556f'
	    			                	},
	    			                	interval: function(index, value){
	    			                		return (index == 5) ? true : false;
	    			                	}
	    			                },
	    			                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
	    			            }
	    			        ],
	    			        yAxis : [   //y轴
	    			            {
	    			                type : 'value',     //轴类型，数值轴，连续数据
	    			                axisLine: {
	    			                    lineStyle: {
	    			                        color: '#2d556f'
	    			                    }
	    			                },
	    			                axisLabel: {
	    			                    color: '#6f9ed6'
	    			                },
	    			                //splitNumber: 10, //分割段数
	    			                splitLine: {    //在表格区域的分割线
	    			                    show: false
	    			                }
	    			            }
	    			        ],
	    			        series: series
	    			    };
		        		mychart.setOption(option);
		    		}
		    	}
		    });
		}
	});
}

/**
 * 显示贫困生人数分布Top10排行榜
 */
function top10_poorNumber(){
	$.ajax({
		type: 'get',
		url: '/poverty/getTopNum',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.uuid+"'>" +
					"<td width='20%'>" + (i+1) + "</td>" +
					"<td width='40%' title='" + item.colleague + "'>" + item.colleague + "</td>" +
					"<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
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
 * 显示贫困生生源地分布Top10排行榜
 */
function top10_poorArea(){
	$.ajax({
		type: 'get',
		url: '/poverty/getTopArea',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.uuid+"'>" +
					"<td width='20%'>" + (i+1) + "</td>" +
					"<td width='40%' title='" + item.province + "'>" + item.province + "</td>" +
					"<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
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
 * 显示学院平均消费Top10排行榜
 */
function top10_average(){	
	$.ajax({
		type: 'get',
		url: '/poverty/collegeAveragePage',
		data: {
			collegeName: "",
			startYear: "",
			endYear: "",
			pageNum: 1,
			pageSize: 10
		},
		dataType: 'json',
		success: function(result){
			var page = result.object;
			if(page){
				var html = "";
				if(page.object){
					$.each(page.object, function(i, item){
						html += "<tr>" +
						"<td width='20%'>" + (i+1) + "</td>" +
						"<td width='40%'>" + item.collegeName + "</td>" +
						"<td width='40%'>" + item.payValue + "元</td>" +
						"</tr>";
					});
				}else{	//数据为空
					html = "<tr><td colspan='8'>无数据</td></tr>";
				}
				$('#top10_3 tbody').html(html);
			}			
		}
	});
}

/**
 * 显示贫困生预测Top10排行榜
 */
function top10_forecast(){
	$.ajax({
		type: 'get',
		url: '/poverty/getTopForeast',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.uuid+"'>" +
					"<td width='20%'>" + (i+1) + "</td>" +
					"<td width='40%' title='" + item.colleague + "'>" + item.colleague + "</td>" +
					"<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
					"</tr>";
				})
			}
			else{	//数据为空
				html = "<tr><td colspan='8'>无数据</td></tr>";
			}
			$('#top10_4 tbody').html(html);			
        }
	});
}

/**
 * 显示异常消费Top10排行榜
 */
function top10_abnormal(){
	$.ajax({
		type: 'get',
		url: '/poverty/getTopAbdCon',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.uuid+"'>" +
					"<td width='20%'>" + (i+1) + "</td>" +
					"<td width='40%' title='" + item.college + "'>" + item.college + "</td>" +
					"<td width='40%' title='" + item.num + "人'>" + item.num + "人</td>" +
					"</tr>";
				})
			}
			else{	//数据为空
				html = "<tr><td colspan='8'>无数据</td></tr>";
			}
			$('#top10_5 tbody').html(html);			
        }
	});
}

/**
 * 显示消费对比Top10排行榜
 */
function top10_comparision(increase){
	$.ajax({
		type: 'get',
		url: '/poverty/findPoorConsumpTop10',
		dataType: 'json',
		data:{
			increase : increase
		},
		success: function(result){
			var data = result.object;
			var html="";
			if(data){
				$.each(data, function(i, item){
					html += "<tr data-id='"+item.uuid+"'>" +
					"<td width='40%' title='" + item.major + "'>" + item.major + "</td>" +
					"<td width='30%' title='" + item.stuName + "'>" + item.stuName + "</td>" +
					"<td width='30%'>" + ((item.increase*100)+"0000").split('.')[0] + "%</td>" +
					"</tr>";
				})
			}
			else{	//数据为空
				html = "<tr><td colspan='8'>无数据</td></tr>";
			}
			$('#top10_6 tbody').html(html);			
        }
	});
}

/**
 * 消费对比Top10排行榜升序降序显示
 */
function change_comparision(){
	var turn=$('#top10_6').find('.layui-icon').attr('title');
	if(turn=="正增长"){//升序显示，图标换成向上
		$('#top10_6').find('.layui-icon').attr('title',"负增长");
		$('#top10_6').find('.layui-icon').html('&#xe61a;');
		top10_comparision("负");
	}
	else{//降序显示，图标换成向下
		$('#top10_6').find('.layui-icon').attr('title',"正增长");
		$('#top10_6').find('.layui-icon').html('&#xe619;');
		top10_comparision("正");
	}
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
};


/*function uploadfile(){
    $.ajax({
        url: '/poverty/upload',//上传地址
        type: 'get',
        data: new FormData($('#uploadForm')[0]),//表单数据
        processData: false,
        contentType: false,
        success:function(data){
        },
        contentType: false, //必须false才会自动加上正确的Content-Type
        processData: false //必须false才会避开jQuery对 formdata 的默认处理
    
    });
}

function import_poorNumber(){
	var iframeWin = null;
	parent.layer.open({
		type: 2,
		title: '数据导入',
		area: ['450px', '200px'],
		skin: 'my-default',
		shade: 0.7,
		btn: ['确定'],
		content: ['page/datamode/dataImport.htm', 'no'],
		success: function(layero, index){
			iframeWin = parent.window[layero.find("iframe")[0]['name']];	//得到iframe窗口对象
			var $layero = $(layero), $title = $layero.find(".layui-layer-title"),
				_title = $title.text();
			$layero.prepend("<img src='image/datamode/layer.png'>");
			$title.html("<div class='mylay-title'>" +
					"<span>" + _title + "</span>" +
					"<hr class='mylay-title-left-hr'>" +
					"<hr class='mylay-title-right-hr'>" +
					"<div class='mylay-title-point'></div></div>");
			$layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
		},
		btn1: function(index){
			//调用上传接口
		}
	})
}*/

/*上传成功弹窗*/
/*function upload_sucess(){
	layui.use('layer', function(){
		$.get("../../page/datamode/uploadSucess.html", function(result){
			  content = result;
			  var layer = layui.layer;
			  layer.open({
				  title: false,
				  btn: false,
				  closeBtn: 0,
				  shadeClose: true,
				  content: content,
				  skin: 'upload-sucess'
				});
		});
	}); 	 
}*/