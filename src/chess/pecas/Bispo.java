package chess.pecas;

import boardgame.Board;
import boardgame.Position;
import chess.Cor;
import chess.PecaXadrez;

public class Bispo extends PecaXadrez{

	public Bispo(Board board, Cor color) {
		super(board, color);
	}
	@Override
	public String toString() {
		return "B";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];
		Position p = new Position(0,0);
		
		// noroeste
		p.setValues(position.getLinha()-1, position.getColuna() -1);
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1 , p.getColuna() - 1);
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		// nordeste
		p.setValues(position.getLinha() - 1, position.getColuna() + 1);
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1 , p.getColuna() + 1);
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		// sudeste
		p.setValues(position.getLinha() + 1, position.getColuna() + 1);
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1 , p.getColuna() + 1);

		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		// sudoeste
		p.setValues(position.getLinha() + 1, position.getColuna()-1);
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1 , p.getColuna() -1 );
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		return mat;
	}
}
