/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author paulohcosta,keony
 */
public class Atendente extends Agent {
    
    ArrayList<String> livrosDisponiveis;
    ArrayList<String> listaTamanho;
    int tamanho;

    protected void setup (){
        this.livrosDisponiveis = new ArrayList<String>();        
        this.livrosDisponiveis.add("Dom Quixote");
        this.livrosDisponiveis.add("Guerra e Paz");
        this.livrosDisponiveis.add("A Montanha Mágica");
        this.livrosDisponiveis.add("Cem Anos de Solidão");
        this.livrosDisponiveis.add("Harry Potter");
        this.livrosDisponiveis.add("AS CRONICAS DE GELO E FOGO");
        
        addBehaviour (new CyclicBehaviour(this){
            
            public void action ( ){
                ACLMessage msg = myAgent.receive();

                if (msg != null ) {
                    
                    ACLMessage reply = msg.createReply();
                    String content = msg.getContent();
                    
                    
                    if (content.contains("Livro")) {
                        content = content.replaceFirst("Livro: ", "");

                        String[] contentSplit = content.split(",");
                        content = contentSplit[0];
                        
                        tamanho = Integer.parseInt(contentSplit[1]);
                        
                        int livroIndex = livrosDisponiveis.indexOf(content);  
                        // não tem o livro
                        if ( tamanho == 1 && livroIndex == -1 ) {                        
                            ACLMessage naoTemLivro = new ACLMessage(ACLMessage.INFORM);
                            naoTemLivro.addReceiver(new AID("cliente", AID.ISLOCALNAME));
                            naoTemLivro.setContent("sugestao");
                            myAgent.send(naoTemLivro);
                            
                            System.out.println("NÃO TEMOS O LIVRO " + content);
                        } else {
                            ACLMessage temLivro = new ACLMessage(ACLMessage.INFORM);
                            temLivro.addReceiver(new AID("cliente", AID.ISLOCALNAME));
                            temLivro.setContent("Livro: " + content);
                            myAgent.send(temLivro);

                            System.out.println("TEMOS O LIVRO " + content);
                        }   
                    } else if(content.contains("Sugestao")) {
                        content = content.replaceFirst("Sugestao: ", "");
                        ACLMessage sugestao = new ACLMessage(ACLMessage.INFORM);
                        sugestao.addReceiver(new AID("cliente", AID.ISLOCALNAME));
                        sugestao.setContent("Livro: As crônicas de gelo e fogo");
                        
                        System.out.println("TEMOS O LIVRO As crônicas de gelo e fogo");
                        
                        myAgent.send(sugestao);
                    }else if(content.contains("Devolveu")) {
                        content = content.replaceFirst("Devolveu: ", "");                      
                        System.out.println("ENTREGOU O LIVRO " + content + "PARA O BIBLIOTECARIO"); 
                        
                        ACLMessage devolucao = new ACLMessage(ACLMessage.INFORM);
                        devolucao.addReceiver(new AID("bibliotecario", AID.ISLOCALNAME));
                        devolucao.setContent("Entrega: " + content);                         
                        myAgent.send(devolucao);

                         
                    }else{
                        ACLMessage atendimento = new ACLMessage(ACLMessage.INFORM);
                        atendimento.addReceiver(new AID("cliente", AID.ISLOCALNAME));
                        atendimento.setContent("suavez");
                        myAgent.send(atendimento);

                    }
                }  
            }
        });
    }
    
}
