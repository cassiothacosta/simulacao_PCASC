import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class EventoTerminoUsoDisco3 extends EventOf2Entities<Disco3, Cliente> {

	public EventoTerminoUsoDisco3(Model owner, String name, boolean showInTrace) {
		super (owner, name, showInTrace);
	}

	@Override

	public void eventRoutine(Disco3 disco3, Cliente cliente) {

		MaquinaDeBusca maquinaDeBusca = (MaquinaDeBusca) getModel();
		
		maquinaDeBusca.sendTraceNote(disco3 + " terminou o uso do Disco3.");

		maquinaDeBusca.liberarDisco3(disco3);		
		
	}
}