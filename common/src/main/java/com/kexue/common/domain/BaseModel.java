package com.kexue.common.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;


public  class BaseModel<T extends Model<?>> extends Model<T> {

    @TableId
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    protected void fullWhenCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    protected void fullWhenUpdate() {
        updateTime = LocalDateTime.now();
    }
}
