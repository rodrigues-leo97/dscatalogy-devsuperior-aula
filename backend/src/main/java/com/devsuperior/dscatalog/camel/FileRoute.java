package com.devsuperior.dscatalog.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileRoute extends RouteBuilder {

    private String path = "C:\\temp\\";

    @Override
    public void configure() throws Exception {//ele irá ficar monitorando as pastas e arquivos enquanto a aplicação roda
        //com o delete=true ele não cria a pasta .camel que na verdade seria um backup
        from("file://" + path + "input?delete=true") //usa o FILE para leitura de arquivo(ou seja, PATH) e irá criar uma pasta caso não tenha(no caso a pasta INPUT), tem como forçar para não criar caso não tenha
                .log("$Arquivo: ${header.CamelFileName} - Path: ${header.CamelFilePath}") //log para exibir no console, exibe o nome do arquivo seguido do caminho
                .log("${file:name}") //exibe o nome do arquivo no console
                .bean("fileComponent")
                .to("file://" + path + "output"); // exige também que se defina um diretório para onde ele irá enviar o arquivo processado, neste caso para OUTPUT

    }
}

@Component
class FileComponent { //ELE EXIJE UM ÚNICO MÉTODO POR CLASSE, NÃO PODE TER MAIS DE UM
    public void log(File file) {
        System.out.println("FileComponent: " + file.getName()); //ele printa o nome do arquivo
    }

}
