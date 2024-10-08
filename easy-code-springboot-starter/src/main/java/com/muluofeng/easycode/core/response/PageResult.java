package com.muluofeng.easycode.core.response;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiexingxing
 * @Created by 2020-02-05 21:04.
 */
@lombok.Data
@NoArgsConstructor
public class PageResult<T> {


    private List<T> list;
    /**
     * 数据总数
     */
    private long totalData;

    private Long current;


    private Long pageTotal;

    private Long pageSize;

    public PageResult(List<T> list, Long current , Long totalData,Long pageTotal, Long pageSize ) {
        this.list = list;
        this.current = current;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.totalData = totalData;
    }

    /**
     * mybatis page 转为 自定义page
     * @param pae
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> fromPage(IPage<T> pae) {
        long pageTotal = (int) Math.ceil(pae.getTotal() * 1.0 / pae.getSize());
        PageResult<T> pageResult = new PageResult<T>(pae.getRecords(), pae.getCurrent(), pae.getTotal(), pageTotal, pae.getSize());
        return pageResult;
    }

}