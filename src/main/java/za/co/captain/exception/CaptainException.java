package za.co.captain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaptainException extends Exception {

    private Exception exception;
    private String hint;

}
