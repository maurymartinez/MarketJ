package com.market.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public abstract class BaseInformation {
    protected Date creationDate = new Date();
    protected Date lastChange;
}
