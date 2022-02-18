package jheadortailserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Game {
    private final ArrayList<Player> players = new ArrayList<>();   

    /**
     * Teste si les joueurs sont prêt
     * @return boolean
     */
    private boolean allPlayersReady() {
        return players.stream().allMatch(p -> (p == null || p.isReady()));
    }

    /**
     * Trouve le joueur par rapport à son id
     * @return int
     */
    private int findPlayerId() {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i) == null) {
                return i+1;
            }
        }
        players.add(null);
        return players.size();
    }
    
    /**
     * Enregistre les joueurs (max 2)
     * @param sock
     */
    public void registerPlayer(Socket sock) {
        int id = findPlayerId();
        Player player = new Player(this, id, sock);
        
        System.out.println("nb joueurs : "+ players.size());
        
        if(players.size() == 3) {
        	System.out.println("Il y a trop de joueur, vous ne pouvez vous connecter.");
        	return;
        }

        System.out.printf("- Player %d arrived\n", id);
        players.set(id-1, player);
        player.start();
    }
    
    /**
     * Attend que tous les joueurs aient joués puis donne le résultat
     * @param choice
     * @return int
     * @throws InterruptedException
     */
    public synchronized int resultat(int choice) throws InterruptedException {
    	
        if(allPlayersReady()) {
            notifyAll();
            
            System.out.printf("All %d played, got \n", players.size());
            
        }
        else {
        	System.out.println("En attente de joueur.");
            wait();
        }
        
        int score = 0;
        for (Player player : players) {
			
        	//Compte le nombre joueur qui a perdu
			if(player.isLoose(choice)) {
				score++;
			}
		}
        
        return score;
    }
    
    public void write(DataOutputStream writer) throws IOException {
    	//Gagné ?
//        writer.writeBoolean(is_head);
//        System.out.println("is_head : "+is_head);
        
        writer.writeInt(players.size());
        System.out.println("players.size() : "+players.size());
        
        for (Player player : players) {
            writer.writeInt(player == null ? -1 : player.getScore());
        }
        writer.flush();
    }
    public void onLeave(int id) {
        System.out.printf("- Player %d left\n", id);
        players.set(id - 1, null);
    }


}
