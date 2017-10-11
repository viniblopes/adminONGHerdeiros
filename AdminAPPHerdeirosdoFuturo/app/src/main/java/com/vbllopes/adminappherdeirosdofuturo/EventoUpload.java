package com.vbllopes.adminappherdeirosdofuturo;

/**
 * Created by vinciusbecker on 6/4/17.
 */

public class EventoUpload {

    String nome;
    String data;

    public String getNome() {return nome;}

    public String getData() {return data;}

    public EventoUpload(String nome, String data) {
        this.nome = nome;
        this.data = data;
    }

    public EventoUpload() {}

}