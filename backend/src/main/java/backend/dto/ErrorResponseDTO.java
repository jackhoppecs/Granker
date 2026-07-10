package backend.dto;

import java.time.Instant;

public class ErrorResponseDTO {
    private int status;
    private String error;
    private String message;
    private Instant timestamp;

    public ErrorResponseDTO(int status, String error, String message){
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
