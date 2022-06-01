package server;
//Imports usados no programa
import client.InterfaceClient;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Interface extends Remote{                                      //Interface do Servidor
	void upLoadFile(byte[] mydata, String nome) throws RemoteException;     //Chama o método de opload
        byte[] downLoadFile(String nome)throws RemoteException;                 //Chama o método de download
        public String[][] listFiles(String caminhoServer) throws RemoteException; //Chama o método que lista os arquivos do servidor
        public void Registry(String nome, InterfaceClient classe, int tempo, int funcao ) throws RemoteException; //Chama o método que faz o registro de interesse e notifica o cliente
        public ArrayList<Interesse> getInteresses()throws RemoteException;      //Receber a lista de interesses
        public String[] getInteressesString() throws RemoteException;           //Pegar a lista de interesses em formado de String
}
