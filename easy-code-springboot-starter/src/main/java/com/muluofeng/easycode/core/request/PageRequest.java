package com.muluofeng.easycode.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiexingxing
 * @Created by 2020-01-10 17:39.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest extends BaseRequest {
    /**
     * 页码
     */
    private long currentPage = 1;

    /**
     * 每页数量
     */
    private long pageSize = 10;


    public PageRequest(Long currentPage, Long pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

}