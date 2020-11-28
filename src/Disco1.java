import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class Disco1 extends Entity{

	private boolean ocupado;

	private Cliente cliente;
	private int visitas;
    private int totalRequisicoesSairam;
    private float totalTempoOcupado;
      
   public Disco1(Model owner, String name, boolean showInTrace) {
	
	   super (owner, name, showInTrace);
		
	   this.ocupado = false;
	}

   public void setOcupado(boolean estaOcupada) {
	   this.ocupado = estaOcupada;
   }

   public void utilizar(Cliente cliente) {
	   
	   MaquinaDeBusca maquinaDeBusca;
	   double tempoUtilizacao;
	   double tempoProcessamento;
	   EventoTerminoUsoDisco1 eventoTerminoUsoDisco1;

	   this.cliente = cliente;

	   maquinaDeBusca = (MaquinaDeBusca) getModel();

	   tempoUtilizacao = MaquinaDeBusca.getTempoUtilizacaoDisco1();
	   maquinaDeBusca.sendTraceNote(this + " serve " + cliente + " por " + tempoUtilizacao + " minutos.");
	   tempoProcessamento = MaquinaDeBusca.distribuicaoTempoServicoDisco1.sample();
	   tempoUtilizacao = MaquinaDeBusca.getTempoUtilizacaoDisco1();
	   totalTempoOcupado += tempoProcessamento;
	   eventoTerminoUsoDisco1 = new EventoTerminoUsoDisco1(maquinaDeBusca, "Evento relacionado ao termino do uso do Disco1 pelo cliente", true);
	   eventoTerminoUsoDisco1.schedule(this, cliente, new TimeSpan (tempoUtilizacao));
	   setVisitas(getVisitas() +1);
   }

   public boolean getOcupado() {
	   return (this.ocupado);
   }

   public Cliente getCliente() {
	   return (this.cliente);
   }
   
   public int getVisitas() {
       return visitas;
   }

   public void setVisitas(int visitas) {
       this.visitas = visitas;
   }


   public int getTotalRequisicoesSairam() {
       return totalRequisicoesSairam;
   }

   public void setTotalRequisicoesSairam(int totalRequisicoesSairam) {
       this.totalRequisicoesSairam = totalRequisicoesSairam;
   }
   
   public float getTotalTempoOcupado() {
	   return totalTempoOcupado;
   }
}