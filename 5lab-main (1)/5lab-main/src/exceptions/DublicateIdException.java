package exceptions;

/**
 * Исключение выбрасываемое в случае если
 * в запарщенной коллекции будут одинаковые id экземпляров
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class DublicateIdException extends Exception {
    public DublicateIdException(int id) {
        super("Dragon с id=" + id + " уже есть");
    }

}
