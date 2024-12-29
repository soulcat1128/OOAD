package com.wangpeng.bms.model;

import java.util.Date;

public class PublicAmount implements AmountCalculation {
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
        // $7 per day, highest $500.
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
                return Math.min((days - period) * 7.0, 500.0);
            }
        }
        for (IBook b : ((BookSeries) book).books) {
            if ( days >= period + b.getPrefix() ) {
                total += Math.min((days - period - b.getPrefix()) * 7.0, 500.0);
            }
            else {
                total += 0.0;
            }
        }
        return total;
    }
}
