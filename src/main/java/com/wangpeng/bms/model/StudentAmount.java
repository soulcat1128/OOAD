package com.wangpeng.bms.model;

import java.util.Date;

public class StudentAmount implements AmountCalculation {
    @Override
    public double calculate(IBook book, Borrow borrow) {
        if (book == null || borrow == null) {
            throw new IllegalArgumentException("Book and borrow cannot be null.");
        }
        if(book instanceof BookSeries) {
            if (((BookSeries) book).books == null || ((BookSeries) book).books.isEmpty()) {
                return 0.0;
            }
        }
        // First 10 days $5 per day, after 10 days $10 per day
        Date borrowDate = borrow.getBorrowtime();
        long days = (System.currentTimeMillis() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
        long period = 30;
        double total = 0.0;
        if (!(book instanceof BookSeries)) {
            period += book.getPrefix();
            if ( days <= period ) {
                return 0.0;
            }
            else {
                if ( days - period <= 10 ) {
                    return (days - period) * 5.0;
                }
                else {
                    return 10 * 5.0 + (days - period - 10) * 10.0;
                }
            }
        }
        for (IBook b : ((BookSeries) book).books) {
            if ( days >= period + b.getPrefix() ) {
                if ( days - period - b.getPrefix() <= 10 ) {
                    total += (days - period - b.getPrefix()) * 5.0;
                }
                else {
                    total += 10 * 5.0 + (days - period - b.getPrefix() - 10) * 10.0;
                }
            }
            else {
                total += 0.0;
            }
        }
        return total;
    }
}
