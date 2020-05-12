package application;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessPosition;
import chess.PartidaXadrez;
import chess.PecaXadrez;

public class Program {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partida = new PartidaXadrez();
		List<PecaXadrez> captured = new ArrayList<>();
		
		while(!partida.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(partida, captured);
				System.out.println();
				System.out.println("Source:");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = partida.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(partida.getPecas(), possibleMoves);
				
				System.out.println();
				System.out.println("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
			
				PecaXadrez pecaCapturada = partida.performChessMove(source, target);
				
				if(pecaCapturada != null) {
					captured.add(pecaCapturada);
				}
				if(partida.getPromoted() != null) {
					System.out.println("Entre com a peça da promoção (B/C/T/R): ");
					String type = sc.nextLine().toUpperCase();
					while(!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("R")) {
						System.out.println("Tipo de promoção invalido");
						type = sc.nextLine().toUpperCase();
					}
					partida.replacePromotedPiece(type);
				}
			}
		
			catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(partida, captured);
	}
}
