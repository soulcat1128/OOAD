package com.wangpeng.bms.model.template;

public interface IBookDisplayTemplate {
    default void display() {
        displaysicInfo();
        displayDetails();
        displayFooter();
    }

    void displaysicInfo();

    void displayDetails();

    default  void displayFooter() {
        System.out.println("Thank you for reading!");
    }
}
