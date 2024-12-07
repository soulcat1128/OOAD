package com.wangpeng.bms.model;

import java.math.BigDecimal;

public interface IBook {
    String getName();
    String getAuthor();
    BigDecimal getPrice();
    String getMaterial();
    String getDesc();
    Integer getId();
    String getImg();
    Integer getTypeId();
    default void add(IBook book) {
        throw new UnsupportedOperationException("does not support this operation.");
    } // Default: UnsupportedOperationException for non-series
    default void remove(IBook book) {
        throw new UnsupportedOperationException("does not support this operation.");
    }
}
