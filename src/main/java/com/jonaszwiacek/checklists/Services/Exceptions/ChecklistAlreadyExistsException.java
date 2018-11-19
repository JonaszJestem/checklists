package com.jonaszwiacek.checklists.Services.Exceptions;

public class ChecklistAlreadyExistsException extends RuntimeException {
    public ChecklistAlreadyExistsException() {
        super("Checklist of given name already exists.");
    }
}
