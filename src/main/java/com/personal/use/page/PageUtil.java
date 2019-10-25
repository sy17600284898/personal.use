package com.personal.use.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;


/**
 * @author shiyan
 * @date 2014-10-13
 */
@ApiModel(value = "分页对象", description = "")
public class PageUtil {

    @ApiModelProperty(value = "分页查询开始记录位置", required = true)
    private int begin;

    @ApiModelProperty(value = "分页查看下结束位置", required = true)
    private int end;

    @ApiModelProperty(value = "每页显示记录数", required = true)
    private int length;

    @ApiModelProperty(value = "查询结果总记录数", required = true)
    private int count;

    @ApiModelProperty(value = "当前页码", required = true)
    private int current;

    @ApiModelProperty(value = "总共页数", required = true)
    private int total;

    public PageUtil() {
    }

    /**
     * 构造函数
     *
     * @param begin
     * @param length
     */
    public PageUtil(int begin, int length) {
        this.begin = begin;
        this.length = length;
        this.end = this.begin + this.length;
        this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
    }

    public PageUtil(int pageNumber, Map<String, Object> searchParams) {
        int pageSize = 10;
        if (searchParams.get("pageSize") != null && searchParams.get("pageSize") != "") {
            pageSize = Integer.parseInt(searchParams.get("pageSize").toString());
        }
        int begin = (pageNumber - 1) * pageSize;
        this.begin = begin;
        this.length = pageSize;
        this.end = this.begin + this.length;
        this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
    }

    /**
     * @param begin
     * @param length
     * @param count
     */
    public PageUtil(int begin, int length, int count) {
        this(begin, length);
        this.count = count;
    }

    /**
     * @return the begin
     */
    public int getBegin() {
        return begin;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @param begin the begin to set
     */
    public void setBegin(int begin) {
        this.begin = begin;
        if (this.length != 0) {
            this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
        }
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
        if (this.begin != 0) {
            this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
        }
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
        this.total = (int) Math.floor((this.count * 1.0d) / this.length);
        if (this.count % this.length != 0) {
            this.total++;
        }
    }

    /**
     * @return the current
     */
    public int getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        if (total == 0) {
            return 1;
        }
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    public boolean hasPreviousPage() {
        return this.current > 1;
    }

    public boolean hasNextPage() {
        return this.current < this.total;
    }

    private String dialect = "mysql";

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

}
