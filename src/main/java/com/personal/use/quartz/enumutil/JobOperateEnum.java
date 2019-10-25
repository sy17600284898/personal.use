package com.personal.use.quartz.enumutil;

/**
 *
 * Timing task enum
 *
 * JobOperateEnum
 * @author: Lenovo
 * @version: 2019-06-26 19:44:21
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public enum JobOperateEnum {
    /**
     * START
     */
    START(1, "启动"),
    /**
     * PAUSE
     */
    PAUSE(2, "暂停"),
    /**
     * DELETE
     */
    DELETE(3, "删除");

    private final Integer value;
    private final String desc;

    JobOperateEnum(final Integer value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getEnumName() {
        return name();
    }
}
