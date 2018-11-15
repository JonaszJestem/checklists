package com.jonaszwiacek.checklists.Services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item of given ID does not exist.")
public class ItemNotFoundException extends RuntimeException {
}
