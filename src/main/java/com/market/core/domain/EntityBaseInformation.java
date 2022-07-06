package com.market.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class EntityBaseInformation extends BaseInformation {
    @Id
    protected String id;
}
