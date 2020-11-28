import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class CPU extends Entity{

	private boolean ocupada;

	private Cliente cliente;
    
   public CPU(Model owner, String name, boolean showInTrace) {
	
	   super (owner, name, showInTrace);
		
	   this.ocupada = false;
	}

   public void setOcupada(boolean estaOcupada) {
	   this.ocupada = estaOcupada;
   }

   public void utilizar(Cliente cliente) {
	   
	   MaquinaDeBusca modeloMaquinaDeBusca;
	   double tempoUtilizacao;
	   EventoTerminoUsoCPU eventoTerminoUsoCPU;

	   this.cliente = cliente;

	   modeloMaquinaDeBusca = (MaquinaDeBusca) getModel();

	   tempoUtilizacao = modeloMaquinaDeBusca.getTempoUtilizacao();
	   modeloMaquinaDeBusca.sendTraceNote(this + " serve " + cliente + " por " + tempoUtilizacao + " minutos.");

	   eventoTerminoUsoCPU = new EventoTerminoUsoCPU(modeloMaquinaDeBusca, "Evento relacionado ao término da lavagem das roupas do cliente", true);
	   eventoTerminoUsoCPU.schedule(this, cliente, new TimeSpan (tempoUtilizacao));
   }

   public boolean getOcupada() {
	   return (this.ocupada);
   }

   public Cliente getCliente() {
	   return (this.cliente);
   }
}