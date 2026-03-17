import bernard_flou.Fabricateur;
import bernard_flou.Fabricateur.Lunette;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static void main() {
        Usine usine = new Usine();

        Map<Fabricateur.TypeLunette, Integer> typesLunettes = new HashMap<>();

        typesLunettes.put(Fabricateur.TypeLunette.BANANA, 8);
        typesLunettes.put(Fabricateur.TypeLunette.CHATGPT, 2);
        typesLunettes.put(Fabricateur.TypeLunette.LE_CHAT, 1);

        List<Lunette> result = usine.produire(typesLunettes);
        System.out.println(result.size());
        for(int i=0; i<result.size(); i++) {
            System.out.println(result.get(i));
        }
    }
}
