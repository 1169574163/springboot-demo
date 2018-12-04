package com.hushijie.springboottest.hctest12.repository.impl;

import com.hushijie.springboottest.hctest12.model.TestDO;
import com.hushijie.springboottest.hctest12.mapper.TestMapper;
import com.hushijie.springboottest.hctest12.repository.TestRepository;
import com.hushijie.springboottest.common.base.repository.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 测试表 数据仓库服务实现类
 * </p>
 *
 * @author wubin
 * @since 2018-12-04
 */
@Repository
public class TestRepositoryImpl extends BaseRepositoryImpl<TestMapper, TestDO>
        implements TestRepository {

}
