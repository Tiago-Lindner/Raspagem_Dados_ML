package br.tiagofl.raspagem.model;

public class Produto {

    private String descricao;
    private String dadosValores;
    private String link;

    public Produto(String descricao, String dadosValores) {
        this.descricao = descricao;
        this.dadosValores = dadosValores;
    }

    public Produto(String descricao, String dadosValores, String link) {
        this.descricao = descricao;
        this.dadosValores = dadosValores;
        this.link = link;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDadosValores() {
        return dadosValores;
    }

    public void setDadosValores(String dadosValores) {
        this.dadosValores = dadosValores;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
