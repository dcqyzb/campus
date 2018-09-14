package com.mit.campus.rest.modular.electricalBehavior.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mit.campus.rest.modular.electricalBehavior.model.ElectricCollegeDormUnclose;

/**
 * 
* 二级页面-宿舍未关电器详情
* @author shuyy
* @date 2018年9月7日
 */
public interface IElectricCollegeDormUncloseService extends IService<ElectricCollegeDormUnclose> {

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
	List<List<ElectricCollegeDormUnclose>> getUncloseDetail(String dormID, String date);
}
