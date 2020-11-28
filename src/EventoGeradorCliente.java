import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class EventoGeradorCliente extends ExternalEvent{
  
	public EventoGeradorCliente(Model owner, String name, boolean showInTrace) {
		super (owner, name, showInTrace);
	}

	@Override
	public void eventRoutine() {
		
		MaquinaDeBusca maquinaDeBusca;
		Cliente cliente;
		EventoGeradorCliente eventoGeradorCliente;
		double instanteChegadaCliente;

		for(MaquinaDeBusca.clientesServidor += 0 ; MaquinaDeBusca.clientesServidor < MaquinaDeBusca.quantidadeMaximaClientes; MaquinaDeBusca.clientesServidor ++) {
			
			maquinaDeBusca = (MaquinaDeBusca) getModel();
			cliente = new Cliente (maquinaDeBusca, "Cliente", true);
			maquinaDeBusca.sendTraceNote ("Cliente chegou a maquina de busca em " + maquinaDeBusca.presentTime());
			
			maquinaDeBusca.servirCliente(cliente);
			
			instanteChegadaCliente = maquinaDeBusca.getTempoEntreChegadasClientes();

			eventoGeradorCliente = new EventoGeradorCliente (maquinaDeBusca, "Evento externo responsavel por gerar um cliente que chegou a maquina de busca", true);

			eventoGeradorCliente.schedule (new TimeSpan(instanteChegadaCliente));

		}
		
	}
}