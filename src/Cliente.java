import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

public class Cliente extends Entity{

	private double inicioTempoResposta;
	private double fimTempoResposta;
	
	public Cliente(Model owner, String name, boolean showInTrace) {
				
		super (owner, name, showInTrace);
		
		TimeInstant tempoCorrente;
		tempoCorrente = owner.presentTime();
		inicioTempoResposta = tempoCorrente.getTimeAsDouble();
	}


	    public double getInicioTempoResposta() {
	        return inicioTempoResposta;
	    }

	    public void setInicioTempoResposta(double inicioTempoResposta) {
	        this.inicioTempoResposta = inicioTempoResposta;
	    }

	    public double getFimTempoResposta() {
	        return fimTempoResposta;
	    }

	    public void setFimTempoResposta(double fimTempoResposta) {
	        this.fimTempoResposta = fimTempoResposta;
	    }

	    public double calculaTempoResposta() {
	        return fimTempoResposta - inicioTempoResposta;
	    }
}