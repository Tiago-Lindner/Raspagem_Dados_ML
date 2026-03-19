package br.tiagofl.raspagem.repository;

import br.tiagofl.raspagem.model.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findTop5ByOrderByPreco();

    @Query("SELECT p FROM ProdutoEntity p WHERE p.descricao ILIKE %:trecho%")
    List<ProdutoEntity> produtosPorTrecho(String trecho);

}
