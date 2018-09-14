/**
 *    数据模式下的招生决策js
 */

var pageSize = 18;	//分页每页显示记录条数
var laypage;	//定义分页的全局变量

/*点击导航切换*/
$("ul.nav_ul li").click(function () {
    var $this = $(this), curIndex = $this.index();
    //加载数据前清空所有的查询条件
    $(".clear_btn").click();
    switch (curIndex) {
        case 0:
            pagi_interviewSource();	//访问来源统计
            $(".rank .title span").text("城市访问量Top10");
            top10_interviewSource(); //城市访问top10
            break;
        case 1:
            pagi_employmentStatus();	//就业情况统计
            $(".rank .title span").text("就业地人数Top10");
            top10_employmentStatus(); //就业地人数top10
            break;
        case 2:
            pagi_interviewTime();	//访问时间统计
            $(".rank .title span").text("访问量Top10");
            top10_interviewTime(); //访问量top10
            break;
        default:
            break;
    }
});

/*页面渲染完成执行*/
$(document).ready(function () {
    /*
     * 调整查询条件样式，当查询条件太多导致换行时调整其上高度
     */
    var w = $(".center_table").width();
    /*if(w < 1144){
        $("#table1 .search_area>div").css("top", "20px");
    }
    if(w < 1085){
        $("#table2 .search_area>div").css("top", "20px");
    }*/
    //赋值laypage
    layui.use('laypage', function () {
        laypage = layui.laypage;

        //默认点击左边导航条第一个
        $("ul.nav_ul li").eq(0).click();
    });

    //通知通告
    top5_notice();
});

/**
 * 访问来源统计
 */
function pagi_interviewSource(curr) {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table1 .search_condition");
    var province = conditions.eq(0).children("input").val(),	//省市关键词
        city = conditions.eq(1).children("input").val(),	//省市关键词
        startYear = conditions.eq(2).children("input.start").val(),	//统计开始时间
        endYear = conditions.eq(2).children("input.end").val();	//统计结束时间
    $(".loading").show();	//显示加载层
    $.ajax({
        type: 'post',
        url: '/enrollDes/getVisitCitySource',
        data: {
            province: province,
            city: city,
            startTime: startYear,
            endTime: endYear,
            pageNum: curr ? curr : 1,
            pageSize: pageSize
        },
        dataType: 'json',
        success: function (result) {
            var page = result.object;
            $(".loading").hide();	//隐藏加载层
            if (page) {
                var html = "";
                if (page.records) {
                    $.each(page.records, function (i, item) {
                        html += "<tr data-id='" + item.uuid + "'>" +
                            "<td width='10%' title='" + item.province + "'>" + item.province + "</td>" +
                            "<td width='10%' title='" + item.city + "'>" + item.city + "</td>" +
                            "<td width='10%' title='" + item.month + "'>" + item.month + "</td>" +
                            "<td width='9%'>" + item.counts + "</td>" +
                            "<td width='9%'>" + item.qihuCount + "</td>" +
                            "<td width='9%'>" + item.baiduCount + "</td>" +
                            "<td width='9%'>" + item.sougouCount + "</td>" +
                            "<td width='9%'>" + item.schoolCount + "</td>" +
                            "<td width='9%'>" + item.enrollCount + "</td>" +
                            "<td width='9%'>" + item.eduSiteCount + "</td>" +
                            "<td width='7%'>" + item.otherCount + "</td>" +
                            "</tr>";
                    });
                } else {	//数据为空
                    html = "<tr><td colspan='11'>无数据</td></tr>";
                }

                $("#table1 tbody").html(html);

                //分页信息
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
                    jump: function (obj, first) {	//当前分页所有选项值、是否首次
                        if (!first) {	//首次不执行
                            pagi_interviewSource(obj.curr);	//获取点击页的数据
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
 * 就业情况统计
 */
function pagi_employmentStatus(curr) {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table2 .search_condition");
    var collegeName = conditions.eq(0).children("input").val(),	//学院
        city = conditions.eq(1).children("input").val(),	//就业地
        employmentArea = conditions.eq(2).children("input").val(),	//就业地
        startYear = conditions.eq(3).children("input.start").val(),	//统计开始时间
        endYear = conditions.eq(3).children("input.end").val();	//统计结束时间
    $(".loading").show();	//显示加载层
    $.ajax({
        type: 'post',
        url: '/enrollDes/getVisitJob',
        data: {
            college: (collegeName == "全部") ? "" : collegeName,
            city: (city == "全部") ? "" : city,
            workArea: (employmentArea == "全部") ? "" : employmentArea,
            startTime: startYear,
            endTime: endYear,
            pageNum: curr ? curr : 1,
            pageSize: pageSize
        },
        dataType: 'json',
        success: function (result) {
            var page = result.object;
            $(".loading").hide();	//隐藏加载层
            if (page) {
                var html = "";
                if (page.records) {
                    $.each(page.records, function (i, item) {
                        html += "<tr data-id='" + item.uuid + "'>" +
                            "<td width='20%' title='" + item.college + "'>" + item.college + "</td>" +
                            "<td width='15%' >" + item.graduteYear + "</td>" +
                            "<td width='10%'>" + item.counts + "</td>" +
                            "<td width='10%'>" + item.manCount + "</td>" +
                            "<td width='10%'>" + item.womanCount + "</td>" +
                            "<td width='20%'>" + item.city + "</td>" +
                            "<td width='15%'>" + item.workArea + "</td>" +
                            "</tr>";
                    });
                } else {	//数据为空
                    html = "<tr><td colspan='7'>无数据</td></tr>";
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
                    first: '首页',
                    last: '尾页',
                    jump: function (obj, first) {	//当前分页所有选项值、是否首次
                        if (!first) {	//首次不执行
                            pagi_employmentStatus(obj.curr);	//获取点击页的数据
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
 * 访问时间统计
 */
function pagi_interviewTime(curr) {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table3 .search_condition");
    var startYear = conditions.eq(0).children("input.start").val(),	//统计开始时间
        endYear = conditions.eq(0).children("input.end").val();	//统计结束时间
    $(".loading").show();	//显示加载层
    $.ajax({
        type: 'post',
        url: '/enrollDes/getVisitTime',
        data: {
            startTime: startYear,
            endTime: endYear,
            pageNum: curr ? curr : 1,
            pageSize: pageSize
        },
        dataType: 'json',
        success: function (result) {
            var page = result.object;
            $(".loading").hide();	//隐藏加载层
            if (page) {
                var html = "";
                if (page.records) {
                    $.each(page.records, function (i, item) {
                        html += "<tr data-id='" + item.uuid + "'>" +
                            "<td width='10%' title='" + item.year + "'>" + item.year + "</td>" +
                            "<td width='10%' >" + item.count + "</td>" +
                            "<td width='7%'>" + item.jan + "</td>" +
                            "<td width='7%'>" + item.feb + "</td>" +
                            "<td width='7%'>" + item.mar + "</td>" +
                            "<td width='7%'>" + item.apr + "</td>" +
                            "<td width='7%'>" + item.may + "</td>" +
                            "<td width='7%'>" + item.june + "</td>" +
                            "<td width='7%'>" + item.july + "</td>" +
                            "<td width='7%'>" + item.aug + "</td>" +
                            "<td width='7%'>" + item.sep + "</td>" +
                            "<td width='7%'>" + item.oct + "</td>" +
                            "<td width='7%'>" + item.nov + "</td>" +
                            "<td width='7%'>" + item.dece + "</td>" +
                            "</tr>";
                    });
                } else {	//数据为空
                    html = "<tr><td colspan='14'>无数据</td></tr>";
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
                    first: '首页',
                    last: '尾页',
                    jump: function (obj, first) {	//当前分页所有选项值、是否首次
                        if (!first) {	//首次不执行
                            pagi_interviewTime(obj.curr);	//获取点击页的数据
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

function export_interviewSource() {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table1 .search_condition");
    var province = conditions.eq(0).children("input").val(),	//省市关键词
        city = conditions.eq(1).children("input").val(),	//省市关键词
        startYear = conditions.eq(2).children("input.start").val(),	//统计开始时间
        endYear = conditions.eq(2).children("input.end").val();	//统计结束时间
    var index = parent.layer.load(2, {shade: 0.3});
    ;
    $.ajax({
        type: 'post',
        url: '/enrollDes/getVisitCitySource',
        dataType: 'json',
        data: {
            province: province,
            city: city,
            startTime: startYear,
            endTime: endYear,
            pageNum: 0,
            pageSize: 0
        },
        success: function (result) {
            var data = result.object;
            if (data.records != null) {
                var title = ["省", "市", "年月", "总访问量", "360搜索", "百度搜索", "搜狗搜索", "学校官网", "学校招生网", "省教育网", "其它"];  //表头
                var datarows = [];      //数据
                $.each(data.records, function (i, o) {
                    var datarow = [];   //每一行的数据
                    //按表头字段顺序填入数据
                    datarow.push(o.province);
                    datarow.push(o.city);
                    datarow.push(o.month);
                    datarow.push(o.counts);
                    datarow.push(o.qihuCount);
                    datarow.push(o.baiduCount);
                    datarow.push(o.sougouCount);
                    datarow.push(o.schoolCount);
                    datarow.push(o.enrollCount);
                    datarow.push(o.eduSiteCount);
                    datarow.push(o.otherCount);

                    datarows.push(datarow);
                });
                var fileName = '学校招生网访问来源统计';//生成的文件名
                //导出
                exportToExcel(title, datarows, fileName);
            }
            parent.layer.close(index);
        }
    });
}

function export_employmentStatus() {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table2 .search_condition");
    var collegeName = conditions.eq(0).children("input").val(),	//学院
        city = conditions.eq(1).children("input").val(),	//就业地
        employmentArea = conditions.eq(2).children("input").val(),	//就业地
        startYear = conditions.eq(3).children("input.start").val(),	//统计开始时间
        endYear = conditions.eq(3).children("input.end").val();	//统计结束时间
    var index = parent.layer.load(2, {shade: 0.3});
    ;
    $.ajax({
        type: 'post',
        url: '/enrollDes/getVisitJob',
        dataType: 'json',
        data: {
            college: (collegeName == "全部") ? "" : collegeName,
            city: (city == "全部") ? "" : city,
            workArea: (employmentArea == "全部") ? "" : employmentArea,
            startTime: startYear,
            endTime: endYear,
            pageNum: 0,
            pageSize: 0
        },
        success: function (result) {
            var data = result.object;
            if (data.records != null) {
                var title = ["学院", "毕业时间", "总人数", "男", "女", "就业地", "地区"];  //表头
                var datarows = [];      //数据
                $.each(data.records, function (i, o) {
                    var datarow = [];   //每一行的数据
                    //按表头字段顺序填入数据
                    datarow.push(o.college);
                    datarow.push(o.graduteYear);
                    datarow.push(o.counts);
                    datarow.push(o.manCount);
                    datarow.push(o.womanCount);
                    datarow.push(o.city);
                    datarow.push(o.workArea);

                    datarows.push(datarow);
                });
                var fileName = '学校就业情况统计';//生成的文件名
                //导出
                exportToExcel(title, datarows, fileName);
            }
            parent.layer.close(index);
        }
    });
}

function export_interviewTime() {
    /*
     * 获取页面选择的查询条件
     */
    var conditions = $("#table3 .search_condition");
    var startYear = conditions.eq(0).children("input.start").val(),	//统计开始时间
        endYear = conditions.eq(0).children("input.end").val();	//统计结束时间
    var index = parent.layer.load(2, {shade: 0.3});
    ;
    $.ajax({
        type: 'post',
        url: '/enrollDes/getVisitTime',
        dataType: 'json',
        data: {
            startTime: startYear,
            endTime: endYear,
            pageNum: 0,
            pageSize: 0
        },
        success: function (result) {
            var data = result.object;
            if (data.records != null) {
                var title = ["年份", "访问总量", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];  //表头
                var datarows = [];      //数据
                $.each(data.records, function (i, o) {
                    var datarow = [];   //每一行的数据
                    //按表头字段顺序填入数据
                    datarow.push(o.year);
                    datarow.push(o.count);
                    datarow.push(o.jan);
                    datarow.push(o.feb);
                    datarow.push(o.mar);
                    datarow.push(o.apr);
                    datarow.push(o.may);
                    datarow.push(o.june);
                    datarow.push(o.july);
                    datarow.push(o.aug);
                    datarow.push(o.sep);
                    datarow.push(o.oct);
                    datarow.push(o.nov);
                    datarow.push(o.dece);

                    datarows.push(datarow);
                });
                var fileName = '访问时间统计';//生成的文件名
                //导出
                exportToExcel(title, datarows, fileName);
            }
            parent.layer.close(index);
        }
    });
}

/**
 * 城市访问量统计top10
 */
function top10_interviewSource() {
    $.ajax({
        type: 'post',
        url: '/enrollDes/getTopVisitCity',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<tr>" +
                        "<td width='20%'>" + (i + 1) + "</td>" +
                        "<td width='40%' title='" + item.city + "'>" + item.city + "</td>" +
                        "<td width='40%' title='" + item.total + "'>" + item.total + "</td>" +
                        "</tr>";
                })
            }
            else {	//数据为空
                html = "<tr><td colspan='3'>无数据</td></tr>";
            }
            $('#top10_1 tbody').html(html);
        }
    });
}

/**
 * 就业人数top10地区
 */
function top10_employmentStatus() {
    $.ajax({
        type: 'post',
        url: '/enrollDes/getTopJob',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<tr data-id='" + item.uuid + "'>" +
                        "<td width='20%'>" + (i + 1) + "</td>" +
                        "<td width='40%' title='" + item.city + "'>" + item.city + "</td>" +
                        "<td width='40%' title='" + item.total + "人'>" + item.total + "人</td>" +
                        "</tr>";
                })
            }
            else {	//数据为空
                html = "<tr><td colspan='3'>无数据</td></tr>";
            }
            $('#top10_2 tbody').html(html);
        }
    });
}

/**
 * 访问时间top10
 */
function top10_interviewTime() {
    $.ajax({
        type: 'post',
        url: '/enrollDes/getTopTime',
        dataType: 'json',
        success: function (result) {
            var data = result.object;
            var html = "";
            if (data) {
                $.each(data, function (i, item) {
                    html += "<tr data-id='" + item.uuid + "'>" +
                        "<td width='20%'>" + (i + 1) + "</td>" +
                        "<td width='40%' title='" + item.year + "'>" + item.year + "</td>" +
                        "<td width='40%' title='" + item.count + "'>" + item.count + "</td>" +
                        "</tr>";
                })
            }
            else {	//数据为空
                html = "<tr><td colspan='3'>无数据</td></tr>";
            }
            $('#top10_3 tbody').html(html);
        }
    });
}

/**
 * 显示最近5条通知通告
 */
function top5_notice() {
    $.ajax({
        type: 'post',
        url: '/campusbd/api/preciseFunding/findTopNotice',
        dataType: 'json',
        data: {
            belong: '招生决策'
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
};

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


