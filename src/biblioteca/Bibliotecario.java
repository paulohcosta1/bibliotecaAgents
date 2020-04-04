package biblioteca;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulohcosta,keony
 */
public class Bibliotecario extends Agent {
    
    protected void setup (){
        
        addBehaviour (new CyclicBehaviour(this){
            
            public void action ( ){
                ACLMessage msg = myAgent.receive();

                if (msg != null ) {
                    
                    ACLMessage reply = msg.createReply();
                    String content = msg.getContent();
                    
                    if (content.contains("Ajuda")) {
                        
                        content = content.replaceFirst("Ajuda: ", "");
                        
                        try {
                            Thread.currentThread().sleep(1000);

                            System.out.println("O BIBLIOTECARIO PEGOU O LIVRO " + content);
                            ACLMessage entrega = new ACLMessage(ACLMessage.INFORM);
                            entrega.addReceiver(new AID("cliente", AID.ISLOCALNAME));
                            entrega.setContent("Entrega: " + content);
                            myAgent.send(entrega);
                        }catch (InterruptedException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                    }else{
                        try {
                            Thread.currentThread().sleep(1000);
                            content = content.replaceFirst("Entrega: ", "");
                            
                             System.out.println("O BIBLIOTECARIO GUARDOU O LIVRO " + content);
                           
                            
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Bibliotecario.class.getName()).log(Level.SEVERE, null, ex);
                        }

                           
                    }

                }
                
            }

        });

    }
    

}