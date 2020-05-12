package chess.pecas;

import boardgame.Board;
import boardgame.Position;
import chess.Cor;
import chess.PartidaXadrez;
import chess.PecaXadrez;

public class Rei  extends PecaXadrez{
	private PartidaXadrez partida;
	
	public Rei(Board board, Cor color, PartidaXadrez partida) {
		super(board, color);
		this.partida = partida;
	}
	
	@Override
	public String toString() {
		return "R";
	}
	
	private boolean canMove(Position position) {
		PecaXadrez p =(PecaXadrez)getBoard().peca(position);
		return p == null || p.getCor() != getCor();
	}
	
	private boolean testRookCastling(Position position) {
		PecaXadrez p = (PecaXadrez)getBoard().peca(position);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getMoveCount() == 0;
	}
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];
		Position p = new Position(0, 0);
		
		//acima
		p.setValues(position.getLinha() - 1 , position.getColuna());
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//abaixo
		p.setValues(position.getLinha() + 1 , position.getColuna());
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//esquerda
		p.setValues(position.getLinha(), position.getColuna() - 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//direita
		p.setValues(position.getLinha(), position.getColuna() + 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//noroeste
		p.setValues(position.getLinha() - 1 , position.getColuna() - 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//nordeste
		p.setValues(position.getLinha() - 1 , position.getColuna() + 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//sudoeste
		p.setValues(position.getLinha() + 1 , position.getColuna() - 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//sudeste
		p.setValues(position.getLinha() + 1 , position.getColuna() + 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		// #specialmove castling
		if(getMoveCount() == 0 && ! partida.getCheck() ) {
			// #specialmove castling kingside rook
			Position posT1 = new Position(position.getLinha(), position.getColuna() + 3);
			if(testRookCastling(posT1)) {
				Position p1 = new Position(position.getLinha(), position.getColuna() + 1);
				Position p2 = new Position(position.getLinha(), position.getColuna() + 2);
				if(getBoard().peca(p1) == null && getBoard().peca(p2) == null) {
					mat[position.getLinha()][position.getColuna() + 2]= true;
				}
			}
		}
		
		// #specialmove castling
		if(getMoveCount() == 0 && ! partida.getCheck() ) {
			// #specialmove castling queenside rook
			Position posT2 = new Position(position.getLinha(), position.getColuna() - 4);
			if(testRookCastling(posT2)) {
				Position p1 = new Position(position.getLinha(), position.getColuna() - 1);
				Position p2 = new Position(position.getLinha(), position.getColuna() - 2);
				Position p3 = new Position(position.getLinha(), position.getColuna() - 3);
				if(getBoard().peca(p1) == null && getBoard().peca(p2) == null && getBoard().peca(p3) == null) {
					mat[position.getLinha()][position.getColuna() - 2]= true;
				}
			}
		}
		
		return mat;
	}
}
