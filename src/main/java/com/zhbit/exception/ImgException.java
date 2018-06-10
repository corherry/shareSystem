package com.zhbit.exception;

import java.io.FileNotFoundException;

public class ImgException extends FileNotFoundException {

    public ImgException(String message) {
        super(message);
    }
}
