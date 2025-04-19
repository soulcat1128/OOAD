package com.bookmanager.bms.exception;

/**
 * 庫存不足異常
 * 也就是 圖書已經借走
 */
public class NotEnoughException extends RuntimeException{
    public NotEnoughException() {
        super();
    }

    public NotEnoughException(String message) {
        super(message);
    }
}
