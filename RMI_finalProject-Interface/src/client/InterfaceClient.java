/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package client;
//Imports para uso no programa
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author lucas
 */
public interface InterfaceClient extends Remote{                //Interface do cliente

    /**
     *
     * @param nome
     * @throws RemoteException
     */
    public void notificacao(String nome)throws RemoteException; //Chamado para o serviço de notificação no client
    public void UploadFile(String caminhoClient, String selecionado) throws RemoteException;
}
