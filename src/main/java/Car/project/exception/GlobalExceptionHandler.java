package Car.project.exception;

import Car.project.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource; // Injection de MessageSource

    /**
     * Gère les erreurs lorsque une entité n'est pas trouvée.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        // Le message de l'exception est maintenant un code (ex: "error.client.notfound")
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, request.getLocale());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("Not Found");
        errorResponse.setMessage(errorMessage); // Message traduit
        errorResponse.setPath(request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les erreurs de ressources qui existent déjà (ex: CIN dupliqué).
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceAlreadyExists(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, request.getLocale());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.CONFLICT.value()); // 409 Conflict est idéal ici
        errorResponse.setError("Conflict");
        errorResponse.setMessage(errorMessage); // Message traduit
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Concatène tous les messages d'erreur de validation
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value()); // 400 Bad Request
        errorResponse.setError("Bad Request");
        errorResponse.setMessage(errorMessage); // Message d'erreur détaillé
        errorResponse.setPath(request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    
}
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        String errorMessage = messageSource.getMessage("error.access.denied", null, request.getLocale());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.FORBIDDEN.value()); // 403 Forbidden
        errorResponse.setError("Forbidden");
        errorResponse.setMessage(errorMessage);
        errorResponse.setPath(request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxSizeException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        // On peut passer la limite de taille au message de traduction
        Object[] args = { ex.getMaxUploadSize() / (1024 * 1024) + "MB" };
        String errorMessage = messageSource.getMessage("error.file.size.exceeded", args, request.getLocale());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value()); // 413 Payload Too Large
        errorResponse.setError("Payload Too Large");
        errorResponse.setMessage(errorMessage);
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

}