package br.tiagofl.raspagem;

import br.tiagofl.raspagem.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.tiagofl.raspagem.principal.Principal;

@SpringBootApplication
public class RaspagemApplication implements CommandLineRunner {

    @Autowired
    private ProdutoRepository repositorioProdutos;

	public static void main(String[] args) {
		SpringApplication.run(RaspagemApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repositorioProdutos);
        principal.iniciarMenu();
    }

}
