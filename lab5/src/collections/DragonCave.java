package collections;

/**
 * Реаализация пещеры экземпляра
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class DragonCave implements Valid {
    private float numberOfTreasures; // Значение поля должно быть больше 0

    public float getNumberOfTreasures() {
        return numberOfTreasures;
    }

    public void setNumberOfTreasures(float numberOfTreasures) {
        this.numberOfTreasures = numberOfTreasures;
    }

    @Override
    public boolean validate() {
        if (numberOfTreasures > 0) {
            return true;
        }
        return false;
    }
}