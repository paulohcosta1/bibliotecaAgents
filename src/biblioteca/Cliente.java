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
    
    ArrayList<String> listaLivrosDisponiveis;

    /**
     * @param args the command line arguments
     */
    protected void setup() {
        this.livrosDisponiveis = new ArrayList<String>(); 
        this.listaLivros = new ArrayList<String>();        
        this.livrosNaoDisponiveis = new ArrayList<String>();        
        this.todosLivros = new ArrayList<String>();
        this.listaLivrosDisponiveis = new ArrayList<String>();
        
        this.livrosDisponiveis.add("Dom Quixote");;
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
        int qtdLivros = new Random().nextInt(2) + 1;
        
        for (int i = 0; i < qtdLivros; i++) {
           this.listaLivros.add(todosLivros.get(i));
        };
         
        addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
               
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("atendente", AID.ISLOCALNAME)); //Precisa ser o mesmo nome que esta la nos argumentos
                msg.setLanguage ("Português");

                msg.setOntology(getLocalName());
                System.out.println("CLIENTE " + getLocalName() + "  CHEGOU NO BALCAO DO ATENDENTE");
                //AQUI ELE PEDE PRA CHECAR SE ESSES LIVROS ESTAO DISPONIVEIS
 
                    
                for (int i = 0; i < listaLivros.size(); i++) {
                    try {
                        System.out.println("O LIVRO " + listaLivros.get(i) + " ESTA DISPONIVEL?");

                        msg.setContent("Livro: " + listaLivros.get(i));
                        
                        myAgent.send(msg);
                        
                        Thread.currentThread().sleep(3000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }          
                              
                }
            }

        });
        
         addBehaviour (new CyclicBehaviour(this) {
            void sugestao() {;
                ACLMessage pedirSugestao = new ACLMessage(ACLMessage.INFORM);
                pedirSugestao.addReceiver(new AID("atendente", AID.ISLOCALNAME));
                pedirSugestao.setContent("Sugestao");
                myAgent.send(pedirSugestao);
                
                System.out.println("Teria alguma sugestão?");
            }
            
            void buscarLivro(String livro) {
                System.out.println("FOI BUSCAR O LIVRO " + livro);
            }
            
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null){
                    String content = msg.getContent();
                  if(content != null && content.contains("Livro")) {
                    try {
                        Thread.currentThread().sleep(3000);
                        buscarLivro(content);
                    }catch(InterruptedException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                      
                  } else if (content.contains("sugestao")) {
                    sugestao();
                  }

//                    
//                    if(listaLivrosDisponiveis.size() > 0) {
//                        try{
//                            for (int i = 0; i < listaLivrosDisponiveis.size(); i++) {
//
//                                Thread.currentThread().sleep(5000);
//                                if(listaLivrosDisponiveis.size() == i){
//                                    System.out.println("O CLIENTE " + getLocalName() + "  FOI BUSCAR O LIVRO "+listaLivrosDisponiveis.get(i) + "E VOLTOU PARA O ATENDENTE");
//                                }else{
//                                   System.out.println("O CLIENTE " + getLocalName() + "  FOI BUSCAR O LIVRO "+listaLivrosDisponiveis.get(i));
//                                }
//                            }
//
////                            ACLMessage msgAtendente = new ACLMessage(ACLMessage.INFORM);;
////                            msgAtendente.addReceiver(new AID("atendente", AID.ISLOCALNAME));
//    //                        msgAtendente.setContent("cliente");
////                            myAgent.send(msgAtendente);;
//
//
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    } else {
//                        System.out.println("CHEGOU NO ELZA");
//                    }
//                    
//                }else{
//                    block();
                }
            }
    });
         
//         
//         addBehaviour(new CyclicBehaviour(this) {
//
//            void atendimento() {
//                
//                try {
//                    Thread.currentThread().sleep(1000);   
//                    
//                    
//                     for (int i = 0; i < listaLivros.size(); i++) {
//
//                            Thread.currentThread().sleep(3000);
//                            
//                            if(listaLivros.size() == i){
//                                 System.out.println("O CLIENTE " + getLocalName() + " ENTREGOU O ULTIMO LIVRO "+listaLivros.get(i) + " PARA DAR BAIXA E FOI PARA CASA");
//                            }else{
//                               System.out.println("O CLIENTE " + getLocalName() + "  ENTREGOU O  LIVRO "+listaLivros.get(i) + " PARA DAR BAIXA ");
//                            }
//                                
//
//                        }
//                   
//                        System.out.println("O CLIENTE " + getLocalName() + "ENTREGOU OS LIVROS PARA O ATENDENTE DAR BAIXA E FOI SAIU DA BIBLIOTECA");
//
//                        ACLMessage msgOperador = new ACLMessage(ACLMessage.INFORM);
//                        msgOperador.addReceiver(new AID("operador", AID.ISLOCALNAME));
//
//                        msgOperador.setContent("compras");
//                        myAgent.send(msgOperador);
//                   
//
//                    myAgent.doDelete();
//
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            };
//
//            @Override
//            public void action() {
//
//                ACLMessage msgRecebida = myAgent.receive();
//                if (msgRecebida != null) {
//                    String conteudo = msgRecebida.getContent();
//
//                    if (conteudo.equalsIgnoreCase("suavez")) {
//                        atendimento();
//
//                    }
//
//                }
//            }
//        });
//                
                
    };
}
