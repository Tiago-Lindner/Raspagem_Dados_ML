package br.tiagofl.raspagem.model;

public record ProdutoDTO(   Long id,
                            String descricao,
                            String preco,
                            String link,
                            String dataConsulta) {
}
