package com.market.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
public abstract class BaseInformation {
    protected Date creationDate;

    @Setter
    protected Date lastChange;
}
