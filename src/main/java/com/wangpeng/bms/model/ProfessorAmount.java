package com.wangpeng.bms.model;

import java.util.Date;

public class ProfessorAmount implements AmountCalculation {
    @Override
    public double calculate(IBook book, Borrow borrow) {
        if (book == null || borrow == null) {
            throw new IllegalArgumentException("Book and borrow cannot be null.");
        }

        if (book instanceof BookSeries)
        {
            if (((BookSeries) book).books == null || ((BookSeries) book).books.isEmpty()) {
                return 0.0;
            }
        }

        // $5 per day
        Date borrowDate = borrow.getBorrowtime();
        long days = (System.currentTimeMillis() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
        long period = 30;
        double total = 0.0;

        if(book instanceof BookSeries) {
            for (IBook b : ((BookSeries) book).books) {
                if ( days >= period + b.getPrefix() ) {
                    total += (days - period - b.getPrefix()) * 5.0;
                }
                else {
                    total += 0.0;
                }
            }
        }
        else {
            period += book.getPrefix();
            if ( days <= period ) {
                return 0.0;
            }
            else {
                return (days - period) * 5.0;
            }
        }

        return total;
    }
}
