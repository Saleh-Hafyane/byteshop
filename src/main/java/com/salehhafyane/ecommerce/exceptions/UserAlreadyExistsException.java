package com.salehhafyane.ecommerce.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String field;

    public UserAlreadyExistsException(String message) {
        super(message);
        this.field = null;
    }

    public UserAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
    }

}
