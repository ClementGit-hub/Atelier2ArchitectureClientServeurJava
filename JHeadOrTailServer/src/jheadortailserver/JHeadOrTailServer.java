package jheadortailserver;

import java.io.IOException;
import java.net.ServerSocket;

public class JHeadOrTailServer {
    
    private final static int PORT = 0x2BAD;

    public static void main(String[] args) {
        Game game = new Game();
        
        ServerSocket sock_listen = null;
        
        try {
        	sock_listen = new ServerSocket(PORT); 
            
            System.out.printf("Ecoute du port : %d\n", PORT);
            
            for (; ; ) {
                game.registerPlayer(sock_listen.accept());
            }
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            
        } finally {
        	
        	if(!sock_listen.isClosed()) {
        		try {
					sock_listen.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }    
}
