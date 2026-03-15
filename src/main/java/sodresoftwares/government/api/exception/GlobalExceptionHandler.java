package sodresoftwares.government.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sodresoftwares.government.api.model.user.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getStatusCode(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(error);
    }
}