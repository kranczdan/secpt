package at.fhburgenland.kundenverw.exception;

/**
 * The NotFoundException class for throwing exception when
 * a sensor was not found.
 */
public class NotFoundException extends Exception {

    /**
     * The constructor of NotFoundException
     * @param errorMessage  the message for the exception
     */
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
