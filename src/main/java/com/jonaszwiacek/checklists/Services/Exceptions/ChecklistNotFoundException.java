package com.jonaszwiacek.checklists.Services.Exceptions;

public class ChecklistNotFoundException extends RuntimeException {
    public ChecklistNotFoundException() {
        super("Checklist of given ID does not exist.");
    }
}
