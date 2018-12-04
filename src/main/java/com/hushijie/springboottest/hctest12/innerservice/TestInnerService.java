package com.hushijie.springboottest.hctest12.innerservice;

import com.hushijie.springboottest.hctest12.model.TestDO;
import com.hushijie.springboottest.hctest12.mapper.TestMapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 测试表 通用能力下沉服务类
 * </p>
 *
 * @author wubin
 * @since 2018-12-03
 */
public interface TestInnerService extends IService<TestDO> {
    /**
     * 获取TestMapper接口
     *
     * @return TestMapper
     */
    TestMapper getMapper();
}
