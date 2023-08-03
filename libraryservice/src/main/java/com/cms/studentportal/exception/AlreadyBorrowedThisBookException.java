package com.cms.studentportal.exception;

public class AlreadyBorrowedThisBookException  extends Exception{

    public AlreadyBorrowedThisBookException(String title){
        super("Student has borrowed this book already and has not returned");
    }
}
