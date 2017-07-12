package br.ufg.inf.dsdm.ondetem.model;

/**
 * Created by ibruno on 11/07/17.
 */

public class Localizacao {

    private String uuid;

    private double latitude;

    private double longitude;

    private long timestamp;

    private String nome;

    public Localizacao() {
    }

    public Localizacao(String uuid, double latitude, double longitude, long timestamp, String nome) {
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.nome = nome;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
