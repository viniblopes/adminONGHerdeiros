package com.vbllopes.adminappherdeirosdofuturo;

/**
 * Created by vinciusbecker on 6/3/17.
 */

public class ImageUpload {

    public  String nome;
    public String url;

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

    public ImageUpload(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    public ImageUpload(){

    }
}
