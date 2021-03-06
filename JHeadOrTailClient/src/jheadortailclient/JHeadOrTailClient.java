package jheadortailclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

			System.out.printf("Vous ?tes le joueur %d\n", num);

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

					System.out.println("Il y a eu un probl?me lors de la saisie.");
				}

				System.out.println("Test quitter");

				if(content.equals("q")) {
					System.out.println("Aurevoir");
					break;
				}   
				
				System.out.println("Choisissez votre main : 0: pierre 1: papier 2: ciseaux");
				int choix = sc.nextInt();
				System.out.println("Choix joueur : "+choix);
				
				writer.writeInt(choix);
				writer.flush();
				 
				int nbJoueur = reader.readInt();
				System.out.println("Nombre de joueurs : "+nbJoueur);

				System.out.printf("Le choix ?tait %s, voici les scores :\n", String.valueOf(choix));

				for (var i = 1; i <= nbJoueur; i++) { 
					int score = reader.readInt();

					System.out.printf("- Le joueur %d%s : %s\n",
							i, i == num ? " (toi)" : "",
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
