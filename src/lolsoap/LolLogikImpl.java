/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.awt.image.BufferedImage;
import java.rmi.Naming;
import java.util.Iterator;
import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tobias
 */
@WebService(endpointInterface = "lolsoap.LolSOAPI")
public class LolLogikImpl implements LolSOAPI {

    String nameOfChamp = null;
    String lolVersion = null;
    String url = null;

    @Override
    public void startGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNameOfCham() {
        if (nameOfChamp == null) {
            return "No Name";
        } else {
            return nameOfChamp;
        }
    }

    @Override
    public String getImage() {

        Client client = ClientBuilder.newClient();
        Response res = client.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
                + "/v1.2/champion?champData=info&api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
                .request(MediaType.APPLICATION_JSON).get();

        String svar = res.readEntity(String.class);

        try {

            JSONObject json = new JSONObject(svar);
            //JSONArray k = json.getJSONArray("data");

            JSONObject songs = json.getJSONObject("data");
            Iterator x = songs.keys();
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()) {
                String key = (String) x.next();
                jsonArray.put(songs.get(key));
            }

            int randomNum = 0 + (int) (Math.random() * 133);
            System.out.println(jsonArray.getJSONObject(randomNum).getString("name"));
            nameOfChamp = jsonArray.getJSONObject(randomNum).getString("name");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Response version = client.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
                + "/v1.2/versions?api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
                .request(MediaType.APPLICATION_JSON).get();

        String versionNr = version.readEntity(String.class);

        try {

            JSONArray versionArray = new JSONArray(versionNr);

            System.out.println(versionArray.get(0));
            lolVersion = versionArray.get(0).toString();

            url = "http://ddragon.leagueoflegends.com/cdn/" + lolVersion + "/img/champion/" + nameOfChamp + ".png";

        } catch (Exception e) {
            e.printStackTrace();

        }
        return url;

    }

    @Override
    public void reset() {
        nameOfChamp = null;
        url = null;
        lolVersion = null;
    }

    @Override
    public Bruger hentBruger(String user, String pass) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Bruger b = ba.hentBruger(user, pass);
        return b;

    }

    @Override
    public void submitGuess(String guess) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
