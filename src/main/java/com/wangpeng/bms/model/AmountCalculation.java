package com.wangpeng.bms.model;

public interface AmountCalculation {
    public double calculate(IBook book, Borrow borrow);
}
