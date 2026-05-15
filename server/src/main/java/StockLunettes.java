import bernard_flou.Fabricateur;

import java.util.HashMap;
import java.util.Map;

public class StockLunettes {
    private final Map<String, Fabricateur.TypeLunette> serials;

    public StockLunettes() {
        this.serials = new HashMap<>();
    }

    public void ajouter(Fabricateur.Lunette lunette) {
        serials.put(lunette.serial, lunette.type);
    }

    public String chercher(String numeroSerie) {
        Fabricateur.TypeLunette res = serials.get(numeroSerie);

        if(res != null) {
            return String.valueOf(res);
        } else {
            return  "invalid";
        }
    }
}
