//UNIFESSPA - Universidade do Sul e Sudeste do Pará
//Alunos: Cristina Vitoria Leal Leite, Lucas Antonio da Silva Lima e Lucas Leite de Oliveira
package client;
//Imposts para uso no programa
import java.io.BufferedOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.Interface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;

/**
 *
 * @author lucas
 */
public class Client {

    /**
     *
     */
    public Registry registry;

    /**
     *
     */
    public Interface carregar;

    /**
     *
     * @param args
     * @throws Exception
     * @throws IOException
     */
    public static void main(String[] args) throws Exception, IOException {
        // TODO Auto-generated method stub
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //Instanciando um buffer de entrada
        Registry registry = LocateRegistry.getRegistry("localhost", 1234);        //Registrando o cliente no RMI
        Interface carregar = (Interface) registry.lookup("Carregar");             //Pegando da rede de nomes a interface cadastrada
        Registros classe = new Registros();                                       //Declarando nova variavel do tipo Registro
        registry.bind("Notificar", classe);                                       //Registrando na rede de nomes o serviço de notificação ao client
        while (true) {                                                              

            try {
                //Declaração de variaveis que serão utilizadas
                String caminhoClient = "src/arquivosDoCliente/";
                String caminhoServer = "src/arquivosDoServidor/";
                String selecionado = null;
                //Criar registro para instanciar o local host

                //Recebimento das instruções atravez do teclado
                System.out.println("""
                                   Digite o servico que deseja utilizar:
                                    1-Upload
                                    2-Download
                                    3-Consultar arquivos ou registrar 
                                   """);
                String teclado = in.readLine();                                    //Recebendo valores do teclado 
                int selecionado2 = 0;
                //Seleção de casos para operar
                switch (teclado) {

                    case "1" -> {
                        System.out.println("Digite o nome do arquivo para envio:");         //
                        selecionado = in.readLine();                                 //Recebendo do teclado e guardando em "selecionado"
                        caminhoClient = caminhoClient + selecionado;                 //Concatenando o caminho da pasta do cliente com o nome do arquivo a ser enviado, para criar o caminho até o arquivo
                        File arquivoSelecionado = new File(caminhoClient);                  //Criando variavel do tipo File com o caminho criado anteriormente
                        byte[] arquivoEnviar = new byte[(int) arquivoSelecionado.length()]; //Criando um vetor de bytes que vai receber o seu tamanho baseado no tamanho do arquivo.
                        FileInputStream fl = new FileInputStream(arquivoSelecionado);       //Criando um novo buffer de entrada para colocar o arquivo a ser enviado nele    
                        fl.read(arquivoEnviar, 0, arquivoEnviar.length);                    //Escrevendo no buffer o arquivo e seu tamanho
                        System.out.println("Upload in progress...");                        //
                        carregar.upLoadFile(arquivoEnviar, selecionado);                    //Chamando o serviço remoto que fará o upload
                        fl.close();                                                         //fechando buffer
                    }

                    case "2" -> {
                        System.out.println("Digite o nome do arquivo para baixar:");        //
                        selecionado = in.readLine();                                 //Recebendo do teclado e guardando em "selecionado"
                        caminhoClient = caminhoClient + selecionado;              //Concatenando o caminho da pasta do cliente com o nome do arquivo a ser enviado, para criar o caminho até o arquivo
                        //File arquivoSelecionado = new File(caminhoServer);                  //Criando variavel do tipo File com o caminho criado anteriormente
                        byte[] arquivoRecebido = carregar.downLoadFile(selecionado);       //Chamando o serviço de download e guardando os bytes do arquivo na variavel "arquivoRecebido"
                        System.out.println("Baixando...");                                  //
                        File arquivo = new File(caminhoClient);                             //Criando nova variavel do tipo file
                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(arquivo));//Criando um buffer de saida
                        output.write(arquivoRecebido);                                      //Escrevendo arquivo recebido 
                        output.flush();                                                     //Forçando escrita
                        output.close();                                                     //Fechando buffer de saida
                    }

                    case "3" -> {

                        System.out.println("////Arquivos disponíveis no servidor atualmente////");      //
                        String caminho[][] = carregar.listFiles(caminhoServer);                         //Chamando o serviço que lista todas as informações dos arquivos da pasta
                        
                        for (int i = 0; i < caminho.length; i++) {                                      //Laço de repetição para imprimir dados                                      
                            System.out.println(caminho[i][0] + " - " + caminho[i][1] + " - " + caminho[i][2]);
                        }

                        System.out.println("////FIM////");                                              //
                        
                        while (selecionado2 < 2) {                                                    //Laço de repetição para selecionar opções de registrar interesse
                            System.out.println("Para registrar interesse em arquivo não disponível digite 0, 1 para cancelar registro e 2 para sair");
                            selecionado2 = Integer.parseInt(in.readLine());                                                //Recebendo opção selecionada
                            if (0 == selecionado2) {                                              //Se for opção zero
                                System.out.println("\nSeus interesses:\n");                             //
                                String[] inter = carregar.getInteressesString();                        //Solicitação de serviço que exibi todos os interesses registrados
                                for (String inter1 : inter) {                                           //Laço de repetição para imprimir os registros          
                                    System.out.println(inter1);                                         //
                                }
                                
                                System.out.println("Digite o nome do arquivo que deseja registrar interesse: ");
                                String arq = in.readLine();                                             //Recebe o nome do arquivo de interesse 
                                carregar.Registry(arq, classe, 10000, 1);                               //Chama o serviço de registro de interesse e cadastra o interesse

                            } else if (1 == selecionado2) {                                       // Se for selecionado 1, ele imprime os interesses
                                System.out.println("\nSeus interesses:\n");                             //
                                String[] inter = carregar.getInteressesString();                        //
                                for (String inter1 : inter) {                                           //
                                    System.out.println(inter1);                                         //
                                }
                                
                                System.out.println("\nDigite o nome do Interesse que deseja remover:\n");
                                String arq = in.readLine();                                             //Recebe o interesse a ser removido da lista de interesses
                                carregar.Registry(arq, classe, 10000, 2);                               //Chama serviço de registro para remover o resgistro de interesse selecionado
                                

                            }
                        }
                    }

                    default -> {                                                                        //Caso padrão para qualquer outra opção
                        System.out.println("Opcao inexistente!");
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    static void notificacao(String msg) {                                       //Método de notificação que é chamado por algum serviço
        System.out.println(msg);
    }

}
