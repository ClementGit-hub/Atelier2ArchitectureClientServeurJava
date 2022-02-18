package jheadortailserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player extends Thread {
    private final Socket _sock;
    private final int _id;
    private final Game _game;
    private int _choice;
    private int _score = 0;
    
    public Player(Game game, int id, Socket sock){
        this._choice = -1;
        _game = game;
        _id = id;
        _sock = sock;
    }
    public boolean isReady() {
        return _choice != -1;
    }
    public int getScore() { 
    	System.out.println("_score : "+_score);
        return _score;
    }    
    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(_sock.getInputStream());
            DataOutputStream writer = new DataOutputStream(_sock.getOutputStream());
            
            writer.writeInt(_id);
            System.out.println("_id : "+_id);
            writer.flush();
            
            for (; ; ) {
                _choice = reader.readInt();
                
                _score += _game.resultat(_choice);

                _game.write(writer);
            }
        }
        catch(InterruptedException | IOException e){
            _game.onLeave(_id);
        }
    }
    
    public int getPlayerId(){
    	return _id;
    }
	public boolean loose(int choice) {
		//A revoir 
		return (_choice+1)%3 == choice;
	}
}
