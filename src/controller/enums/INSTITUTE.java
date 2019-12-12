package controller.enums;

public enum INSTITUTE {
    KE("Katedra elektrotechniky"),
    KIT("Katedra informačních technologií"),
    KMF("Katedra matematiky a fyziky"),
    KRP("Katedra řízení procesů"),
    KST("Katedra matematiky a fyziky");

    private String value;
    INSTITUTE(String value){this.value = value;}

    public String getValue() {return value; }

    @Override
    public String toString() {
        return value;
    }
}
