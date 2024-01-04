package at.fhburgenland.product.dto;

public class BestellungEventDto {
    private BestellungDto bestellung;
    private String type;

    public BestellungDto getBestellung() {
        return bestellung;
    }

    public void setBestellung(BestellungDto bestellung) {
        this.bestellung = bestellung;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BestellungEventDto{" +
                "bestellung=" + bestellung +
                ", type='" + type + '\'' +
                '}';
    }
}
