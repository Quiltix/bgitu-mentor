package com.bgitu.mentor.common;



import com.bgitu.mentor.common.dto.MessageDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDto> handleValidationExceptions(MethodArgumentNotValidException ex){
        log.error("MethodArgumentNotValidException:{}", String.valueOf(ex));
        return ResponseEntity.badRequest().body(new MessageDto(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MessageDto> handleUserNotFoundExceptions(UsernameNotFoundException ex){
        log.error("UsernameNotFoundException:{}", String.valueOf(ex));
        return ResponseEntity.badRequest().body(new MessageDto("User not found"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageDto> handleAccessDeniedExceptions(AccessDeniedException ex){
        log.error("AccessDeniedException:{}", String.valueOf(ex));
        return ResponseEntity.status(401).body(new MessageDto(ex.getMessage()));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageDto> handleEntityNotFoundExceptions(EntityNotFoundException ex){
        log.error("Entity not found exception:{}", String.valueOf(ex));
        return ResponseEntity.status(400).body(new MessageDto(ex.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageDto> handleIllegalArgumentExceptionExceptions(IllegalArgumentException ex){
        log.error("IllegalArgumentExceptionExceptions:{}", String.valueOf(ex));
        return ResponseEntity.status(400).body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<MessageDto> handleEntityExistsExceptionExceptions(EntityExistsException ex){
        log.error("EntityExistsException:{}", String.valueOf(ex));
        return ResponseEntity.badRequest().body(new MessageDto(ex.getMessage()));

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageDto> handleBadCredentialsExceptionExceptions(BadCredentialsException ex){
        log.error("BadCredentialsExceptionExceptions:{}", String.valueOf(ex));
        return ResponseEntity.status(400).body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        return ResponseEntity.status(500).body(new MessageDto("Internal server error: " + ex.getMessage()));
    }
    
}
