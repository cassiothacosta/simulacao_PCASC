import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class CPU extends Entity{

	private boolean ocupada;

	private Cliente cliente;
    private int visitas;
    private int totalRequisicoesSairam;
    private float totalTempoOcupado;
    
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
	   double tempoProcessamento;
	   EventoTerminoUsoCPU eventoTerminoUsoCPU;

	   this.cliente = cliente;

	   modeloMaquinaDeBusca = (MaquinaDeBusca) getModel();
	   tempoProcessamento = MaquinaDeBusca.distribuicaoTempoServicoCPU.sample();
	   tempoUtilizacao = MaquinaDeBusca.getTempoUtilizacaoCPU();
	   totalTempoOcupado += tempoProcessamento;
	   modeloMaquinaDeBusca.sendTraceNote(this + " serve " + cliente + " por " + tempoUtilizacao + " minutos.");

	   eventoTerminoUsoCPU = new EventoTerminoUsoCPU(modeloMaquinaDeBusca, "Evento relacionado ao termino do uso da CPU pelo cliente", true);
	   eventoTerminoUsoCPU.schedule(this, cliente, new TimeSpan (tempoUtilizacao));
	   setVisitas(getVisitas() + 1);
   }

   public boolean getOcupada() {
	   return (this.ocupada);
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