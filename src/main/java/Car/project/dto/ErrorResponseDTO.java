package Car.project.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}