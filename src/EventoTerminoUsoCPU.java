import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class EventoTerminoUsoCPU extends EventOf2Entities<CPU, Cliente> {

	public EventoTerminoUsoCPU(Model owner, String name, boolean showInTrace) {
		super (owner, name, showInTrace);
	}

	@Override

	public void eventRoutine(CPU cpu, Cliente cliente) {

		MaquinaDeBusca maquinaDeBusca = (MaquinaDeBusca) getModel();
		
		maquinaDeBusca.sendTraceNote(cliente + " terminou o uso da CPU.");

		maquinaDeBusca.liberarCPU();		
		
	}
}