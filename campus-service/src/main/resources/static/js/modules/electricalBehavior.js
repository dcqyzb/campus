/**
 * 用电行为js
 */

$(function(){
	var $parentIframe = $(window.parent.document.getElementById("contentFrame")),
		$charts = $(".chart"),
		$tableAll = $(".table_all");
	
	$("body").css("height", $parentIframe.height());
	/*图的高度调整，须在设定本页面body高度为父页面iframe高度后调整，否则不能适应屏幕大小*/
	for(var i=0, len=$charts.length; i<len; i++){
		var $item = $charts.eq(i);
		$item.css("height", ($item.parent().parent().height()-50)+"px");
	}
	$tableAll.css("height", ($tableAll.parent().parent().height()*0.7)+"px");
	
	/*
	 * 节能宿舍排行统计
	 */
	$.ajax({
		type: 'get',
		url: '/electricalBehavior/getDormitoryRank',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data){
				var html = "";
				$.each(data, function(i, item){
					html += "<tr>" +
							"<td width='15%'>"+(i+1)+"</td>" +
							"<td width='25%' title='"+item.dormitory+"'>"+item.dormitory+"</td>" +
							"<td width='15%'>"+item.number+"</td>" +
							"<td width='45%'>"+item.electricity+"</td>" +							
							"</tr>";
				});
				$(".table_content tbody").html(html);	
			}
		}
	});
				
	drawChart1();
	drawChart2();
	drawChart3();
	drawChart4();
	
	//时间轴选择
	var $time1_li = $("#time1 li"), $time2_li = $("#time2 li"),
		 $time3_li = $("#time3 li");
	$time1_li.on("click", function(){
		changeTimeline(this, chart1, true);
	});
	$time2_li.on("click", function(){
		changeTimeline(this, chart3, true);
	});
	$time3_li.on("click", function(){
		changeTimeline(this, chart4);
	});
	$time1_li.eq(1).click();
	$time2_li.eq(1).click();
	$time3_li.eq(0).click();
});

/*用电趋势分析*/
var chart1;
function drawChart1(){
	chart1 = echarts.init(document.getElementById("chart1"));
	
	var option = {
		baseOption: {
			color: ['#74c8e4', '#f28240', '#56b3c2', '#d05e7f', '#588ee4',
			        '#af92d5', '#5dc394', '#818855'],
			timeline: {
				axisType: 'category',
	            label: {
	                color: '#668db6',
	                position: 8
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
				trigger: 'axis',
				formatter: function(params){
					var _option = chart1.getOption(),
						_timeline = _option.timeline[0],
						_chooseValue = _timeline.data[_timeline.currentIndex].value;
					var date = _chooseValue + "月" + params[0].axisValue + "日";
					var html = date + "<br/>";
					$.each(params, function(i, item){
						html += item.marker + item.seriesName + ": " + item.value[1] + " 度<br/>";
					});
					return html;
				},
				position: function(point, params, dom, rect, size){
					return [point[0] + 5, '2%'];
				}
			},
			grid: {
	        	bottom: '30%'
	        },
			xAxis: [
			    {
				    type : 'value',
				    axisLine: {         //轴线
	                    lineStyle: {    //轴线样式
	                        color: '#2d556f'
	                    }
	                },
	                axisLabel: {        //轴的刻度标签设置
	                    color: '#6f9ed6'   //文本颜色
	                },
	                splitLine: {    //在grid中横向线的配置
		                show: false     //不显示
		            },
		            min: 1,
	                max: 31
			    }
			],
			yAxis: [
			    {
			    	type : 'value',     //轴类型，数值轴，连续数据
				    name: '度',
				    nameTextStyle: {    //名称样式
		                color: '#6f9ed6',
		                fontSize: 14
		            },
	                axisLine: {
	                    lineStyle: {
	                        color: '#2d556f'
	                    }
	                },
	                axisLabel: {
	                    color: '#6f9ed6'
	                },
	                splitLine: {    //在grid中横向线的配置
		                show: false     //不显示
		            }
			    }
			]
		}
	}
	
	chart1.setOption(option);
	//时间切换监听
	chart1.on("timelinechanged", function(param){
    	var _curIndex = param.currentIndex,	//当前下标
			_option = chart1.getOption(),
			_timeline = _option.timeline[0],	//时间轴
			_chooseValue = _timeline.data[_curIndex].value;	//时间选择值
    	
    	//切换时间，x轴需做相应的变化
    	if($("#time1 li.selected").attr("class").indexOf("year") != -1){ 		
    		chart1.setOption({
        		xAxis: {max: 12}
        	});
    	}else{
    		var dates = _chooseValue.split("-");
    		chart1.setOption({
        		xAxis: {max: mGetDate(dates[0], dates[1])}
        	});
    	}
    	
    	chart1.showLoading();
    	$.ajax({
    		type: 'get',
    		url: '/electricalBehavior/getAcademyElectric',
    		data: {
    			date: _chooseValue
    		},
    		dataType: 'json',
    		success: function(result){
    			var data = result.object;
    			if(data){
    				var series = [];
    				$.each(data, function(i, item){
    					if(i>3) return;	//数据过多不显示，只显示4条
    					//数据排序，以保证连线是按坐标轴顺序一致
    			        for(var i=1, len=item.data.length; i<len; i++){
    			        	for(var j=i; j>0; j--){
    			        		var m = item.data[j], n = item.data[j-1];
    			        		if(m[0] < n[0]){	
    			        			item.data[j] = n;
    			        			item.data[j-1] = m;
    			        		}else if(m[0] == n[0]){
    			        			if(m[1] < n[1]){
    			        				res[j] = n;
    			            			res[j-1] = m;
    			        			}
    			        		}else{
    			        			break;
    			        		}
    			        	}
    			        }
    					series.push({
    						type: 'line',
    						name: item.name,
    						data: item.data
    					})
    				});
    				chart1.setOption({
    					series: series
    				});
    			}else{	//数据为空
    				chart1.setOption(option, true);
    				chart1.setOption({baseOption: {timeline: _timeline}});
    			}
    			chart1.hideLoading();
    		}
    	});
	});
}

/*窃电嫌疑分析*/
function drawChart2(){
	var chart2 = echarts.init(document.getElementById("chart2"));
	var color_arr = ['#4ff8f5', '#9f7f0e','#517ff7','#11a010'];
	var option = {
		tooltip: {  //提示框
			trigger: 'axis',    //坐标轴触发
			borderWidth: tip.borderWidth,
			borderColor: tip.borderColor,
			position: tip.position,
			formatter: function(params){
				if(!params) {return;}
				var html = params[0].data[0] + "<br/>";
				$.each(params, function(i, item){
					html += item.marker + item.data[2].split("-")[0] + "栋" +
							item.data[2].split("-")[1] + "室： " +
							(Math.round(item.data[1] * 100) / 100) + " 度<br/>";
				});
				return html;
			}
	    },
		legend: {
			bottom: '5%',
			textStyle: {
                color: '#b2c0c3',
                fontSize: 14
            },
			data: ['1栋','2栋','3栋','4栋','5栋','6栋','7栋','8栋','9栋','10栋','11栋','12栋'],
			selectedMode: 'single',
			inactiveColor: '#aaa'
		},
		grid: {
        	bottom: '30%'
        },
		xAxis: [
		    {
			    type : 'category',
			    axisLine: {         //轴线
                    lineStyle: {    //轴线样式
                        color: '#2d556f'
                    }
                },
                axisLabel: {        //轴的刻度标签设置
                    color: '#6f9ed6'   //文本颜色
                },
                splitLine: {    //在grid中横向线的配置
	                show: false     //不显示
	            },
                data:  ['2016-03','2016-04','2016-05','2016-06','2016-07','2016-08','2016-09','2016-10','2016-11','2016-12',
                       '2017-01','2017-02','2017-03','2017-04','2017-05','2017-06','2017-07','2017-08','2017-09','2017-10','2017-11','2017-12',
                       '2018-01','2018-02']
		    }
		],
		yAxis: [
		    {
		    	type : 'value',     //轴类型，数值轴，连续数据
			    name: '度',
			    nameTextStyle: {    //名称样式
	                color: '#6f9ed6',
	                fontSize: 14
	            },
                axisLine: {
                    lineStyle: {
                        color: '#2d556f'
                    }
                },
                axisLabel: {
                    color: '#6f9ed6'
                },
                splitLine: {    //在grid中横向线的配置
	                show: false     //不显示
	            }
		    }
		]
	};
	chart2.setOption(option);

	/*
	 * 楼栋选择切换，num 表示选中的楼栋号
	 */
	var legendChange = function(num){
		if(!num){return;}
//		chart2.showLoading();	//显示‘加载’
		$.ajax({
			type: 'get',
			url: '/electricalBehavior/getStealElectricTop4',
			dataType: 'json',
			data : {
				position : num
			},
			success: function(result){
				var data = result.object;
				if(data){
					var series = [];
					for(var i=0; i<12; i++){
						if(num != (i+1)){	//其它栋的series，保证legend全部显示
							series.push({type: 'line', name: (i+1)+'栋', data: []});
						}
					}
					$.each(data, function(i, item){
						var dorm = item.name.split("-");
						series.push({
							name: dorm[0]+"栋",
							type: 'line' ,
							smooth: true,
					    	data: item.value,
					    	color: color_arr[i]
						});
					});
					chart2.setOption({
						series: series
					});
				}else{	//数据为空时，清空series
					chart2.setOption(option, true);
					var series = [];
					for(var i=0; i<12; i++){
						series.push({type: 'line', name: (i+1)+'栋', data: []});
					}
					chart2.setOption({series: series});
				}
//				chart2.hideLoading();
			}
		});
	}
	//绑定"楼栋"点击切换事件，每点击一下重新请求获取数据
	chart2.on('legendselectchanged', function(param){
		var lou = param.name.split("栋")[0];	//楼层
		legendChange(lou);
	});
	/*
	 * 由于echarts没有'legendselectchanged'对应的Action,所以通过'legendSelect'动作触发第一次加载
	 */
	chart2.on('legendselected', function(param){
		var lou = param.name.split("栋")[0];	//楼层
		legendChange(lou);
	});
	//默认触发显示'1栋'
	chart2.dispatchAction({type: 'legendSelect', name: '1栋'});
}

/*违章电器使用嫌疑*/
var chart3;
function drawChart3(){
	chart3 = echarts.init(document.getElementById("chart3"));
	
	var option = {
		baseOption: {
			timeline: {
				axisType: 'category',
	            label: {
	                color: '#668db6',
	                position: 8
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
				formatter: function(params) {
	                return  "<div style='width:150px'>" +params.value[3]+
	                "<p>" +
 					"<span style='width:60%;float:left;'>"+params.value[2].split('-')[0]+"栋"+params.value[2].split('-')[1]+"室"+"</span>" +
					"<span style='width:40%;text-align:left;'float:right;>"+
							params.value[1].substring(0,4)+"%</span>" +
					"</p>" +
					"</div>";
		        },
			},
			visualMap: {
		        min: 0,
		        max: 100,
		        dimension: 1,
		        left: 'right',
		        top: 'top',
		        //text: ['HIGH', 'LOW'], // 文本，默认为数值文本
		        calculable: true,
		        itemWidth:18,
		        itemHeight:160,
		        textStyle: {
		                color:'#3259B8',
		                height: 56,
		                fontSize:11,
		                lineHeight:60,
		            },
		        inRange: {
		            color: ['#41bd7e', '#00a3ff', '#3EACE5', '#e6367f']
		        },
		        padding:[50, 20],
		        //orient:'horizontal',
		    },
			grid: {
		    	bottom: '30%'
		    },
			xAxis: [
			    {
				    type : 'category',
				    axisLine: {         //轴线
		                lineStyle: {    //轴线样式
		                    color: '#2d556f'
		                }
		            },
		            axisLabel: {        //轴的刻度标签设置
		                color: '#6f9ed6'   //文本颜色
		            },
		            splitLine: {    //在grid中横向线的配置
		                show: false     //不显示
		            },
		            data: ['1栋','2栋','3栋','4栋','5栋','6栋','7栋','8栋','9栋','10栋','11栋','12栋']
			    }
			],
			yAxis: [
			    {
			    	type : 'value',     //轴类型，数值轴，连续数据
				    name: '嫌疑度（%）',
				    nameTextStyle: {    //名称样式
		                color: '#6f9ed6',
		                fontSize: 14
		            },
		            axisLine: {
		                lineStyle: {
		                    color: '#2d556f'
		                }
		            },
		            axisLabel: {
		                color: '#6f9ed6'
		            },
		            splitLine: {    //在grid中横向线的配置
		                show: false     //不显示
		            },
		            max: 100
			    }
			]
		}
	};
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart3.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择下标
			_option = chart3.getOption(),
			_timeline = _option.timeline[0],	//时间轴
			_chooseValue = _timeline.data[_curIndex].value;	//当前选择的时间值
	 		
		chart3.showLoading();	//显示‘加载’
		//获取点击年月的各楼栋宿舍违章嫌疑度
		$.ajax({
			type: 'get',
			url: '/electricalBehavior/getDormHighPower',
			data: {
				date : _chooseValue
			},
			dataType: 'json',
			success: function(result){
				var data = result.object;
				if(data){
					var series = [];
					series.push({
						type: 'scatter',
						data: data
					});
					chart3.setOption({
						series: series
					});
				}else{
					chart3.setOption(option, true);
					chart3.setOption({baseOption: {timeline: _timeline}});
				}
				chart3.hideLoading();
			}
		});
	});
	chart3.setOption(option);
}

//提示框
var tip = {
	trigger: 'item',
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
}

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
}

/*未关电器情况分布*/
var chart4;
function drawChart4(){
	chart4 = echarts.init(document.getElementById("chart4"));
	
	var option2 = {
	    baseOption: {
	        timeline: {
	            width: '55%',
	            left: 'center',
	            bottom: -10,
	            axisType: 'category',
	            label: {	//时间轴标签
	                color: '#668db6',
	                position: 8
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
	        	trigger: tip.trigger,
	        	borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position,
	        	formatter: function(params){
	            	return "<div style='width:100px'>" +
	            				"<p style='border-bottom:1px solid #ddd;'>" +
	            					"<span style='width:70%;float:left;'>"+params.name+"</span>" +
	            					"<span style='width:30%;text-align:left;'float:right;>"+
	            						"未关"+"</span>" +
	            				"</p>" +
	            				"<p><span style='width:70%;float:left;'>时间</span>" +
	            					"<span style='width:30%;text-align:left;'float:right;>"+
	            						params.value[1]+"</span>" +
	            				"</p>" +
	            				"<p><span style='width:70%;float:left;'>次数</span>" +
	            					"<span style='width:30%;text-align:left;'float:right;>"+
	            						params.value[0]+"</span>" +
	            				"</p>" +	            			
		        			"</div>";
	            }	
	        },
	        toolbox: toolbox,
	        series: [
	            {
	                type: 'pie',
	                radius: [0, '15%'],
	                center: ['50%', '50%'],
	                z: 1,
	                itemStyle: {
	                    normal: {
	                        color: '#2292ba',
	                    }
	                },
	                labelLine: {
	                    normal: {
	                        show: false
	                    }
	                },
	                silent: true
	            },
	            {
	                type: 'pie',
	                radius: ['8%', '9%'],
	                center: ['50%', '50%'],
	                z: 1,
	                itemStyle: {
	                    normal: {
	                        color: 'white',
	                    }
	                },
	                labelLine: {
	                    normal: {
	                        show: false
	                    }
	                },
	                silent: true
	            },
	            {
	                type: 'pie',
	                radius: ['15%', '70%'],
	                center: ['50%', '50%'],
	                roseType: 'area',
	                z: 2,
	                label: {
	                    normal: {show: true},
	                    emphasis: {show: true}
	                },
	                labelLine: {
	                    normal: {
	                        show: true,
	                        length: 0,
	                        length2: 0
	                    }
	                }
	            },
	            {
	                type: 'pie',
	                radius: ['15%', '70%'],
	                center: ['50%', '50%'],
	                roseType: 'area',
	                z: 2,
	                label: {
	                    normal: {
	                        show: true,
	                        position: 'inside',
	                        formatter: '{d}%'
	                    },
	                    emphasis: {show: true}
	                }
	            }
	        ]
	    }
	}
	chart4.setOption(option2);

	//各项的颜色
	var color = ['#b4c35c', '#877739', '#c4b574', '#b39d48', '#dcb685', '#cb945e', '#ac683b', '#be715f', '#c87773', '#a84b53',
	             '#cd636d', '#106d99', '#65b55c'];
	
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart4.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择下标
			_option = chart4.getOption(),
			_timeline = _option.timeline[0],	//时间轴
			_chooseValue = _timeline.data[_curIndex].value;	//当前选择的时间值

		chart4.showLoading();	//显示‘加载’
		//获取点击年的各学院贫困生人数分布
		$.ajax({
			type: 'get',
			url: '/electricalBehavior/getUnclosedRank',
			data: {
				date: _chooseValue
			},
			dataType: 'json',
			success: function(result){
				var data = result.object;	//数据
				if(data){	//数据不为空
					//为每一个数据项配置相应的颜色
					echarts.util.each(data, function(item, index) {
					    item.itemStyle = {
					        normal: {
					            color: color[index]
					        }
					    };
					});
					//与时间轴长度相同的options
					var options = [{series: [{data: []},{data: []},{data: []},{data: []},]},
					   			{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]},
								{series: [{data: []},{data: []},{data: []},{data: []},]} ];
					//设置当前选择的时间的series
					options[_curIndex] = {
				    	series: [
			    	         {data:[100]},{data:[100]},
			    	         {data:data},{data:data}
				    	]
				    }
					chart4.setOption({
						options: options
					});
				}else{	//数据为空时
					chart4.setOption(option2, true);
					chart4.setOption({baseOption: {timeline: _timeline}});
				}
				chart4.hideLoading();
			}
		});
	});
	//默认点击最后一个时间点
//	chart4.dispatchAction({type: 'timelineChange', currentIndex: 4})
}

