package controller.enums;

import model.Rating;

public enum RATING_GRADE {
    A("5 - skvělý"),
    B("4 - super"),
    C("3 - dobrý"),
    D("2 - nic moc"),
    E("1 - špatný");

    private String type;

    RATING_GRADE(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString() {return type;}

    public static RATING_GRADE convertToRATING_GRADE(Rating rt){
        switch (rt.getHodnota()){
            case 5:
                return RATING_GRADE.A;
            case 4:
                return RATING_GRADE.B;
            case 3:
                return RATING_GRADE.C;
            case 2:
                return RATING_GRADE.D;
            case 1:
                return RATING_GRADE.E;
            default:
                return null;
        }
    }

    public static int getPoints(RATING_GRADE rg){
        switch (rg){
            case A:
                return 5;
            case B:
                return 4;
            case C:
                return 3;
            case D:
                return 2;
            case E:
                return 1;
            default:
                return 0;
        }
    }

}