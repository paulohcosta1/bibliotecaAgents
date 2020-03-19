/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 *
 * @author paulohcosta
 */
public class Atendente extends Agent {
    
    ArrayList<String> livrosDisponiveis;

    
    protected void setup (){
        
        ArrayList<String> livrosDisponiveis;
        this.livrosDisponiveis = new ArrayList<String>(); 

        this.livrosDisponiveis.add("Dom Quixote");
        this.livrosDisponiveis.add("Guerra e Paz");
        this.livrosDisponiveis.add("A Montanha Mágica");
        this.livrosDisponiveis.add("Cem Anos de Solidão");
        this.livrosDisponiveis.add("Harry Potter");
        
        
        addBehaviour (new CyclicBehaviour(this){
            
        public void action ( ){
            ACLMessage msg = myAgent.receive();

            if (msg != null ) {
                             System.out.println(msg);

                ACLMessage reply = msg.createReply();
                String content = msg.getContent();
               
//                if (content.equalsIgnoreCase( "Fogo")){
//                    reply.setPerformative(ACLMessage.INFORM) ;
//                    reply.setContent("Recebi seu aviso! Obrigado por auxiliar meu serviço");
//                    myAgent.send(reply);
//                    System.out.println("O agente "+ msg.getSender().getName()+" avisou de um incêndio");
//                    System.out.println( "Vou ativar os procedimentos de combate ao incêndio!");
//                }else{
//                    block();
//                }
//                        
        }  
        } // fim do a c t i on ( )
        }); // fim do addBehaviour ( )
    }
    
}
