package exceptions;

/**
 * Если экземпляр не создан или не прошел валидацию методом
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class InvalidDragonException extends Exception {
    public InvalidDragonException(String message) {
        super(message);
    }
}
