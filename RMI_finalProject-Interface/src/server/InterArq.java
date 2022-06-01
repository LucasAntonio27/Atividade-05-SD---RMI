package server;
//Imposts usados no programa
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterArq extends Remote{                           //Interface do objeto de registro de interesses
	public String getNome()throws RemoteException;              //Serviço de pegar o nome do interesse
	public int getTempo()throws RemoteException;                //Serviço de pegar o tempo de duração do interesse
        public void setTempo(int tempo)throws RemoteException;      //Serviço de colocar o tempo do interesse
        public void setNome(String nome)throws RemoteException;     //Serviço de colocar o nome do interesse
        
}
