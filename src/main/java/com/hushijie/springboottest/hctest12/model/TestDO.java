package com.hushijie.springboottest.hctest12.model;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.hushijie.springboottest.common.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 测试表
 * </p>
 *
 * @author wubin
 * @since 2018-12-03
 */
@TableName("hc_test")
public class TestDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 性别 0-未知(默认) 1-男 2-女
     */
    @TableField("sex")
    private String sex;

    /**
     * 乐观锁
     */
    @TableField("lock_version")
    private LocalDateTime lockVersion;

    /**
     * 逻辑删除 【-1-已删除 0-未删除(默认)】
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


    public String getCode() {
        return code;
    }

    public TestDO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestDO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public TestDO setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public LocalDateTime getLockVersion() {
        return lockVersion;
    }

    public TestDO setLockVersion(LocalDateTime lockVersion) {
        this.lockVersion = lockVersion;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public TestDO setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override
    public String toString() {
        return "TestDO = " + JSON.toJSONString(this);
    }
}
