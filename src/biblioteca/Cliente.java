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
 * @author paulohcosta,keony
 */
public class Cliente extends Agent {
    
    ArrayList<String> listaLivros;

    ArrayList<String> livrosDisponiveis;
    ArrayList<String> livrosEntrega;

    ArrayList<String> livrosNaoDisponiveis;
    List<String> todosLivros;
    
    ArrayList<String> listaLivrosDisponiveis;

    /**
     * @param args the command line arguments
     */
    protected void setup() {
        this.livrosDisponiveis = new ArrayList<String>(); 
        this.livrosEntrega = new ArrayList<String>(); 
        this.listaLivros = new ArrayList<String>();        
        this.livrosNaoDisponiveis = new ArrayList<String>();        
        this.todosLivros = new ArrayList<String>();
        this.listaLivrosDisponiveis = new ArrayList<String>();
        
        
        
        this.livrosEntrega.add("Mistborn");
        this.livrosEntrega.add("A Caverna do Dragão");
        this.livrosEntrega.add("A Arte da guerra");

        
        this.livrosDisponiveis.add("Dom Quixote");
        this.livrosDisponiveis.add("Guerra e Paz");
        this.livrosDisponiveis.add("A Montanha Mágica");
        this.livrosDisponiveis.add("Cem Anos de Solidão");
        this.livrosDisponiveis.add("Harry Potter");
        
       
        this.livrosNaoDisponiveis.add("O Iluminado");
        
      
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
            
            void entregarLivros(){
             ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
             msg.addReceiver(new AID("atendente", AID.ISLOCALNAME)); //Precisa ser o mesmo nome que esta la nos argumentos
            
             for (int i = 0; i < livrosEntrega.size(); i++) {
                    try {
                        System.out.println(getLocalName() + " DEVOLVEU O LIVRO " + livrosEntrega.get(i));
                        
                        msg.setContent("Devolveu: " + livrosEntrega.get(i));                        
                        
                        myAgent.send(msg);
                        
                        Thread.currentThread().sleep(3000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }          
                              
                }
            }
            
            
            @Override
            public void action() {
               
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("atendente", AID.ISLOCALNAME)); //Precisa ser o mesmo nome que esta la nos argumentos
                msg.setLanguage ("Português");

                msg.setOntology(getLocalName());
                System.out.println( getLocalName() + "  CHEGOU NO BALCAO DO ATENDENTE");
                //AQUI ELE PEDE PRA CHECAR SE ESSES LIVROS ESTAO DISPONIVEIS
                
                entregarLivros();
 
                    
                for (int i = 0; i < listaLivros.size(); i++) {
                    try {
                        System.out.println("O LIVRO " + listaLivros.get(i) + " ESTA DISPONIVEL?");

                        msg.setContent("Livro: " + listaLivros.get(i) + "," +  listaLivros.size());
                        
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
                livro = livro.replaceFirst("Livro: ", "");
                ACLMessage pedirAjuda = new ACLMessage(ACLMessage.INFORM);
                pedirAjuda.addReceiver(new AID("bibliotecario", AID.ISLOCALNAME));
                pedirAjuda.setContent("Ajuda: "+livro);
                myAgent.send(pedirAjuda);
                
                System.out.println(getLocalName() +" PEDIU AJUDA AO BIBLIOTECARIO PARA PEGAR O LIVRO " + livro);
            }
            
            void atendimento() {
//                
                try {
                    Thread.currentThread().sleep(1000);   

                    for (int i = 0; i < listaLivros.size(); i++) {

                        Thread.currentThread().sleep(1000);
                        int livroIndex = listaLivros.indexOf(listaLivros.get(i)) + 1;

                        if(listaLivros.size() == livroIndex){
                             System.out.println(getLocalName() + " ENTREGOU O ULTIMO LIVRO "+listaLivros.get(i) + " PARA DAR BAIXA AO ATENDENTE E FOI PARA CASA");
                        }else{
                           System.out.println(getLocalName() + "  ENTREGOU O  LIVRO "+listaLivros.get(i) + " PARA DAR BAIXA AO ATENDENTE ");
                        }                               

                    } 
                     
                    myAgent.doDelete();



                } catch (InterruptedException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }

            };
            
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null){
                    String content = msg.getContent();
                  if(content.contains("Livro")) {
                    try {
                        Thread.currentThread().sleep(3000);
                        buscarLivro(content);
                    }catch(InterruptedException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                      
                  } else if (content.contains("sugestao")) {
                    sugestao();
                  }else if(content.contains("Entrega")){
                      content = content.replaceFirst("Entrega: ", "");
                      
                      int livroIndex = listaLivros.indexOf(content);
                      
                      if(livroIndex == -1){ //CASO TIVER SUGESTAO, ELE ADD NO ARRAY
                          listaLivros.add(content);
                      }
                    
                      
                      if(listaLivros.size() == (livroIndex+1)){
                          
                        ACLMessage pedirSugestao = new ACLMessage(ACLMessage.INFORM);
                        pedirSugestao.addReceiver(new AID("atendente", AID.ISLOCALNAME));
                        pedirSugestao.setContent("atendimento");
                        myAgent.send(pedirSugestao);
                          
                        System.out.println("BIBLIOTECARIO ENTREGOU O LIVRO  " + content + " E O CLIENTE FOI PARA O ATENDIMENTO");
                      }else{
                        System.out.println("BIBLIOTECARIO ENTREGOU O LIVRO  " + content);
                      }


                  }else if(content.equalsIgnoreCase("suavez")) {
                      atendimento();
                      
                  }

                }
            }
    });         

                
                
    };
}
