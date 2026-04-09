package edu.eci.arsw.exam.events;

import edu.eci.arsw.exam.IdentityGenerator;
import edu.eci.arsw.exam.Product;
import java.util.Random;
import java.io.*;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import edu.eci.arsw.exam.remote.ManejadorOfertasStub;
public class OffertMessageListener implements MessageListener {

    Random rand = new Random(System.currentTimeMillis());
    private ManejadorOfertasStub manejadorOfertas;

    public OffertMessageListener() {
        super();
        System.out.println("Comprador #"+IdentityGenerator.actualIdentity+" esperando eventos...");
    }

    public void setManejadorOfertas(ManejadorOfertasStub manejadorOfertas){
        this.manejadorOfertas = manejadorOfertas;
    }
    @Override
    public void onMessage(Message message) {
        try {
            Product receivedProduct = new Product(message.getBody());
            System.out.println("Comprador #"+IdentityGenerator.actualIdentity+" recibió: "+receivedProduct.getCode());
            
            int montoOferta = Math.abs(rand.nextInt(99999999));

            manejadorOfertas.agregarOferta(
                IdentityGenerator.actualIdentity,
                receivedProduct.getCode(),
                montoOferta);
            
        } catch (Exception e) {
            throw new RuntimeException("An exception occured while trying to get a AMQP object:" + e.getMessage(), e);
        }

    }

}
