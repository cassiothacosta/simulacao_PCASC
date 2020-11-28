import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.report.FileOutput;

import java.util.Random;

public class MaquinaDeBusca extends Model{

		private static double tempoSimulacao = 5000;
		public static double tempoRespostaTotal = 0;
		public static int clientesServidor;
		public static double quantidadeRequisicoes = 0;
		public static double quantidadeMaximaClientes = 1;
		private static FileOutput reportWriter;
		private static FileOutput reportWriter1;
		private static FileOutput reportWriter2;
		private static FileOutput reportWriter3;
		
		private Queue<Cliente> filaCPU;
		private Queue<Cliente> filaDisco1;
		private Queue<Cliente> filaDisco2;
		private Queue<Cliente> filaDisco3;
		
		private static CPU cpu;
		private static Disco1 disco1;
		private static Disco2 disco2;
		private static Disco3 disco3;
		
		
	
		private ContDistExponential distribuicaoTempoChegadasClientes;
		
		static ContDistUniform distribuicaoTempoServicoCPU;
		static ContDistUniform distribuicaoTempoServicoDisco1;
		static ContDistUniform distribuicaoTempoServicoDisco2;
		static ContDistExponential distribuicaoTempoServicoDisco3;
		
		public MaquinaDeBusca(Model owner, String name, boolean showInReport, boolean showIntrace) {
			super (owner, name, showInReport, showIntrace);
		}
		
		@Override
		public String description() {
			return ("Esse e o modelo de eventos discretos de uma maquina de busca. " +
					"Clientes chegam em uma lavanderia self-service para lavarem suas roupas. " +
					"Eles esperam em uma fila do tipo FIFO até que a máquina de lavar-roupas fique disponível " +
					"para ser utilizada. " +
					"Depois que a lavagem das roupas trazidas pelo cliente termina, " +
					"o cliente deixa a lavanderia " +
					"e a máquina de lavar que foi utilizada torna-se disponível para servir o próximo cliente.");
		}
		
		@Override
		
		public void init() {
			
			filaCPU = new Queue<Cliente> (this, "Fila de clientes aguardando CPU", true, true);
			filaDisco1 = new Queue<Cliente> (this, "Fila de clientes aguardando Disco 1", true, true);
			filaDisco2 = new Queue<Cliente> (this, "Fila de clientes aguardando Disco 2", true, true);
			filaDisco3 = new Queue<Cliente> (this, "Fila de clientes aguardando Disco 3", true, true);
		    
			cpu = new CPU (this, "CPU", true);
			disco1 = new Disco1 (this, "Disco1", true);
			disco2 = new Disco2 (this, "Disco2", true);
			disco3 = new Disco3 (this, "Disco3", true);
			
			distribuicaoTempoChegadasClientes = new ContDistExponential (this, "Distribuicao do tempo entre chegadas sucessivas de clientes a maquina de busca", 750, true, true);
			
			distribuicaoTempoChegadasClientes.setNonNegative(true);
			
			distribuicaoTempoServicoCPU = new ContDistUniform (this, "Distribuicao do tempo de servico da CPU", 5.0, 25.0, true, true);
			distribuicaoTempoServicoDisco1 = new ContDistUniform (this, "Distribuicao do tempo de servico do Disco1", 0.0, 30.0, true, true);
			distribuicaoTempoServicoDisco2 = new ContDistUniform (this, "Distribuicao do tempo de servico do Disco2", 0.0, 30.0, true, true);
			distribuicaoTempoServicoDisco3 = new ContDistExponential (this, "Distribuicao do tempo de servico do Disco3", 25.0, true, true);
			
			distribuicaoTempoServicoCPU.setNonNegative(true);
			distribuicaoTempoServicoDisco1.setNonNegative(true);
			distribuicaoTempoServicoDisco2.setNonNegative(true);
			distribuicaoTempoServicoDisco3.setNonNegative(true);
		}
		
		@Override
		public void doInitialSchedules() {
			
			EventoGeradorCliente eventoGeradorCliente;
				
			eventoGeradorCliente = new EventoGeradorCliente (this, "Evento externo responsavel por gerar um cliente que chega a maquina de busca", true);
			
			eventoGeradorCliente.schedule(new TimeSpan(0.5));
		}
		
		public void servirCliente(Cliente cliente) {
	        if (cpu.getOcupada()) {

	            filaCPU.insert(cliente);
	            Random rndNumber = new Random();
	            int random = rndNumber.nextInt(100);
	            if (random >= 60 && random < 90) {
	            	servirClienteDisco1(cliente);
	            } else if (random <= 30) {
	            	servirClienteDisco2(cliente);
	            } else if (random > 30 && random < 60) {
	            	servirClienteDisco3(cliente);
	            } else {
	            	clientesServidor --;
	                cliente.setFimTempoResposta(this.presentTime().getTimeAsDouble());
	                tempoRespostaTotal += cliente.calculaTempoResposta();
	                ++quantidadeRequisicoes;
	                EventoGeradorCliente eventoGeradorCliente = new EventoGeradorCliente(this, "Evento externo respons�vel por gerar um cliente que chega ao servidor", true);
	                eventoGeradorCliente.schedule(new TimeSpan(this.getTempoEntreChegadasClientes()));
	            }
	        } else {
	            servirClienteCPU(cliente);
	        }
	    }
		
		public double getTempoEntreChegadasClientes(){
			
			return (distribuicaoTempoChegadasClientes.sample());
		}
		
		public static double getTempoUtilizacaoCPU() {
			
			return (distribuicaoTempoServicoCPU.sample());	
		}

		public static double getTempoUtilizacaoDisco1() {
			
			return (distribuicaoTempoServicoDisco1.sample());	
		}
		
		public static double getTempoUtilizacaoDisco2() {
			
			return (distribuicaoTempoServicoDisco2.sample());	
		}
		
		public static double getTempoUtilizacaoDisco3() {
			
			return (distribuicaoTempoServicoDisco3.sample());	
		}
		
		public void servirClienteCPU(Cliente cliente) {
				
				cpu.setOcupada(true);

				cpu.utilizar(cliente);
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

		public void liberarCPU() {
		
			Cliente cliente;
			
			sendTraceNote("Liberando CPU...");
			
			if (filaCPU.isEmpty()){
				
				sendTraceNote("CPU esperando clientes...");
				cpu.setOcupada(false);
				
			}else{
				
				sendTraceNote("CPU sera realocado.");
				
				cliente = filaCPU.first();
				filaCPU.remove(cliente);
				cpu.setTotalRequisicoesSairam(cpu.getTotalRequisicoesSairam() + 1);
				cpu.utilizar(cliente);
			}
		}
		
		public void liberarDisco1() {
			
			Cliente cliente;
			
			sendTraceNote("Liberando Disco1...");
			
			if (filaDisco1.isEmpty()){
				
				sendTraceNote("Disco1 esperando clientes...");
				disco1.setOcupado(false);
				
			}else{
				
				sendTraceNote("Disco1 sera realocado.");
				
				cliente = filaDisco1.first();
				filaDisco1.remove(cliente);
				disco1.setTotalRequisicoesSairam(disco1.getTotalRequisicoesSairam() + 1);
				disco1.utilizar(cliente);
			}
		}
		
		public void liberarDisco2() {
			
			Cliente cliente;
			
			sendTraceNote("Liberando Disco2...");
			
			if (filaDisco2.isEmpty()){
				
				sendTraceNote("Disco2 esperando clientes...");

				disco2.setOcupado(false);
				
			}else{
				
				sendTraceNote("Disco2 sera realocado.");

				cliente = filaDisco2.first();
				filaDisco2.remove(cliente);
				disco2.setTotalRequisicoesSairam(disco2.getTotalRequisicoesSairam() + 1);

				disco2.utilizar(cliente);
			}
		}
		
		public void liberarDisco3() {
			
			Cliente cliente;
			
			sendTraceNote("Liberando Disco3...");

			if (filaDisco3.isEmpty()){

				sendTraceNote("Disco3 esperando clientes...");
				disco3.setOcupado(false);
				
			}else{
				
				sendTraceNote("Disco3 sera realocado.");

				cliente = filaDisco3.first();
				filaDisco3.remove(cliente);
				disco3.setTotalRequisicoesSairam(disco3.getTotalRequisicoesSairam() + 1);

				disco3.utilizar(cliente);
			}
		}
		
		public static double calculaThroughputSistema() {
	        return quantidadeRequisicoes / tempoSimulacao;
	    }

	    public static double calculaThroughputCPU() {
	        return cpu.getTotalRequisicoesSairam() / tempoSimulacao;
	    }

	    public static double calculaUtilizacaoCPU() {
	        return getTempoUtilizacaoCPU() / tempoSimulacao;
	    }

	    public static float calculaNumeroMedioVisitasCPU() {
	        return (float) (cpu.getVisitas() / quantidadeRequisicoes);
	    }

	    public static double calculaThroughputDisco1() {
	        return disco1.getTotalRequisicoesSairam() / tempoSimulacao;
	    }

	    public static double calculaUtilizacaoDisco1() {
	        return getTempoUtilizacaoDisco1() / tempoSimulacao;
	    }

	    public static float calculaNumeroMedioVisitasDisco1() {
	        return (float) (disco1.getVisitas() / quantidadeRequisicoes);
	    }
	    
	    public static double calculaThroughputDisco2() {
	        return disco2.getTotalRequisicoesSairam() / tempoSimulacao;
	    }

	    public static double calculaUtilizacaoDisco2() {
	        return getTempoUtilizacaoDisco2() / tempoSimulacao;
	    }

	    public static float calculaNumeroMedioVisitasDisco2() {
	        return (float) (disco2.getVisitas() / quantidadeRequisicoes);
	    }
	    
	    public static double calculaThroughputDisco3() {
	        return disco3.getTotalRequisicoesSairam() / tempoSimulacao;
	    }

	    public static double calculaUtilizacaoDisco3() {
	        return getTempoUtilizacaoDisco3() / tempoSimulacao;
	    }

	    public static float calculaNumeroMedioVisitasDisco3() {
	        return (float) (disco3.getVisitas() / quantidadeRequisicoes);
	    }
	    
	    public double getQuantidadeMaximaClientes() {
	    	return quantidadeMaximaClientes;
	    }
	    	
			
		
	    public static void main(String[] args) {
	    
			MaquinaDeBusca modeloMaquinaDeBusca;
			Experiment experimento;
			reportWriter = new FileOutput();
//			reportWriter1 = new FileOutput();
//			reportWriter2 = new FileOutput();
//			reportWriter3 = new FileOutput();
			
			reportWriter.open("ThroughputSistemaDiscoBalanceado.txt");
//			reportWriter1.open("VisitasDisco1.txt");
//			reportWriter2.open("VisitasDisco2.txt");
//			reportWriter3.open("VisitasDisco3.txt");
			
			for(int i = 1; i <= 50; i ++) {
		
				quantidadeRequisicoes = 0;
				tempoRespostaTotal = 0;
				clientesServidor = 0;
				
				modeloMaquinaDeBusca = new MaquinaDeBusca (null, "Modelo de uma maquina de busca", true, true);

				experimento = new Experiment ("Experimento da maquina de busca");
				
				modeloMaquinaDeBusca.connectToExperiment(experimento);

				experimento.stop(new TimeInstant(tempoSimulacao));

				experimento.tracePeriod(new TimeInstant(0.0), new TimeInstant(tempoSimulacao));
				experimento.setSilent(true);
				experimento.start();
				experimento.report();
				experimento.finish();
				
				//reportWriter.writeln("############## Quantidade de Clientes : " + quantidadeMaximaClientes + " ##############");
	
//				reportWriter.writeln("Tempo de resposta da Maquina de busca : " + tempoRespostaTotal);
				reportWriter.writeln("" + calculaThroughputSistema());
//				reportWriter.writeln("" + calculaThroughputCPU());
//				reportWriter1.writeln("" + calculaThroughputDisco1());
//				reportWriter2.writeln("" + calculaThroughputDisco2());
//				reportWriter3.writeln("" + calculaThroughputDisco3());
//				reportWriter.writeln("" + calculaUtilizacaoCPU());
//				reportWriter1.writeln("" + calculaUtilizacaoDisco1());
//				reportWriter2.writeln("" + calculaUtilizacaoDisco2());
//				reportWriter3.writeln("" + calculaUtilizacaoDisco3());
//				reportWriter.writeln("" + calculaNumeroMedioVisitasCPU());
//				reportWriter1.writeln("" + calculaNumeroMedioVisitasDisco1());
//				reportWriter2.writeln("" + calculaNumeroMedioVisitasDisco2());
//				reportWriter3.writeln("" + calculaNumeroMedioVisitasDisco3());
//				reportWriter.writeln("" + cpu.getTotalTempoOcupado());
//				reportWriter.writeln("" + disco1.getTotalTempoOcupado());
//				reportWriter.writeln("" + disco2.getTotalTempoOcupado());
//				reportWriter.writeln("" + disco3.getTotalTempoOcupado());
//				reportWriter.writeln("Quantidade m�xima de requisicoes : " + quantidadeRequisicoes);
//				
				
				
				
				quantidadeMaximaClientes++;
				
			}
									
			reportWriter.close();
//			reportWriter1.close();
//			reportWriter2.close();
//			reportWriter3.close();

			
			   
		}
}
