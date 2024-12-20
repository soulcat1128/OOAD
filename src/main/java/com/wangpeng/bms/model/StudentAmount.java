package com.wangpeng.bms.model;

import java.util.Date;

public class StudentAmount implements AmountCalculation {
    @Override
    public double calculate(Borrow borrow) {
        // First 10 days $5 per day, after 10 days $10 per day
        Date borrowDate = borrow.getBorrowtime();
        long days = (System.currentTimeMillis() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
        if ( days <= 30 ) {
            return 0;
        }
        else {
            if ( days - 30 <= 10 ) {
                return (days - 30) * 5;
            }
            else {
                long remain = days - 30;
                return 10 * 5 + (remain - 10) * 10;
            }
        }
    }
}
