/**
 * 招生决策js
 */

var EffectChart = [];	//存放有动画效果的图实例对象，父页面获取
$(function(){
	var $parentIframe = $(window.parent.document.getElementById("contentFrame")),
		$charts = $(".chart");
	
	$("body").css("height", $parentIframe.height());
	/*图的高度调整，须在设定本页面body高度为父页面iframe高度后调整，否则不能适应屏幕大小*/
	for(var i=0, len=$charts.length; i<len; i++){
		var $item = $charts.eq(i);
		$item.css("height", ($item.parent().parent().height()-50)+"px");
	}
	$charts.eq(1).css("height", ($charts.eq(1).height()+20)+"px");
	
	drawChart1();
	drawChart2();
	drawChart3();
	drawChart4();
	drawChart5();
	drawChart6();
	
	//时间轴选择
	var $time1_li = $("#time1 li");
	$time1_li.on("click", function(){
		changeTimeline(this, chart5);
	});
});

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
    right: '2%'     
}

//时间轴
var timeline = {
	    axisType: 'category',
	    width: '70%',
	    right: '10%',
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
	    data: mGetTimelineData("year")	//获取时间轴数据，以‘年’为单位
}

/*访问情况分析堆叠柱状图*/
function drawChart1(){
	var chart1 = echarts.init(document.getElementById("chart1"));
	
	var option = {
		baseOption: {
			color: ['#B44D5E','#BD8680','#4DB49B','#414A4E','#4CB9E7','#809BBD','#B49A4D'],
	        timeline: {
	            width: '90%',
	            left: 'center',
	            bottom: -10,
	            axisType: 'category',
	            data: mGetTimelineData("month"),	//获取时间轴数据
	            label: {	//时间轴标签
	                color: '#668db6',
	                position: 8,
	                formatter : function(s) {
	                	var yyyyMM = s.split("-"), m = parseInt(yyyyMM[1]), text = "";
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
	        },
	        legend: {
	        	right: '2%',
	        	bottom: '20%',
	        	orient:'vertical',
	        	padding:2,
	        	itemGap: 5,
	        	itemWidth:8,
	        	itemHeight:8,
	            textStyle: {
	                color: '#b0bfc2'
	            },
	        	data: [{name:'360搜索',icon:'circle'},{name:'百度',icon:'circle'},
	        	       {name:'省招生网',icon:'circle'},{name:'省教育网',icon:'circle'},
	        	       {name:'搜狗',icon:'circle'},{name:'学校官网',icon:'circle'},
	        	       {name:'其他',icon:'circle'}]
	        },
	        tooltip: {
	        	trigger: 'axis',
	        	axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	            },
	            borderWidth: tip.borderWidth,
	            borderColor: tip.borderColor,
	            position: tip.position,
	            formatter: function(params){
	            	var sum=0;
	            	var pHtml="";
	            	for(var i=0;i<params.length;i++){
	            		sum=sum+params[i].value;
	            		pHtml=pHtml+"<p> "+params[i].marker+" "+params[i].seriesName+" "+params[i].value+"</p>";
	            	}
	            	return "<div style='width:120px'>" +
    				"<p style='margin-left:18%;'>总数" +    					
    					sum+
    				"</p>" +
    					pHtml+
    			"</div>";
	            }	
	        },	        
	        toolbox: toolbox,
	        grid: {
	        	top:'7%',
	        	right: '22%',
	        	bottom:'17%'
	        },
	        xAxis: {
	        	type: 'value',
	        	name: '次数',
	        	nameTextStyle: {
                	color: '#6f9ed6'
                },
                nameGap: 5,
                axisLine: {
                    lineStyle: {
                        color: '#2d556f'
                    }
                },
                axisLabel: {
                    color: '#6f9ed6'
                },
                splitLine: {    //在表格区域的分割线
                    show: true,
                    lineStyle: {color: '#263d5d'}
                },
	        },
	        yAxis: {
	        	type: 'category',
	        	axisLine: {         //轴线
                    lineStyle: {    //轴线样式
                        color: '#2d556f'
                    }
                },
                axisLabel: {        //轴的刻度标签设置
                    color: '#6f9ed6'   //文本颜色
                },
                data:[]
	        },
	        series: [
	            {type: 'bar',name: '360搜索',stack: '1'},
	            {type: 'bar',name: '百度',stack: '1'},
	            {type: 'bar',name: '省招生网',stack: '1'},
	            {type: 'bar',name: '省教育网',stack: '1'},
	            {type: 'bar',name: '搜狗',stack: '1'},
	            {type: 'bar',name: '学校官网',stack: '1'},
	            {type: 'bar',name: '其他',stack: '1',barWidth:10}
	        ]
		},
		options:[]
	};
	
	chart1.setOption(option);
	
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart1.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			_option = chart1.getOption(),
			_timeline = _option.timeline[0],
			_chooseMonth = _timeline.data[_curIndex].value;	//选择的时间值
		
		chart1.showLoading();	//显示‘加载’
		//获取点击月份的各城市的访问情况分析
		$.ajax({
			type: 'post',
			url: '/enrollDes/getWebCitys',
			data: {
				"currentMonth": _chooseMonth
			},
			dataType: 'json',
			success: function(result){
				var res = result.object;	//数据
				if(res){	//数据不为空
					//与时间轴长度相同的options
					var options = [{series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]},
					               {series: [{data: []},{data: []},{data: []},{data: []},{data: []},{data: []},{data: []}]}
   								   ];
					//设置当前选择的时间的series
					options[_curIndex] = {
    				    	series: [
    			    	         {data:res[1][0]},{data:res[1][1]},{data:res[1][2]},{data:res[1][3]},{data:res[1][4]},{data:res[1][5]},{data:res[1][6]}
    				    	]
    				}
					
					chart1.setOption({
						baseOption:{
							yAxis: {
					            data: res[0]
					        }
						},
				        options:options
				    });
				}else{
					chart1.setOption(option, true);
					chart1.setOption({baseOption: {timeline: _timeline}});
				}
				chart1.hideLoading();
			}
		});
	});
	//默认点击最后一个时间点
	chart1.dispatchAction({type: 'timelineChange', currentIndex: 0})
}

/*生源地分布地图+折线图*/
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
        
        var convertData2 = function(data){
        	var res = [];
            for (var i = 0; i < data.length; i++) {
                var dataItem = data[i];
                var fromCoord = geoCoordMap[dataItem[0].name];
                if(dataItem[0].name=="全国"){
                	fromCoord = geoCoordMap["贵州"];
                }              
                var toCoord = geoCoordMap[dataItem[1].name];
                if (fromCoord && toCoord) {
                    res.push({
                        fromName: dataItem[0].name,
                        toName: dataItem[1].name,
                        coords: [fromCoord, toCoord],
                        value:dataItem[1].value
                    });
                }
            }
            return res;
        }
        
        var option = {
        	baseOption: {
        		grid:{
        			top:'8%',
        			right:'3%',
        			width:'14%',
        			height:'25%'
        		},
        		xAxis: {
		        	type: 'category',
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
	                    color: '#6f9ed6',
	                    fontSize:8,
	                    showMinLabel:true
	                }
		        },
		        yAxis: {
		        	type: 'value',
		        	name: '人数',
		        	nameTextStyle:{
		        		color: '#6f9ed6'
		        	},
		        	//boundaryGap:false,
		        	axisLine: {
	                    lineStyle: {
	                        color: '#2d556f'
	                    }
	                },
	                axisTick:{
		            	show:false
		            },
	                axisLabel: {
	                    color: '#6f9ed6',
	                    showMinLabel:false
	                },
	                splitLine: {   
	                    show: false
	                },
		        },
        		timeline: {
    	            bottom: -10,
        			axisType: 'category',
    	            data: mGetTimelineData("year"),	//获取时间轴数据，以‘年’为单位
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
        		},
        		geo: {  //使用地理坐标系
                    map: 'china',
                    left: '5%',
                    right: '25%',
                    top: '0%',
                    //bottom: '12%',
                    selectedMode:'single',
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
        		},
        		series: [
        		    {
        		    	type: 'effectScatter',
        		    	coordinateSystem: 'geo',    //参考地理坐标系
        		    	rippleEffect: {
                            brushType: 'stroke'     //涟漪特效
                        },
        		    	symbol: 'image://../image/xing.png',
        		    	symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                            return val[2] /5;
                        },
                        itemStyle: {    //散点的样式
                            normal: {
                                color: '#97d75f'   //颜色
                            }
                        },
        		    	tooltip: {
        		        	trigger: 'item',
        		            borderWidth: tip.borderWidth,
        		            borderColor: tip.borderColor,
        		            position: tip.position,
        		            formatter: function(params){
        	        			return params.name+"："+params.value[2]+"人";
        	        		}
        		        }	
        		    },
        		    {
        		    	type: 'lines',
        		    	zlevel: 1,
        		    	effect: {
        		            show: true,
        		            period: 6,
        		            trailLength: 0.7,
        		            color: '#fff',
        		            symbolSize: 3
        		        },
        		        lineStyle: {
        		            normal: {
        		                //color: color[i],
        		                width: 0,
        		                curveness: 0.2
        		            }
        		        },
        		    	data: []
        		    },
        		    {
        		    	type: 'lines',
        		        zlevel: 2,
        		        symbol: ['none', 'arrow'],
        		        symbolSize: 10,
        		        effect: {
        		            show: true,
        		            period: 6,
        		            trailLength: 0,
        		            symbol: 'circle',
        		            symbolSize: 5
        		        },
        		        lineStyle: {
        		            normal: {
        		                color: '#f2ef7c',
        		                width: 1,
        		                opacity: 0.6,
        		                curveness: 0.2
        		            }
        		        },
        		    	tooltip: {
        		        	trigger: 'item',
        		            borderWidth: tip.borderWidth,
        		            borderColor: tip.borderColor,
        		            position: tip.position,
        		            formatter: function(params){
        		            	if(params.data.fromName=="全国"){
        		            		params.data.fromName="";
        		            	}
        	        			return params.data.fromName+"->"+params.data.toName+"："+params.value+"人";
        	        		}
        		        },
        		        data: []
        		    },
        		    {
        		    	type:'line',
        		    	itemStyle:{
        		    		color: '#51c7c7',
        		    		borderWidth:3
        		    	},
        		    	lineStyle:{
        		    		color: '#51c7c7',
        		    	},
        		    	areaStyle:{
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
        	        			return params.value[0]+"年："+params.value[1]+"人";
        	        		}	
        		        }	
        		    }
        		]
        	},
        	options: []
        }
        
        chart2.setOption(option);    
        
        var _chooseYear,name;
      //绑定时间轴点击切换事件，每点击一下重新请求获取数据
        chart2.on('timelinechanged', function(param){
    		var _curIndex = param.currentIndex, //当前选择时间的下标
    			_option = chart2.getOption(),	
    			_chooseYear = _option.timeline[0].data[_curIndex].value;	//选择的时间值
    		
    		chart2.showLoading();	//显示‘加载’
    		//获取生源地分布
    	    $.ajax({
    	    	type: 'post',
    	    	url: '/enrollDes/birthPlaceDis',
    	    	dataType: 'json',
    	    	data:{
    	    		"year":_chooseYear,
    	    		"school":"全校",
    	    	},
    	    	success: function(result){
    	    		var data = result.object;
    				if(data){			
    					/*将各省生源地分布写入到地图右侧列表*/
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
    			        var $map_ul = $(".map_list ul"), html = "",oHtml="";
    			        $.each(data, function(i, item){
    			        	if(item.isTop)
    			        	{
    			        		oHtml="<div class='isTop'></div>";
    			        	}
    			        	else{
    			        		oHtml="<div></div>";
    			        	}
    			            html += "<li data-name='"+item.name+"'>" +oHtml+
    			            			"<span>"+item.name+"</span>" +
    			            			"<span>"+item.value+"人</span>" +
    			            		"</li>";
    			        });
    			        $map_ul.html(html);
    			        
    			        //点击列表项与地图联动
    			        $map_ul.find("li").on("click", function(){
	                    	$(this).addClass("hover").siblings().removeClass("hover");
	                    	
	                    	name = $(this).attr("data-name");	//获取所点击项的省名称
    			        	chart2.dispatchAction({	//触发chart2的地图选择事件
    			        		type: 'geoSelect',
    			        		name: name	//指定省
    			        	});
    			        	chart2.dispatchAction({	//触发chart2的显示提示框事件
    			        		type: 'showTip',
    			        		seriesIndex: 0,
    			        		name: name
    			        	});
    			        	chart2.showLoading();	//显示‘加载’
    			        	//获取点击各年份的各地区的就业情况分析访问情况分析
    			    		$.ajax({
    			    			type: 'post',
    			    			url: '/enrollDes/getCityinfo',
    			    			data: {
    			    				"year": _chooseYear,
    			    				"school":'全校',
    			    				"city":name
    			    			},
    			    			dataType: 'json',
    			    			success: function(result){
    			    				var res = result.object;	//数据
    			    				if(res){	//数据不为空
    			    					//与时间轴长度相同的options
    			    					var options=[{series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]}];
    			    					
    			    					//设置当前选择的时间的series
    			    					options[_curIndex] = {
    			    							series: [{data: convertData(data)},{data: convertData2(res[0])},{data: convertData2(res[0])},{data:res[1]}]
    			    					}
    			    					
    			    					chart2.setOption({
    			    						baseOption:{
    			    							xAxis: {
    			    					            data: [{value: _chooseYear-2, tooltip: {show: false}},{value: _chooseYear-1, tooltip: {show: false}},{value: _chooseYear, tooltip: {show: false}}]
    			    					        }
    			    						},
    			    				        options:options
    			    				    });   			    					
    			    				}
    			    				chart2.hideLoading();
    			    			}
    			    		});
    			        });
    			        
    			        chart2.on('geoselectchanged', function(param1){
    			        	name=param1.batch[0].name;
    			        	var $li_cur = $(".map_list ul").find("li[data-name='"+name+"']"),
    			        	_index = $li_cur.index();
    			        	$li_cur.addClass("hover").siblings().removeClass("hover");
    	                	$map_ul.scrollTop((_index-4)*21);
    			        	chart2.showLoading();	//显示‘加载’
    			        	//获取点击各年份的各地区的就业情况分析访问情况分析
    			    		$.ajax({
    			    			type: 'post',
    			    			url: '/enrollDes/getCityinfo',
    			    			data: {
    			    				"year": _chooseYear,
    			    				"school":'全校',
    			    				"city": name
    			    			},
    			    			dataType: 'json',
    			    			success: function(result){
    			    				var res = result.object;	//数据
    			    				if(res){	//数据不为空
    			    					//与时间轴长度相同的options
    			    					var options=[{series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]},
    			    					             {series: [{data: []},{data: []},{data: []},{data:[]}]}];
    			    					
    			    					//设置当前选择的时间的series
    			    					options[_curIndex] = {
    			    							series: [{data: convertData(data)},{data: convertData2(res[0])},{data: convertData2(res[0])},{data:res[1]}]
    			    					}
    			    					
    			    					chart2.setOption({
    			    						baseOption:{
    			    							xAxis: {
    			    					            data: [{value: _chooseYear-2, tooltip: {show: false}},{value: _chooseYear-1, tooltip: {show: false}},{value: _chooseYear, tooltip: {show: false}}]
    			    					        }
    			    						},
    			    				        options:options
    			    				    });   			    					
    			    				}
    			    				chart2.hideLoading();
    			    			}
    			    		});
    			        }) 
    			        if(name){
        					var $li_cur = $(".map_list ul").find("li[data-name='"+name+"']"),
                    		_index = $li_cur.index();
    	                	$li_cur.click();
    	                	$(".map_list ul").scrollTop((_index-4)*21);
        				}
        				else{
        					$(".map_list ul li").eq(0).click();
            		        $(".map_list ul").scrollTop(0);
        				}
    				} 
    				  		         				
    				chart2.hideLoading();
    	    	}   	
    	    });
    	});
    	//默认点击第一个时间轴
    	chart2.dispatchAction({type: 'timelineChange', currentIndex: 0});
	});
}

/*就业情况分析散点图*/
function drawChart3(){
	var chart3 = echarts.init(document.getElementById("chart3"));
	var color = ['#52AEE1','#CAAA2F','#8A83B7','#C7697B','#CD804A','#3CBF97','#088462'];
	var option = {
		baseOption: {
			legend: {
				bottom:'2%',
				selectedMode:'single',
				left: '10%',
				inactiveColor:'#aaa',
				textStyle:{
					color: '#b2c0c3' 
				},
		        data:['华东','华南','华中','华北','西北','西南','东北']
		    },
			timeline: {
    			axisType: 'category',
    			orient: 'vertical',
    			inverse: true,
    			left: null,
    			right: '5%',
    			top: '20%',
    			bottom: '30%',
    			width: 50,
    			height: null,
    			inverse: true,
	            data: mGetTimelineData("year"),	//获取时间轴数据，以‘年’为单位
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
    		grid: {
    			right:'20%',
    			bottom:'20%'
    		},
    		xAxis: {
    			type: 'category',
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
                },
    			data: []
    		},
    		yAxis: {
    			 name: '人数',
		    	 nameTextStyle:{
		    		 color: '#6f9ed6',
		    		 padding: [0, 45, 0, 0]
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
	            	lineStyle: {    
                       color: '#2d556f',
                       opacity:0.5
                   }
                }
    		},
    		tooltip:{
    			borderWidth: tip.borderWidth,
        	    borderColor: tip.borderColor,
        	    position: tip.position,
        		formatter: function(params){
        			return params.value[2]+"："+params.value[1]+"人</br>男："+params.value[3]+"人</br>女："+params.value[4]+"人";
        		}
    		},
    		series: [
    		    {
    		    	name:'华东',
    		    	type: 'scatter',
    		    	symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]},
    		    {
		    		name:'华南',
		    		type: 'scatter',
    		    	symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]},
    		    {
		    		name:'华中',
		    		type: 'scatter',
		    		symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]},
    		    {
		    		name:'华北',
		    		type: 'scatter',
		    		symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]},
    		    {
		    		name:'西北',
		    		type: 'scatter',
		    		symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]},
    		    {
		    		name:'西南',
		    		type: 'scatter',
		    		symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]},
    		    {
		    		name:'东北',
		    		type: 'scatter',
		    		symbolSize: function (val) {    //图形大小，需根据值做相应调整，防止图形过大或过小
                        return val[1] / 2;
                    },
                    itemStyle:{
                    	color: function() {
                            return color[Math.round(Math.random() * 6)];
                        }
                    },
    		    	data:[]}
    		]
		}
	};
	
	chart3.setOption(option);
	
	var areaName="华东";
	var year,_curIndex,option,length;
	//绑定图例点击切换事件，每点击一下重新请求获取数据
	chart3.on('legendselectchanged', function(param){
		areaName=param.name;
		chart3.showLoading();	//显示‘加载’
		//获取点击各年份的各地区的就业情况分析访问情况分析
		$.ajax({
			type: 'post',
			url: '/enrollDes/getWorkCitys',
			data: {
				"year": year,
				"college":'全校',
				"area":areaName
			},
			dataType: 'json',
			success: function(result){
				var res = result.object;	//数据
				if(res){	//数据不为空
					//与时间轴长度相同的options
					var options=[];
					for(var i=0;i<length;i++){
						options[i]={series: [{name:'华东',data: []},
						                     {name:'华南',data: []},
						                     {name:'华中',data: []},
						                     {name:'华北',data: []},
						                     {name:'西北',data: []},
						                     {name:'西南',data: []},
						                     {name:'东北',data: []}]};
					}
					//设置当前选择的时间的series
					options[_curIndex] = {
					    	series: [
				    	         {name:areaName,data:res[1]}
					    	]
					}
					
					chart3.setOption({
						baseOption:{
							xAxis: {
					            data: res[0]
					        }
						},
				        options:options
				    });
				}
				chart3.hideLoading();
			}
		});
	});

	chart3.on('timelinechanged', function(param1){
		_curIndex = param1.currentIndex;//当前选择时间的下标
		option = chart3.getOption();	
		year = option.timeline[0].data[_curIndex].value;//选择的时间值
		length = option.timeline[0].data.length;		
		
		chart3.showLoading();	//显示‘加载’
		//获取点击各年份的各地区的就业情况分析访问情况分析
		$.ajax({
			type: 'post',
			url: '/enrollDes/getWorkCitys',
			data: {
				"year": year,
				"college":'全校',
				"area":areaName
			},
			dataType: 'json',
			success: function(result){
				var res = result.object;	//数据
				if(res){	//数据不为空
					//与时间轴长度相同的options
					var options=[];
					for(var i=0;i<length;i++){
						options[i]={series: [{name:'华东',data: []},
						                     {name:'华南',data: []},
						                     {name:'华中',data: []},
						                     {name:'华北',data: []},
						                     {name:'西北',data: []},
						                     {name:'西南',data: []},
						                     {name:'东北',data: []}]};
					}
					//设置当前选择的时间的series
					options[_curIndex] = {
					    	series: [
				    	         {name:areaName,data:res[1]}
					    	]
					}
					
					chart3.setOption({
						baseOption:{
							xAxis: {
					            data: res[0]
					        }
						},
				        options:options
				    });
				}
				chart3.hideLoading();
			}
		});
	});
	
	//默认点击第一个图例
	chart3.dispatchAction({type: 'legendSelect', name: '华东'});
	//默认点击第一个时间轴
	chart3.dispatchAction({type: 'timelineChange', currentIndex: 0});
}

/*专业倾向分析树图*/
function drawChart4(){
	var chart4 = echarts.init(document.getElementById("chart4"));
	
	chart4.showLoading();	//显示‘加载’
	$.ajax({
	type: 'post',
	url: '/enrollDes/getDeTree',
	data: {
		"college": "贵州省经济贸易学校"
	},
	dataType: 'json',
	success: function(result){
		var res = result.object;	//数据
		if(res){	//数据不为空
			res.label={
				color: 'rgb(49,167,217)',
			}
			echarts.util.each(res.children, function (datum, index) {
		        index % 2 === 0 && (datum.collapsed = true);
		    });
			chart4.setOption({
				toolbox:toolbox,
				series: [{
					name:'专业倾向分析',
					type: 'tree',
    		    	symbol:'circle',
    		    	left:'25%',
    		    	right:'50%',
    		    	bottom:0,
//    		    	initialTreeDepth:1,//树图初始展开的层级（深度）
    		    	itemStyle:{
    		    		color: 'rgb(65,140,169)',
    		    		borderColor: 'rgb(65,140,169)'
    		    	},
    		    	label:{
    		    		position:'left',
    		    		color: '#80BFD0',
    		    		padding:3
    		    	},
    		    	lineStyle:{
    		    		color: 'rgb(62, 116, 154)'
    		    	},
    		    	leaves:{
    		    		label:{
	    		    		position:'right'
	    		    	}
    		    	},
		            data: [res]
		        }]				
		    });
		}			
		chart4.hideLoading();
	}
});
}	

/*访问时间分布折线图*/
var chart5;
function drawChart5(){
	chart5 = echarts.init(document.getElementById("chart5"));
	
	var option = {
			baseOption:{
				grid: {
		            containLabel: true
		        },
				xAxis: {
					type: 'category',
					nameTextStyle:{
			    		 color: '#6f9ed6',
			    		 padding: [25, 0, 0, 0]
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
	 	            	show:true,
	 	            	lineStyle: {    
	 	            		color: '#2d556f',
	                        opacity:0.5
	                    }
	                },
	                data:[]
			    },
			    yAxis: {
			    	 name: '次数',
			    	 nameTextStyle:{
			    		 color: '#6f9ed6',
			    		 padding: [0, 45, 0, 0]
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
		            formatter: '{c}'	
		        },	
		        series: 
			        {
				        type:'line',
			            smooth:true,
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
			            }
				    }
			},
			options:[]		    
	};
	chart5.setOption(option);
	
	//绑定时间轴点击切换事件，每点击一下重新请求获取数据
	chart5.on('timelinechanged', function(param){
		var _curIndex = param.currentIndex,	//当前选择时间的下标
			_option = chart5.getOption(),	
			_chooseValue = _option.timeline[0].data[_curIndex].value,//选择的时间值
			length = _option.timeline[0].data.length;	
	    	
    	if($("#time1 li.selected").attr("class").indexOf("year") != -1){
			chart5.setOption({
				baseOption:{
					xAxis:{
						name:'月'
					},
					/*timeline: {
						data: [{value:'2013'},{value:'2014'},{value:'2015'},{value:'2016'},{value:'2017'}]
			        }*/
				}
		    });
			//time = '2017';
//			time = _chooseValue;	//选择年时直接传参数为年
		}else if($("#time1 li.selected").attr("class").indexOf("month") != -1){	//选择月显示时，传参为‘<当年>-<选择的月>’
			//time = new Date().getFullYear()+"-"+_chooseValue;
			chart5.setOption({
				baseOption:{
					xAxis:{
						name:'天'
					}
				}
		    });
			//time="2015-"+_chooseValue;
		}else{
			//time = new Date().getFullYear()+"-"+new Date().getMonth()+"-"+_chooseValue;
			chart5.setOption({
				baseOption:{
					xAxis:{
						name:'时'
					}
				}
		    });
			_chooseValue = "2015-1-"+_chooseValue;
		}
		
		chart5.showLoading();	//显示‘加载’
		//获取点击月份的各城市的访问情况分析
		$.ajax({
			type: 'post',
			url: '/enrollDes/getWebVisitTime',
			data: {
				"date": _chooseValue
			},
			dataType: 'json',
			success: function(result){
				var res = result.object;	//数据
				if(res){	//数据不为空
					//与时间轴长度相同的options
					var options=[];
					for(var i=0;i<length;i++){
						options[i]={series: [{data: []}]};
					}
					//设置当前选择的时间的series
					options[_curIndex] = {
    				    	series: [
    			    	         {data:res[1]}
    				    	]
    				}
					
					chart5.setOption({
						baseOption:{
							xAxis: {
					            data: res[0]
					        }
						},
				        options:options
				    });
				}
				chart5.hideLoading();
			}
		});
	});
	//默认点击最后一个时间点
	chart5.dispatchAction({type: 'timelineChange', currentIndex: 4})
}

/*招生咨询热词字符云*/
function drawChart6(){
	var chart6 = echarts.init(document.getElementById("chart6"));
    var color = ['#84e268', '#dd9b45', '#21b3da', '#edfae6', '#e27270'];
    chart6.setOption({
    	tooltip: {
    		borderWidth: tip.borderWidth,
    	    borderColor: tip.borderColor,
    	    position: tip.position,
    		formatter: function(params){
    			return "与"+params.name+"相关内容 "+"<br/>共 "+params.value+"条";
    		}
    	},
        series: [{
            type: 'wordCloud',
            shape: 'rectangle',
            sizeRange: [14, 20],//字体大小范围
            gridSize: 30, //字符间距
            bottom: 0,
            left: 'center',
            top: 'center',
            textStyle: {
                normal: {
                    color: function() {
                        return color[Math.round(Math.random() * 4)];
                    }
                },
                emphasis: {
                    shadowBlur: 20,
                    shadowColor: '#333'
                }
            },
            data: []
        }]
    });
    chart6.showLoading();
    $.ajax({
    	type: 'post',
    	url: '/enrollDes/getWebWords',
    	dataType: 'json',
    	success: function(result){
    		var data = result.object;
    		if(data){
    			chart6.setOption({
    				series: [{
    					data: data
    				}]
    			});
    		}
    		chart6.hideLoading();
    	}
    });
}

