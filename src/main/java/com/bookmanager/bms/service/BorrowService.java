package com.bookmanager.bms.service;

import com.bookmanager.bms.model.Borrow;

import java.util.List;
import java.util.Map;

public interface BorrowService {
    Integer getCount();

    Integer getSearchCount(Map<String, Object> params);

    Map<String, Object> searchBorrowsByPage(Map<String, Object> params);

    Integer addBorrow(Borrow borrow);

    Map<String, Object> addBorrow2(Integer userid, Integer bookid);

    Integer deleteBorrow(Borrow borrow);

    Integer deleteBorrows(List<Borrow> borrows);

    Integer updateBorrow(Borrow borrow);

    Map<String, Object> returnBook(Integer borrowid, Integer bookid);

    Borrow queryBorrowsById(Integer borrowid);

    List<Borrow> findOverdueBooks();

    Integer getBorrowByUserIdAndBookId(Integer userId, Integer bookId);

    Integer updateBorrow2(Borrow borrow);
}
