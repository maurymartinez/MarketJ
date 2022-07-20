package com.market.core.domain.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageSearch {
    private int page = 0;
    private int size = 20;

    private List<FieldSearch> fields = new ArrayList<>();
}
