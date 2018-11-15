package com.jonaszwiacek.checklists.Services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Checklist of given name already exists.")
public class ChecklistAlreadyExistsException extends RuntimeException {
}
