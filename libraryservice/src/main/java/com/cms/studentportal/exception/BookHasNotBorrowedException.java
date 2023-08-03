package com.cms.studentportal.exception;

public class BookHasNotBorrowedException  extends Exception{

    public BookHasNotBorrowedException(String title){
        super("This book has not been borrowed by student ");
    }
}
