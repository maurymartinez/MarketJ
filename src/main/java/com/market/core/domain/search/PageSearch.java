package com.market.core.domain.search;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PageSearch {
    private int page = 0;
    private int size = 20;

    private List<FieldSearch> fields;
}
