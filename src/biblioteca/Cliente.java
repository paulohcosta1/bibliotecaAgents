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
                msg.addReceiver(new AID("atendente", AID.ISLOCALNAME)); //Precisa ser o mesmo nome que esta la nos argumentos
                msg.setLanguage ("Português");


                msg.setOntology(getLocalName());
                System.out.println("CLIENTE " + getLocalName() + "  CHEGOU NO BALCAO DO ATENDENTE");
                //AQUI ELE PEDE PRA CHECAR SE ESSES LIVROS ESTAO DISPONIVEIS
                System.out.println(listaLivros);

                    
                    
                for (int i = 0; i < listaLivros.size(); i++) {
                    try {
                        System.out.println("O LIVRO " + listaLivros.get(i) + " ESTA DISPONIVEL?");

                        Thread.currentThread().sleep(1000);
                        msg.setContent("O LIVRO " + listaLivros.get(i) + " ESTA DISPONIVEL?");

                        myAgent.send(msg);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }          
                              
                }
            }

        });
         
         addBehaviour (new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null){
                    
                    String content = msg.getContent();
                    
                    for (int i = 0; i < listaLivros.size(); i++) {
                       
                        if(content.equalsIgnoreCase("O LIVRO " + listaLivros.get(i) + "NAO ESTA DISPONIVEL")){ //padrao de resposta
                            listaLivros.remove(i);                                    
                        }
                       
                    }
                    
                    try{
                        for (int i = 0; i < listaLivros.size(); i++) {

                            Thread.currentThread().sleep(5000);
                            if(listaLivros.size() == i){
                                 System.out.println("O CLIENTE " + getLocalName() + "  FOI BUSCAR O LIVRO "+listaLivros.get(i) + "E VOLTOU PARA O ATENDENTE");
                            }else{
                               System.out.println("O CLIENTE " + getLocalName() + "  FOI BUSCAR O LIVRO "+listaLivros.get(i));
                            }
                                

                        }
                        
                        ACLMessage msgAtendente = new ACLMessage(ACLMessage.INFORM);
                        msgAtendente.addReceiver(new AID("atendente", AID.ISLOCALNAME));
                        msgAtendente.setContent("cliente");
                        myAgent.send(msgAtendente);
                        
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                }else{
                    block();
                }
            }
    });
         
         addBehaviour(new CyclicBehaviour(this) {

            void atendimento() {
                
                try {
                    Thread.currentThread().sleep(1000);   
                    
                    
                     for (int i = 0; i < listaLivros.size(); i++) {

                            Thread.currentThread().sleep(3000);
                            
                            if(listaLivros.size() == i){
                                 System.out.println("O CLIENTE " + getLocalName() + " ENTREGOU O ULTIMO LIVRO "+listaLivros.get(i) + " PARA DAR BAIXA E FOI PARA CASA");
                            }else{
                               System.out.println("O CLIENTE " + getLocalName() + "  ENTREGOU O  LIVRO "+listaLivros.get(i) + " PARA DAR BAIXA ");
                            }
                                

                        }
                   
                        System.out.println("O CLIENTE " + getLocalName() + "ENTREGOU OS LIVROS PARA O ATENDENTE DAR BAIXA E FOI SAIU DA BIBLIOTECA");

                        ACLMessage msgOperador = new ACLMessage(ACLMessage.INFORM);
                        msgOperador.addReceiver(new AID("operador", AID.ISLOCALNAME));

                        msgOperador.setContent("compras");
                        myAgent.send(msgOperador);
                   

                    myAgent.doDelete();

                } catch (InterruptedException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            @Override
            public void action() {

                ACLMessage msgRecebida = myAgent.receive();
                if (msgRecebida != null) {
                    String conteudo = msgRecebida.getContent();

                    if (conteudo.equalsIgnoreCase("suavez")) {
                        atendimento();

                    }

                }
            }
        });
                
                

        
    }

    

    
    


    
}
