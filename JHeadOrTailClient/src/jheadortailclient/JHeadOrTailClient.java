package jheadortailclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class JHeadOrTailClient {

	private final static String SERVER = "127.0.0.1";
	private final static int PORT = 0x2BAD;
	private final static int TIMEOUT = 30000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		SocketAddress addr = new InetSocketAddress(InetAddress.getByName(SERVER), PORT);
		Socket socket = new Socket();
		Scanner sc =new Scanner(System.in);

		try {            
			socket.connect(addr, TIMEOUT);
			DataInputStream reader = new DataInputStream(socket.getInputStream());
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());                
			int num = reader.readInt();

			System.out.printf("Vous �tes le joueur %d\n", num);

			// tour de jeu
			for(;;) {

				String content = null;

				// test si le joueur veut jouer ou quittez le jeu
				while(true) {
					System.out.println("Voulez-vous jouez ou quittez ? J:Jouer Q:Quit : ");
					content = sc.next().toLowerCase();

					if(content.equals("j") || content.equals("q")) {
						break;
					}

					System.out.println("Il y a eu un probl�me lors de la saisie.");
				}

				System.out.println("Test quitter");

				if(content.equals("q")) {
					System.out.println("Aurevoir");
					break;
				}   
				
				//Revoir readme : 2 joueurs
				
				System.out.println("Choisissez votre main : 0: pierre 1: papier 2: ciseaux");
				int choix = sc.nextInt();
				
				writer.writeInt(choix);
				writer.flush();
				
//				try {
//				    while (true) 
//				        System.out.println("reader.readInt() : "+reader.readInt());
//				} catch (EOFException ignored) {
//				    System.out.println("[EOF]");
//				}
				
//				System.out.println("passe : ");
				
				int lecturePremier = reader.readInt();
				System.out.println("lecturePremier : "+lecturePremier);
				int score_num = reader.readInt();
				System.out.println("score_num : "+score_num);

				//Faire une enum�ration
				//                System.out.printf("It was %s, here are the scores :\n", choix ? "HEAD" : "TAIL");
				System.out.printf("It was %s, here are the scores :\n", String.valueOf(choix));

				for (var i = 1; i <= score_num; i++) {
					int score = reader.readInt();

					System.out.printf("- Player %d%s : %s\n",
							i, i == num ? " (you)" : "",
									score >= 0 ? Integer.toString(score) : "-"
							);
				}	


			}
		}
		catch(IOException e) {
			System.err.println(e.getMessage());
		}
	}
}