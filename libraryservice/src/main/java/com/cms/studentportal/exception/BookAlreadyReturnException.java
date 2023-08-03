package com.cms.studentportal.exception;

public class BookAlreadyReturnException  extends Exception{

    public BookAlreadyReturnException(String title){
        super("Student has already return this book");
    }
}
