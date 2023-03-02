package com.increff.pos.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.increff.pos.model.MessageData;
import com.increff.pos.service.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		return data;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		Throwable cause = ex.getCause();
		MessageData data = new MessageData();
		String message;
		if (cause instanceof InvalidFormatException) {
			InvalidFormatException ife = (InvalidFormatException) cause;
			message = "Invalid value for field " + ife.getPath().get(0).getFieldName();
		} else if (cause instanceof UnrecognizedPropertyException) {
			UnrecognizedPropertyException upe = (UnrecognizedPropertyException) cause;
			message = "Unrecognized field " + upe.getPropertyName();
		} else {
			message = "Required request body is invalid!";
		}
		data.setMessage(message);
		return data;
	}


	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final MessageData handleConstraintViolation(ConstraintViolationException ex) {
		List<String> details = ex.getConstraintViolations()
				.parallelStream()
				.map(e -> e.getPropertyPath() +" " + e.getMessage())
				.collect(Collectors.toList());
		MessageData data = new MessageData();
		String detailsString = details.toString();
		detailsString = detailsString.replace("[","").replace("]", "");
		String[] stringArray = detailsString.split(",");
		Arrays.parallelSetAll(stringArray, (i) -> stringArray[i].trim());
		HashSet<String> stringHashSet = new HashSet<>(Arrays.asList(stringArray));
		final StringBuilder finalString = new StringBuilder();
		stringHashSet.forEach((String string)-> finalString.append(", ").append(string));
		String resultString = finalString.substring(2);
		data.setMessage(resultString);
		return data;
	}
}