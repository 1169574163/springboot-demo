package com.hushijie.springboottest.hctest12.innerservice.impl;

import com.hushijie.springboottest.hctest12.model.TestDO;
import com.hushijie.springboottest.hctest12.mapper.TestMapper;
import com.hushijie.springboottest.hctest12.innerservice.TestInnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试表 通用能力下沉服务实现类
 * </p>
 *
 * @author wubin
 * @since 2018-12-03
 */
@Service
public class TestInnerServiceImpl extends ServiceImpl<TestMapper, TestDO> implements TestInnerService {
    @Override
    public TestMapper getMapper() {
        return this.baseMapper;
    }
}
