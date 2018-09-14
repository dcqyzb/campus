/**
 * 
 */

var EffectChart = [];	//存放有动画效果的图实例对象，父页面获取
$(function(){
	var $parentIframe = $(window.parent.document.getElementById("contentFrame"));
	$("body").css("height", $parentIframe.height());

	drawChart1();
	drawChart2();
	drawChart3();
	drawChart4();
	
	//时间轴选择
	var $time1_li = $("#time1 li");
	$time1_li.on("click", function(){
		changeTimeline(this, chart2);
	});
	$time1_li.eq(0).click();
	
});

//工具栏配置
var toolbox = {
    show: true,
    feature: {
        magicType: {
            type: ['bar', 'line']
        },
    	saveAsImage: {}
    },
    iconStyle: {
        borderColor: '#1a6a9b'
    },
    top: '0%',
    right: '5%'     
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

var timeline = {
	    axisType: 'category',
	    width: '50%',
	    left: 'center',
	    symbol: 'circle',
	    label: {	//时间轴标签
	        position: 8,
	        color: '#668db6',
	        formatter : function(s) {
	            return (new Date(s)).getFullYear()+"年";
	        }
	    },	    
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
	    },
	    data: [{value:'2013'},{value:'2014'},{value:'2015'},{value:'2016'},{value:'2017'}]
}

function drawChart1(){
    $.get("../plugins/echarts4/map/gz.json",function(map){
        var chart1 = echarts.init(document.getElementById('chart1'));
        EffectChart.push(chart1);
        
        echarts.registerMap('gz',map);
        
        var option = {
        	baseOption:{
        		//图例组件。
           	    legend: {
           	    	right:'30%',
           	    	bottom:'15%',
           	    	orient: 'vertical',
           	    	itemWidth:20,
           	    	itemHeight:20,
                    textStyle: {
                        color: '#fff'
                    },
           	    	data: [{
           	    	    name: '无线AP',
           	    	    icon: 'image://../image/ap.png'
           	    	}]
                },
                //提示框组件。
                tooltip:{
                },
                //地理坐标系组件。
                geo: {
                	map: 'gz',
                    left: '8%',
                    aspectScale: 1.1,//长宽比，需同时调整上面同一参数
                    zoom: 1.2,//缩放，需同时调整上面同一参数
                    label: {
                    	show: true
                    },
                    itemStyle:{
                    	areaColor:'#0e4267',
                    	borderColor:'#3dbacc',
                	    shadowColor: '#1be0f4',
                	    shadowBlur: 2
                    },
                    emphasis:{
                    	label:{
                    		show:false
                    	},
                    	itemStyle:{
                    		areaColor:'transparent'
                    	}
                    },
	                silent: true
                },
                //时间轴组件。
                timeline: {
    	            width: '40%',
    	            right: '2%',
    	            bottom: '5%',
    	            axisType: 'category',
    	            data: mGetTimelineData("month"),	//获取时间轴数据
    	            label: {	//时间轴标签
    	                color: '#668db6',
    	                position: 8,
    	                formatter : function(s) {
    	                	var yyyyMM = s.split("-"), m = parseInt(yyyyMM[1]);
							//if(m == 1){
							//	text = m + "月\n" + yyyyMM[0];
							//}else{
								text = m + "月";
							//}
		                    return text;
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
    	        }
        	},
        	options:[]		
        };
        chart1.setOption(option);
        
        var curYear=new Date().getFullYear();
    	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
        chart1.on('timelinechanged', function(param){
    		var _curIndex = param.currentIndex,	//当前选择时间的下标
    			option = chart1.getOption(),	
    			_chooseValue = option.timeline[0].data[_curIndex].value,//选择的时间值
    			length = option.timeline[0].data.length;	
    		
    		chart1.showLoading();	//显示‘加载’
    		
    			$.ajax({
    				type: 'post',
    				url: '/netWork/getFlowDivice',
    				data: {
    					"month": _chooseValue
    				},
    				dataType: 'json',
    				success: function(result){
    					var res = result.object;	//数据
    					if(res){	//数据不为空    	
    						var series = [];
    		    			$.each(res, function(i, item){
    		    				series.push({
    		    					type: 'effectScatter',
    		    					name: '无线AP',
    		    					showEffectOn: 'render',
    		    					coordinateSystem: 'geo',
    		    					rippleEffect:{
    		    						scale:item[3]/200,
    		    						brushType:'stroke'
    		    					},
    		    					symbolSize:30,
    		    					itemStyle: {
    		    		                normal: {
    		    		                    color: '#f4e925',
    		    		                    shadowBlur: 10,
    		    		                    shadowColor: '#333'
    		    		                }
    		    		            },
    		    					data:[item]
    		    				});
    		    			});
    		    			
    		    			series.push({
		                        type: 'scatter',
		                        name: '无线AP',
		                        coordinateSystem: 'geo',
		                        symbol:'image://../image/ap.png',
		    					symbolSize:30,
		                        data: res,
		                        tooltip:{
		                        	trigger: 'item',
		            	            borderWidth: tip.borderWidth,
		            	            borderColor: tip.borderColor,
		            	            position: tip.position,
		            				formatter: function(params) {
		            					return "<div style='width:280px'>" +
					    				"<p><span style='width:55%;float:left;'>设备编号："+
					    						params.data[2]+"</span>" +
											"<span style='width:45%;text-align:left;'float:right;>终端连接数："+
												params.data[3]+"</span>" +
										"</p>" +
										"<p><span style='width:55%;float:left;'>安装时间："+
												params.data[4]+"</span>" +
											"<span style='width:45%;text-align:left;'float:right;>年限："+
												params.data[5]+"</span>" +
										"</p>" +
										"<p><span style='width:55%;float:left;'>当前宽带：" +
												params.data[6]+"</span>" +
											"<span style='width:45%;text-align:left;'float:right;>宽带使用率："+
												params.data[7]+"</span>" +
										"</p>" +
										"<p><span style='width:100%;float:left;'>宽带消耗：" +
												params.data[8]+"</span>" +
										"</p>" +
									"</div>";
		            		        }
		                        }   		    			
		                    });
				    		//与时间轴长度相同的options
							var options=[];
							for(var i=0;i<length;i++){
								options[i] = {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},
								                       {data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},
								                       {data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]};
							}
							//设置当前选择的时间的series
							options[_curIndex] = {
							    	series: series
							}
							chart1.setOption({
						        options:options
						    });				
    					}   					
    					chart1.hideLoading();
    				}
    			});
    	});
    	//默认点击最后一个时间点
    	chart1.dispatchAction({type: 'timelineChange', currentIndex: 11})
    });
}

/*总体宽带趋势折线图*/
var chart2;
function drawChart2(){
	chart2 = echarts.init(document.getElementById('chart2'));
	
	var option = {
			baseOption:{
				legend:{
					right:0,
					bottom: '35%',
					orient:'vertical',
					itemGap:4,
					itemWidth:8,
					itemHeight:4,
					textStyle: {
				        color: '#b2c0c3'   
				    },
					data: [
					       {name: '无线',icon: 'rect'},
					       {name: '有线',icon: 'rect'}
					]
				},
				grid: {
		            left: '1%',
		            top: '15%',
		            right:'15%',
		            bottom: '20%',
		            containLabel: true
		        },
				xAxis: {
					type : 'category', 
					boundaryGap:false,
					nameTextStyle:{
			    		 color: '#6f9ed6',
			    		 padding: [25, 0, 0, 0]
			    	},
					axisLine: {     
						lineStyle: {    
							color: '#2d556f'
	                    }
		            },
		            axisTick:{
		            	show:false
		            },
		            axisLabel: {  
		            	color: '#6f9ed6'  
		            },
	 	            splitLine: {
	 	            	show:true,
	 	            	lineStyle: {    
	 	            		color: '#2d556f',
	                        opacity:0.5
	                    }
	                }
			    },
			    yAxis: {
			    	 name: 'M',
			    	 type: 'value', 
			    	 nameTextStyle:{
			    		 color: '#6f9ed6',
			    		 padding: [0, 45, 0, 0]
			    	 },
			    	 splitNumber:8,
					 axisLine: {     
						 lineStyle: {    
								color: '#2d556f'
		                 }
		             },
		             axisTick:{
		            	 show:false
		             },
		             axisLabel: {  
		            	 color: '#6f9ed6'  
		             },
	 	             splitLine: {    
	 	            	 show:true,
	 	            	 lineStyle: {    
	 	            		color: '#2d556f',
	                        opacity:0.5
	                     }
	                 }
			    },
			    toolbox:toolbox,
			    timeline:timeline,
			    tooltip: {
		        	trigger: 'axis',
		            borderWidth: tip.borderWidth,
		            borderColor: tip.borderColor,
		            position: tip.position,
		            formatter: function(params){
	            		return params[0].seriesName+"："+params[0].value+"M<br/>"
	            		+params[1].seriesName+"："+params[1].value+"M";
	            	}	
		        },
		        series: [
		            {
				    	 name: '无线',
					        type:'line',
				            smooth:true,
				            symbol: 'none',
				            lineStyle:{
				            	color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
			                        offset: 0,
			                        color: 'rgb(70,255,131)'
			                    }, {
			                        offset: 1,
			                        color: 'rgb(255, 70, 131)'
			                    }])
				            },
				            areaStyle: {
				                normal: {
				                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
				                        offset: 0,
				                        color: 'rgb(70,255,131)'
				                    }, {
				                        offset: 1,
				                        color: 'rgb(255, 70, 131)'
				                    }]),
				                    opacity:0.5
				                }
				            }
				    },
				    {
				    	 name: '有线',
					        type:'line',
				            smooth:true,
				            symbol: 'none',
				            lineStyle:{
				            	color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
			                        offset: 0,
			                        color: 'rgb(65,140,169)'
			                    }, {
			                        offset: 1,
			                        color: 'rgb(62, 116, 154)'
			                    }])
				            },
				            areaStyle: {
				                normal: {
				                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
				                        offset: 0,
				                        color: 'rgb(65,140,169)'
				                    }, {
				                        offset: 1,
				                        color: 'rgb(62, 116, 154)'
				                    }]),
				                    opacity:0.5
				                }
				            },
				            zlevel:1
				    }
		        ]
			},
			options:[]		    
	};
	chart2.setOption(option);
	
    //时间切换
	chart2.on("timelinechanged", function(param){   	
    	chart2.showLoading();  	
    	var _curIndex = param.currentIndex,
			_option = chart2.getOption(),
			_timeline = _option.timeline[0],
			_timelength = _timeline.data.length,
			_chooseValue = _timeline.data[_curIndex].value;
    	
    	$.ajax({
    		type: 'post',
    		url: '/netWork/getBandWidthTency',
    		data: {
    			college:'全校',
    			time: _chooseValue
    		},
    		dataType: 'json',
    		success: function(result){
    			var res = result.object;
    			if(res){
					var options = [];
					for(var i=0; i<_timelength; i++){
						options[i] = {series: [{data: []},{data: []}]};
					}
					options[_curIndex] = {
							series: [{data: res[0][1]},{data: res[1][1]}]
				    }
					chart2.setOption({
						baseOption:{
							xAxis: {
					            data: res[0][0]
					        }
						},
						options: options
					});
    			}else{
    				chart2.setOption(option, true);
    				chart2.setOption({baseOption: {timeline: _timeline}});
    			}
    			chart2.hideLoading();
    		}
    	});
    });
	//默认点击最后一个时间点
	chart2.dispatchAction({type: 'timelineChange', currentIndex: 4})
}

/*网络行为与成绩饼图+折线图*/
function drawChart3(){
	var chart3 = echarts.init(document.getElementById('chart3'));

	var option = {
			baseOption:{
				color:['#CD617B','#67D1D1','#BBB85B','#5A8EE4'],
		        graphic:{
		        	elements: [
        	           {
			            type:'text',
			            left:'15.5%',
			            top:'20.5%',
			            style:{
			                text:'上网\n时长',
			                font: '14px "Microsoft YaHei"',
			                textAlign:'center',
			                fill:'white',
			                width:30,
			                height:30
			            }
        	           },
        	           {
				            type:'text',
				            left:'center',
				            top:'20.5%',
				            style:{
				                text:'上网\n次数',
				                font: '14px "Microsoft YaHei"',
				                textAlign:'center',
				                fill:'white',
				                width:30,
				                height:30
				            }
	        	           },
        	           {
				            type:'text',
				            left:'79.5%',
				            top:'20.5%',
				            style:{
				                text:'流量\n分布',
				                font: '14px "Microsoft YaHei"',
				                textAlign:'center',
				                fill:'white',
				                width:30,
				                height:30
				            }
	        	           }]
		        },		        
		        xAxis: {
			    	 nameTextStyle:{
			    		 color: '#6f9ed6'
			    	 },
			    	 splitNumber:10,
					 axisLine: {     
						 lineStyle: {    
							color: '#2d556f'
	                     }
		             },
		             axisTick:{
		            	 show:false
		             },
		             axisLabel: {  
		                 color: '#6f9ed6'  
		             },
	 	             splitLine: {    
		                 show: false
	                 }
		        },
		        yAxis: {
		        	name: '分',
		            type: 'category',
					nameTextStyle:{
			    		 color: '#6f9ed6',
			    		 padding: [0, 45, 0, 0]
			    	 },
		        	boundaryGap:false,
					axisLine: {     
						lineStyle: {    
							color: '#2d556f'
	                    }
		            },
		            axisTick:{
		            	show:false
		            },
		            axisLabel: {  
		                color: '#6f9ed6'  
		            },
	 	            splitLine: {
	 	            	 show: false
	                },
	                data:['<60','60-69','70-79','80-89','90-99','100']
		        },
		        grid: {
		        	top: '50%',
		        	left:'15%'
		        		},
		        toolbox:toolbox,	
		        tooltip:{},
			    timeline:{
			    	axisType: 'category',
				    width: '90%',
				    left: 'center',
				    bottom:'-5%',
				    symbol: 'circle',
				    label: {	//时间轴标签
				        position: 8,
				        color: '#668db6'
				    },	    
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
				    },
				    data: mGetTimelineData("term")
			    },
		        series: [
		            {
		                type: 'pie',
		                id: 'pie1',
		                radius: ['15%', '25%'],
		                center: ['18%', '25%'],
		                avoidLabelOverlap: true,
		                label: {
		                    formatter: [
		                        '{a|{b}}',
		                        '{b|{d}%}'
		                    ].join('\n'),

		                    rich: {
		                        a: {
		                            color: 'white',
		                            align: 'right'
		                        },
		                        b: {
		                        	color:'white',
		                        	align: 'right',
		                            backgroundColor: '#2779A9'
		                        }
		                    }
		                },
		                labelLine: {
		                    normal: {
		                        show: false,
		                        length:5,
		                        length2:0		                        
		                    }
		                }
		            },
		            {
		                type: 'pie',
		                id: 'pie2',
		                radius: ['15%', '25%'],
		                center: ['50%', '25%'],
		                avoidLabelOverlap: true,
		                label: {
		                    formatter: [
		                        '{a|{b}}',
		                        '{b|{d}%}'
		                    ].join('\n'),

		                    rich: {
		                        a: {
		                            color: 'white',
		                            align: 'right'
		                        },
		                        b: {
		                        	color:'white',
		                        	align: 'right',
		                            backgroundColor: '#2779A9'
		                        }
		                    }
		                },
		                labelLine: {
		                    normal: {
		                        show: false,
		                        length:5,
		                        length2:0		                        
		                    }
		                }
		            },
		            {
		                type: 'pie',
		                id: 'pie3',
		                radius: ['15%', '25%'],
		                center: ['82%', '25%'],
		                avoidLabelOverlap: true,
		                label: {
		                    formatter: [
		                        '{a|{b}}',
		                        '{b|{d}%}'
		                    ].join('\n'),

		                    rich: {
		                        a: {
		                            color: 'white',
		                            align: 'right'
		                        },
		                        b: {
		                        	color:'white',
		                        	align: 'right',
		                            backgroundColor: '#2779A9'
		                        }
		                    }
		                },
		                labelLine: {
		                    normal: {
		                        show: false,
		                        length:5,
		                        length2:0		                        
		                    }
		                }
		            },		            
		            {
		            	type: 'line', 
		            	smooth: true, 
		            	lineStyle:{
			            	color: 'rgb(65,140,169)'
			            },
			            itemStyle:{
			            	color: 'rgb(62, 116, 154)',
			            	opacity:0
			            },
			            areaStyle: {
				            color: {
				                type: 'linear',
				                x: 0,
				                y: 0,
				                x2: 0,
				                y2: 1,
				                colorStops: [{
				                    offset: 0, color: 'rgb(102, 255, 255)' // 0% 处的颜色
				                }, {
				                    offset: 1, color: 'rgb(74, 100, 149)' // 100% 处的颜色
				                }],
				                globalCoord: false // 缺省为 false
				            }
			            },
				        tooltip: {
				        	trigger: 'item',
				            borderWidth: tip.borderWidth,
				            borderColor: tip.borderColor,
				            position: tip.position,
				            formatter: function(params){
			            		return "成绩 "+params.data[1]+"分<br/>上网平均时长 "+params.data[0]+'h';
			            	}
				        },
		            }
		        ]
			},
			options:[]		
	    };
		
	chart3.setOption(option);
	
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart3.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			option = chart3.getOption(),	
			_chooseValue = option.timeline[0].data[_curIndex].value,//选择的时间值
			length = option.timeline[0].data.length;	
		
		var schoolYear=_chooseValue.substring(0,4);
		var term=_chooseValue.substring(4);
			chart3.showLoading();	//显示‘加载’
		
			$.ajax({
				type: 'post',
				url: '/netWork/getStatsByTerms',
				data: {
					"school":"全校",
		    		"schoolYear":schoolYear,
		    		"term":term
				},
				dataType: 'json',
				success: function(result){
					var res = result.object;	//数据
					if(res){	//数据不为空
						$.ajax({
					    	type: 'post',
					    	url: '/netWork/getGradeByTerms',
					    	dataType: 'json',
					    	data:{
					    		"school":"全校",
					    		"schoolYear":schoolYear,
					    		"term":term
					    	},
					    	success: function(result1){
					    		var res1 = result1.object;
					    		//与时间轴长度相同的options
								var options=[];
								for(var i=0;i<length;i++){
									options[i]={series: [{data: []},{data: []},{data: []},{data: []}]};
								}
								//设置当前选择的时间的series
								options[_curIndex] = {
			    				    	series: [
			    			    	         {data:res[0]},{data:res[1]},{data:res[2]},{data:res1}
			    				    	]
			    				}
								
								chart3.setOption({
							        options:options
							    });
					    	}
					    });					
					}
					chart3.hideLoading();
				}
			});
	});
	//默认点击最后一个时间点
	chart3.dispatchAction({type: 'timelineChange', currentIndex: 9})
}

/*沉迷网络关键词云*/
function drawChart4(){
	var chart4 = echarts.init(document.getElementById('chart4'));
	var color = ['#84e268', '#dd9b45', '#21b3da', '#edfae6', '#e27270'];
	chart4.setOption({
		tooltip: {
    		borderWidth: tip.borderWidth,
    	    borderColor: tip.borderColor,
    	    position: tip.position,
    		formatter: function(params){
    			return "占比 "+params.data.tip+"<br/>时长 "+params.data.value+"小时";
    		}
    	},
        series: [
            {
                type: 'wordCloud',
                shape: 'rectangle',
                sizeRange: [12, 24],
                rotationRange: [-90,90],
                gridSize: 18, 
                bottom: 0,
                width: '90%',
                textStyle: {
                    normal: {
                    	color: function() {
                            return color[Math.round(Math.random() * 4)];
                        }
                    },
                    emphasis: {
                        shadowBlur: 10,
                        shadowColor: '#333'
                    }
                },
                data: []
            }]
	    });
	
	 chart4.showLoading();
	    $.ajax({
	    	type: 'post',
	    	url: '/netWork/getNetWords',
	    	dataType: 'json',
	    	success: function(result){
	    		var data = result.object;
	    		if(data){
	    			chart4.setOption({
	    				series: [{
	    					data: data
	    				}]
	    			});
	    		}
	    		chart4.hideLoading();
	    	}
	    });
}