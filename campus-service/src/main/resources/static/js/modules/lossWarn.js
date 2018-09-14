/**
 * 失联js
 */

$(function () {
    var $parentIframe = $(window.parent.document.getElementById("contentFrame")),
        $charts = $(".chart"),
        $tableAll = $(".table_all");

    $("body").css("height", $parentIframe.height());
    /*图的高度调整，须在设定本页面body高度为父页面iframe高度后调整，否则不能适应屏幕大小*/
    /*for(var i=0, len=$charts.length; i<len; i++){
        var $item = $charts.eq(i);
        $item.css("height", ($item.parent().height()-50)+"px");
    }*/
    $tableAll.css("height", ($tableAll.parent().height() * 0.8) + "px");
    $tableAll.find(".table_content").css("height", ($tableAll.height() - 30) + "px");

    drawMap();
    drawChart2();
    wordCloud();
});

var time;	//地图所选时间
var myChart;

function drawMap() {
    myChart = echarts.init(document.getElementById('myChart'));

    $.get("../plugins/echarts4/map/gz.json", function (map) {
        echarts.registerMap('gz', map);

        var option = {
            baseOption: {
                timeline: {
                    width: '40%',
                    right: 10,
                    bottom: 50,
                    axisType: 'category',
                    label: {	//时间轴标签
                        color: '#668db6',
                        position: 8,
                        formatter: function (s) {
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
                    },
                    data: mGetTimelineData("month")	//获取最近12个月，不包括当月
                },
                //图例组件
                legend: {
                    right: '8%',
                    bottom: '15%',
                    orient: 'vertical',
                    //itemGap:5,
                    //itemWidth:10,
                    //itemHeight:10,
                    //selectedMode:false,
                    textStyle: {
                        color: '#b0bfc2',
                        fontSize: 14
                    },
                    data: [{
                        name: '学生失联区',
                        icon: 'circle'
                    },
                        {
                            name: '学生活跃区',
                            icon: 'circle'
                        },
                        {
                            name: '学生活动轨迹',
                            icon: 'image://../image/track.png'
                        },
                        {
                            name: '失联学生常出入场所',
                            icon: 'image://../image/always.png'
                        }]
                },
                //提示框组件
                tooltip: {
                    trigger: 'item',
                    /*formatter: function(params){
                        return "停留时间:"+params.value[2]+"小时<br/>停留次数:"+params.value[3]+"次";
                    }*/
                },
                //地理坐标系组件
                geo: {
                    map: 'gz',
                    left: '8%',
                    //right: '15%',
                    aspectScale: 1.1,//长宽比，需同时调整上面同一参数
                    zoom: 1.2,//缩放，需同时调整上面同一参数
                    label: {
                        show: true
                    },
                    itemStyle: {
                        areaColor: '#0e4267',
                        borderColor: '#3dbacc',
                        //borderWidth:1.5,
                        shadowColor: '#1be0f4',
                        shadowBlur: 2
                    },
                    emphasis: {
                        label: {
                            show: false
                        },
                        itemStyle: {
                            areaColor: 'transparent'
                        }
                    },
                    silent: true
                },
                series: [
                    {
                        type: 'scatter',
                        name: '学生失联区',
                        coordinateSystem: 'geo',
                        itemStyle: {
                            color: '#F43031'
                        },
                        symbolSize: function (val) {
                            return val[2] / 10;
                        },
                        tooltip: {show: false}
                    }, {
                        type: 'scatter',
                        name: '学生活跃区',
                        coordinateSystem: 'geo',
                        itemStyle: {
                            color: '#00e3e6'
                        },
                        symbolSize: function (val) {
                            return val[2] / 10;
                        },
                        tooltip: {show: false}
                    }, {
                        type: 'map',
                        name: '失联学生常出入场所',
                        map: 'gz',
                        left: '8%',
                        //right: '15%',
                        aspectScale: 1.1,//长宽比，需同时调整下面同一参数
                        zoom: 1.2,//缩放，需同时调整下面同一参数
                        z: 1,
                        label: {show: true},
                        itemStyle: {
                            areaColor: '#0e4267',
                            borderColor: '#3dbacc',
                            //borderWidth:1.5,
                            shadowColor: '#1be0f4',
                            shadowBlur: 2
                        },
                        silent: true,
                        data: [{
                            name: '体育运动场',
                            itemStyle: {
                                areaColor: '#561823',
                                opacity: 0.75
                            },
                            tooltip: {show: false}
                        }, {
                            name: '山体',
                            itemStyle: {
                                areaColor: '#561823',
                                opacity: 0.75
                            },
                            tooltip: {show: false}
                        }, {
                            name: '民族食堂',
                            itemStyle: {
                                areaColor: '#561823',
                                opacity: 0.75
                            },
                            tooltip: {show: false}
                        }]
                    }, {
                        type: 'scatter',
                        name: '学生活动轨迹',
                        coordinateSystem: 'geo',
                        itemStyle: {
                            color: '#94CF71',
                            opacity: 1
                        }
                    },
                    {
                        type: 'lines',
                        name: '学生活动轨迹',
                        coordinateSystem: 'geo',
                        symbol: ['none', 'arrow'],
                        lineStyle: {
                            color: 'white',
                            opacity: 1
                        },
                        data: []
                    }
                ]
            }
        };
        myChart.setOption(option);
        //时间切换监听
        myChart.on("timelinechanged", function (param) {
            var _curIndex = param.currentIndex,	//当前选择下标
                _option = myChart.getOption(),
                _timeline = _option.timeline[0],	//时间轴
                _timelength = _timeline.data.length,	//时间轴数据长度
                _chooseValue = _timeline.data[_curIndex].value;	//当前选择的时间值

            time = _chooseValue;

            myChart.showLoading();
            $.ajax({
                type: 'post',
                url: '/lossContactWarn/getUncontactPlace',
                data: {
                    time: _chooseValue
                },
                dataType: 'json',
                success: function (result) {
                    var data = result.object;
                    if (data) {
                        var series = myChart.getOption().series,
                            huoyue = [], shilian = [];
                        $.each(data, function (i, item) {
                            if (item.name == "学生活跃区") {
                                //huoyue = item.value;	//学生活跃区数据
                                series[1].data = item.value;
                            }
                            if (item.name == "学生失联区") {
                                //shilian = item.value;	//学生失联区数据
                                series[0].data = item.value;
                            }
                        });
                        series[3].data = [];
                        series[4].data = [];
                        //myChart.setOption({series:[{data:shilian}, {data:huoyue}]});
                        myChart.setOption({series: series});
                    } else {
                        myChart.setOption(option, true);
                        myChart.setOption({baseOption: {timeline: _timeline}});
                    }
                    myChart.hideLoading();
                }
            });

            drawChart1();
        });
        //触发时间切换事件
        myChart.dispatchAction({type: 'timelineChange', currentIndex: 11});
    });
}

//表格失联学生数据
function drawChart1() {
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getUncontactStus',
        data: {
            time: time
        },
        //async: false,
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            if (data) {
                var html = "";
                $.each(data, function (i, item) {
                    var warntime = item.warntime.substring(0, item.warntime.lastIndexOf(":")),
                        processtime = item.processtime.substring(0, item.processtime.lastIndexOf(":"));
                    html += "<tr data-phone='" + item.phone + "' data-dormitory='" + item.dormitory + "' " +
                        "data-room='" + item.roommate + "' data-friend='" + item.friend + "' " +
                        "data-instructor='" + item.instructor + "' " +
                        "data-trajectory='" + item.trajectory + "'>" +
                        "<td width='13%' title='" + item.name + "'>" + item.name + "</td>" +
                        "<td width='19%' title='" + item.stuclass + "'>" + item.stuclass + "</td>" +
                        "<td width='24%' title='" + item.warntime + "'>" + warntime + "</td>" +
                        "<td width='24%' title='" + item.processtime + "'>" + processtime + "</td>" +
                        "<td width='10%' title='" + item.status + "'>" + item.status + "</td>" +
                        "<td width='10%'>操作</td>" +
                        "</tr>";
                });
                $(".table_content tbody").html(html);

                //表格行点击事件监听
                $(".table_content tr").on("click", function () {
                    //获取需显示的数据
                    var $tr = $(this), $tds = $tr.find("td"),
                        stuName = $tds.eq(0).text(),	//学生名
                        stuClass = $tds.eq(1).text(),	//学生学院班级
                        phone = $tr.attr("data-phone"),	//电话
                        dormitory = $tr.attr("data-dormitory"),	//宿舍
                        roommate = $tr.attr("data-room").split(","),	//室友
                        friend = $tr.attr("data-friend").split(","),	//亲密朋友
                        instructor = $tr.attr("data-instructor"),	//辅导员
                        trajectory = $tr.attr("data-trajectory").split("|");	//失联轨迹

                    $tr.addClass("selected").siblings("tr").removeClass("selected");

                    /*表格点击行显示提示框信息*/
                    var html = "", room_html = "", friend_html = "";
                    $.each(roommate, function (i, n) {
                        if (i == 0) {
                            room_html += "<span style='position:relative;'>" + n + "</span><br/>";
                        } else {
                            room_html += "<span style='position:relative;left:70px;'>" + n + "</span><br/>";
                        }
                    });
                    $.each(friend, function (i, n) {
                        if (i == 0) {
                            friend_html += "<span style='position:relative;'>" + n + "</span><br/>";
                        } else {
                            friend_html += "<span style='position:relative;left:70px;'>" + n + "</span><br/>";
                        }
                    });
                    html = "<div>" +
                        "姓名：" + stuName + "<br/>" +
                        "电话：" + phone + "<br/>" +
                        "院系：" + stuClass + "<br/>" +
                        "宿舍：" + dormitory + "<br/>" +
                        "宿舍人员：" + room_html +
                        "亲密好友：" + friend_html +
                        "辅导员：" + instructor + "</div>";
                    html += "<hr style='width:8%;height:1px;left:-3px;top:-3px;'>" +
                        "<hr style='width:1px;height:8%;left:-3px;top:-3px;'>" +
                        "<hr style='width:8%;height:1px;right:-3px;top:-3px;'>" +
                        "<hr style='width:1px;height:8%;right:-3px;top:-3px;'>" +
                        "<hr style='width:8%;height:1px;left:-3px;bottom:-3px;'>" +
                        "<hr style='width:1px;height:8%;left:-3px;bottom:-3px;'>" +
                        "<hr style='width:8%;height:1px;right:-3px;bottom:-3px;'>" +
                        "<hr style='width:1px;height:8%;right:-3px;bottom:-3px;'>";

                    $("#fixTip").html(html).css("display", "block");

                    /*地图中活动轨迹数据*/
                    var huodong = [], guiji = [];	//活动点与轨迹数据
                    for (var i = 0, len = trajectory.length; i < len; i++) {
                        var n = trajectory[i], n_tras = n.split(",");	//格式为[坐标轴+时间+次数]
                        huodong.push({
                            value: n_tras,
                            tooltip: {
                                trigger: 'item',
                                borderWidth: 1,
                                borderColor: 'gold',
                                position: function (point, params, dom, rect, size) {
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
                                    $dom.html(_domHtml).addClass("tip").css("borderRadius", "0");
                                },
                                formatter: function (params) {
                                    return "停留时间:" + params.value[2] + "小时<br/>停留次数:" + params.value[3] + "次";
                                }
                            }
                        });
                        if (i != len - 1) {	//形成轨迹数据
                            var m = trajectory[i + 1], m_tras = m.split(",");	//获取下一个活动点
                            guiji.push({
                                coords: [[n_tras[0], n_tras[1]], [m_tras[0], m_tras[1]]]
                            });
                        }
                    }

                    //对地图在原有基础上增加系列'活动轨迹'
                    var series = myChart.getOption().series, s = [];
                    s.push(series[0]), s.push(series[1]), s.push(series[2]);
                    s.push({
                        type: 'scatter',
                        name: '学生活动轨迹',
                        coordinateSystem: 'geo',
                        itemStyle: {
                            color: '#94CF71',
                            opacity: 1
                        },
                        symbolSize: function (val) {
                            return val[2] / 2;
                        },
                        data: huodong
                    }, {
                        type: 'lines',
                        name: '学生活动轨迹',
                        coordinateSystem: 'geo',
                        symbol: ['none', 'arrow'],
                        symbolSize: 18,
                        lineStyle: {
                            width: 2,
                            color: 'white',
                            opacity: 1
                        },
                        data: guiji
                    });
                    myChart.setOption({series: s});
                });
            } else {
                $(".table_content tbody").html("<tr><td colspan='6'>无数据</td></tr>");
            }
        }
    });
}

function drawChart2() {
    var myChart1 = echarts.init(document.getElementById('chart2'));

    var option = {
        baseOption: {
            color: ['#4c9e60', '#549fbc', '#ab6565', '3adac3f', '#ad4137', '#ab6565', '#2ec7c9', '#b6a2de', '#5ab1ef', '#ffb980', '#d87a80',
                '#8d98b3', '#e5cf0d', '#97b552', '#95706d', '#dc69aa',
                '#07a2a4', '#9a7fd1', '#588dd5', '#f5994e', '#c05050'],
            timeline: {
                width: '80%',
                left: '10%',
                bottom: -10,
                axisType: 'category',
                data: mGetTimelineData("month"),
                label: {
                    color: '#668db6',
                    position: 8,
                    formatter: function (s) {
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
            tooltip: {
                borderWidth: 1,
                borderColor: 'gold',
                position: function (point, params, dom, rect, size) {
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
                    $dom.html(_domHtml).addClass("tip").css("borderRadius", "0");
                },
                formatter: function (params) {
                    return params.marker + params.seriesName + ' ' + params.value[2] + '<br/>' +
                        params.value[0] + ' 失联 ' + params.value[3] + " 人" +
                        "<br/>" + params.value[4];
                }
            },
            grid: {
                top: '15%',
                //bottom: '5%'
            },
            xAxis: {
                type: 'category',
                axisLabel: {
                    color: '#6f9ed6'
                },
                axisLine: {
                    lineStyle: {
                        color: '#2d556f'
                    }
                },
                axisTick: {show: false},
                data: ['A1实训教学楼', 'A2实训教学楼', 'A3信息楼', 'A4实训楼', 'A5实训楼', 'B1学生宿舍',
                    'B2学生宿舍', 'B3学生宿舍', 'C1食堂', 'C3民族食堂', 'D1图书综合楼', 'E1风雨操场', '体育运动场']
            },
            yAxis: {
                type: 'value',
                name: '日',
                nameGap: 5,
                nameTextStyle: {
                    color: '#6f9ed6'
                },
                max: 30,
                splitLine: {
                    show: false
                },
                axisLabel: {
                    color: '#6f9ed6'
                },
                axisLine: {
                    lineStyle: {
                        color: '#2d556f'
                    }
                }
            }
        }
    };
    myChart1.setOption(option);
    myChart1.on("timelinechanged", function (param) {
        var _curIndex = param.currentIndex,	//当前选择下标
            _option = myChart1.getOption(),
            _timeline = _option.timeline[0],	//时间轴
            _timelength = _timeline.data.length,	//时间轴数据长度
            _chooseValue = _timeline.data[_curIndex].value;	//当前选择的时间值

        myChart1.showLoading();
        $.ajax({
            type: 'post',
            url: '/lossContactWarn/getUncontactStatistics',
            data: {
                time: _chooseValue
            },
            dataType: 'json',
            success: function (result) {
                var data = result.object;
                if (data) {
                    var series = [];
                    $.each(data, function (i, item) {
                        series.push({
                            type: 'scatter',
                            name: item.name,
                            data: item.value,
                            symbolSize: function (v) {
                                return v[3] * 6;
                            }
                        });
                    });
                    var options = [];
                    for (var i = 0; i < _timelength; i++) {
                        options[i] = {series: [{data: []}]};
                    }
                    options[_curIndex] = {
                        series: series
                    }
                    myChart1.setOption({
                        options: options
                    });
                } else {
                    myChart1.setOption(option, true);
                    myChart1.setOption({baseOption: {timeline: _timeline}});
                }
                myChart1.hideLoading();
            }
        });
    });
    myChart1.dispatchAction({type: 'timelineChange', currentIndex: 11});
}

function wordCloud() {
    var wordCloud = echarts.init(document.getElementById('chart3'));
    var color = ['#84e268', '#dd9b45', '#21b3da', '#edfae6', '#68dcf5', '#3ba189'];
    wordCloud.setOption({
        tooltip: {
            borderWidth: 1,
            borderColor: 'gold',
            position: function (point, params, dom, rect, size) {
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
                $dom.html(_domHtml).addClass("tip").css("borderRadius", "0");
            },
            formatter: function (params) {
                return params.marker + params.name + "<hr>人数： " + params.value[0] +
                    "<br/>占比： " + params.value[1];
            }
        },
        series: [{
            type: 'wordCloud',
            shape: 'square',
            sizeRange: [12, 25],//字体大小范围
            rotationRange: [-90, 90],//字体旋转角度区间
            //rotationStep: 90,//旋转角度间隔
            gridSize: 18, //字符间距
            bottom: 0,
            width: '90%',//遮罩图片宽度
            //height: 250,//遮罩图片高度
            left: 'center',
            top: 'center',
            textStyle: {
                normal: {
                    color: function () {
                        return color[Math.round(Math.random() * 3)];
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
    wordCloud.showLoading();
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getUncontactKeywords',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            if (data) {
                wordCloud.setOption({
                    series: [{
                        data: data
                    }]
                });
            }
            wordCloud.hideLoading();
        }
    });
}

$(document).bind("click", function (e) {
    var $fixTip = $("#fixTip")[0],
        tipX = $fixTip.offsetLeft, tipY = $fixTip.offsetTop,
        tipWidth = $fixTip.offsetWidth, tipHeight = $fixTip.offsetHeight;
    var target = e.target, e_clientX = e.clientX, e_clientY = e.clientY;
    if ((target.nodeName != "TD" && target.nodeName != "TR" && target.nodeName != "TABLE")
        && (e_clientX < tipX || e_clientX > (tipX + tipWidth)
            || e_clientY < tipY || e_clientY > (tipY + tipHeight))) {
        $("#fixTip").css("display", "none");
    }
});

