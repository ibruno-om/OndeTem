package br.ufg.inf.dsdm.ondetem.model;

/**
 * Created by ibruno on 11/07/17.
 */

public class Localizacao {

    private String uuid;

    private double latitude;

    private double longitude;

    public Localizacao() {
    }

    public Localizacao(String uuid, double latitude, double longitude) {
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
