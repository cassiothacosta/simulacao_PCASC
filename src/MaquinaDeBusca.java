import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;

public class MaquinaDeBusca extends Model{

	// Definição do tempo de simulação.
		private static double tempoSimulacao = 5000;
		
		private Queue<Cliente> filaCPU;
		private Queue<Cliente> filaDisco1;
		private Queue<Cliente> filaDisco2;
		private Queue<Cliente> filaDisco3;
		
		private CPU cpu;
		private Disco1 disco1;
		private Disco2 disco2;
		private Disco3 disco3;
		
	
		private ContDistExponential distribuicaoTempoChegadasClientes;
		
		private ContDistUniform distribuicaoTempoServicoMaquinaLavar;
		
		public MaquinaDeBusca(Model owner, String name, boolean showInReport, boolean showIntrace) {
			super (owner, name, showInReport, showIntrace);
		}
		
		@Override
		public String description() {
			return ("Esse é o modelo de eventos discretos de uma lavanderia. " +
					"Clientes chegam em uma lavanderia self-service para lavarem suas roupas. " +
					"Eles esperam em uma fila do tipo FIFO até que a máquina de lavar-roupas fique disponível " +
					"para ser utilizada. " +
					"Depois que a lavagem das roupas trazidas pelo cliente termina, " +
					"o cliente deixa a lavanderia " +
					"e a máquina de lavar que foi utilizada torna-se disponível para servir o próximo cliente.");
		}
		
		@Override
		
		public void init() {
			
			filaCPU = new Queue<Cliente> (this, "Fila de clientes aguardando serviço", true, true);
			filaDisco1 = new Queue<Cliente> (this, "Fila de clientes aguardando serviço", true, true);
			filaDisco2 = new Queue<Cliente> (this, "Fila de clientes aguardando serviço", true, true);
			filaDisco3 = new Queue<Cliente> (this, "Fila de clientes aguardando serviço", true, true);
		    
			cpu = new CPU (this, "CPU", true);
			disco1 = new Disco1 (this, "CPU", true);
			disco2 = new Disco2 (this, "CPU", true);
			disco3 = new Disco3 (this, "CPU", true);
			
			distribuicaoTempoChegadasClientes = new ContDistExponential (this, "Distribuição do tempo entre chegadas sucessivas de clientes à lavanderia", 40.0, true, true);
			
			distribuicaoTempoChegadasClientes.setNonNegative(true);
			
			distribuicaoTempoServicoMaquinaLavar = new ContDistUniform (this, "Distribuição do tempo de serviço da máquina de lavar roupas", 20.0, 40.0, true, true);
			
			distribuicaoTempoServicoMaquinaLavar.setNonNegative(true);
		}
		
		@Override
		public void doInitialSchedules() {
			
			EventoGeradorCliente eventoGeradorCliente;
				
			eventoGeradorCliente = new EventoGeradorCliente (this, "Evento externo responsável por gerar um cliente que chega à lavanderia", true);
			
			eventoGeradorCliente.schedule(new TimeSpan(0.0));
		}
		
		public double getTempoEntreChegadasClientes(){
			
			return (distribuicaoTempoChegadasClientes.sample());
		}
		
		public double getTempoUtilizacao() {
			
			return (distribuicaoTempoServicoMaquinaLavar.sample());	
		}

		public void servirClienteCPU(Cliente cliente) {
				
			if ((cpu.getOcupada()) == false){
				
				cpu.setOcupada(true);

				cpu.utilizar(cliente);
			}else{
				filaCPU.insert(cliente);
			}
		}
		public void servirClienteDisco1(Cliente cliente) {
			
			if ((disco1.getOcupado()) == false){
				
				disco1.setOcupado(true);

				disco1.utilizar(cliente);
			}else{
				filaDisco1.insert(cliente);
			}
		}
		public void servirClienteDisco2(Cliente cliente) {
			
			if ((disco2.getOcupado()) == false){
				
				disco2.setOcupado(true);

				disco2.utilizar(cliente);
			}else{
				filaDisco2.insert(cliente);
			}
		}
		public void servirClienteDisco3(Cliente cliente) {
			
			if ((disco3.getOcupado()) == false){
				
				disco3.setOcupado(true);

				disco3.utilizar(cliente);
			}else{
				filaDisco3.insert(cliente);
			}
		}

		public void liberarCPU(CPU cpu) {
		
			Cliente cliente;
			
			sendTraceNote("Liberando CPU...");
			
			if (filaCPU.isEmpty()){
				
				sendTraceNote("CPU esperando clientes...");
				cpu.setOcupada(false);
				
			}else{
				
				sendTraceNote("CPU sera realocado.");
				
				cliente = filaCPU.first();
				filaCPU.remove(cliente);
				
				cpu.utilizar(cliente);
			}
		}
		public void liberarDisco1(Disco1 disco1) {
			
			Cliente cliente;
			
			sendTraceNote("Liberando Disco1...");
			
			if (filaDisco1.isEmpty()){
				
				sendTraceNote("Disco1 esperando clientes...");
				disco1.setOcupado(false);
				
			}else{
				
				sendTraceNote("Disco1 sera realocado.");
				
				cliente = filaDisco1.first();
				filaDisco1.remove(cliente);
				
				disco1.utilizar(cliente);
			}
		}
		public void liberarDisco2(Disco2 disco2) {
			
			Cliente cliente;
			
			sendTraceNote("Liberando Disco2...");
			
			if (filaDisco2.isEmpty()){
				
				sendTraceNote("Disco2 esperando clientes...");
				disco2.setOcupado(false);
				
			}else{
				
				sendTraceNote("Disco2 sera realocado.");

				cliente = filaDisco2.first();
				filaDisco2.remove(cliente);

				disco2.utilizar(cliente);
			}
		}
		public void liberarDisco3(Disco3 disco3) {
			
			Cliente cliente;
			
			sendTraceNote("Liberando Disco3...");

			if (filaDisco3.isEmpty()){

				sendTraceNote("Disco3 esperando clientes...");
				disco3.setOcupado(false);
				
			}else{
				
				sendTraceNote("Disco3 sera realocado.");

				cliente = filaDisco3.first();
				filaDisco3.remove(cliente);

				disco3.utilizar(cliente);
			}
		}
		
	    public static void main(String[] args) {
	    
			MaquinaDeBusca modeloMaquinaDeBusca;
			Experiment experimento;

			modeloMaquinaDeBusca = new MaquinaDeBusca (null, "Modelo de uma maquina de busca", true, true);

			experimento = new Experiment ("Experimento da maquina de busca");

			modeloMaquinaDeBusca.connectToExperiment(experimento);

			experimento.setShowProgressBar(true);

			experimento.stop(new TimeInstant(tempoSimulacao));   

			experimento.tracePeriod(new TimeInstant(0.0), new TimeInstant(tempoSimulacao));

			experimento.start();

			experimento.report();

			experimento.finish();
			   
		}
}
