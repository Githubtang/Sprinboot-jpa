package com.example.springboottyy.common.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

/**
 * @Author: GithubTang
 * @Description: 排除JSON敏感属性
 * @Date: 2025/3/15 17:23
 * @Version: 1.0
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {
    public PropertyPreExcludeFilter()
    {
    }

    public PropertyPreExcludeFilter addExcludes(String... filters)
    {
        for (int i = 0; i < filters.length; i++)
        {
            this.getExcludes().add(filters[i]);
        }
        return this;
    }
}
