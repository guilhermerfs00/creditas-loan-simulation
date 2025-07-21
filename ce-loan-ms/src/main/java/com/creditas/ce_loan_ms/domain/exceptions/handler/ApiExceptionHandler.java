package com.creditas.ce_loan_ms.domain.exceptions.handler;

import com.creditas.ce_loan_ms.domain.dto.ErrorDTO;
import com.creditas.ce_loan_ms.domain.exceptions.BaseException;
import com.creditas.ce_loan_ms.domain.exceptions.BusinessException;
import com.creditas.ce_loan_ms.domain.exceptions.NotFoundException;
import com.creditas.ce_loan_ms.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BASE_ERROR_MESSAGE = "base.message.error";
    private static final String UNEXPECTED_ERROR_MESSAGE = "base.message.error-unexpected";
    private static final String VALIDATION_ERROR_MESSAGE = "base.message.validation-error";
    private static final String INVALID_REQUEST_BODY_MESSAGE = "base.message.invalid-request-body";
    private static final String UNSUPPORTED_MEDIA_TYPE_MESSAGE = "base.message.unsupported-media-type";

    private final MessageUtils messageUtils;

    @ExceptionHandler({
            BusinessException.class,
            NotFoundException.class
    })
    public ResponseEntity<ErrorDTO> handleBusinessException(@NonNull BaseException ex) {
        log.warn("Business exception occurred: {} - Status: {}", ex.getMessage(), ex.getStatus());

        try {
            String message = messageUtils.getMessage(ex.getMessage(), ex.getArgs());
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .message(message)
                    .build();
            return ResponseEntity.status(ex.getStatus()).body(errorDTO);
        } catch (Exception messageException) {
            log.error("Error while processing business exception message", messageException);
            String fallbackMessage = messageUtils.getMessage(BASE_ERROR_MESSAGE);
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .message(fallbackMessage)
                    .build();
            return ResponseEntity.status(ex.getStatus()).body(errorDTO);
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode statusCode,
                                                                  WebRequest request) {
        log.warn("Validation error occurred: {}", ex.getMessage());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();

        String detailedMessage = errors.isEmpty()
                ? messageUtils.getMessage(VALIDATION_ERROR_MESSAGE)
                : String.format("%s - %s", messageUtils.getMessage(VALIDATION_ERROR_MESSAGE), String.join(", ", errors));

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(detailedMessage)
                .build();
        return ResponseEntity.status(statusCode).headers(headers).body(errorDTO);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.warn("Request body is not readable: {}", ex.getMessage());

        String message = messageUtils.getMessage(INVALID_REQUEST_BODY_MESSAGE);
        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).headers(headers).body(errorDTO);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {
        log.warn("Unsupported media type: {}", ex.getContentType());

        List<String> supportedMediaTypes = ex.getSupportedMediaTypes()
                .stream()
                .filter(Objects::nonNull)
                .map(MediaType::toString)
                .toList();

        String baseMessage = messageUtils.getMessage(UNSUPPORTED_MEDIA_TYPE_MESSAGE);
        String detailedMessage = supportedMediaTypes.isEmpty()
                ? baseMessage
                : String.format("%s. Supported types: %s", baseMessage, String.join(", ", supportedMediaTypes));

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(detailedMessage)
                .build();
        return ResponseEntity.status(status).headers(headers).body(errorDTO);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleGenericException(@NonNull Exception ex) {
        log.error("Unexpected error occurred", ex);

        String message = messageUtils.getMessage(UNEXPECTED_ERROR_MESSAGE);
        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}