package io.github.kennethfan.openapi.client.model.response;

import io.github.kennethfan.openapi.client.model.request.PageCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
public class PagedData<T> extends ResponseData<List<T>> {

    /**
     * 总数
     */
    private long totalSize;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 当前页
     */
    private int currentPage;


    public static <E> PagedData<E> success(PageCondition pageCondition, long total, List<E> elements) {
        PagedData<E> page = new PagedData<>();
        page.setCurrentPage(pageCondition.fixedPageNo())
                .setPageSize(pageCondition.limit())
                .setTotalSize(total)
                .setData(elements);

        return page;
    }
}
