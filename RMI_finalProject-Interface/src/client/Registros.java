/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;
//Imports usados no programa
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author lucas
 */
public class Registros extends UnicastRemoteObject implements InterfaceClient, Serializable {

    /**
     *
     * @throws RemoteException
     */
    protected Registros() throws RemoteException {                        //Definindo a super classe
        super();
    }
    private static final long serialVersionUID = 1L;
    
    /**
     *
     * @param nome
     */
    public void notificacao(String nome){                                       //Método que chama a notificação no cliente
        String msg = "O Arquivo: " + nome + " esta disponivel!";
        Client.notificacao(msg);                                                //Envia a mensagem para o cliente ser notificado sobre qual arquivo ficou disponivel
        //System.out.println("Chegou Arquivo");
    }

    @Override
    public void UploadFile(String caminhoClient, String selecionado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}