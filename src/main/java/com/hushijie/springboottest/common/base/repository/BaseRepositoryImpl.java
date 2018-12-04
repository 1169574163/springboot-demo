package com.hushijie.springboottest.common.base.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * BaseRepositoryImpl
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/12/4
 */
public class BaseRepositoryImpl<M extends BaseMapper<T>, T>
        extends ServiceImpl<M, T> implements BaseRepository<T> {
}
