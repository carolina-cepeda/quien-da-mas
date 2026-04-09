/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exam.remote;

import edu.eci.arsw.exam.FachadaPersistenciaOfertas;
import edu.eci.arsw.exam.MainFrame;
import edu.eci.arsw.exam.events.OffertMessageProducer;
;

/**
 *
 * @author hcadavid
 */
public class ManejadorOfertasSkeleton implements ManejadorOfertasStub{

    private FachadaPersistenciaOfertas fpers=null;
    private MainFrame mainFrame = null;
    private OffertMessageProducer messageProducer = null;


    public void setFachadaPersistenciaOfertas(FachadaPersistenciaOfertas fpers) {
        this.fpers = fpers;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public synchronized void agregarOferta(String codOferente,String codprod,int monto) {
        
        if (!fpers.getMapaOfertasRecibidas().containsKey(codprod)){
            //se ha recibido la primera oferta 
            fpers.getMapaOfertasRecibidas().put(codprod, 1);
            //se asigna el monto propuesto como mejor oferta
            fpers.getMapaMontosAsignados().put(codprod, monto);
            // se asigna al oferente como ganador provisional
            fpers.getMapaOferentesAsignados().put(codprod, codOferente);
        } else {
            int ofertasActuales = fpers.getMapaOfertasRecibidas().get(codprod);
            fpers.getMapaOfertasRecibidas().put(codprod, ofertasActuales + 1);
            
            if (fpers.getMapaMontosAsignados().get(codprod) > monto) {
                fpers.getMapaMontosAsignados().put(codprod, monto);
                fpers.getMapaOferentesAsignados().put(codprod, codOferente);
            }
            
            if (ofertasActuales + 1 == 3) {
                String ganador = fpers.getMapaOferentesAsignados().get(codprod);
                int montoGanador = fpers.getMapaMontosAsignados().get(codprod);

                if (mainFrame != null) {
                mainFrame.mostrarOfertaGanadora(codprod, ganador, montoGanador);
            } else {
                System.out.println("Ganador producto " + codprod + ": " + ganador + " con $" + montoGanador);
            }
                try {
                    String mensaje = "GANADOR:" + ganador + ":PRODUCTO:" + codprod;
                    messageProducer.sendMessages(mensaje);
                } catch (Exception e) {
                    System.err.println("Error al notificar ganador: " + e.getMessage());
                }
            }
        }
        
    }
    
    public void setMessageProducer(OffertMessageProducer messageProducer) {
    this.messageProducer = messageProducer;
}
    
}