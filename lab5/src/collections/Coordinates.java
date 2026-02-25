package collections;

/**
 * Реаализация координат экземпляра
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Coordinates implements Valid {
    private Double x; // Значение поля должно быть больше -257, Поле не может быть null
    private Integer y; // Значение поля должно быть больше -324, Поле не может быть null

    public Double getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public boolean validate() {
        if (x <= -257.0 || x == null) {
            return false;
        }
        if (y <= -324 || x == null) {
            return false;
        }

        return true;
    }

}
