package com.jonaszwiacek.checklists.Services.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Checklist of given ID does not exist.")
public class ChecklistNotFoundException extends RuntimeException {
}
