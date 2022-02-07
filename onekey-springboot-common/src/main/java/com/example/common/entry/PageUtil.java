package com.example.common.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PageUtil {

    private Long id;

    /**
     *  当前页
     */
    private int pageNo;

    /**
     *  每页大小
     */
    private int pageSize;
}
