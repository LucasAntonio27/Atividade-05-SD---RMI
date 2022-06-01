package server;

import java.rmi.RemoteException;

public class Interesse extends RemoteException implements InterArq{             //Classe do objeto dos interesses
	
	private static final long serialVersionUID = 1L;
	private String nome;                                                    //Declaração de variaveis
	private int tempo;                  
	//private String diretorio;
        public Interesse(String nome, int tempo){                               //Método que define o nome e tempo do interesse
            this.nome = nome;                                                   //Variavel nome dessa classe recebe o nome do argumento
            this.tempo = tempo;                                                 //Variavel tempo dessa classe recebe o tempo do argumento
        }
        @Override                                                               //Sobrescrever
	public String getNome() {                                               //Retorna o nome do objeto
		return nome;                                                        
	}
	public void setNome(String nome) {                                      //Definir somente o nome do objeto
		this.nome = nome;
	}
        @Override
	public int getTempo(){                                                  //REtorna o tempo de duração do objeto
		return tempo;
	}
	public void setTempo(int tempo) {                                       //Definir somento o tempo de duração do objeto
		this.tempo = tempo;
	}
        
	
}
