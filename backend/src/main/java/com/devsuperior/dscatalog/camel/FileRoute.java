package com.devsuperior.dscatalog.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.FilterProcessor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileRoute extends RouteBuilder {

    private String path = "C:\\temp\\";

    @Override
    public void configure() throws Exception {
        from("file://" + path + "input?recursive=true&delete=true")
                .choice() //para dizer que a partir do HEADER da mensagem do FILE eu posso tomar uma descisão
                    .when(simple("${header.CamelFileLength < 422}")) //se o HEADER for menor doq 422 bytes eu envio para o BEAN
                         .to("bean:fileComponent") //não preciso gerar um arquivo novo a partir do FROM, posso somente transmitir ele para o fileComponent
        .otherwise() //caso não atenda a condição de cima
                .process(new FileProcessor());

    }
}

//alternativa para quando não se quer usar os BEANS e COMPONENTS, ai se faz dessa forma
//class  FileProcessor implements Processor { //método que terá o objeto de intercâmbio que está passando na execução do fluxo do CONFIGURE
//    @Override
//    public void process(Exchange exchange) throws Exception {
//        System.out.println("Processor: " + exchange.getIn().getBody()); //mostra o caminho do arquivo que está sendo consumindo
//
//    }
//}

@Component
class FileComponent { //ELE EXIJE UM ÚNICO MÉTODO POR CLASSE, NÃO PODE TER MAIS DE UM
    public void log(File file) {
        String msg = file.getName();
        System.out.println("FileComponent: " + file.getName()); //ele printa o nome do arquivo
    }

}

class FileProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("PROCESSOR: " + exchange.getIn().getBody());
    }
}


// ele irá ficar monitorando as pastas e arquivos enquanto a aplicação roda
//        //com o delete=true ele não cria a pasta .camel que na verdade seria um backup
//        from("file://" + path + "input?delete=true") //usa o FILE para leitura de arquivo(ou seja, PATH) e irá criar uma pasta caso não tenha(no caso a pasta INPUT), tem como forçar para não criar caso não tenha
//        .log("$Arquivo: ${header.CamelFileName} - Path: ${header.CamelFilePath}") //log para exibir no console, exibe o nome do arquivo seguido do caminho
//        .log("${file:name}") //exibe o nome do arquivo no console
//        .bean("fileComponent")
//        .to("file://" + path + "output"); // exige também que se defina um diretório para onde ele irá enviar o arquivo processado, neste caso para OUTPUT
