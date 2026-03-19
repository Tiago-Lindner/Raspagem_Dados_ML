package br.tiagofl.raspagem.principal;

import br.tiagofl.raspagem.model.Produto;
import br.tiagofl.raspagem.model.ProdutoDTO;
import br.tiagofl.raspagem.model.ProdutoEntity;
import br.tiagofl.raspagem.repository.ProdutoRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private List<ProdutoDTO> dadosProdutos = new ArrayList<>();
    private ProdutoRepository repositorioProdutos;
    Scanner leitura = new Scanner(System.in);

    public Principal(ProdutoRepository repositorioProdutos) {
        this.repositorioProdutos = repositorioProdutos;
    }

    public void iniciarMenu() throws InterruptedException {
        int opcao = -1;
        while (opcao != 0) {
            var menu = """
                    
                    1 - Buscar produtos
                    2 - Listar Produtos do banco de dados                  
                    3 - Listar top 5 Produtos mais baratos
                    4 - Buscar Produtos no banco com trecho
                    
                    0 - Sair """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    pesquisar();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    listarMaisBaratos();
                    break;
                case 4:
                    buscaTrecho();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    System.exit(0);

                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }
    }




    public void pesquisar() throws InterruptedException {
        System.out.println("Digite o nome do produto para pesquisar: ");
        var pesquisar = leitura.nextLine();

        rasparDados(pesquisar);
    }

    private void listarProdutos() {
             dadosProdutos = repositorioProdutos.findAll().stream()
                    .map(s -> new ProdutoDTO(s.getId(), s.getDescricao(), s.getPreco(), s.getLink(), s.getDataConsulta()))
                    .collect(Collectors.toList());
       for (ProdutoDTO prod: dadosProdutos){
           System.out.println(prod);


        }

    }

    private void listarMaisBaratos() {
        List<ProdutoEntity> produtosTop = repositorioProdutos.findTop5ByOrderByPreco();
        produtosTop.forEach(s ->
                System.out.println(s.getDescricao() + " preço: " + s.getPreco()));
    }

    private void buscaTrecho() {
        System.out.println("Insira o trecho para busca: ");
        var trecho = leitura.nextLine();

        List<ProdutoEntity> produtosTrecho = repositorioProdutos.produtosPorTrecho(trecho);
        produtosTrecho.forEach(s ->
                System.out.println(s.getDescricao() + " preço: " + s.getPreco()));
    }




    public void rasparDados(String produtoPesquisar) throws InterruptedException {
        //definir o caminho do driver
        System.setProperty("webdriver.edge.driver", "src/main/resources/msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();

        //uso memória compartilhada
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        options.addArguments("--headless");

        //evitar detecção de sites
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", null);

        options.addArguments("window-size=1280,720");

        //para ajudar a não ser identificado como bot
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 OPR/124.0.0.0");

        WebDriver driver = new EdgeDriver(options);

        driver.get("https://lista.mercadolivre.com.br/");

        WebElement inputPesquisa = driver.findElement(By.xpath("//input[@id=\"cb1-edit\"]"));

        inputPesquisa.sendKeys(produtoPesquisar);
        inputPesquisa.submit();

        Thread.sleep(1000);


        //adiciona as decrições do produto numa lista
        List<WebElement> descricoesProdutos = driver.findElements(By.xpath("//a[@class=\"poly-component__title\"]"));

        //adiciona os preços dos produtos em outra lista
        List<WebElement> valoresProdutos = driver.findElements(By.xpath("//span[@class=\"andes-money-amount andes-money-amount--cents-superscript\"]"));

        //Adiciona os links
        List<WebElement> linksProdutos = driver.findElements(By.xpath("//a[@class=\"poly-component__title\"]"));




        ArrayList<ProdutoEntity> produtosLista = new ArrayList<>();
        for(int i = 0; i < descricoesProdutos.size(); i++) {

            produtosLista.add(new ProdutoEntity(descricoesProdutos.get(i).getText(), valoresProdutos.get(i).getText().replace("\n","").replace(".", ""), linksProdutos.get(i).getAttribute("href"), pegarDataHora()));
            //System.out.println("\n" + produtos.get(i).getDescricao() + "\n" + produtos.get(i).getDadosValores());
        }

        for(ProdutoEntity produto: produtosLista) {
            repositorioProdutos.save(produto);
            System.out.println( "\n" + produto.getDescricao());
            System.out.println(produto.getPreco());
            System.out.println(produto.getLink());
            System.out.println(produto.getDataConsulta());
            System.out.println("\n");
        }

        driver.quit();
    }

    private String pegarDataHora(){
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();

        Locale brasil = new Locale("pt", "BR"); //Retorna do país e a língua

        DateFormat formato = DateFormat.getDateInstance(DateFormat.FULL, brasil);
        DateFormat hora = DateFormat.getTimeInstance();

        String dataHora = "Data e hora da consulta: "+ formato.format(data) + ", " +  hora.format(data);
        return dataHora;
    }

}



//Testes
//    List<WebElement> valores2 = driver.findElements(By.xpath("//span[@class=\"andes-money-amount andes-money-amount--cents-superscript\"]"));
//
//    for(int i = 0; i < valores2.size(); i++) {
//        //System.out.println(valores2.get(i).getText().replace("\n","") + "\n");
//        System.out.println(valores2.get(i).getAttribute("aria-label"));
//    }

// pegar os links (href)
//    for (WebElement link : linksProdutos) {
//        String hrefUrl = link.getAttribute("href"); // Get the value of the 'href' attribute
//        if (hrefUrl != null && !hrefUrl.isEmpty()) {
//            System.out.println(hrefUrl);
//        }
//    }

//ArrayList<Produto> produtos = new ArrayList<>();
//        for(int i = 0; i < descricoesProdutos.size(); i++) {
//
//        produtos.add(new Produto(descricoesProdutos.get(i).getText(), valoresProdutos.get(i).getText().replace("\n",""), linksProdutos.get(i).getAttribute("href")));
//        //System.out.println("\n" + produtos.get(i).getDescricao() + "\n" + produtos.get(i).getDadosValores());
//        }
//
//        for(Produto produto: produtos) {
//        System.out.println( "\n" + produto.getDescricao());
//        System.out.println(produto.getDadosValores());
//        System.out.println(produto.getLink());
//        }