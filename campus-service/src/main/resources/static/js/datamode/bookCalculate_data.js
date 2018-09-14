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
		pagi_readerBorrow();
		$(".rank .title span").text("读者借阅量Top10");
		top10_readerBorrow();
		break;
	case 1: 
		pagi_bookBorrow();
		$(".rank .title span").text("图书借阅次数Top10");
		top10_bookBorrow();
		break;
	case 2: 
		pagi_bookRequire();
		$(".rank .title span").text("图书需求量Top10");
		top10_bookRequire();
		break;
	default: 
		break;
	}	
});

/*页面渲染完成执行*/
$(document).ready(function(){
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
	cluster="";
}

/**
 * 点击读者借阅分析(一般)
 */
function normal_readerBorrow(){
	cluster="2.0";
	pagi_readerBorrow(1,cluster);
}

/**
 * 点击读者借阅分析(活跃)
 */
function active_readerBorrow(){
	cluster="1.0";
	pagi_readerBorrow(1,cluster);
}

/**
 * 点击读者借阅分析(懒惰)
 */
function lazy_readerBorrow(){
	cluster="0.0";
	pagi_readerBorrow(1,cluster);
}

/**
 * 分页显示读者借阅分析
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_readerBorrow(curr,cluster){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var readerName = conditions.eq(0).children("input").val(),	//读者姓名
		collegeName = conditions.eq(1).children("input").val(),	//学院名称
		startYear= conditions.eq(2).children("input.start").val(),	//选择的起始年份
		endYear = conditions.eq(2).children("input.end").val();	//选择的结束年份

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findReaderByCondition',
		data: {
			readerName:readerName,
			college: (collegeName == "全部") ? "" : collegeName,
			startYear: startYear,
			endYear: endYear,
			cluster: cluster? cluster :"",
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
						var name_html = "<td width='10%'> " + item.readerName + " </td>";
						if(item.bookSum > 0){
							name_html = "<td width='10%' class='clickable' onclick='show_bookType("+
							item.readerID+","+item.schoolYear+")'> " 
								+ item.readerName + " </td>"; //显示学生借过的图书类别
						}
						var cluster_html = "";
						if(item.cluster == "1.0"){
							cluster_html = "<td width='10%'>活跃</td>"; 
						}
						else if(item.cluster == "0.0"){
							cluster_html = "<td width='10%'>懒惰</td>";
						}
						else{
							cluster_html = "<td width='10%'>一般</td>";
						}
						var times_html = "<td width='10%'> " + item.borrowTimes + " </td>";
						if(item.borrowTimes > 0){
							times_html = "<td width='10%' class='clickable' onclick='show_borrowHistroy("+
							'"'+item.readerName+'"'+","+item.readerID+","+item.schoolYear+")'> " 
								+ item.borrowTimes + " </td>"; //显示学生的借阅记录
						}
						
						html += "<tr data-id='"+item.uuid+"'>" +
							"<td width='15%'>"+item.readerID+"</td>"+
							name_html+
							"<td width='5%''>"+item.readerSex+"</td>"+
							"<td width='10%'>"+item.collegeName+"</td>"+
							"<td width='10%' title='"+item.major+"'>"+item.major+"</td>"+
							"<td width='5%'>"+item.readerIdentity+"</td>"+
							cluster_html+
							"<td width='10%'>"+item.bookSum+"</td>"+
							times_html+
							"<td width='10%'>"+item.schoolYear+"</td>"+
							"<td width='5%'>"+item.dormitory+"</td>"+
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
								pagi_readerBorrow(obj.curr,cluster);	//获取点击页的数据
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
 * 点击图书借阅分析(一般)
 */
function normal_bookBorrow(){
	cluster="1.0";
	pagi_bookBorrow(1,cluster);
}

/**
 * 点击图书借阅分析(热门)
 */
function hot_bookBorrow(){
	cluster="2.0";
	pagi_bookBorrow(1,cluster);
}

/**
 * 点击图书借阅分析(冷门)
 */
function upset_bookBorrow(){
	cluster="0.0";
	pagi_bookBorrow(1,cluster);
}

/**
 * 分页显示图书借阅分析
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_bookBorrow(curr,cluster){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table2 .search_condition");
	var bookName = conditions.eq(0).children("input").val(),	//书名
		typeName = conditions.eq(1).children("input").val(),	//分类名称
		startYear= conditions.eq(2).children("input.start").val(),	//选择的起始年份
		endYear = conditions.eq(2).children("input.end").val();	//选择的结束年份

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findBookByCondition',
		data: {
			bookName:bookName,
			typeName: (typeName == "全部") ? "" : typeName,
			startYear: startYear,
			endYear: endYear,
			cluster: cluster? cluster :"",
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
						var name_html = "<td width='10%' title='"+item.bookName+"'> " + item.bookName + " </td>";
						var times_html = "<td width='5%'> " + item.borrowTimes + " </td>";
						if(item.borrowTimes > 0){
							name_html = "<td width='10%' class='clickable' onclick='show_mostMajor("+item.uuid+","+item.year+")' title='"+item.bookName+"'> " 
								+ item.bookName + " </td>"; //显示该本书借阅最多的专业
							times_html = "<td width='5%' class='clickable' onclick='show_bookHistory("+'"'+item.bookName+'"'+","+item.uuid+","+item.year+")'> " 
								+ item.borrowTimes + " </td>"; //显示该本书的借阅记录
						}
						var cluster_html = "";
						if(item.cluster == "2.0"){
							cluster_html = "<td width='5%'>热门</td>"; 
						}
						else if(item.cluster == "0.0"){
							cluster_html = "<td width='5%'>冷门</td>";
						}
						else{
							cluster_html = "<td width='5%'>一般</td>";
						}
						
						html += "<tr data-id='"+item.uuid+"'>" +							
							name_html+
							"<td width='10%' title='"+item.typeName+"'>"+item.typeName+"</td>"+
							cluster_html+
							"<td width='10%'>"+item.year+"</td>"+
							times_html+
							"<td width='10%' title='"+item.author+"'>"+item.author+"</td>"+
							"<td width='10%' title='"+item.publish+"'>"+item.publish+"</td>"+
							"<td width='5%' title='"+item.edition+"'>"+item.edition+"</td>"+
							"<td width='5%' title='"+item.publicationDate+"'>"+item.publicationDate+"</td>"+			
							"<td width='5%'>"+item.purchaseYear+"</td>"+
							"<td width='10%'>"+item.position+"</td>"+
							"<td width='5%'>"+item.damage+"</td>"+
							"<td width='10%' class='clickable' onclick='show_borrowRelate("+item.uuid+","+'"'+item.bookName+'"'+")'>"+"借阅关系"+"</td>"+
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
								pagi_bookBorrow(obj.curr,cluster);	//获取点击页的数据
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
 * 分页显示图书需求趋势
 * @param curr	当前页，为空默认显示第1页
 */
function pagi_bookRequire(curr){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table3 .search_condition");
	var bookTypeName = conditions.eq(0).children("input").val(),	//学院名称
		bookType = conditions.eq(1).children("input").val();	//选择的起始年份

	$(".loading").show();	//显示加载层
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findBookForecastByCondition',
		data: {
			bookName:bookTypeName,
			typeName: (bookType == "全部") ? "" : bookType,
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
						var name_html = "<td width='20%' title='"+item.bookTypeName+"'>" + item.bookTypeName + " </td>";
						if(item.duplicateNum > 0){
							name_html = "<td width='20%' class='clickable' onclick='show_bookDetail("+item.uuid+","+'"'+item.bookTypeName+'"'+")' title='"+item.bookTypeName+"'> " 
								+ item.bookTypeName + " </td>"; //显示该书类下有哪些具体的书
						}
						
						html += "<tr data-id='"+item.uuid+"'>" +							
								"<td width='10%'>"+(i+1)+"</td>"+
								name_html+
								"<td width='15%'>"+item.bookType+"</td>"+
								"<td width='15%'>"+item.duplicateNum+"</td>"+
								"<td width='15%'>"+item.forecastNum+"</td>"+
								"<td width='15%'>"+item.requireNum+"</td>"+
								"<td width='10%'>"+item.borrowDegree+"</td>"+
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
								pagi_bookRequire(obj.curr);	//获取点击页的数据
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
 * 点击读者姓名，显示读者借过的图书类别饼状图
 */
function show_bookType(readerID,year){
	parent.layer.open({
		title:  "历史图书类别：",
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
				type: 'post',
				url: '/bookCalculate/getborrowType',
				data: {
					readerID:readerID,
					year:year
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//初始化echarts实例
		    		    var mychart = echarts.init($content[0]);
		    		  //各项的颜色
						var color = ['#95e5e4', '#d15f69', '#e59bac', '#e6470f', '#ef7512', '#f09511', '#f4c74a',
							        '#bcb75b', '#67d1d1', '#3aa289', '#62c899', '#1abb92', '#445fa6', '#4178d3',
							        '#2195d2', '#46afe6'];
		    		    var series = [];
	        			series.push({
	        				type: 'pie',
	                        data: data,
	                        label:{
	                            formatter: '{b}: {d}%'
	                        }
	        			});
	    			    var option = {
	    			    	color:color,
	    			        series: series
	    			    };
		        		mychart.setOption(option);
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>当年无历史借阅记录!</div>");
					}
				}
				});
			}
	});
}

/**
 * 点击读者借书次数，显示读者历史借阅记录
 * @param readerName 读者姓名
 * @param readerID 读者号
 * @param schoolYear 时间
 */
function show_borrowHistroy(readerName,readerID,schoolYear){
	parent.layer.open({
		title: readerName+schoolYear+"年借阅记录",
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
				type: 'post',
				url: '/bookCalculate/getBorrowRecordByReaderID',
				data: {
					readerID:readerID,
					year:schoolYear
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//弹窗中内容
						var top="<div class='reader'><span>读者："+readerName+"</span>" +
									"&nbsp;&nbsp;&nbsp;<span>"+schoolYear+"年总借书次数："+data.length+"次</span>"+
								"</div>";
						$content.html(top+
								      '<div class="history">'+
									  '<div class="history_main">'+
									  '<div class="history_line"></div><ul></ul></div></div>'
									 );
						$.each(data, function(i, item){
							var period = "";
							//具体时间段
							var times = item.value;
							//遍历
							for(var m = 0,len=times.length;m<len;m++){
								if(times[m]){
									period += "<div class='period1' style='display: list-item;list-style: disc;'>" +
											"《"+times[m][0]+"》"+"&nbsp;&nbsp;&nbsp;"+
											times[m][1]+"&nbsp;&nbsp;&nbsp;"+
											times[m][2]+"&nbsp;&nbsp;&nbsp;"+ "</div>";
								}
							}
							$content.find(".history_main ul").append("<li>"+
										item.name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;借阅量："+
										(times.length)+period+"</li>");
						});
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>当年无历史借阅记录!</div>");
						}
					}
				});
			}	
	});
}

/**
 * 点击图书书名，显示借过此书的学院字符云
 */
function show_mostMajor(bookID,year){
	parent.layer.open({
		title:  "学院借阅占比：",
		area: ['440px', '258px'],
		skin: 'my-default',
		shade: 0.7,
		shadeClose: true,
		btn: [],
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
			
			$.ajax({
				type: 'post',
				url: '/bookCalculate/getBookCollegeRate',
				data: {
					bookID:bookID,
					year:year
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data){						
						//初始化echarts实例
						var mychart = echarts.init($content[0]);
						//各项的颜色
						var color = ['#95e5e4', '#d15f69', '#e59bac', '#e6470f', '#ef7512', '#f09511', '#f4c74a',
							        '#bcb75b', '#67d1d1', '#3aa289', '#62c899', '#1abb92', '#445fa6', '#4178d3',
							        '#2195d2', '#46afe6'];
						mychart.setOption({
							tooltip: {
								borderWidth: tip.borderWidth,
					            borderColor: tip.borderColor,
					            position: tip.position,
					            formatter: function(param){
					            	var percent=Math.round(parseFloat(param.value*100)*100)/100;
					            	return param.marker + param.name + " : " + percent+"%";
					            }
							},
					        series: [{
					            type: 'wordCloud',
					            shape: 'rectangle',
					            sizeRange: [16, 24],//字体大小范围
					            rotationRange: [-45, 45],//字体旋转角度区间
					            gridSize: 16, //字符间距
					            bottom: 0,
					            left: 'center',
					            top: 'center',
					            textStyle: {
					                normal: {
					                    color: function() {
					                    	return color[Math.round(Math.random() * 16)];
					                    }
					                }
					            },
					            data: data
					        }]
					    });
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>当年无历史借阅记录!</div>");
					}
				}
			});
		}
	})	
}

/**
 * 点击图书借阅次数，显示图书历史借阅记录
 * @param bookName 图书名
 * @param uuid 图书号
 * @param year 时间
 */
function show_bookHistory(bookName,uuid,year){
	parent.layer.open({
		title: "《"+bookName+"》"+year+"年借阅记录",
		type: 1,
		area: ['570px', '322px'],
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
				type: 'post',
				url: '/bookCalculate/getBorrowRecordByBookID',
				data: {
					bookID:uuid,
					year:year
				},
				dataType: 'json',
				success: function(result){
					var data = result.object;
					if(data && data != ""){
						//弹窗中内容
						var top="<div class='reader'><span>图书："+bookName+"</span>" +
									"&nbsp;&nbsp;&nbsp;<span>"+year+"年总被借次数："+data.length+"次</span>"+
								"</div>";
						$content.html(top+
								      '<div class="history">'+
									  '<div class="history_main">'+
									  '<div class="history_line"></div><ul></ul></div></div>'
									 );
						$.each(data, function(i, item){
							var period = "";
							//具体时间段
							var times = item.value;
							//遍历
							for(var m = 0,len=times.length;m<len;m++){
								if(times[m]){
									period +="<span style='margin-left: 20px;color: #61c6f6;'>"+
											times[m][0]+"&nbsp;&nbsp;&nbsp;"+
											times[m][1]+"&nbsp;&nbsp;&nbsp;"+
											times[m][2]+"&nbsp;&nbsp;&nbsp;</span>";
								}
							}
							$content.find(".history_main ul").append("<li>"+
										item.name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;借阅次数："+
										(times.length)+period+"</li>");
						});
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>当年无历史借阅记录!</div>");
						}
					}
				});
			}	
	});
}

/**
 * 点击书类名，显示该书类下有哪些具体的书
 * @param bookID
 * @param bookTypeName
 */
function show_bookDetail(bookID,bookTypeName){
	parent.layer.open({
		title: '"'+bookTypeName+'"'+"书类下详情：",
		area: ['740px', '258px'],
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
				type: 'post',
				url: '/bookCalculate/getSubBookInfo',
				dataType: 'json',
				data:{
					bookID:bookID
				},
				success: function(result){
					var data = result.object;
					if(data){
						var html="";
						var thead = "<thead><tr><th style='width:25%'>书名</th>"
							  +"<th style='width:20%'>出版社</th>"
							  +"<th style='width:10%'>版次</th>"
							  +"<th style='width:15%'>作者</th>"
							  +"<th style='width:10%'>出版时间</th>" 
							  +"<th style='width:10%'>采购年份</th>" 
							  +"<th style='width:10%'>摆放位置</th>" 
							  +"</tr></thead>";
						$.each(data, function(i, item){
							html+="<tr><td style='width:25%' title='"+item.bookName+"'>"+item.bookName+"</td>"
									 +"<td style='width:20%' title='"+item.publish+"'>"+item.publish+"</td>"
									 +"<td style='width:10%'>"+item.edition+"</td>"
									 +"<td style='width:15%' title='"+item.author+"'>"+item.author+"</td>"
									 +"<td style='width:10%'>"+item.publicationDate+"</td>"
									 +"<td style='width:10%'>"+item.purchaseYear+"</td>"
									 +"<td style='width:10%'>"+item.position+"</td>";
						
						});
						$layero.find(".layui-layer-content").html("<table>"+thead+html+"</table>");					
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>无该类图书!</div>");
					}
				}
			});
		}
	});
}

var tip = {
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
	    }	
	};

/**图书借阅关系详情
 * @param bookID
 * @param bookName
 */
function show_borrowRelate(bookID,bookName){
	parent.layer.open({
		title: "《"+bookName+"》"+"借阅关系：",
		area: ['540px', '400px'],
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
			var $content = $layero.find(".layui-layer-content");
			
			$.ajax({
				type: 'post',
				url: '/bookCalculate/getSubBookBorrowRelate',
				dataType: 'json',
				data:{
					bookID:bookID,
					bookName:bookName
				},
				success: function(result){
					var data = result.object;
					if(data){
						var chart4 = echarts.init($content[0]);
						var option = {
							color: ['#95e5e4', '#d15f69', '#e59bac', '#e6470f', '#ef7512', '#f09511', '#f4c74a',
							        '#bcb75b', '#67d1d1', '#3aa289', '#62c899', '#1abb92', '#445fa6', '#4178d3',
							        '#2195d2', '#46afe6', '#63cff5', '#5fd696', '#f86515', '#ffb109', '#355cb9',
							        '#db684b'],
							tooltip: {
								borderWidth: tip.borderWidth,
					            borderColor: tip.borderColor,
					            position: tip.position,
					            formatter: function(param){
					            	if(param.dataType == "node"){
					            		return param.marker + param.name + " : " + param.value;
					            	}else{
					            		return param.name.replace(">", "-") + " : " + (param.value * 100) + "%";
					            	}
					            }
							},
							series: [{
								type: 'graph',
								categories: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21],
								layout: 'force',
								force: {
									repulsion: [400, 500],
									edgeLength: [100, 120]
								},
								label: {
									show: true,
									position: 'bottom'
								},
								emphasis: {
									label: {
										show: false
									}
								},
								symbolSize: function(v){
									return v*10;
								},
								roam: true,
								draggable: true,
								focusNodeAdjacency: true,
								
								data: data[0],
								links: data[1]
							}]
						};	
						chart4.setOption(option);
					}
					else{
						$content.html("<div style='color:#fff;text-align: center;'>该书无历史借阅关系!</div>");
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
 * 导出读者借阅分析
 */
function export_readerBorrow(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table1 .search_condition");
	var readerName = conditions.eq(0).children("input").val(),	//读者姓名
		collegeName = conditions.eq(1).children("input").val(),	//学院名称
		startYear= conditions.eq(2).children("input.start").val(),	//选择的起始年份
		endYear = conditions.eq(2).children("input.end").val();	//选择的结束年份
	
	var data_c = {
			'readerName':readerName,
			'college': (collegeName == "全部") ? "" : collegeName,
			'startYear': startYear,
			'endYear': endYear,
			'cluster': cluster? cluster :""
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findReaderByCondition',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["学号","姓名","性别","学院","班级","年级","类型","总借阅量",
				             "总借书次数","学年","楼栋"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据

					//聚类结果判断
					var cluster = "";
					if(o.cluster == "1.0"){
						cluster = "活跃"; 
					}
					else if(o.cluster == "0.0"){
						cluster = "懒惰";
					}
					else{
						cluster = "一般";
					}
					
					//按表头字段顺序填入数据
					datarow.push(o.readerID);
					datarow.push(o.readerName);
					datarow.push(o.readerSex);
					datarow.push(o.collegeName);
					datarow.push(o.major);
					datarow.push(o.readerIdentity);
					datarow.push(cluster);
					datarow.push(o.bookSum);
					datarow.push(o.borrowTimes);
					datarow.push(o.schoolYear);
					datarow.push(o.dormitory);
					
					datarows.push(datarow);
				});
				var fileName = '读者借阅分析';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出图书借阅分析
 */
function export_bookBorrow(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table2 .search_condition");
	var bookName = conditions.eq(0).children("input").val(),	//书名
		typeName = conditions.eq(1).children("input").val(),	//分类名称
		startYear= conditions.eq(2).children("input.start").val(),	//选择的起始年份
		endYear = conditions.eq(2).children("input.end").val();	//选择的结束年份
	
	var data_c = {
			'bookName':bookName,
			'typeName': (typeName == "全部") ? "" : typeName,
			'startYear': startYear,
			'endYear': endYear,
			'cluster': cluster? cluster :""
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findBookByCondition',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["书名","分类","热度","学年","借阅次数","作者","出版社","版次",
				             "出版时间","采购年份","摆放位置","破损程度"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据

					//聚类结果判断
					var cluster = "";
					if(o.cluster == "2.0"){
						cluster = "热门"; 
					}
					else if(o.cluster == "0.0"){
						cluster = "冷门";
					}
					else{
						cluster = "一般";
					}
					
					//按表头字段顺序填入数据
					datarow.push(o.bookName);
					datarow.push(o.typeName);
					datarow.push(cluster);
					datarow.push(o.year);
					datarow.push(o.borrowTimes);
					datarow.push(o.author);
					datarow.push(o.publish);
					datarow.push(o.edition);
					datarow.push(o.publicationDate);
					datarow.push(o.purchaseYear);
					datarow.push(o.position);
					datarow.push(o.damage);
					
					datarows.push(datarow);
				});
				var fileName = '图书借阅分析';//生成的文件名
				//导出
				exportToExcel(title,datarows,fileName);
			}
			parent.layer.close(index);
        }
	});
}

/**
 * 导出图书需求趋势
 */
function export_bookRequire(){
	/*
	 * 获取页面选择的查询条件
	 */
	var conditions = $("#table3 .search_condition");
	var bookTypeName = conditions.eq(0).children("input").val(),	//学院名称
		bookType = conditions.eq(1).children("input").val();	//选择的起始年份
	
	var data_c = {
				'bookName':bookTypeName,
				'typeName': (bookType == "全部") ? "" : bookType
	};
	var index = parent.layer.load( 2,  { shade: 0.3 });;
	//导出
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findBookForecastByCondition',
		dataType: 'json',
		data:$.extend(data_c,data_d),//合并参数
		success: function(result){
			var data = result.object;
			if(data.records != null){
				var title = ["序号","书类名","分类","已有复本数","预测复本数","需求量","热度"];  //表头
				var datarows = [];      //数据
				$.each(data.records, function(i, o){
					var datarow = [];   //每一行的数据

					//按表头字段顺序填入数据
					datarow.push(i+1);
					datarow.push(o.bookTypeName);
					datarow.push(o.bookType);
					datarow.push(o.duplicateNum);
					datarow.push(o.forecastNum);
					datarow.push(o.requireNum);
					datarow.push(o.borrowDegree);
					
					datarows.push(datarow);
				});
				var fileName = '图书需求趋势';//生成的文件名
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
 * 显示读者借阅量Top10排行榜
 */
function top10_readerBorrow(){		
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findReaderByCondition',
		data: {
			readerName:"",
			collegeName: "",
			startYear: "",
			endYear: "",
			cluster:"",
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
							html += "<tr>" +
							"<td width='15%'>" + (i+1) + "</td>" +
							"<td width='40%'>" + item.readerName + "</td>" +
							"<td width='45%'>" + item.bookSum  + "</td>" +
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
 * 显示图书借阅次数Top10排行榜
 */
function top10_bookBorrow(){		
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findBookByCondition',
		data: {
			bookName:"",
			typeName: "",
			startYear: "",
			endYear: "",
			cluster:"",
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
							html += "<tr>" +
							"<td width='20%'>" + (i+1) + "</td>" +
							"<td width='50%' title='"+item.bookName+"'>" + item.bookName + "</td>" +
							"<td width='30%'>" + item.borrowTimes  + "</td>" +
							"</tr>";
						});
					}else{	//数据为空
						html = "<tr><td colspan='8'>无数据</td></tr>";
					}
					$('#top10_2 tbody').html(html);
				}
			}
		}
	});
}

/**
 * 显示图书需求量Top10排行榜
 */
function top10_bookRequire(){		
	$.ajax({
		type: 'post',
		url: '/bookCalculate/findBookForecastByCondition',
		data: {
			bookName:"",
			typeName: "",
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
							html += "<tr>" +
							"<td width='20%'>" + (i+1) + "</td>" +
							"<td width='50%' title='"+item.bookTypeName+"'>" + item.bookTypeName + "</td>" +
							"<td width='30%'>" + item.requireNum  + "</td>" +
							"</tr>";
						});
					}else{	//数据为空
						html = "<tr><td colspan='8'>无数据</td></tr>";
					}
					$('#top10_3 tbody').html(html);
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
			belong:'图书测算'
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