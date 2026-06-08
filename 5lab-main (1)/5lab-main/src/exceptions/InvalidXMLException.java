package exceptions;

/**
 * Ошибка при парсинге XML файла
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class InvalidXMLException extends Exception {
    public InvalidXMLException(String message) {
        super(message);
    }
}
