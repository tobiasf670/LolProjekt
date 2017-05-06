/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Tobias
 */
public class JavaClient {

    static LolSOAPI spil;
    static boolean leaveRoom = true;
    static boolean lr = true;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, Exception {
        // TODO code application logic 

        System.out.println("Velkommen til LOLQUIZ");
        Scanner scan = new Scanner(System.in);
        System.out.println("Indtast Brugernavn");
        String user = scan.next();
        System.out.println("Indtast password!");
        String pass = scan.next();

        boolean legitUser = false;

        URL url = new URL("http://ec2-35-165-42-120.us-west-2.compute.amazonaws.com:9901/LolSOAP?wsdl");
       // URL url = new URL("http://localhost:9901/LolSOAP?wsdl");

        QName qname = new QName("http://lolsoap/", "GameHandlerService");
        Service service = Service.create(url, qname);
        LolSOAPI ba = service.getPort(LolSOAPI.class);
        String bruger = null;

        try {
            bruger = ba.hentBruger(user, pass);
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

        if (bruger != null) {
            legitUser = true;
        }

        if (legitUser) {
            System.out.println("Du er nu logget ind!");

        } else {
            System.out.println("Login fejlet");
            legitUser = false;
        }
        boolean igen = true;
        if (legitUser) {

            while (igen) {
                try {
                    igen = spilSpillet(ba, bruger);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
    }

    private static boolean spilSpillet(LolSOAPI face, String p) throws InterruptedException, AWTException {
        spil = face;
        UUID id = null;
        int valg = 0;

        while (true) {
            Scanner scan = new Scanner(System.in);
            UUID[] setOfGames;
            System.out.println("**********-MENU-*****************");
            System.out.println("*********************************");
            System.out.println("Klik 1 ......... Find et spil");
            System.out.println("Klik 2 ......... Start spil");
            System.out.println("Klik 3 ......... Lav et spil");
            System.out.println("Klik 4 ......... Se vinder af dine spil");
            System.out.println("Klik 5 ......... Exit");
            System.out.println("*********************************");
            System.out.println("*********************************");
            System.out.println();
            System.out.print("Dit valg : ");
            try {
                valg = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("\u001B[31m" + "Det er kun muligt at skrive til fra 1-4" + "\u001B[0m");
            }

            switch (valg) {
                case 1:

                    setOfGames = spil.findGames();
                    if (setOfGames.length == 0) {

                        System.out.println("\u001B[31m" + "Ikke nogle aktive spil fundet" + "\u001B[0m");

                        break;
                    }
                    System.out.format("%-15s%-15s\n", "Game nr", "Spiller");

                    for (int i = 0; setOfGames.length > i; i++) {
                        int gameNr = i + 1;
                        System.out.format("%-15s%-15s\n", gameNr, Arrays.toString(spil.getUsernames(setOfGames[i])));
                    }

                    System.out.println("Indtast nr, på det game du vil joine ( gå tilbage klik -1)");
                    System.out.print("Indtast nr : ");
                    boolean getGame = true;
                    do {
                        int gameValg = scan.nextInt();
                        if (gameValg == -1) {
                            System.out.println("\u001B[31m" + "Du valgte at gå tilbage" + "\u001B[0m");
                            break;
                        }
                        if (gameValg <= setOfGames.length) {
                            spil.joinGame(setOfGames[gameValg - 1], p);
                            if (waitForStart(id, p)) {
                                try {
                                    Robot robot = new Robot();
                                    // Simulate a key press
                                    robot.keyPress(KeyEvent.VK_ENTER);
                                    robot.keyRelease(KeyEvent.VK_ENTER);

                                } catch (AWTException e) {
                                    e.printStackTrace();
                                }

                                startGame(p, setOfGames[gameValg - 1], scan);
                            }

                            break;

                        }
                    } while (getGame);

                    break;

                case 2:
                    if (id == null) {
                        System.out.println("\u001B[31m" + "DU SKAL FØRST LAVE ET SPIL" + "\u001B[0m");
                        break;
                    }
                    startGame(p, id, scan);
                    break;

                case 3:
                    id = spil.createNewGame(p);
                    if (waitingRoomm(id)) {
                        try {                   // Problemer med at lukke en tråd når en anden spiller join gamet 
                                                // derfor bliver vi nød til at simulere et click.
                            Robot robot = new Robot();
                            // Simulate a key press
                            robot.keyPress(KeyEvent.VK_ENTER);
                            robot.keyRelease(KeyEvent.VK_ENTER);

                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                        System.out.println("KLIK 2 for at starte spillet");
                        if (scan.next().equals("2")) {
                            startGame(p, id, scan);
                        }
                    }
                    System.out.println("\u001B[32m" + "Der er nu oprettet et spil, spil det fra menu ved at klikke 2" + "\u001B[0m");

                    break;

                case 5:

                    return false;
       
                case 4:
                    System.out.format("%-20s%-20s%12s\n", "Game", "Spiller", "Vinderen");
                    UUID[] games = spil.getPlayersGames(p);
                    for (int i = 0; games.length > i; i++) {

                        System.out.format("%-15s%-20s%15s\n", i, Arrays.toString(spil.getUsernames(games[i])), spil.getWinner(games[i]));

                    }
                    break;

                default:
                    System.out.println("\u001B[31m" + "Dit valgte input er ikke i menuen" + "\u001B[0m");

            }
        }

    }

    public static void startGame(String p, UUID id, Scanner scan) {
        spil.startGame(p);

        while (!spil.playerDoneGuessing(id, p)) {

            System.out.println("Hvem har titlen : " + spil.getChampionTitle(p));

            boolean tryAgain = true;
            do {

                String guess = scan.next();
                if (guess.equals("done")) {
                    break;
                }
                if (guess.equals("skip")) {
                    spil.skipChampion(p);
                   
                    break;
                  
                }

                if (spil.guessChampion(p, guess)) {

                    tryAgain = false;
                } else {

                    System.out.println("Forkert");
                    tryAgain = false;
                }

            } while (tryAgain);

        }
        
    }

    public static boolean waitingRoomm(UUID id) throws InterruptedException {
        Thread thread = new Thread();

        String[] usersInGame = spil.getUsernames(id);
        UUID[] gg = spil.findGames();
        leaveRoom = true;
        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Scanner scan = new Scanner(System.in);
                String input = "";
                synchronized (this) {

                    while (!leaveRoom) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(JavaClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    while (leaveRoom) {

                        input = scan.next();
                        if (input.equals("1")) {
                            leaveRoom = false;
                        }

                    }
                }
            }
        });;
        inputThread.start();

        while (usersInGame.length < 2 && leaveRoom) {
            usersInGame = spil.getUsernames(id);
            thread.sleep(1500);
            System.out.println("VENTER PÅ EN ANDEN SPILLER ");
            System.out.println("Forlad klik 1");

        }
        boolean result = leaveRoom;
        leaveRoom = false;
        inputThread.interrupt();
       
        return result;

    }

    public static boolean waitForStart(UUID id, String p) throws InterruptedException {
        Thread thread = new Thread();
        leaveRoom = true;
        Thread inputThread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                Scanner scan = new Scanner(System.in);
                String input = "";
                synchronized (this) {

                    while (!leaveRoom) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(JavaClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    while (leaveRoom) {

                        input = scan.next();
                        if (input.equals("1")) {
                            leaveRoom = false;
                        }

                    }
                }
            }
        });;
        inputThread1.start();

        while (!spil.isGameStarted(p)) {
             thread.sleep(1500);
            System.out.println("Spillet er ikke startet");
            System.out.println("Forlad klik 1");
            if (!leaveRoom) {

                break;
            }
        }
        boolean res = leaveRoom;
        leaveRoom = false;
        inputThread1.interrupt();
        
        return res;

    }

}
