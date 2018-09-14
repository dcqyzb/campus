package com.mit.campus.rest.modular.electricalBehavior.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mit.campus.rest.modular.electricalBehavior.dao.ElectricCollegeDormUncloseMapper;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormUnclose;
import com.mit.campus.rest.modular.electricalBehavior.service.IElectricCollegeDormUncloseService;

/**
 * 
* 二级页面-宿舍未关电器详情
* @author shuyy
* @date 2018年9月7日
 */
@Service
public class ElectricCollegeDormUncloseServiceImpl
        extends ServiceImpl<ElectricCollegeDormUncloseMapper, ElectricCollegeDormUnclose>
        implements IElectricCollegeDormUncloseService {
    @Autowired
    private ElectricCollegeDormUncloseMapper electricCollegeDormUncloseMapper;

    
    /**
     * 
    * 
    * 二级页面-获取宿舍该月未关电器记录
    * @param @param dormID
    * @param @param date
    * @param @return
    * @return List<List<ElectricCollegeDormUnclose>>
    * @throws
    * @author shuyy
     */
    @Override
	public List<List<ElectricCollegeDormUnclose>> getUncloseDetail(String dormID, String date){
		EntityWrapper<ElectricCollegeDormUnclose> wrapper = new EntityWrapper<>();
		wrapper.eq("dormID", dormID).and("date like {0}", date+"%");
		String orderby = " order by o.date desc";
		String groupby = " group by o.date";
		List<List<ElectricCollegeDormUnclose>> all = new ArrayList<List<ElectricCollegeDormUnclose>>();
		try{
			Wrapper<ElectricCollegeDormUnclose> wrapper2 = wrapper.clone();
			wrapper.orderBy("date", false);
			List<ElectricCollegeDormUnclose> list = electricCollegeDormUncloseMapper.selectList(wrapper);
			wrapper2.groupBy("date");
			wrapper.orderBy("date", false);
			List<ElectricCollegeDormUnclose> date_list =electricCollegeDormUncloseMapper.selectList(wrapper2);
			all.add(date_list);
			all.add(list);
			return all;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
