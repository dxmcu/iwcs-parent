package com.wisdom.iwcs.service.task.intf;

import com.wisdom.iwcs.common.utils.Result;
import com.wisdom.iwcs.domain.task.PlBufSupplyRequest;

public interface IPlBufSupplyService {
    Result plBufSupply(PlBufSupplyRequest plBufSupplyRequest);
}
