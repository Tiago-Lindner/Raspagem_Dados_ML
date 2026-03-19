package br.tiagofl.raspagem.controller;

import br.tiagofl.raspagem.model.Produto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WebScraping {
    public static void main(String[] args) throws InterruptedException {

        Scanner leitura = new Scanner(System.in);

        System.out.println("Digite o nome do produto a pesquisar: ");
        var pesquisar = leitura.nextLine();

        rasparDados(pesquisar);
    }

    private static void rasparDados(String produtoPesquisar) throws InterruptedException {
        //definir o caminho do driver
        System.setProperty("webdriver.edge.driver", "src/main/resources/msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();

        //uso memória compartilhada
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

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



        ArrayList<Produto> produtos = new ArrayList<>();
        for(int i = 0; i < descricoesProdutos.size(); i++) {

            produtos.add(new Produto(descricoesProdutos.get(i).getText(), valoresProdutos.get(i).getText().replace("\n","")));
            //System.out.println("\n" + produtos.get(i).getDescricao() + "\n" + produtos.get(i).getDadosValores());
        }

        for(Produto produto: produtos) {
            System.out.println(produto.getDescricao());
            System.out.println(produto.getDadosValores() + "\n");
        }
    }
}
