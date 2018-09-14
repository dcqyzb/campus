/**
 * 精准资助js
 */

var EffectChart = [];	//存放有动画效果的图实例对象，父页面获取
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
	$tableAll.css("height", ($tableAll.parent().parent().height()*0.75)+"px");
	$tableAll.find(".table_content").css("height", ($tableAll.height()-30)+"px");
	
	if(window.screen.width < 1700){
		$(".time").css("left", '3%');
	}
	
	drawChart1();
	drawChart2();
	drawChart3();
	drawChart4();
	drawChart5();

	//获取下阶段贫困生
	$.ajax({
		type: 'get',
		url: '/preciseFunding/getPoorForecast',
		url: '/poverty/getPoorForecast',
		dataType: 'json',
		success: function(result){
			var data = result.object;
			if(data){
				var html = "";
				$.each(data, function(i, item){
					html += "<tr>" +
							"<td width='22%' title='"+item.major+"'>"+item.major+"</td>" +
							"<td width='23%' title='"+item.colleague+"'>"+item.colleague+"</td>" +
							"<td width='22%'>"+item.total+"</td>" +
							"<td width='10%'>"+item.poorest+"</td>" +
							"<td width='10%'>"+item.medium+"</td>" +
							"<td width='13%'>"+item.normal+"</td>" +
							"</tr>";
				});
				$(".table_content tbody").html(html);	
			}
		}
	});
	
	//时间轴选择
	var $time1_li = $("#time1 li"), $time2_li = $("#time2 li");
	$time1_li.on("click", function(){
		changeTimeline(this, chart3);
	});
	$time2_li.on("click", function(){
		changeTimeline(this, chart4);
	});
	$time1_li.eq(0).click();
	$time2_li.eq(1).click();
	
	
	
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

/*各学院贫困生人数分布*/
function drawChart1(){
	var chart1 = echarts.init(document.getElementById("chart1"));
	
	var option2 = {
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
	        	trigger: tip.trigger,
	        	borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position,
	        	formatter: function(params){
	            	return "<div style='width:100px'>" +
	            				"<p style='border-bottom:1px solid #ddd;'>" +
	            					"<span style='width:70%;float:left;'>"+params.name+"</span>" +
	            					"<span style='width:30%;text-align:left;'float:right;>"+
	            						params.value[0]+"人</span>" +
	            				"</p>" +
	            				"<p><span style='width:70%;float:left;'>一般</span>" +
	            					"<span style='width:30%;text-align:left;'float:right;>"+
	            						params.value[3]+"人</span>" +
	            				"</p>" +
	            				"<p><span style='width:70%;float:left;'>困难</span>" +
	            					"<span style='width:30%;text-align:left;'float:right;>"+
	            						params.value[2]+"人</span>" +
	            				"</p>" +
	            				"<p><span style='width:70%;float:left;'>特困</span>" +
		        					"<span style='width:30%;text-align:left;float:right;'>"+
		        						params.value[1]+"人</span>" +
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
	                name: '贫困生人数分布',
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
	                name: '贫困生人数分布',
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
	chart1.setOption(option2);

	//各项的颜色
	var color = ['#b4c35c', '#877739', '#c4b574', '#b39d48', '#dcb685', '#cb945e', '#ac683b', '#be715f', '#c87773', '#a84b53',
	             '#cd636d', '#106d99', '#65b55c'];
	
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart1.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			option = chart1.getOption(),	
			_chooseYear = option.timeline[0].data[_curIndex].value;	//选择的时间值
		
		chart1.showLoading();	//显示‘加载’
		//获取点击年的各学院贫困生人数分布
		$.ajax({
			type: 'get',
			url: '/poverty/getPoorNumber',
			data: {
				year: _chooseYear
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
								{series: [{data: []},{data: []},{data: []},{data: []},]} ];
					//设置当前选择的时间的series
					options[_curIndex] = {
				    	series: [
			    	         {data:[100]},{data:[100]},
			    	         {data:data},{data:data}
				    	]
				    }
					chart1.setOption({
						options: options
					});
				}
				chart1.hideLoading();
			}
		});
	});
	//默认点击最后一个时间点
	chart1.dispatchAction({type: 'timelineChange', currentIndex: 4})
}

/*各地区贫困生生源地分布*/
var chart2;
function drawChart2(){
	chart2 = echarts.init(document.getElementById("chart2"));
	EffectChart.push(chart2);
	//注册地图
	$.get("../plugins/echarts4/map/china.json", function(chinaJson){
		echarts.registerMap('china', chinaJson) //注册
        //各省地理位置坐标信息，可做调整
        var geoCoordMap = {
            '北京': [116.46, 39.92],
            '上海': [121.48, 31.22],
            '天津': [117.2, 39.13],
            '重庆': [106.54, 29.59],
            '香港': [114.1, 22.38],
            '澳门': [113.58, 22.15],
            '河北': [114.336873, 38.21885],
            '河南': [113.453802, 34.895028],
            '云南': [102.599397, 25.248948],
            '辽宁': [123.241164, 41.948112],
            '黑龙江': [126.479088, 45.985284],
            '湖南': [112.800698, 28.474291],
            '安徽': [117.170056, 31.99595],
            '山东': [116.912494, 36.812038],
            '新疆': [87.476819, 43.894927],
            '江苏': [118.715429, 32.246466],
            '浙江': [120.040035, 30.350837],
            '江西': [115.808656, 28.774611],
            '湖北': [114.116105, 30.764814],
            '广西': [108.265765, 23.020403],
            '甘肃': [103.66644, 36.218003],
            '山西': [112.349964, 38.044464],
            '内蒙古': [111.614073, 40.951504],
            '陕西': [108.780889, 34.408508],
            '吉林': [126.57, 43.87],
            '福建': [119.156964, 26.182279],
            '贵州': [106.499624, 26.844365],
            '广东': [113.233035, 23.224606],
            '青海': [101.605943, 36.752842],
            '西藏': [90.972306, 29.838888],
            '四川': [103.924003, 30.796585],
            '宁夏': [106.094884, 38.624116],
            '海南': [110.179083, 19.921006],
            '台湾': [121.36464, 25.248948]
        };
		
        //转换贫困生数据至echarts散点图的标准data格式
        var convertData = function(data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var geoCoord = geoCoordMap[data[i].name];   //获取某省的地理坐标
                if (geoCoord) {
                    res.push({
                        name: data[i].name,
                        value: geoCoord.concat(data[i].value)   //长度为3的数组，经纬度+值（贫困生人数）
                    });
                }
            }
            return res;
        };

        var option = {
            title: {
                show: false
            },
            tooltip: {
            	trigger: tip.trigger,
            	borderWidth: tip.borderWidth,
                borderColor: tip.borderColor,
                position: tip.position,
                formatter: function (params) {
                	/*与ul联动*/
                	var $map_ul = $(".map_list ul"),
                		$li_cur = $map_ul.find("li[data-name='"+params.name+"']"),
                		_index = $li_cur.index();
                	$li_cur.addClass("hover").siblings().removeClass("hover");
                	$map_ul.scrollTop((_index-4)*21);
                	
                    return "<div style='width:110px'>" +
			    				"<p style='border-bottom:1px solid #ddd;'>" +
			    					"<span style='width:60%;float:left;'>"+params.name+"省</span>" +
									"<span style='width:40%;text-align:left;'float:right;>"+
										params.value[2]+"人</span>" +
								"</p>" +
								"<p><span style='width:60%;float:left;'>一般</span>" +
									"<span style='width:40%;text-align:left;'float:right;>"+
										params.value[5]+"人</span>" +
								"</p>" +
								"<p><span style='width:60%;float:left;'>困难</span>" +
									"<span style='width:40%;text-align:left;'float:right;>"+
										params.value[4]+"人</span>" +
								"</p>" +
								"<p><span style='width:60%;float:left;'>特困</span>" +
									"<span style='width:40%;text-align:left;float:right;'>"+
										params.value[3]+"人</span>" +
								"</p>" +
							"</div>";;
                }
            },
            geo: {  //使用地理坐标系
                map: 'china',
                left: '5%',
                right: '15%',
                top: 0,
                bottom: 0,
                label: {
                    emphasis: {
                        show: false
                    }
                },
                //roam: true, //地图缩放
                itemStyle: {    //数据区域样式
                    normal: {
                        areaColor: '#0e4267',   //区域颜色
                        borderColor: '#3dbacc',  //区域边框
                        shadowColor: '#1be0f4',
                        shadowBlur: 2
                    },
                    emphasis: {
                        areaColor: '#00a9e0'
                    }
                },
                regions: [{	//对一些省自治区做样式调整
                	name: '内蒙古',
                	itemStyle: {
                		areaColor: '#0e4267',
                		borderColor: '#3dbacc',
                		borderWidth: 1,
            			shadowColor: '#125b92',
            			shadowBlur: 50,
                	}
                },
                {
                	name: '甘肃',
                	itemStyle: {
                		areaColor: '#0e4267',
                		borderColor: '#3dbacc',
                		borderWidth: 1,
            			shadowColor: '#125b92',
            			shadowBlur: 50,
            			shadowOffsetY: -20
                	}
                },
                {
                	name: '辽宁',
                	itemStyle: {
                		areaColor: '#0e4267',
                		borderColor: '#3dbacc',
                		borderWidth: 1,
            			shadowColor: '#125b92',
            			shadowBlur: 50,
            			shadowOffsetY: 20
                	}
                },
                {
                	name: '山东',
                	itemStyle: {
                		areaColor: '#0e4267',
                		borderColor: '#3dbacc',
                		borderWidth: 1,
            			shadowColor: '#125b92',
            			shadowBlur: 50,
            			shadowOffsetX: 20
                	}
                },
                {
                	name: '江苏',
                	itemStyle: {
                		areaColor: '#0e4267',
                		borderColor: '#3dbacc',
                		borderWidth: 1,
            			shadowColor: '#125b92',
            			shadowBlur: 50,
            			shadowOffsetX: 30
                	}
                }]
            },
            series: [
                {
                    name: '全国贫困生生源地分布',
                    type: 'effectScatter',      //散点图系列
                    coordinateSystem: 'geo',    //参考地理坐标系
                    rippleEffect: {
                    	//period: 0,
                        brushType: 'stroke'     //涟漪特效
                    },
                    //symbol: 'arrow',          //散点图形
                    symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[2] / 100;
                    },
                    itemStyle: {    //散点的样式
                        normal: {
                            color: 'gold'   //颜色
                        }
                    },
                    //data: convertData(data) //数据
                },
            ]
        };
        chart2.setOption(option);
        
        chart2.showLoading();
        //获取各地区贫困生分布
        $.ajax({
        	type: 'get',
			url: '/poverty/getPoorArea',
			dataType: 'json',
			success: function(result){
				var data = result.object;
				if(data){
					chart2.setOption({
			        	series: [{data: convertData(data)}]
			        });
				
					/*将各省贫困生人数写入到地图右侧列表*/
					//对数据进行值排序
					for(var i=1, len=data.length; i<len; i++){
			        	for(var j=i; j>0; j--){
			        		if(data[j].value[0] > data[j-1].value[0]){
			        			var tmp = data[j];
			        			data[j] = data[j-1];
			        			data[j-1] = tmp;
			        		}
			        	}
			        }
			        var $map_ul = $(".map_list ul"), html = "";
			        $.each(data, function(i, item){
			            html += "<li data-name='"+item.name+"'>" +
			            			"<span>"+item.name+"</span>" +
			            			"<span>"+item.value[0]+"人</span>" +
			            		"</li>";
			        });
			        $map_ul.html(html);
			        
			        //点击列表项与地图联动
			        $map_ul.find("li").on("click", function(){
			        	var name = $(this).attr("data-name");	//获取所点击项的省名称
			        	chart2.dispatchAction({	//触发chart2的地图选择事件
			        		type: 'geoSelect',
			        		name: name	//指定省
			        	});
			        	chart2.dispatchAction({	//触发chart2的显示提示框事件
			        		type: 'showTip',
			        		seriesIndex: 0,
			        		name: name
			        	});
			        });
				}
				chart2.hideLoading();
			}
        });
        
        /*绑定地图选择事件，只选择一个数据项*/
        chart2.on("geoSelected", function(params){
            var arr = [];
        	$.each(params.selected, function(name, value){
        		if(value && name !== params.name){
        			arr.push(name);
        		}
        	});
        	chart2.dispatchAction({
        		type: 'geoUnSelect',
        		name: arr
        	});
        });
	});
}

/*学院平均消费*/
var chart3;
function drawChart3(){
	chart3 = echarts.init(document.getElementById("chart3"));    //初始化实例
	
    var option = {
    	baseOption: {
    		timeline: {
    			width: '70%',
    			left: '18%',
	            bottom: '2%',
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
	        tooltip: {  //提示框
	            trigger: 'axis',    //坐标轴触发
	        	borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position
	        },
	        toolbox: toolbox,
	        grid : {
	        	left: '15%',
	        	right: '5%',
	        	bottom: '30%'
	        },
	        xAxis : [   //x轴
	            {
	                type : 'category',  //轴类型，类目型
	                //name: '学院',       //显示的轴名称
	                axisLine: {         //轴线
	                    lineStyle: {    //轴线样式
	                        color: '#2d556f'
	                    }
	                },
	                axisLabel: {        //轴的刻度标签设置
	                    color: '#6f9ed6'   //文本颜色
	                },
	                //各学院数据，需改进，最好从后台获取
	                data : ['经管学院','体教学院','管理学院','理学院','化学学院','生科学院','食品学院',
	                        '材料学院','环化学院','机电学院','建筑学院','信息学院']
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
	                //splitNumber: 5, //分割段数
	                splitLine: {    //在表格区域的分割线
	                    show: false
	                },
	            }
	        ],
	        series: [
	            {
	                name: '学院消费水平',
	                type: 'bar',    //柱状图
	                barWidth: 15,
	                itemStyle: {    //图形样式
	                    normal: {
	                        color: new echarts.graphic.LinearGradient(  //颜色设置渐变
	                            0, 0, 0, 1,
	                            [
	                                {offset: 0, color: '#64d5e3'},
	                                {offset: 1, color: '#50a5e5'}
	                            ]
	                        )
	                    }
	                },
	                //data: [10000, 9000, 8600, 16000, 21000, 9200, 15200, 15732, 25000, 15630, 14795, 23450]
	            }
	        ]
    	}
    };
    chart3.setOption(option);
    //绑定时间切换事件
    chart3.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择下标
			option = chart3.getOption(),
			_timeline = option.timeline[0],	//时间轴
			_timelength = _timeline.data.length,	//时间轴数据长度
			_chooseValue = _timeline.data[_curIndex].value;	//当前选择的时间值
		
		chart3.showLoading();
		//获取数据
		$.ajax({
			type: 'get',
			url: '/poverty/getCollegeAverage',
			data: {
				time: _chooseValue
			},
			dataType: 'json',
			success: function(result){
				var data = result.object;
				if(data){
					var options = [];
					for(var i=0; i<_timelength; i++){
						options[i] = {series: [{data: []}]};
					}
					options[_curIndex] = {
				    	series: [{data: data}]
				    }
					chart3.setOption({
						options: options
					});
				}
				chart3.hideLoading();
			}
		});
	});
    //chart3.dispatchAction({type: 'timelineChange', currentIndex: 4})
}

/*异常消费情况*/
var chart4;
function drawChart4(){
	var mychart = echarts.init(document.getElementById("chart4"));    //初始化实例
	chart4 = mychart;    

    //将数据转换为标准格式
    var convertData = function(data){
        var res = [];
        $.each(data, function(i, item){
            $.each(item.value, function(j, n){
                res.push({
                    name: item.name,
                    value: n
                });
            })
        });
        return res;
    };
    //形成折线数据
    var convertToLineData = function(data, name){
        var res = [], tmp = data;
        $.each(tmp, function(i, item){
            $.each(convertData(item.data), function(j, n){
                if(n.name == name){
                    res.push([ n.value[0], n.value[1] ] );
                }
            })
        });
        //排序
        for(var i=1, len=res.length; i<len; i++){
        	for(var j=i; j>0; j--){
        		var m = res[j], n = res[j-1];
        		if(m[0] < n[0]){	
        			res[j] = n;
        			res[j-1] = m;
        			/*var tmp = res[j];
        			res[j] = res[j-1];
        			res[j-1] = tmp;*/
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
        return res;
    };

    var itemStyle = {   //系列的样式
        normal: {
        	//color: 'rgba(0,0,0,0)',
        	//borderWidth: 1,
        	//borderColor: 
            opacity: 0.8,
            shadowBlur: 100,
            shadowColor: '#275f54'
        }
    };
    
    var color = ['#d5ca3c', '#5ab860', '#d54733', '#62b9d5'];
    var option = {
    	baseOption: {
    		timeline: {
    			width: '70%',
    			left: '18%',
	            bottom: '2%',
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
	        color: [        //系列颜色数组，按顺序取颜色，如果未定义，取echarts定义的默认值
	            '#d6cc39', '#5cba64', '#d54733', '#60bad4'
	        ],
	        tooltip: {
	            borderColor: tip.borderColor,
	            borderWidth: tip.borderWidth,
	            position: tip.position,
	            formatter: function (param, ticket, callback) {
	            	/*
	            	 * 获取参数
	            	 */
	            	var _option = mychart.getOption(),
	            		_timeline = _option.timeline[0],
	            		_time = _timeline.data[_timeline.currentIndex].value.toString();	//时间选择值
	            	var name = param.name.split(","), 
	            		stuName = name[0],	//学生姓名 
	            		className = name[1],	//班级信息
	                	value = param.value;	//数组，[横坐标, 消费值, 年增长率, 月增长率]
	            	
	            	/*
	            	 * 生成显示增长率
	            	 */
	            	var rateHtml = "";	//显示增长率文字
	            	//首先判断选择的是年还是月
	            	if(_time.indexOf("-") != -1) {
	            		//选择月
	            		if(value[3] != ""){	//月增长率为空，则不显示，不为空则显示月增长率
	            			//再判断是增长还是下降
	            			rateHtml = (value[3].indexOf("+") != -1) ? 
	            					(_time + " 消费总额<span style='color:#5cba64'>同比增长  " + value[3].split("+")[1]+"</span>") :	//增长
	            						((value[3].indexOf("-") != -1) ? 
	            								(_time + " 消费总额<span style='color:#d54733'>同比下降  " + value[3].split("-")[1]+"</span>") :	//下降
	            									(_time + " 消费总额<span style='color:#5cba64'>同比增长  " + value[3]+"</span>"));
	            			rateHtml += "<br/>";
	            		}
	            	}else{
	            		//选择的是年
	            		if(value[2] != ""){	//年增长率为空，则不显示，不为空则显示年增长率
	            			//再判断是增长还是下降
	            			rateHtml = (value[2].indexOf("+") != -1) ? 
	            					(_time + " 消费总额<span style='color:#5cba64'>同比增长  " + value[2].split("+")[1]+"</span>") :	//增长
	            						((value[2].indexOf("-") != -1) ? 
	            								(_time + " 消费总额<span style='color:#d54733'>同比下降  " + value[2].split("-")[1]+"</span>") :	//下降
	            									(_time + " 消费总额<span style='color:#5cba64'>同比增长  " + value[2]+"</span>"));
	            			rateHtml += "<br/>";
	            		}
	            	}
	            	//返回，“消费类型+消费值+同比增长率+姓名班级”
	            	return param.marker + param.seriesName + "消费： " + value[1] + " 元<br/>" +
	            			rateHtml + 
	            			stuName + ",  " + className;
	            }
	        },
	        toolbox: toolbox,
	        legend: {   //图例组件
	        	right: 0,
	        	bottom: '30%',
	            orient: 'vertical',
	            data: ['水电', '餐饮', '娱乐', '其他'],
	            itemWidth: 8,
	            textStyle: {
	                color: '#b0bfc2',
	                fontSize: 14
	            }
	        },
	        grid: {
	        	right: '15%',
	        	bottom: '30%'
	        },
	        xAxis: {    //x轴
	            type: 'value',  //值类型，这里也可定义为类目轴
	            //name: '日期',
	            nameGap: 16,    //名称与轴末端间隔的距离
	            nameTextStyle: {    //名称样式
	                color: '#6f9ed6',
	                fontSize: 14
	            },
	            max: 31,    //最大值
	            //splitNumber: 31,    //轴的分割区间数
	            splitLine: {    //在grid中横向线的配置
	                show: false     //不显示
	            },
	            axisLine: {     //轴的配置
	                lineStyle: {
	                    color: '#2d556f'
	                }
	            },
	            axisLabel: {        //轴的刻度标签设置
	                color: '#6f9ed6'   //文本颜色
	            }
	        },
	        yAxis: {    //y轴
	            type: 'value',  //值类型
	            name: '消费（元）',
	            nameLocation: 'end',    //名称的位置
	            nameGap: 20,
	            nameTextStyle: {
	                color: '#6f9ed6',
	                fontSize: 16
	            },
	            axisLine: {     //轴的配置
	                lineStyle: {
	                    color: '#2d556f'
	                }
	            },
	            axisLabel: {        //轴的刻度标签设置
	                color: '#6f9ed6'   //文本颜色
	            },
	            splitLine: {    //这里为grid中纵向线的配置
	                show: false     //不显示
	            }
	        },
	        series: [
	            {
	                name: '水电',
	                type: 'scatter',
	                symbolSize: 8,
	                itemStyle: itemStyle
	            },
	            {
	                name: '餐饮',
	                type: 'scatter',
	                symbolSize: 8,
	                itemStyle: itemStyle
	            },
	            {
	                name: '娱乐',
	                type: 'scatter',
	                symbolSize: 8,
	                itemStyle: itemStyle
	            },
	            {
	                name: '其他',
	                type: 'scatter',
	                symbolSize: 8,
	                itemStyle: itemStyle
	            },
	            {
	            	type: 'line',
	            	itemStyle: {
	            		color: '#65c0d5'
	            	},
	            	symbol: 'none',
	            	data: []
	            }
	        ]
    	}
    };
    mychart.setOption(option);

	var all = null;	//获取到的数据
    
    var date = new Date(), curYear = date.getFullYear();
    //时间切换
    mychart.on("timelinechanged", function(param){   	
    	mychart.showLoading();
    	var _curIndex = param.currentIndex,
			_option = mychart.getOption(),
			_timeline = _option.timeline[0],
			_timelength = _timeline.data.length,
			_chooseValue = _timeline.data[_curIndex].value;

		mychart.setOption(option, true);
		mychart.setOption({baseOption: {timeline: _timeline}});
    	//切换时间，x轴需做相应的变化
    	if($("#time2 li.selected").attr("class").indexOf("year") != -1){ 		
    		mychart.setOption({
        		xAxis: {max: 12}
        	});
    	}else{
    		var dates = _chooseValue.split("-");
    		mychart.setOption({
        		xAxis: {max: mGetDate(dates[0], dates[1])}
        	});
    	}
    	$.ajax({
    		type: 'get',
    		url: '/poverty/getAbnormalConsumption',
    		data: {
    			time: _chooseValue,
    			num: 5
    		},
    		dataType: 'json',
    		success: function(result){
    			var alldata = result.object;
    			if(alldata){
    				all = alldata;
					var options = [];
					for(var i=0; i<_timelength; i++){
						options[i] = {series: [{data: []},{data: []},{data: []},{data: []},{data: []}]};
					}
					var series = [];
					$.each(alldata, function(i, item){
						var typeName = item.type;
						series.push({
							name: typeName,
							type: 'scatter',
							symbolSize: 8,
			                itemStyle: itemStyle,
			                data: convertData(item.data)
						});
					});
					series.push({type: 'line', itemStyle: {color: '#65c0d5'}});
					options[_curIndex] = {
				    	series: series
				    }
					mychart.setOption({
						options: options
					});
    			}
				mychart.hideLoading();
    		}
    	});
    });
    //绑定悬浮事件，悬浮时折线显示所在点的人的所有消费
    mychart.on('mouseover', function(param){
    	if(param.seriesType === "scatter"){
    		var op = mychart.getOption();
    		op.series[4].data = convertToLineData(all, param.name);
    		var series = op.series;
        	mychart.setOption({
        		series: series
        	});
    	}
    });
}

/*贫困生消费对比*/
function drawChart5(){
	//初始化echarts实例
    var mychart = echarts.init(document.getElementById("chart5"));

    var itemStyle = {
    	emphasis: {
    		lineStyle: {
    			color: 'red',
	    		shadowColor: '#0c1c2c',
	    		shadowBlur: 10
    		}
    	}
    };
    
    var option = {
        color: ['#ffff00', '#e47833', '#00ff00','#ff00ff','#00ffff'],
        title: {    //标题
            show: false
        },
        tooltip: {  //提示框
            trigger: 'axis',    //坐标轴触发
        	borderWidth: tip.borderWidth,
            borderColor: tip.borderColor,
            position: tip.position,
            /*formatter: function(param){
                return param.seriesName+"<br/>"+param.name+"消费： "+param.value+" 元";
            }*/
        },
        toolbox: toolbox,
        grid: {
            left: '5%',
            right: '5%',
            //top: '25%',
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
        ]
    }
    mychart.setOption(option);
    mychart.showLoading();
    $.ajax({
    	type: 'get',
    	url: '/poverty/getPoorComparison',
    	dataType: 'json',
    	success: function(result){
    		data = result.object;
    		if(data){
    			var titles = [];
    			var legend = {
    					x:'right',
    					y:'bottom',
    					orient:'horizontal',
    					padding:[0,20,0,0],
    					textStyle:{color:'#fff'},
    					data:titles
    			};
    			var series = [];
        		$.each(data, function(i, item){
        			titles[i] = item.name.split(",")[0];
        			series.push({
        				name: item.name.split(",")[0],
        				type: 'line',
                        itemStyle: itemStyle,
                        data: item.value
        			});
        		});
        		mychart.setOption({
        			legend:legend,
        			series: series
        		});
    		}
    		mychart.hideLoading();
    	}
    });
}

