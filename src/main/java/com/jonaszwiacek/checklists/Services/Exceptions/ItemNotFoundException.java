package com.jonaszwiacek.checklists.Services.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item of given ID does not exist in checklist.")
public class ItemNotFoundException extends RuntimeException {
}
