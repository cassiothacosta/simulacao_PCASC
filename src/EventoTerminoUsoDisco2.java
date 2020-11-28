import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class EventoTerminoUsoDisco2 extends EventOf2Entities<Disco2, Cliente> {

	public EventoTerminoUsoDisco2(Model owner, String name, boolean showInTrace) {
		super (owner, name, showInTrace);
	}

	@Override

	public void eventRoutine(Disco2 disco2, Cliente cliente) {

		MaquinaDeBusca maquinaDeBusca = (MaquinaDeBusca) getModel();
		
		maquinaDeBusca.sendTraceNote(disco2 + " terminou o uso do Disco2.");

		maquinaDeBusca.liberarDisco2(disco2);		
		
	}
}