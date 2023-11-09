package io.github.kennethfan.openapi.client.model.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class PageCondition {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 500;

    /**
     * 每页数据量
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 当前分页1 开始
     */
    private int pageNo = 1;

    public int limit() {
        return pageSize < 0 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
    }

    public int fixedPageNo() {
        return Math.max(pageNo, 1);
    }

    public int offset() {
        return (fixedPageNo() - 1) * limit();
    }
}
