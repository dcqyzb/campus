
/**
 * 时间轴上的年月切换
 * @param obj	点击的对象
 * @param chart	绑定的echarts实例
 * @param isCurYear	点击【按年】时是否需要当前年，为true时表示包含当前年，为false或空时表示不包含当前年
 * @param isCurMonth	点击【按月】时是否需要当前月，为true时表示包含当前月，为false或空时表示不包含当前月
 */
function changeTimeline(obj, chart, isCurYear, isCurMonth){
	var $obj = $(obj), 
		_index = $obj.index(),	//索引值
		_class = $obj.attr("class"),	//获取class判断是‘年’还是‘月’还是‘日’
		_isYear = (_class.indexOf("year") !== -1),	//是否是‘年’
		_isMonth = (_class.indexOf("month") !== -1);	//是否是‘月’
	
	$obj.addClass("selected").siblings().removeClass("selected");
	var data, _data_len;	
	if(_isYear){	//点击【按年】
		data = mGetTimelineData("year", isCurYear);
	}else if(_isMonth){	//点击【按月】
		data = mGetTimelineData("month", isCurMonth);
	}else {
		data = mGetTimelineData("day");
	}
	_data_len = data.length;

	//配置option
	chart.setOption({
		baseOption: {
			timeline: {
				currentIndex: _data_len - 1,
				label: {
					formatter : function(s) {	
						var text;
						if(_isYear){
							text = s + "年";
						}else if(_isMonth){
							var yyyyMM = s.split("-"), m = parseInt(yyyyMM[1]);
							//if(m == 1){
							//	text = m + "月\n" + yyyyMM[0];
							//}else{
								text = m + "月";
							//}
						}else{
							text = s + "日";
						}
	                    return text;
	                }
				},
				data: data
			}
		}
	});
	//默认点击触发最后一个时间
	chart.dispatchAction({type: 'timelineChange', currentIndex: _data_len - 1 });
}

/**
 * 获取时间轴的数据
 * @param str	要获取的时间类别，为'year'表示获取年，为'month'表示获取月，为'day'表示获取日，为‘term’表示获取学期
 * @param isCur	表示是否包含当前年或月，为true时包含，为false或空时表示不包含
 * @returns {Array}
 */
function mGetTimelineData(str, isCur){
	if(!str){	//参数空判断
		return;
	}
	var curDate = new Date(),	//获取当前时间
		curYear = curDate.getFullYear(),	//获取当前年份
		curMonth = curDate.getMonth(),	//获取当前月份
		returnArray = [];	//返回的数组
	
	switch(str){
	case 'year':
		var years = mGetLast5Years(isCur);	//获取近五年
		for(var i=0, len=years.length; i<len; i++){
			returnArray.push({value: years[i], tooltip: {show: false}});
		}
		break;
	case 'month':
		//var months = mGetLast12Months(isCur);	//获取最近12个月
		var months = mGet12Months();
		for(var i=0, len=months.length; i<len; i++){
			returnArray.push({value: months[i], tooltip: {show: false}});
		}
		break;
	case 'day':
		var days = mGetDate(curYear, (curMonth == 0) ? 12 : curMonth);	//获取上月的总天数
		for(var i=0; i<days; i++){
			returnArray.push({value: i+1, tooltip: {show: false}});
		}
		break;
	case 'term':
		var term;
		//获取近五年的所有学期
		for(var i = curYear-5; i<curYear; i++){
			term = (i)+"上";
			returnArray.push({value: term, tooltip: {show: false}});
			term = (i)+"下";
			returnArray.push({value: term, tooltip: {show: false}});
		}
		break;
	default:
		returnArray = null;
		break;
	}
	return returnArray;
}

/**
 * 获取最近5年
 * @author leoya
 * @param flag	布尔值，是否包含当前年，为true时包含，为false不包含，默认即为空时不包含
 * @returns {Array}	近五年的数组
 */
function mGetLast5Years(flag){
	var result = [], d = new Date(),	//获取当前时间
		curYear = d.getFullYear();	//当前年
	for(var i=0; i<5; i++){
		var y;
		if(i == 0 && flag){
			//需包含当前年
			y = curYear;
		}else{
			y = curYear - 1;
			curYear--;
		}
		result.push(y);
	}
	return result.reverse();
}

//获取2017年12个月
function mGet12Months(){
	var result = [];
	for(var i = 0; i < 12; i++) {
	    var m = (i+1) < 10 ? "0" + (i+1) : (i+1);	//小于10的前面加0 
		result.push("2017-" + m);   
	} 
	return result;
}

/**
 * 获取过去12个月
 * @author leoya
 * @param flag	布尔值，是否包含当前月，为true时包含，为false不包含，默认即为空时不包含
 * @returns {Array}	12个月的数组，格式为'yyyy-MM'
 */
function mGetLast12Months(flag){
	var result = [], d = new Date();	//获取当前时间
    for(var i = 0; i < 12; i++) {
    	if(i == 0 && flag){
    		//当需包含当前月
    	}else{
            d.setMonth(d.getMonth() - 1);  
    	}
        var m = d.getMonth() + 1;  
        m = m < 10 ? "0" + m : m;	//小于10的前面加0  
        //在这里可以自定义输出的日期格式  
        result.push(d.getFullYear() + "-" + m);   
    }  
    return result.reverse();  
}

/**
 * 获取某年某月的天数
 * @param year	要获取的年份
 * @param month	要获取的月份
 * @returns {Number}	返回该年该月的天数	
 */
function mGetDate(year, month){
	if(!year || !month){	//判断参数不为空
		return;
	}
	var d = new Date(year, month, 0);
	return d.getDate();
}

