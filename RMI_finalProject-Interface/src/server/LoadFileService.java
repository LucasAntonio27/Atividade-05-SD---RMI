package server;

//Imports usados no programa
import client.InterfaceClient;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class LoadFileService extends UnicastRemoteObject implements Interface, InterArq, Serializable { //Principal classe de serviços do servidor

    //private String currentDir = "C:\\Users\\lucas\\eclipse-workspace\\RMI_finalProject\\src\\arquivosDoServidor\\"; //Definindo diretório base do servidor
    private ArrayList<Interesse> Interesses = new ArrayList();                  //Gerando uma nova lista
    private List<Path> caminhosPath = new ArrayList();                          //Gerando uma lista de caminhos

    protected LoadFileService() throws RemoteException {                        //Definindo a super classe
        super();
    }
    private static final long serialVersionUID = 1L;

    //Classe que faz o upload dos arquivos
    public ArrayList<Interesse> getInteresses() throws RemoteException {        //Retorna a lista de interesses 
        return Interesses;
    }

    public String[] getInteressesString() throws RemoteException {              //Retorna a lista de interesses em formado de string
        String[] listaInte = new String[Interesses.size()];                     //Definindo o tamanho do vetor de String
        for (int i = 0; i < Interesses.size(); i++) {                           //Laço para colocar todos os interesses da lista no vetor de String
            listaInte[i] = Interesses.get(i).getNome();
        }
        return listaInte;                                                       //Retornando vetor de String
    }

    public void upLoadFile(byte[] mydata, String nome) throws RemoteException { //Método que faz o upload dos arquivos
        BufferedOutputStream output = null;                                     //Criando variavel do buffer
        try {

            //Verificar conteudo e nome do arquivo
            String nomeArquivo = nome;                                          //Variavel que recebe o nome do arquivo
            byte[] conteudoArquivo = mydata;                                    //Vetor de bytes recebe os dados do arquivo que vai ser enviado
            if (nomeArquivo == null || conteudoArquivo == null) {
                throw new RemoteException("O Arquivo pode ter conteudo nulo");
            }
            //Criando arquivo
            String caminhoArquivo = "src/arquivosDoServidor/" + nomeArquivo;    //Pegando caminho base do servidor e concatenando com o nome para formar o caminho do arquivo
            File file = new File(caminhoArquivo);                               //Criando uma nova variavel do tipo File
            if (!file.exists()) {                                               //Verificando se o arquivo existe
                file.createNewFile();
            }
            //Salvando o conteudo do Arquivo
            output = new BufferedOutputStream(new FileOutputStream(file));      //Pegando o que foi enviando do cliente e colocando no buffer de saida
            output.write(conteudoArquivo);                                      //Escrevendo o conteudo recebido
            output.flush();                                                     //Forçando escrita com flush
            output.close();                                                     //Fechando buffer
        } catch (RemoteException ex) {                                          //Excessões
            throw ex;
        } catch (Exception ex) {
            throw new RemoteException(ex.getLocalizedMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                    output = null;
                } catch (Exception ex) {
                }
            }
        }

    }

    public byte[] downLoadFile(String nome) throws RemoteException {            //Método que realisa download de arquivos que estão na pasta do servidor
        byte[] dados;                                                           //vetor de bytes
        File arquivo = new File("src/arquivosDoServidor/" + nome);		//Criando uma variavel do tipo File e atribuindo a ela o arquivo escolhido
        dados = new byte[(int) arquivo.length()];                               //Fornecendo o tamanho do vetor de bytes
        FileInputStream input;                                                  //Variavel do tipo buffer de entrada
        try {
            input = new FileInputStream(arquivo);                               //Colocando o arquivo criado no buffer de entrada     
            try {
                input.read(dados, 0, dados.length);                             //Escrevendo os dados 
            } catch (IOException e) {                                           //Excessões

                e.printStackTrace();
            }
            try {
                input.close();                                                  //Fechando buffer de entrada
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return dados;

    }

    public String[][] listFiles(String caminhoServer) throws RemoteException {    //Método que retorna todas as informações dos arquivos
        //File serverDir = new File(caminhoServer);
        String[][] infos = null;                                                //Criando Matriz de String
        try ( Stream<Path> paths = Files.walk(Paths.get("C:\\Users\\lucas\\OneDrive\\Documentos\\GitHub\\RMI_finalProject\\src\\arquivosDoServidor"))) {   //Pega todos os caminhos dos arquivos e coloca em um array do tipo path
            caminhosPath = paths.toList();                                      //Pega todos os caminhos e define eles como do tipo lista
            infos = new String[caminhosPath.size()][3];                         //Definindo o tamanho da matriz
        } catch (IOException ex) {
            Logger.getLogger(LoadFileService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (int lin = 0; lin < caminhosPath.size(); lin++) {               //Laço de repetição para colocar os dados da lista na matriz do tipo String
                infos[lin][0] = "Nome: " + caminhosPath.get(lin).getFileName(); //Pegando o nome
                //System.out.println(infos[lin][0]);
                infos[lin][1] = "Última modificação: " + Files.getLastModifiedTime(caminhosPath.get(lin));//Pegando a data da última modificação
                //System.out.println(infos[lin][1]);
                infos[lin][2] = "Tamanho: " + Files.size(caminhosPath.get(lin));//Pegando o tamanho em bytes
                //System.out.println(infos[lin][2]);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoadFileService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return infos;                                                           //Retorna a matriz de String
    }

    public void Registry(String nome, InterfaceClient classe, int tempo, int funcao) throws RemoteException {      //Mértodo remoto que faz o registro de interesse e notifica o cliente
        switch (funcao) {                                                       //Switch case para definir quão função do serviço de registro iremos acessar
            case 0:                                                             //Caso 0 é para notificar o cliente que o que foi registrado interesse está disponível
                classe.notificacao(nome);                                       //Chamando serviço de notificação do cliente
                break;                                  
            case 1:                                                             //Caso 1 é para adicionar um novo interesse 
                Interesses.add(new Interesse(nome, tempo));                     //Adicionando novo interesse a lista
                /*try {
                Thread nova = new Thread(new ContadorDeTempo(nome,tempo));
                nova.start();
                } catch (InterruptedException ex) {
                Logger.getLogger(LoadFileService.class.getName()).log(Level.SEVERE, null, ex);
                }*/ break;
            case 2:                                                             //Caso 2 é para remover o interesse desejado
                for (int i = 0; i < Interesses.size(); i++) {                   //Laço de repetição para procurar o interesse a ser removido
                    Interesse p = Interesses.get(i);                            //Pegando o nteresse que esta nessa posição
                    
                    if (p.getNome().equals(nome)) {                             //Se o nome do interesse a ser removido for igual
                        
                        
                        // Remove.
                        Interesses.remove(p);                                   //Remove esse interesse da lista
                        
                        // Sai do loop.
                        break;
                    }
                }   break;
            default:
                break;
        }

    }

    @Override
    public String getNome() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getTempo() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setTempo(int tempo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setNome(String nome) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
