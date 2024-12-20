package com.wangpeng.bms.model;

import java.util.Date;

public class PublicAmount implements AmountCalculation {
    @Override
    public double calculate(Borrow borrow) {
        // $7 per day, highest $500.
        Date borrowDate = borrow.getBorrowtime();
        long days = (System.currentTimeMillis() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
        if ( days <= 30 ) {
            return 0;
        }
        else {
            return Math.min((days - 30) * 7, 500);
        }
    }

}
