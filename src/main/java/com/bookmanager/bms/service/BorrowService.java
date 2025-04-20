package com.bookmanager.bms.service;

import com.bookmanager.bms.model.Borrow;

import java.util.List;
import java.util.Map;

public interface BorrowService {
    Integer getCount();

    Integer getSearchCount(Map<String, Object> params);

    List<Borrow> searchBorrowsByPage(Map<String, Object> params);

    Integer addBorrow(Borrow borrow);

    Integer addBorrow2(Borrow borrow);

    Integer deleteBorrow(Borrow borrow);

    Integer deleteBorrows(List<Borrow> borrows);

    Integer updateBorrow(Borrow borrow);

    Integer updateBorrow2(Borrow borrow);

    Borrow queryBorrowsById(Integer borrowid);

    List<Borrow> findOverdueBooks();
}
