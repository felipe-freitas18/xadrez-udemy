package chess.pecas;

import boardgame.Board;
import boardgame.Position;
import chess.Cor;
import chess.PecaXadrez;

public class Cavalo  extends PecaXadrez{

	public Cavalo(Board board, Cor color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "C";
	}
	
	private boolean canMove(Position position) {
		PecaXadrez p =(PecaXadrez)getBoard().peca(position);
		return p == null || p.getCor() != getCor();
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];
		Position p = new Position(0, 0);
		
		p.setValues(position.getLinha() - 1 , position.getColuna() - 2 );
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() - 2 , position.getColuna() - 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() - 2, position.getColuna() + 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() - 1, position.getColuna() + 2);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() + 1 , position.getColuna() + 2);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() + 2, position.getColuna() + 1);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() + 2 , position.getColuna() - 1 );
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValues(position.getLinha() + 1, position.getColuna() - 2);
		if(getBoard().posicaoExiste(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		return mat;
	}
}
