//UNIFESSPA - Universidade do Sul e Sudeste do Pará
//Alunos: Cristina Vitoria Leal Leite, Lucas Antonio da Silva Lima e Lucas Leite de Oliveira
package server;
//Imports usados no programa
import client.InterfaceClient;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Optional;

public class Server {                                                           //Classe do servidor

    protected Server() throws RemoteException {                                 //Definindo super classe
        super();
    }

    public static void main(String[] args) throws AlreadyBoundException, IOException, InterruptedException, RemoteException, NotBoundException {//Main
        String caminhoServer = "src/arquivosDoServidor/";                       //Diretorio base do servidor
        // TODO Auto-generated method stub
        java.rmi.registry.LocateRegistry.createRegistry(1234);                  //Criando registro do servidor na rede
        //criando um novo objeto do tipo Rmi_correctq
        LoadFileService carregar = new LoadFileService();                       //Criando variavel do tipo LoadFileService
        //Arquivos achar = new Arquivos();
        //Criar registro para instanciar o local host
        //Registra e instancia os nomes das classes no serviço de nomes
        Registry registry = LocateRegistry.getRegistry("localhost", 1234);      //Registrando servidor na rede local
        registry.bind("Carregar", carregar);                                    //Cadastrando o serviço carregar que leva todos os serviços principais do servidor
        System.out.println("Servidor pronto");
        WatchService watcher = FileSystems.getDefault().newWatchService();      //Criando serviço de observador do diretório
        //Diretório que será verificado se o arquivo foi criado
        Path diretorio = Paths.get(caminhoServer);                              //Pegando o caminho do diretório
        //registra o serviço criado
        diretorio.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);      //registra o serviço criado, no caso estamos passando que estamos interessados apenas nos novos arquivos do diretório
        ArrayList<Interesse> Interesses = carregar.getInteresses();             //Recebo a lista de interesses atraves do serviço que me retorna a lista
        while (true) {                                                          //Enquanto for verdadeiro
            
            WatchKey key = watcher.take();                                      //O método take irá retornar uma WatchKey onde ocorreu a aparição do novo arquivo
            Optional<WatchEvent<?>> watchEvent = key.pollEvents().stream().findFirst();//Com o método pollEvents nos acessamos os eventos armazenados na Watch key
            if (watchEvent.isPresent()) {                                       //Se o evento existir então
                if (watchEvent.get().kind() == StandardWatchEventKinds.OVERFLOW) { //Verificando se ocorreu um evento overflow, esse evento pode ser capturado apesar de não termos registrado nele. Isso pode acontecer caso o evento seja perdido ou descartado devido algum comportamento inesperado
                    continue;
                }

                Path path = (Path) watchEvent.get().context();                  //Pegando o nome do arquivo no caminho do evento

                for (int i = 0; i < Interesses.size(); i++) {                   //Laço de repetição para navegar
                    if (Interesses.get(i).getNome().equals(path.toString())) {  //Se o interesse dessa posição for igual ao novo arquivo adicionado
                        InterfaceClient classe = (InterfaceClient) registry.lookup("Notificar"); //Criando variavel do tipo InterfaceCliente
                        carregar.Registry(Interesses.get(i).getNome(), classe, 0, 0);           //Solicitando o serviço de registro para notificar o cliente
                        System.out.println("Tem arquivo");
                    }
                }

                boolean valid = key.reset();                                    //Resetando Key
                if (!valid) {
                    break;
                }
            }

        }
        watcher.close();                                                        //Fechar o observador de diretório
    }
}
