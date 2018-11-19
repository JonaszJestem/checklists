package com.jonaszwiacek.checklists.Services.Exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        super("Item of given ID does not exist in checklist.");
    }
}
