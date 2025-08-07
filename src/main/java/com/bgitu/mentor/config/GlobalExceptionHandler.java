package com.bgitu.mentor.config;



import com.bgitu.mentor.common.dto.ErrorResponseDto;
import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- 400 Bad Request: Ошибки, связанные с некорректными данными от клиента ---
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ErrorResponseDto> handleClientInputExceptions(Exception ex, WebRequest request) {
        log.warn("Client Error (400 Bad Request): {}", ex.getMessage());

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            Map<String, String> errors = new HashMap<>();
            validationEx.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST, "Ошибка валидации", request, errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String message = ex.getMessage();
        if (ex instanceof HttpMessageNotReadableException && ex.getMessage().contains("Role")) {
            message = "Передана некорректная роль. Допустимые значения: MENTOR, STUDENT.";
        }

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // --- 403 Forbidden: Ошибки, связанные с отсутствием прав доступа ---
    @ExceptionHandler({AccessDeniedException.class, SecurityException.class})
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedExceptions(Exception ex, WebRequest request) {
        log.warn("Access Denied (403 Forbidden): {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.FORBIDDEN, "Доступ запрещен", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // --- 404 Not Found: Ошибки, связанные с отсутствием запрашиваемого ресурса ---
    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundExceptions(RuntimeException ex, WebRequest request) {
        log.warn("Resource Not Found (404 Not Found): {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // --- 409 Conflict: Ошибки, связанные с конфликтом состояний (например, попытка создать уже существующую сущность) ---
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictExceptions(EntityExistsException ex, WebRequest request) {
        log.warn("Conflict (409 Conflict): {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.CONFLICT, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // --- 413 Payload Too Large: Ошибки, связанные со слишком большим объемом данных ---
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        log.warn("Payload Too Large (413): {}", ex.getMessage());
        String message = "Файл слишком большой. Максимально допустимый размер — 10MB.";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.PAYLOAD_TOO_LARGE, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // --- 500 Internal Server Error: Все остальные, непредвиденные ошибки ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unhandled Internal Server Error (500): ", ex); // Логируем со стектрейсом
        String message = "Произошла непредвиденная ошибка на сервере.";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}