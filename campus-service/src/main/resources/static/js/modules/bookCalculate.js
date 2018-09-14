/**
 * 图书测算js
 */
$(function() {
	var $parentIframe = $(window.parent.document.getElementById("contentFrame")), 
		$charts = $(".chart"), 
		$tableAll = $(".table_all");

	$("body").css("height", $parentIframe.height());
	/*图的高度调整，须在设定本页面body高度为父页面iframe高度后调整，否则不能适应屏幕大小*/
	for (var i = 0, len = $charts.length; i < len; i++) {
		var $item = $charts.eq(i);
		$item.css("height", ($item.parent().parent().height() - 50) + "px");
	}
	$tableAll.css("height", ($tableAll.parent().parent().height() * 0.75) + "px");
	$tableAll.find(".table_content").css("height", ($tableAll.height()-30)+"px");

	if (window.screen.width < 1700) {
		$(".time").css("left", '3%');
	}

	drawChart1();
	drawChart2();
	drawChart3();
	drawChart4();
	drawChart5();

	//获取图书借阅排行
	$.ajax({
		type : 'post',
		url : '/bookCalculate/getBookRank',
		dataType : 'json',
		success : function(result) {
			var data = result.object;
			if (data) {
				var html = "";
				$.each(data, function(i, item) {
					html += "<tr>" + "<td width='10%'>" + (i + 1) + "</td>"
							+ "<td width='40%' title='" + item.bookName + "'>"
							+ item.bookName + "</td>"
							+ "<td width='15%' title='" + item.borrowTimes
							+ "'>" + item.borrowTimes + "</td>"
							+ "<td width='15%' title='" + item.borrowRate
							+ "'>" + item.borrowRate + "</td>"
							+ "<td width='10%' title='" + item.total + "'>"
							+ item.total + "</td>" + "<td width='10%' title='"
							+ item.store + "'>" + item.store + "</td>"
							+ "</tr>";
				});
				$(".table_content tbody").html(html);
			}
		}
	});
	
	//时间轴选择
	var $time1_li = $("#time1 li");
	$time1_li.on("click", function(){
		var $obj = $(this), 
			_index = $obj.index();	//索引值
		$obj.addClass("selected").siblings().removeClass("selected");
		chart1.dispatchAction({
			type: 'timelineChange',
			currentIndex: 4
		});
	});
	$time1_li.eq(1).click();
});

//工具栏配置
var toolbox = {
    show: true,
    right: '5%',
    top: '0%',
    feature: {
        magicType: {
            show: true,
            type: ['bar', 'line']
        },
        saveAsImage: { show: true }
    },
    iconStyle: {
        normal: {
            borderColor: '#1a6a9b'
        }
    }
};

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

/*图书借阅分析*/
var chart1;
function drawChart1(){
	chart1 = echarts.init(document.getElementById("chart1"));
    
	var option = {
		baseOption: {
			color: ['#95e5e4', '#d15f69', '#e59bac', '#e6470f', '#ef7512', '#f09511', '#f4c74a',
			        '#bcb75b', '#67d1d1', '#3aa289', '#62c899', '#1abb92', '#445fa6', '#4178d3',
			        '#2195d2', '#46afe6'],
			timeline: {
				width: '50%',
				left: '28%',
	            bottom: '2%',
	            axisType: 'category',
	            data: mGetTimelineData("year"),	//获取时间轴数据，以‘年’为单位，包含当前年
	            label: {
	                color: '#668db6',
	                position: 8,
	                formatter : function(s) {
	                    return s+"年";
	                }
	            },
	            symbol: 'circle',
	            itemStyle: {
	                normal: {color: '#1d476d', borderWidth: 0},
	                emphasis: {color: '#50aee1', borderWidth: 0}
	            },
	            checkpointStyle: {
	                color: '#3679a4',
	                borderWidth: 1,
	                borderColor: '#50aee1'
	            },
	            controlStyle: {
	                show: false
	            }
			},
			tooltip: {  //提示框
	            trigger: 'item',
	            borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position,
	            formatter: function(param){
	            	return param.marker + param.name + " : " + param.percent+"%";
	            }
	        },
			series: [{
				color: ['#2096d4', '#e9772e', '#28bfba'],
				//name: '图书借阅热门',
                type: 'pie',
                radius: [0, '30%'],
                center: ['50%', '45%'],
                z: 2,
                label: {
                    normal: {
                        show: true,
                        position: 'inside',
                        formatter: '{b}\n{d}%'
                    }
                }
			},{
                //name: '图书借阅分类',
                type: 'pie',
                radius: ['40%', '55%'],
                center: ['50%', '45%'],
                z: 2,
                label: {
                    normal: {
                        show: true,
                        formatter: '{b}'
                    }
                },
                labelLine: {
                    normal: {
                        show: true,
                        length: 15,
                        length2: 0
                    }
                }
            }]
		}
	};
	chart1.setOption(option);
	chart1.on("timelinechanged", function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			_option = chart1.getOption(),
			_timeline = _option.timeline[0],
			_chooseYear = _timeline.data[_curIndex].value,	//选择的时间值
			_li_index = $("#time1 li.selected").index(),
			baseurl = '/bookCalculate',
			url = _li_index ? baseurl+'/getBookBorrowRate' : baseurl+'/getReaderBorrowRate';
		
		chart1.showLoading();
		$.ajax({
			type: 'post',
			url: url,
			data: {
				year: _chooseYear
			},
			dataType: 'json',
			success: function(result){
				var data = result.object;
				if(data){
					chart1.setOption({
						series: [{data: data[0]}, {data: data[1]}]
					});
				}else{
					chart1.setOption(option, true);
    				chart1.setOption({baseOption: {timeline: _timeline}});
				}
				chart1.hideLoading();
			}
		});
	});
	chart1.dispatchAction({type: 'timelineChange', currentIndex: 4});
}

/*图书需求变化趋势*/
function drawChart2() {
	var chart2 = echarts.init(document.getElementById("chart2"));
	//var color = {"复本数": "#63cff5", "建议购买数": "#5fd696", "超出预算数": "#f86515", "预测数": "#ffb109"};
	var option2 = {
		color : [ '#63cff5', '#5fd696', '#f86515', '#feb207' ], //各系列颜色
		tooltip : { //提示框
			trigger : 'axis', //坐标轴触发
			borderWidth: tip.borderWidth,
            borderColor: tip.borderColor,
            position: tip.position
		},
		toolbox : toolbox,
		legend : {
			bottom: '10%',
			textStyle : {
				color : '#fff'
			},
			data : [ '复本数', '建议购买数', '超出预算数', '预测数' ]
		},
		grid : {
			bottom : '28%'
		},
		xAxis : [ //x轴
			{
				type : 'category', //轴类型，类目型
				axisLine : { //轴线
					lineStyle : { //轴线样式
						color : '#2d556f'
					}
				},
				axisLabel : { //轴的刻度标签设置
					color : '#6f9ed6' //文本颜色
				},
	            axisTick: {show: false},
				data : [ '自然科学总论', '航空、航天', '文教体育', '数理化学', '哲学宗教', '军事', '社科总论', 
				         '语言文字', '经济', '医药卫生', '天文地球', '生物科学', '综合类', '农业科学', '艺术',
				         '交通运输', '政治法律', '环境科学', '文学', '工业技术', '马、列、毛', '历史地理']
			}
		],
		yAxis : [ //y轴
			{
				type : 'value', //轴类型，数值轴，连续数据
				name: '本',
				nameTextStyle: {
                	color: '#6f9ed6'
                },
				axisLine : {
					lineStyle : {
						color : '#2d556f'
					}
				},
				axisLabel : {
					color : '#6f9ed6'
				},
				splitNumber : 10, //分割段数
				splitLine : { //在表格区域的分割线
					show : false
				}
			} 
		],
		series : [ {
			name : '复本数',
			type : 'bar', //柱状图
			stack : '总量'
		}, {
			name : '建议购买数',
			type : 'bar', //柱状图
			stack : '总量'
		}, {
			name : '超出预算数',
			type : 'bar', //柱状图
			stack : '总量'
		}, {
			name : '预测数',
			type : 'line', 
			showAllSymbol: true,
			symbolSize: 8,
			lineStyle: {
				width: 4
			}
		} ]
	}
	chart2.setOption(option2);
	chart2.showLoading();
	$.ajax({
		type: 'post',
		url: '/bookCalculate/getBookForecast',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data){
				/*
				 * 将预测数据的点顺序转成与坐标轴顺序一致，才按顺序连线
				 */
				var yuce_data = {};	//返回的预测数据转换成key、value形式
				$.each(data[3], function(i, n){
					var name = n[0];
					yuce_data[name] = n[1];
				})
				var linedata = [], x = option2.xAxis[0].data;	//连线数据，顺序要与坐标顺序一致
				$.each(x, function(i, item){
					linedata.push([item, yuce_data[item]]);
				});
				
				var series = [];
				$.each(data, function(i, n){
					series.push({
						data: (i != 3) ? n : linedata
					});
				});
				chart2.setOption({
					series: series
				});
			}
			chart2.hideLoading();
		}
	});
}

/*懒惰读者行为分析*/
function drawChart3(){
	var chart3 = echarts.init(document.getElementById("chart3"));
	
	var option = {
		baseOption: {
			timeline: {
	            width: '80%',
	            left: 'center',
	            bottom: -10,
	            axisType: 'category',
	            data: mGetTimelineData("year"),	//获取时间轴数据，以‘年’为单位,默认不包含当前年
	            currentIndex: 4,
	            label: {	//时间轴标签
	                color: '#668db6',
	                position: 8,
	                formatter : function(s) {
	                    return (new Date(s)).getFullYear()+"年";
	                }
	            },
	            symbol: 'circle',
	            itemStyle: {
	                normal: {color: '#1d476d', borderWidth: 0},
	                emphasis: {color: '#50aee1', borderWidth: 0}
	            },
	            checkpointStyle: {
	                color: '#3679a4',
	                borderWidth: 1,
	                borderColor: '#50aee1'
	            },
	            controlStyle: {
	                show: false
	            }
	        },
			tooltip: {
				borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position
			},
			radar: {	//雷达坐标基础配置
	            center: ['50%', '50%'],	//中心点
	            radius: '75%',	//半径
	            splitNumber: 5,
	            name: {	//轴名称样式
	                color:'#b6bfd0'
	            },
	            axisLine: {	//轴样式
	                lineStyle: {
	                	width: 2,
	                    color: '#174465'
	                }
	            },
	            splitLine: {	//分割线样式
	                lineStyle: {
	                	width: 2,
	                    color: '#2e6c91'
	                }
	            },
	            splitArea: {
	                areaStyle: {
	                    color: 'none'
	                }
	            }
			},
			series: [{
				type: 'radar'
			}]
		}
	};
	chart3.setOption(option);
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart3.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			_option = chart3.getOption(),
			_timeline = _option.timeline[0],
			_chooseYear = _timeline.data[_curIndex].value;	//选择的时间值
		chart3.showLoading();
		
		$.ajax({
			type: 'post',
			url: '/bookCalculate/getLazyReader',
			data: {
				year: _chooseYear
			},
			dataType: 'json',
			success: function(result){
				var data = result.object;
				if(data){
					var indicator = [], value = [], max = 0;
					$.each(data, function(i, n){
						max = (max < n[1]) ? n[1] : max;
						indicator.push({
							name: n[0]
						});
						value.push(Math.round(parseFloat(n[1]) *100) / 100);
					});
					//设置每个坐标的最大值
					$.each(indicator, function(i, n){
						n.max = max;
					});
					chart3.setOption({
						radar: {indicator: indicator},
						series: [{
							data: [{
								name: '原因占比',
								value: value,
								lineStyle: {
				                    color: '#fdd714'
				                },
								areaStyle: {
				                    opacity: 0.8,
				                    color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [
				                        {
				                            color: '#877339',
				                            offset: 0
				                        },
				                        {
				                            color: '#846c2e',
				                            offset: 1
				                        }
				                    ])
				                }
							}]
						}]
					});
				}else{	//数据为空
					chart3.setOption(option, true);
					chart3.setOption({baseOption: {timeline: _timeline}});
				}
				chart3.hideLoading();
			}	
		});
	});
	//默认点击最后一个时间点
	chart3.dispatchAction({type: 'timelineChange', currentIndex: 4})
}

function drawChart4(){
	var chart4 = echarts.init(document.getElementById("chart4"));
	
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
            		return param.name.replace(">", "-") + " : " + (param.value / 100) + "%";
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
				return v;
			},
			roam: true,
			draggable: true,
			focusNodeAdjacency: true
		}]
	};	
	chart4.setOption(option);
	chart4.showLoading();
	$.ajax({
		type: 'post',
		url: '/bookCalculate/getBookBorrowRelate',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data){
				chart4.setOption({
					series: [{
						data: data[0],
						links: data[1]
					}]
				});
			}
			chart4.hideLoading();
		}
	});
}

/*冷门图书借阅分析*/
function drawChart5(){
	var chart5 = echarts.init(document.getElementById("chart5"));

	var color = ['#345bba', '#de6a51', '#dd294e', '#41944c', '#0b94e5'];

    var option = {
        baseOption: {
            timeline: {
                width: '50%',
                left: 'center',
                bottom: '0%',
                axisType: 'category',
	            data: mGetTimelineData("year"),	//获取时间轴数据，以‘年’为单位，含当前年
	            currentIndex: 4,
	            label: {	//时间轴标签
	                color: '#668db6',
	                position: 8,
	                formatter : function(s) {
	                    return s+"年";
	                }
	            },
                symbol: 'circle',
	            itemStyle: {
	                normal: {color: '#1d476d', borderWidth: 0},
	                emphasis: {color: '#50aee1', borderWidth: 0}
	            },
	            checkpointStyle: {
	                color: '#3679a4',
	                borderWidth: 1,
	                borderColor: '#50aee1'
	            },
	            controlStyle: {
	                show: false
	            }
            },
            tooltip: {
                trigger: 'item',
                borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position,
                formatter: function (data) {
                    return data.name + '：' + data.value; //提示框显示信息
                }
            },
            series: [{
            	type: 'sankey',
                layout: 'none',
                left: '15%',
                right: '15%',
                top: '2%',
                bottom: '10%',
                height: '85%',
                label: {
                	color: 'white'
                },
                lineStyle: {
                    color: 'target',
                    opacity: 0.8
                },
                emphasis: {
                	lineStyle: {opacity: 1}
                },
                data: [{}],
                links: [{}]
            }]
        }
    };
    chart5.setOption(option);
    
    var all;	//存储后台返回数据
    //时间切换事件
    chart5.on("timelinechanged", function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			_option = chart5.getOption(),	
			_timeline = _option.timeline[0],
			_chooseYear = _timeline.data[_curIndex].value;	//选择的时间值
	    
		chart5.showLoading();
		$.ajax({
			type: 'post',
			url: '/bookCalculate/getUpsetBook',
			data: {
				year: _chooseYear
			},
			dataType: 'json',
			success: function(result){
				var data = result.object;
				if(data){
					all = data;
					var nodes = [], m = 0;	
					$.each(data[0], function(i, n){
						if(n.type){	//图书分类节点
							//当值>20时显示标签
							var label = (n.value > 20) ? {position: 'left'} : {show: false};
							nodes.push({
								name: n.name,
								value: n.value,
								label: label,
				                itemStyle: {color: '#baaa4b'}
							});
						}else{	//原因节点
							nodes.push({
								name: n.name,
								value: n.value,
				                itemStyle: {color: color[m]}
							});
							m++;
						}
					});
					chart5.setOption({
						series: [{
							data: nodes,
							links: data[1]
						}]
					});
				}else{	//返回空值
					chart5.setOption(option, true);
					chart5.setOption({baseOption: {timeline: _timeline}});
				}
				chart5.hideLoading();
			}
		});
    });
    //默认触发时间轴最后一个点
    chart5.dispatchAction({type: 'timelineChange', currentIndex: 4});
    //绑定点击事件，当点击某个项时只显示该项关联的线，其它线隐藏
    chart5.on('click', function(param){
    	if(param.dataType == "node"){	//只能为node类型   	
	    	var name = param.name,	//点击项的名称
	    		_option = chart5.getOption(),	//配置项
				_timeline = _option.timeline[0],	//时间轴	
				nodes = _option.series[0].data,	//所有node节点
				links = all[1],	//所有edge线数据
				newLinks = [];	//存放新的edge数据
	    	//重新设置option
			chart5.setOption(option, true);
			chart5.setOption({baseOption: {timeline: _timeline}});
			
			//循环每一个edge数据项，当点击项的名称与该edge数据项的source或target名称相等时，
			//则该edge数据项的线样式设置透明度为0.8，否则为0即不显示
			$.each(links, function(i, n){
				if(name != n.source && name != n.target){
					n.lineStyle = {
						opacity: 0
					};
				}else {
					n.lineStyle = {
						opacity: 0.8
					};
				}
			});
			chart5.setOption({
				series: [{
					data: nodes,
					links: links
				}]
			});	
    	}
    });
    //双击事件，重新回到所有线显示状态
    chart5.on('dblclick', function(param){
    	var links = all[1];
    	$.each(links, function(i, n){
			n.lineStyle = {
				opacity: 0.8
			};
		});
		chart5.setOption({
			series: [{
				links: links
			}]
		});	
    });
}



