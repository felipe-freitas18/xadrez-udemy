package chess.pecas;

import boardgame.Board;
import boardgame.Position;
import chess.Cor;
import chess.PartidaXadrez;
import chess.PecaXadrez;

public class Peao extends PecaXadrez {
	private PartidaXadrez partida;
	
	public Peao(Board board, Cor color, PartidaXadrez partida) {
		super(board, color);
		this.partida = partida;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];
		
		Position p = new Position(0,0);
		
		if(getCor() == Cor.WHITE) {
			p.setValues(position.getLinha() - 1, position.getColuna());
			if(getBoard().posicaoExiste(p) && !getBoard().pecaExistente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
		
			p.setValues(position.getLinha() - 2, position.getColuna());
			Position p2 = new Position(position.getLinha() - 1, position.getColuna());
			if(getBoard().posicaoExiste(p) && !getBoard().pecaExistente(p) &&
					getBoard().posicaoExiste(p2) && !getBoard().pecaExistente(p2) && getMoveCount() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			p.setValues(position.getLinha() - 1, position.getColuna() - 1);
			if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			p.setValues(position.getLinha() - 1, position.getColuna() + 1);
			if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			//special move en passant white
			if(position.getLinha() == 3) {
				Position left = new Position(position.getLinha(), position.getColuna() - 1);
				if(getBoard().posicaoExiste(left) && isThereOpponentPiece(left) && getBoard().peca(left) == partida.getEnPassantVulnerable()) {
					mat[left.getLinha() - 1][left.getColuna()] = true;
				}
				Position right = new Position(position.getLinha(), position.getColuna() + 1);
				if(getBoard().posicaoExiste(right) && isThereOpponentPiece(right) && getBoard().peca(right) == partida.getEnPassantVulnerable()) {
					mat[right.getLinha() - 1][right.getColuna()] = true;
				}
			}
		} 
		else {
			p.setValues(position.getLinha() + 1, position.getColuna());
			if(getBoard().posicaoExiste(p) && !getBoard().pecaExistente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
		
			p.setValues(position.getLinha() + 2, position.getColuna());
			Position p2 = new Position(position.getLinha() + 1, position.getColuna());
			if(getBoard().posicaoExiste(p) && !getBoard().pecaExistente(p) &&
					getBoard().posicaoExiste(p2) && !getBoard().pecaExistente(p2) && getMoveCount() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			p.setValues(position.getLinha() + 1, position.getColuna() - 1);
			if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			p.setValues(position.getLinha() + 1, position.getColuna() + 1);
			if(getBoard().posicaoExiste(p) && isThereOpponentPiece(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			//special move en passant black
			if(position.getLinha() == 4) {
				Position left = new Position(position.getLinha(), position.getColuna() - 1);
				if(getBoard().posicaoExiste(left) && isThereOpponentPiece(left) && getBoard().peca(left) == partida.getEnPassantVulnerable()) {
					mat[left.getLinha() + 1][left.getColuna()] = true;
				}
				Position right = new Position(position.getLinha(), position.getColuna() + 1);
				if(getBoard().posicaoExiste(right) && isThereOpponentPiece(right) && getBoard().peca(right) == partida.getEnPassantVulnerable()) {
					mat[right.getLinha() + 1][right.getColuna()] = true;
				}
			}
		}
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
