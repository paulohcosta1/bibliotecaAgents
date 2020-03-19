/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *
 * @author paulohcosta
 */
public class Cliente extends Agent {
    
    ArrayList<String> listaLivros;
    ArrayList<String> livrosDisponiveis;
    ArrayList<String> livrosNaoDisponiveis;
    List<String> todosLivros;


    /**
     * @param args the command line arguments
     */
    protected void setup() {
        this.livrosDisponiveis = new ArrayList<String>(); 
        this.listaLivros = new ArrayList<String>();        
        this.livrosNaoDisponiveis = new ArrayList<String>();        
        this.todosLivros = new ArrayList<String>();
        
        this.livrosDisponiveis.add("Dom Quixote");
        this.livrosDisponiveis.add("Guerra e Paz");
        this.livrosDisponiveis.add("A Montanha Mágica");
        this.livrosDisponiveis.add("Cem Anos de Solidão");
        this.livrosDisponiveis.add("Harry Potter");
        
       
        this.livrosNaoDisponiveis.add("O Iluminado");
        this.livrosNaoDisponiveis.add("The Witcher");
        this.livrosNaoDisponiveis.add("The Hobbit");
        
      
       this.todosLivros = Stream.of(this.livrosDisponiveis, this.livrosNaoDisponiveis)
                                        .flatMap(x -> x.stream())
                                        .collect(Collectors.toList());

        Collections.shuffle(todosLivros); 
        
        this.listaLivros = new ArrayList<String>();
        int qtdLivros = new Random().nextInt(8);
        
        
        for (int i = 0; i < qtdLivros; i++) {
           this.listaLivros.add(todosLivros.get(i));
        }
        
         addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("atendente", AID.ISLOCALNAME));
                msg.setLanguage ("Português");


                msg.setOntology(getLocalName());
                System.out.println("CLIENTE " + getLocalName() + "  CHEGOU NO BALCAO DO ATENDENTE");
                //AQUI ELE PEDE PRA CHECAR SE ESSES LIVROS ESTAO DISPONIVEIS
                
                try {
                    Thread.currentThread().sleep(1000);
                    msg.setContent("listalivro:" + listaLivros);
                    System.out.println("CLIENTE  tem esses livros" + listaLivros );

                    myAgent.send(msg);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }

                              

            }

        });
         
//         addBehaviour (new CyclicBehaviour(this) {
//            public void action() {
//                ACLMessage msg = myAgent.receive();
//                if (msg != null){
//                    String content = msg.getContent();
//                    System.out.println("--> " + msg.getSender().getName() + ":" + content);
//                }else{
//                    block();
//                }
//            }
//    });
                
                

        
    }

    

    
    


    
}
