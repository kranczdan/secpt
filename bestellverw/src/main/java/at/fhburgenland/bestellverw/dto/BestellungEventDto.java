package at.fhburgenland.bestellverw.dto;

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
}
