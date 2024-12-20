package com.wangpeng.bms.model;

import java.util.Date;

public class ProfessorAmount implements AmountCalculation {
    @Override
    public double calculate(Borrow borrow) {
        // $5 per day
        Date borrowDate = borrow.getBorrowtime();
        long days = (System.currentTimeMillis() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
        if ( days <= 30 ) {
            return 0;
        }
        else {
            return (days - 30) * 5;
        }
    }

}
