package jheadortailserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final ArrayList<Player> players = new ArrayList<>();
    private boolean is_head = false;
    private final Random rand = new Random();    

    private boolean allPlayersReady() {
        return players.stream().allMatch(p -> (p == null || p.isReady()));
    }

    private int findPlayerId() {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i) == null) {
                return i+1;
            }
        }
        players.add(null);
        return players.size();
    }
    
    public void registerPlayer(Socket sock) {
        int id = findPlayerId();
        Player player = new Player(this, id, sock);
        
        System.out.println("nb joueurs : "+ players.size());
        
        if(players.size() == 3) {
        	System.out.println("Il y a trop de joueur, vous ne pouvez vous connecter.");
        	
        	// Problème pour 3
//        	System.exit (0);
        	
        	return;
        }

        System.out.printf("- Player %d arrived\n", id);
        players.set(id-1, player);
        player.start();
    }
//    public synchronized boolean waitHeadOrTail() throws InterruptedException {
//        if(allPlayersReady()) {
//            notifyAll();
//            is_head = rand.nextInt(2) != 0;
//            System.out.printf("All %d played, got %s\n", players.size(), is_head ? "HEAD" : "TAIL");
//        }
//        else {
//            wait();
//        }
//        return is_head;
//    }
    
//	public boolean resultat(int _choice) {
//		// TODO Auto-generated method stub
//		return false;
//	}
    
    public synchronized int resultat(int choice) throws InterruptedException {
    	
    	boolean win;
    	
        if(allPlayersReady()) {
            notifyAll();
            
            System.out.printf("All %d played, got \n", players.size());
            
        }
        else {
        	System.out.println("En attente de joueur.");
            wait();
        }
        
        int n = 0;
        for (Player player : players) {
			
			if(player.loose(choice)) {
				n++;
			}
		}
        
        System.out.println("Passe ??");
        
        return n;
    }
    
    
    
    
    
    public synchronized boolean waitHeadOrTail() throws InterruptedException {
    	
        if(allPlayersReady()) {
            notifyAll();
            
            is_head = rand.nextInt(2) != 0;
            
            
            
            System.out.printf("All %d played, got %s\n", players.size(), is_head ? "HEAD" : "TAIL");
        }
        else {
        	System.out.println("En attente de joueur.");
            wait();
        }
        return is_head;
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
