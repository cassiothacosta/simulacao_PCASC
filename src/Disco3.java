import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class Disco3 extends Entity{

	private boolean ocupado;

	private Cliente cliente;
      
   public Disco3(Model owner, String name, boolean showInTrace) {
	
	   super (owner, name, showInTrace);
		
	   this.ocupado = false;
	}

   public void setOcupado(boolean estaOcupada) {
	   this.ocupado = estaOcupada;
   }

   public void utilizar(Cliente cliente) {
	   
	   MaquinaDeBusca maquinaDeBusca;
	   double tempoUtilizacao;
	   EventoTerminoUsoDisco3 eventoTerminoUsodisco3;

	   this.cliente = cliente;

	   maquinaDeBusca = (MaquinaDeBusca) getModel();

	   tempoUtilizacao = maquinaDeBusca.getTempoUtilizacao();
	   maquinaDeBusca.sendTraceNote(this + " serve " + cliente + " por " + tempoUtilizacao + " minutos.");

	   eventoTerminoUsodisco3 = new EventoTerminoUsoDisco3(maquinaDeBusca, "Evento relacionado ao termino do uso do disco3 pelo cliente", true);
	   eventoTerminoUsodisco3.schedule(this, cliente, new TimeSpan (tempoUtilizacao));
   }

   public boolean getOcupado() {
	   return (this.ocupado);
   }

   public Cliente getCliente() {
	   return (this.cliente);
   }
}