package com.example.psh.errorhandling.exceptions;

public class InvalidIdRepresentationException extends RuntimeException {

    public InvalidIdRepresentationException(String invalidId) {
        super("Invalid representation of ObjectId: " + invalidId);
    }
}
