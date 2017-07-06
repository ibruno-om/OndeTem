package br.ufg.inf.dsdm.ondetem.model;

import java.io.Serializable;

/**
 * Created by ibruno on 05/07/17.
 */

public class Pergunta implements Serializable {

    private String conteudo;

    public Pergunta() {

    }

    public Pergunta(String uid, String conteudo) {
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pergunta pergunta = (Pergunta) o;

        return conteudo != null ? conteudo.equals(pergunta.conteudo) : pergunta.conteudo == null;

    }

    @Override
    public int hashCode() {
        return conteudo != null ? conteudo.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getConteudo();
    }
}
