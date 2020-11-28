import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class EventoTerminoUsoDisco1 extends EventOf2Entities<Disco1, Cliente> {

	public EventoTerminoUsoDisco1(Model owner, String name, boolean showInTrace) {
		super (owner, name, showInTrace);
	}

	@Override

	public void eventRoutine(Disco1 disco1, Cliente cliente) {

		MaquinaDeBusca maquinaDeBusca = (MaquinaDeBusca) getModel();
		
		maquinaDeBusca.sendTraceNote(cliente + " terminou o uso do Disco1.");

		maquinaDeBusca.liberarDisco1();		
		
	}
}