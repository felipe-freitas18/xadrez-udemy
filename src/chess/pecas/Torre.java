package chess.pecas;

import boardgame.Board;
import boardgame.Position;
import chess.Cor;
import chess.PecaXadrez;

public class Torre extends PecaXadrez{

	public Torre(Board board, Cor color) {
		super(board, color);
	}
	@Override
	public String toString() {
		return "T";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];
		Position p = new Position(0,0);
		
		// acima da peça
		p.setValues(position.getLinha()-1, position.getColuna());
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		// esquerda da peça
		p.setValues(position.getLinha(), position.getColuna() - 1);
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		// direita da peça
		p.setValues(position.getLinha(), position.getColuna() + 1);
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		// baixo da peça
		p.setValues(position.getLinha() + 1, position.getColuna());
		while(getBoard().posicaoExiste(p)&& !getBoard().pecaExistente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() + 1);
		}
		
		if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		
		return mat;
	}
}
