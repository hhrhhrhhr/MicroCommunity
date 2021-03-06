package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeFeeConfigConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteFeeConfigListener")
public class DeleteFeeConfigListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFeeBMO feeBMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "configId", "费用项ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        feeConfigDto.setIsDefault("T");
        int feeCount = feeConfigInnerServiceSMOImpl.queryFeeConfigsCount(feeConfigDto);
        if (feeCount > 0) {
            throw new IllegalArgumentException("该费用项目不能删除");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        feeBMOImpl.deleteFeeConfig(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeFeeConfigConstant.DELETE_FEECONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
