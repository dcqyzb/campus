/**
 *    失联预警下的数据模式js
 */

var pageSize = 18;	//分页每页显示记录条数
var laypage;	//定义分页的全局变量

/*页面渲染完成执行*/
$(document).ready(function () {
    /*
     * 调整查询条件样式，当查询条件太多导致换行时调整其上高度
     */
    var w = $(".center_table").width();
    if (w < 1112) {
        $("#table1 .search_area>div").css("top", "20px");
    }
    if (w < 1036) {
        $("#table3 .search_area>div").css("top", "20px");
    }
    //赋值laypage
    layui.use('laypage', function () {
        laypage = layui.laypage;

        //默认点击左边导航条第一个
        $("ul.nav_ul li").eq(0).click();
    });

    //通知通告
    top5_notice();
});

/*点击导航切换*/
$("ul.nav_ul li").click(function () {
    var $this = $(this), curIndex = $this.index();
    //加载数据前清空所有的查询条件
    $(".clear_btn").click();

    switch (curIndex) {
        case 0:
            pagi_lossWarnStus();	//失联学生
            $(".rank .title span").text("学院失联人数Top10");
            top10_lossNumber(); //学院失联人数top10
            break;
        case 1:
            pagi_lossAreaStatistic();	//失联地点统计
            $(".rank .title span").text("失联地点人数Top10");
            top10_lossArea(); //失联地点人数top10
            break;
        case 2:
            pagi_lossReason();	//失联原因分析
            $(".rank .title span").text("失联原因Top10");
            top10_lossReason(); //失联原因top10
            break;
        default:
            break;
    }
});

/**
 * 分页显示失联预警学生信息
 * @param curr    当前页，为空默认显示第1页
 */
function pagi_lossWarnStus(curr) {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table1 .search_condition");
    //姓名关键词
    var name = conditions.eq(0).children("input").val(),
        //学院
        collegeName = conditions.eq(1).children("input").val(),
        //预警时间-开始时间
        startYear = conditions.eq(2).children("input.start").val(),
        //预警时间-结束时间
        endYear = conditions.eq(2).children("input.end").val(),
        //失联状态
        state = conditions.eq(3).children("input").val();
    //显示加载层
    $(".loading").show();
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getLossWarnIncidents',
        data: {
            stuClass: (collegeName == "全部") ? "" : collegeName,
            stuName: name,
            dealStatus: (state == "全部") ? "" : state,
            startTime: startYear,
            endTime: endYear,
            pageNum: curr ? curr : 1,
            pageSize: pageSize
        },
        dataType: 'json',
        success: function (result) {
            var page = result.object;
            //隐藏加载层
            $(".loading").hide();
            if (page) {
                var html = "";
                if (page.records) {
                    $.each(page.records, function (i, item) {
                        //不同失联状态，字体显示不同颜色
                        var statehtml = "<td width='10%'></td>";
                        switch (item.status) {
                            case '确认失联':
                                statehtml = "<td width='10%' onclick='show_lossWarnRunTask(this)' class='clickable' style='color:#ed2e2e;' title='" + item.status + "'>" + item.status + "</td>";
                                break;
                            case '保卫处调查':
                                statehtml = "<td width='10%' onclick='show_lossWarnRunTask(this)' class='clickable' style='color:#f5a958;' title='" + item.status + "'>" + item.status + "</td>";
                                break;
                            case '联系学生亲属及朋友':
                                statehtml = "<td width='10%' onclick='show_lossWarnRunTask(this)' class='clickable' style='color:#f5de68;' title='" + item.status + "'>" + item.status + "</td>";
                                break;
                            case '可能失联':
                                statehtml = "<td width='10%' onclick='show_lossWarnRunTask(this)' class='clickable' style='color:#fb7d5f;' title='" + item.status + "'>" + item.status + "</td>";
                                break;
                            case '辅导员排查':
                                statehtml = "<td width='10%' onclick='show_lossWarnRunTask(this)' class='clickable' style='color:#82bce6;' title='" + item.status + "'>" + item.status + "</td>";
                                break;
                            case '系统预警':
                                statehtml = "<td width='10%' onclick='show_lossWarnRunTask(this)' class='clickable' style='color:#4cfbc4;' title='" + item.status + "'>" + item.status + "</td>";
                                break;
                            default:
                                break;
                        }

                        var teahtml = item.teaName.substring(0, item.teaName.indexOf("1"));
                        html += "<tr data-ucId='" + item.ucId + "'>" +
                            "<td width='13%' title='" + item.stuId + "'>" + item.stuId + "</td>" +
                            "<td width='8%' title='" + item.stuName + "'>" + item.stuName + "</td>" +
                            "<td width='15%' class='overflow' title='" + item.stuClass + "'>" + item.stuClass + "</td>" +
                            "<td width='18%' class='overflow' title='" + item.createTime + "'>" + item.createTime + "</td>" +
                            statehtml +
                            //"<td width='10%' class='clickable' title='" + item.status + "'>" + item.status + "</td>" +
                            "<td width='12%' title='" + item.stuPhone + "'>" + item.stuPhone + "</td>" +
                            "<td width='8%' class='clickable' onclick='show_personDetail(this)' name='" + item.teaName + "' title='" + item.teaName + "'>" + teahtml + "</td>" +
                            "<td width='8%' class='clickable' onclick='show_personDetail(this)' name='" + item.roomMates + "' title='" + item.roomMates.replace(/[,;]/g, '&#10') + "'>详情 </td>" +
                            "<td width='8%' class='clickable' onclick='show_personDetail(this)' name='" + item.friends + "' title='" + item.friends.replace(/[,;]/g, '&#10') + "'>详情 </td>" +
                            "</tr>";
                    });
                } else {
                    html = "<tr><td colspan='9'>无数据</td></tr>";
                }

                $("#table1 tbody").html(html);

                //分页信息
                var totalCount = page.total,
                    totalPage = page.pages,
                    curPage = page.current,
                    pageSize = page.size;

                laypage.render({
                    //容器
                    elem: $("#pagination"),
                    //数据总数
                    count: totalCount,
                    //每页显示条数
                    limit: pageSize,
                    //当前页
                    curr: curPage,
                    //连续出现的页码个数
                    groups: 3,
                    prev: '<',
                    next: '>',
                    first: '首页',
                    last: '尾页',
                    //当前分页所有选项值、是否首次
                    jump: function (obj, first) {
                        //首次不执行
                        if (!first) {
                            //获取点击页的数据
                            pagi_lossWarnStus(obj.curr);
                        }
                        if (totalCount) {
                            var firstR = 0, lastR = 0;
                            if (obj.curr == totalPage) {
                                lastR = totalCount;
                                firstR = pageSize * (obj.curr - 1) + 1;
                            } else {
                                firstR = (obj.curr - 1) * pageSize + 1;
                                lastR = obj.curr * pageSize;
                            }
                            $("#pageText").html("共 <span class='num'>" + totalPage + "</span> 页 ，" +
                                "<span class='num'>" + totalCount + "</span> 条，" +
                                " 当前第 <span class='num'>" + curPage + "</span>" +
                                " 页，第 <span class='num'>" + firstR + "</span> 条至" +
                                "第 <span class='num'>" + lastR + "</span> 条");
                        } else {
                            $("#pageText").html("");
                        }
                    }
                });
            }
        }
    });
}

/**
 * 分页显示失联地点统计信息
 * @param curr    当前页，为空默认显示第1页
 */
function pagi_lossAreaStatistic(curr) {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table2 .search_condition");
    //地点关键词
    var area = conditions.eq(0).children("input").val(),
        //年份开始时间
        startYear = conditions.eq(1).children("input.start").val(),
        //年份结束时间
        endYear = conditions.eq(1).children("input.end").val();
    //显示加载层
    $(".loading").show();
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getSiteStatistics',
        data: {
            siteName: area,
            startTime: startYear,
            endTime: endYear,
            pageNum: curr ? curr : 1,
            pageSize: pageSize
        },
        dataType: 'json',
        success: function (result) {
            var page = result.object;
            //隐藏加载层
            $(".loading").hide();
            if (page) {
                var html = "";
                if (page.records) {
                    $.each(page.records, function (i, item) {
                        //var n = ((curr ? curr : 1) - 1) * page.pageSize + i + 1;
                        html += "<tr data-id='" + item.uuid + "'>" +
                            "<td width='7%'>" + (((curr ? curr : 1) - 1) * page.pageSize + i + 1) + "</td>" +
                            "<td width='20%' class='clickable' onclick='show_lossSiteDetail(this)' title='" + item.siteName + "'>" + item.siteName + "</td>" +
                            "<td width='15%' title='" + item.countYear + "'>" + item.countYear + "</td>" +
                            "<td width='10%' title='" + item.actLevel + "'>" + item.actLevel + "</td>" +
                            "<td width='10%' title='" + item.amount + "'>" + item.amount + "</td>" +
                            "<td width='10%' title='" + item.responsible + "'>" + item.responsible + "</td>" +
                            "<td width='20%' title='" + item.siteTip + "'>" + item.siteTip + "</td>" +
                            "<td width='8%' title='" + item.monAmount + "'>" + item.monAmount + "</td>" +
                            "</tr>";
                    });
                } else {
                    html = "<tr><td colspan='8'>无数据</td></tr>";
                }

                $("#table2 tbody").html(html);

                //分页信息
                var totalCount = page.total,
                    totalPage = page.pages,
                    curPage = page.current,
                    pageSize = page.size;

                laypage.render({
                    //容器
                    elem: $("#pagination"),
                    //数据总数
                    count: totalCount,
                    //每页显示条数
                    limit: pageSize,
                    //当前页
                    curr: curPage,
                    //连续出现的页码个数
                    groups: 3,
                    prev: '<',
                    next: '>',
                    first: '首页',
                    last: '尾页',
                    //当前分页所有选项值、是否首次
                    jump: function (obj, first) {
                        //首次不执行
                        if (!first) {
                            //获取点击页的数据
                            pagi_lossAreaStatistic(obj.curr);
                        }
                        if (totalCount) {
                            var firstR = 0, lastR = 0;
                            if (obj.curr == totalPage) {
                                lastR = totalCount;
                                firstR = pageSize * (obj.curr - 1) + 1;
                            } else {
                                firstR = (obj.curr - 1) * pageSize + 1;
                                lastR = obj.curr * pageSize;
                            }
                            $("#pageText").html("共 <span class='num'>" + totalPage + "</span> 页 ，" +
                                "<span class='num'>" + totalCount + "</span> 条，" +
                                " 当前第 <span class='num'>" + curPage + "</span>" +
                                " 页，第 <span class='num'>" + firstR + "</span> 条至" +
                                "第 <span class='num'>" + lastR + "</span> 条");
                        } else {
                            $("#pageText").html("");
                        }
                    }
                });
            }
        }
    });
}

/**
 * 分页显示失联原因分析
 * @param curr    当前页，为空默认显示第1页
 */
function pagi_lossReason(curr) {
    //获取页面选择的查询条件
    var conditions = $("#table3 .search_condition");
    //姓名关键词
    var name = conditions.eq(0).children("input").val(),
        //学院
        collegeName = conditions.eq(1).children("input").val(),
        //原因
        reason = conditions.eq(2).children("input").val(),
        //年份开始时间
        startYear = conditions.eq(3).children("input.start").val(),
        //年份结束时间
        endYear = conditions.eq(3).children("input.end").val();
    //显示加载层
    $(".loading").show();
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getReasonStatistics',
        data: {
            stuName: name,
            stuClass: (collegeName == "全部") ? "" : collegeName,
            /*schoolYear: startYear,*/
            startTime: startYear,
            endTime: endYear,
            lossReason: reason,
            pageNum: curr ? curr : 1,
            pageSize: pageSize
        },
        dataType: 'json',
        success: function (result) {
            var page = result.object;
            //隐藏加载层
            $(".loading").hide();
            if (page) {
                var html = "";
                if (page.records) {
                    $.each(page.records, function (i, item) {
                        var time = "";
                        if (item.dealDuration.indexOf("天") != -1) {
                            var s = item.dealDuration.split(/[天|小时|分|秒]/);
                            time = s[0] + "天" + Math.round(parseFloat(s[1] * 1 + s[3] / 60 + s[4] / 3600) * 100) / 100 + "小时";
                        } else if (item.dealDuration.indexOf("小时") != -1) {
                            var s = item.dealDuration.split(/[小时|分|秒]/);
                            time = Math.round(parseFloat(s[0] * 1 + s[2] / 60 + s[3] / 3600) * 100) / 100 + "小时";
                        } else if (item.dealDuration.indexOf("分") != -1) {
                            var s = item.dealDuration.split(/[分|秒]/);
                            time = Math.round(parseFloat(s[0] / 60 + s[1] / 3600) * 100) / 100 + "小时";
                        } else if (item.dealDuration.indexOf("秒") != -1) {
                            time = Math.round(parseFloat(item.dealDuration.split(/秒/)[0] / 3600) * 100) / 100 + "小时";
                        }
                        html += "<tr data-id='" + item.uuid + "'>" +
                            "<td width='12%' title='" + item.stuId + "'>" + item.stuId + "</td>" +
                            "<td width='7%' class='clickable' onclick='show_lossReasonDetail(this)' title='" + item.stuName + "'>" + item.stuName + "</td>" +
                            "<td width='15%' class='overflow' title='" + item.stuClass + "'>" + item.stuClass + "</td>" +
                            /*"<td width='8%' title='" + item.place + "'>" + item.place + "</td>" +*/
                            "<td width='8%' title='" + item.schoolYear + "'>" + item.schoolYear + "</td>" +
                            "<td width='15%' class='overflow' title='" + item.lossTime + "'>" + item.lossTime + "</td>" +
                            "<td width='15%' class='overflow' title='" + item.findTime + "'>" + item.findTime + "</td>" +
                            "<td width='9%' title='" + item.findSite + "'>" + item.findSite + "</td>" +
                            "<td width='9%' title='" + item.lossReason + "'>" + item.lossReason + "</td>" +
                            "<td width='10%' class='overflow' title='" + time + "'>" + time + "</td>" +
                            "</tr>";
                    });
                } else {
                    html = "<tr><td colspan='9'>无数据</td></tr>";
                }

                $("#table3 tbody").html(html);

                //分页信息
                var totalCount = page.total,
                    totalPage = page.pages,
                    curPage = page.current,
                    pageSize = page.size;

                laypage.render({
                    //容器
                    elem: $("#pagination"),
                    //数据总数
                    count: totalCount,
                    //每页显示条数
                    limit: pageSize,
                    //当前页
                    curr: curPage,
                    //连续出现的页码个数
                    groups: 3,
                    prev: '<',
                    next: '>',
                    first: '首页',
                    last: '尾页',
                    //当前分页所有选项值、是否首次
                    jump: function (obj, first) {
                        //首次不执行
                        if (!first) {
                            //获取点击页的数据
                            pagi_lossReason(obj.curr);
                        }
                        if (totalCount) {
                            var firstR = 0, lastR = 0;
                            if (obj.curr == totalPage) {
                                lastR = totalCount;
                                firstR = pageSize * (obj.curr - 1) + 1;
                            } else {
                                firstR = (obj.curr - 1) * pageSize + 1;
                                lastR = obj.curr * pageSize;
                            }
                            $("#pageText").html("共 <span class='num'>" + totalPage + "</span> 页 ，" +
                                "<span class='num'>" + totalCount + "</span> 条，" +
                                " 当前第 <span class='num'>" + curPage + "</span>" +
                                " 页，第 <span class='num'>" + firstR + "</span> 条至" +
                                "第 <span class='num'>" + lastR + "</span> 条");
                        } else {
                            $("#pageText").html("");
                        }
                    }
                });
            }
        }
    });
}

/**
 * 失联学生预警模块下点击辅导员、亲密好友、宿舍人员
 */
function show_personDetail(obj) {
    var $this = $(obj),
        index = $this.index(),
        title = $this.parent().parent().parent().parent().siblings().find("th").eq(index).text(),
        name = $this.attr("name");
    parent.layer.open({
        title: title + "详细信息",
        area: ['400px', '200px'],
        skin: 'my-default',
        shade: 0.7,
        shadeClose: true,
        btn: [],
        success: function (layero, index) {
            var $layero = $(layero),
                $title = $layero.find(".layui-layer-title"),
                $content = $layero.find(".layui-layer-content"),
                _title = $title.text();
            $layero.prepend("<img src='image/datamode/layer.png'>");
            $title.html("<div class='mylay-title'>" +
                "<span>" + _title + "</span>" +
                "<hr class='mylay-title-left-hr'>" +
                "<hr class='mylay-title-right-hr'>" +
                "<div class='mylay-title-point'></div></div>");
            $layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
            $content.css("padding", "10px 20px");

            var html = "";
            if (name.indexOf(",") != -1) {
                var ss = name.split(",");
                $.each(ss, function (i, n) {
                    if (n) {
                        html += "<tr style='width:100%;color:white;font-size:16px;'><td width='50%' style='border:none;'>" + n.substring(0, n.indexOf('1')) + "</td>" +
                            "<td width='50%' style='border:none;'>电话：" + n.substring(n.indexOf('1')) + "</td></tr>";
                    }
                })
            } else {
                html += "<tr style='width:100%;color:white;font-size:16px;'><td width='50%' style='border:none;'>" + name.substring(0, name.indexOf('1')) + "</td>" +
                    "<td width='50%' style='border:none;'>电话：" + name.substring(name.indexOf('1')) + "</td></tr>";
            }
            $content.html(html);
        }
    });
}

/**
 * 显示失联学生状态流程信息
 */
function show_lossWarnRunTask(obj) {
    var $p_tr = $(obj).parent(),
        id = $p_tr.attr("data-ucId"),
        name = $p_tr.children("td").eq(1).text(),
        collegeName = $p_tr.children("td").eq(2).text(),
        phoneNumber = $(obj).next().text();
    var o = {
        "id": id,
        "name": name,
        "collegeName": collegeName,
        "phoneNumber": phoneNumber
    }
    parent.layer.open({
        type: 2,
        title: "失联实时状态",
        area: ['610px', '320px'],
        skin: 'my-default',
        shade: 0.7,
        shadeClose: true,
        btn: [],
        content: 'page/datamode/lossWarnLayer/lossWarnRunTask.htm',
        success: function (layero, index) {
            var $layero = $(layero),
                $title = $layero.find(".layui-layer-title"),
                $content = $layero.find(".layui-layer-content"),
                _title = $title.text();
            $layero.prepend("<img src='image/datamode/layer.png'>");
            $title.html("<div class='mylay-title'>" +
                "<span>" + _title + "</span>" +
                "<hr class='mylay-title-left-hr'>" +
                "<hr class='mylay-title-right-hr'>" +
                "<div class='mylay-title-point'></div></div>");
            $layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
            $content.css("padding", "10px 20px");

            var iframeWin = parent.window[layero.find("iframe")[0]['name']];	//得到iframe窗口对象
            iframeWin.open_success(o);
        }
    });
}

/**
 * 弹窗显示某失联地点的失联信息统计
 */
function show_lossSiteDetail(obj) {
    var $this = $(obj),
        placeName = $this.text(),
        year = $this.next().text();
    var o = {
        "placeName": placeName,
        "year": year
    }
    parent.layer.open({
        type: 2,
        title: "失联地事件数",
        area: ['700px', '400px'],
        skin: 'my-default',
        shade: 0.7,
        shadeClose: true,
        btn: [],
        content: 'page/datamode/lossWarnLayer/lossWarnSiteStatistic.htm',
        success: function (layero, index) {
            var $layero = $(layero),
                $title = $layero.find(".layui-layer-title"),
                $content = $layero.find(".layui-layer-content"),
                _title = $title.text();
            $layero.prepend("<img src='image/datamode/layer.png'>");
            $title.html("<div class='mylay-title'>" +
                "<span>" + _title + "</span>" +
                "<hr class='mylay-title-left-hr'>" +
                "<hr class='mylay-title-right-hr'>" +
                "<div class='mylay-title-point'></div></div>");
            $layero.find(".layui-layer-ico").html("<img width='100%' height='100%' src='image/close.png'>");
            //$content.css("padding", "10px 20px");

            var iframeWin = parent.window[layero.find("iframe")[0]['name']];	//得到iframe窗口对象
            iframeWin.open_success(o);
        }
    });
}

function show_lossReasonDetail(obj) {
    var uuid = $(obj).parent().attr("data-id"),
        name = $(obj).text();
    parent.layer.open({
        title: "学生标签：" + name,
        area: ['400px', '200px'],
        skin: 'my-default',
        shade: 0.7,
        shadeClose: true,
        btn: [],
        success: function (layero, index) {
            var $layero = $(layero),
                $title = $layero.find(".layui-layer-title"),
                $content = $layero.find(".layui-layer-content"),
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
                url: '/lossContactWarn/getReasonById',
                data: {
                    uuid: uuid
                },
                dataType: 'json',
                success: function (result) {
                    var data = result.object;
                    if (data) {
                        //组成关键字数组
                        var keywords = data.stuTag.split(/[,;]/);
                        //词云数组，必须包含value属性
                        var d = [];
                        $.each(keywords, function (i, n) {
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
                                //字体大小范围
                                sizeRange: [16, 16],
                                //字体旋转角度区间
                                rotationRange: [-45, 45],
                                //字符间距
                                gridSize: 16,
                                bottom: 10,
                                left: 'center',
                                top: 'center',
                                textStyle: {
                                    normal: {
                                        color: function () {
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
    });
}

function top10_lossNumber() {
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getTopWarn',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<tr>" +
                        "<td width='20%'>" + (i + 1) + "</td>" +
                        "<td width='40%' title='" + item.college + "'>" + item.college + "</td>" +
                        "<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
                        "</tr>";
                })
            }
            else {
                html = "<tr><td colspan='3'>无数据</td></tr>";
            }
            $('#top10_1 tbody').html(html);
        }
    });
}

function top10_lossArea() {
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getTopSite',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<tr>" +
                        "<td width='20%'>" + (i + 1) + "</td>" +
                        "<td width='40%' title='" + item.siteName + "'>" + item.siteName + "</td>" +
                        "<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
                        "</tr>";
                })
            }
            else {
                html = "<tr><td colspan='3'>无数据</td></tr>";
            }
            $('#top10_2 tbody').html(html);
        }
    });
}

function top10_lossReason() {
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getTopReason',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<tr>" +
                        "<td width='20%'>" + (i + 1) + "</td>" +
                        "<td width='40%' title='" + item.lossReason + "'>" + item.lossReason + "</td>" +
                        "<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
                        "</tr>";
                })
            }
            else {
                html = "<tr><td colspan='3'>无数据</td></tr>";
            }
            $('#top10_3 tbody').html(html);
        }
    });
}


/**
 * 导出失联预警学生名单
 */
function export_Warn() {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table1 .search_condition");
    var name = conditions.eq(0).children("input").val(),	//姓名关键词
        collegeName = conditions.eq(1).children("input").val(),	//学院
        startYear = conditions.eq(2).children("input.start").val(),	//预警时间-开始时间
        endYear = conditions.eq(2).children("input.end").val(),	//预警时间-结束时间
        state = conditions.eq(3).children("input").val();	//失联状态

    var index = parent.layer.load(2, {shade: 0.3});
    ;
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getLossWarnIncidents',
        data: {
            stuClass: (collegeName == "全部") ? "" : collegeName,
            stuName: name,
            dealStatus: (state == "全部") ? "" : state,
            startTime: startYear,
            endTime: endYear,
            pageNum: 0,
            pageSize: 0
        },
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            if (data.records != null) {
                //表头
                var title = ["学号", "姓名", "院系", "预警时间", "最新状态", "最新处理时间", "辅导员",
                    "亲密朋友", "室友", "学生手机号", "最后出现时间", "最后出现地点"];
                //数据
                var datarows = [];
                $.each(data.records, function (i, o) {
                    //每一行的数据
                    var datarow = [];
                    //按表头字段顺序填入数据
                    datarow.push(o.stuId);
                    datarow.push(o.stuName);
                    datarow.push(o.stuClass);
                    datarow.push(o.createTime);
                    datarow.push(o.status);
                    datarow.push(o.dealTime);
                    datarow.push(o.teaName);
                    datarow.push(o.friends);
                    datarow.push(o.roomMates);
                    datarow.push(o.stuPhone);
                    datarow.push(o.lastShowTime);
                    datarow.push(o.lastShowSite);

                    datarows.push(datarow);
                });
                //生成的文件名
                var fileName = '失联预警名单';
                //导出
                exportToExcel(title, datarows, fileName);
            }
            parent.layer.close(index);
        }
    });
}

/**
 * 导出失联地点统计信息
 */
function export_Area() {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table2 .search_condition");
    var area = conditions.eq(0).children("input").val(),	//地点关键词
        startYear = conditions.eq(1).children("input.start").val(),	//年份开始时间
        endYear = conditions.eq(1).children("input.end").val();	//年份结束时间

    var index = parent.layer.load(2, {shade: 0.3});
    ;
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getSiteStatistics',
        data: {
            siteName: area,
            startTime: startYear,
            endTime: endYear,
            pageNum: 0,
            pageSize: 0
        },
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            if (data.records != null) {
                var title = ["地点名", "统计年份", "失联总人数", "监控个数", "活跃指数(1-10)", "区域责任人", "位置信息"];  //表头
                var datarows = [];      //数据
                $.each(data.records, function (i, o) {
                    var datarow = [];   //每一行的数据
                    //按表头字段顺序填入数据
                    datarow.push(o.siteName);
                    datarow.push(o.countYear);
                    datarow.push(o.amount);
                    datarow.push(o.monAmount);
                    datarow.push(o.actLevel);
                    datarow.push(o.responsible);
                    datarow.push(o.siteTip);

                    datarows.push(datarow);
                });
                var fileName = '失联地点统计信息';//生成的文件名
                //导出
                exportToExcel(title, datarows, fileName);
            }
            parent.layer.close(index);
        }
    });
}

/**
 * 导出失联原因统计信息
 */
function export_Reason() {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table3 .search_condition");
    var name = conditions.eq(0).children("input").val(),	//姓名关键词
        collegeName = conditions.eq(1).children("input").val(),	//学院
        reason = conditions.eq(2).children("input").val(),	//原因
        startYear = conditions.eq(3).children("input.start").val(),	//年份开始时间
        endYear = conditions.eq(3).children("input.end").val();	//年份结束时间

    var index = parent.layer.load(2, {shade: 0.3});
    ;
    $.ajax({
        type: 'post',
        url: '/lossContactWarn/getReasonStatistics',
        data: {
            stuName: name,
            stuClass: (collegeName == "全部") ? "" : collegeName,
            /*schoolYear: startYear,*/
            startTime: startYear,
            endTime: endYear,
            lossReason: reason,
            pageNum: 0,
            pageSize: 0
        },
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            if (data.records != null) {
                var title = ["学号", "姓名", "院系", "生源地", "入学年份",
                    "失联时间", "找到时间", "找到地点",
                    "处理时长", "失联原因", "学生标签"];  //表头
                var datarows = [];      //数据
                $.each(data.records, function (i, o) {
                    var datarow = [];   //每一行的数据
                    //按表头字段顺序填入数据
                    datarow.push(o.stuId);
                    datarow.push(o.stuName);
                    datarow.push(o.stuClass);
                    datarow.push(o.place);
                    datarow.push(o.schoolYear);
                    datarow.push(o.lossTime);
                    datarow.push(o.findTime);
                    datarow.push(o.findSite);
                    datarow.push(o.dealDuration);
                    datarow.push(o.lossReason);
                    datarow.push(o.stuTag);

                    datarows.push(datarow);
                });
                var fileName = '失联原因统计信息';//生成的文件名
                //导出
                exportToExcel(title, datarows, fileName);
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
function exportToExcel(title, data, fileName) {
    if (data != null) {
        //导出到excel
        var option = {};//一张sheet
        option.fileName = fileName + "_" + new Date().getTime();
        option.datas = [
            {
                sheetData: data,//数据源
                sheetName: 'sheet1',//sheet名
                sheetHeader: title,//表头
            }
        ];
        var toExcel = new ExportJsonExcel(option);
        toExcel.saveExcel();
    } else {
        alert("无数据导出");
    }
}

/**
 * 显示最近5条通知通告
 */
function top5_notice() {
    $.ajax({
        type: 'post',
        url: '/preciseFunding/findTopNotice',
        dataType: 'json',
        data: {
            belong: '失联预警'
        },
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<li>" +
                        "<span class='notice_title'>" + item.noticeTitle + "</span>" +
                        "<span class='notice_time'>" + item.noticeTime + "</span>" +
                        "<div class='notice_content'>" + item.noticeContent + "</div>" +
                        "</li>";
                })
            }
            else {	//数据为空
                html = "<div style='color: #82ABC1;text-align:center;'>没有最新的通知通告</div>";
            }
            $('#notice ul').html(html);
        }
    });
}

