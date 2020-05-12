package chess;

import boardgame.Board;
import boardgame.Peca;
import boardgame.Position;

public abstract class PecaXadrez extends Peca {
	private Cor cor;
	private int moveCount;

	public PecaXadrez(Board board, Cor color) {
		super(board);
		this.cor = color;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getMoveCount() {
		return moveCount;
	}

	public void increaseMoveCount() {
		moveCount = getMoveCount() + 1;
	}
	
	public void decreaseMoveCount() {
		moveCount = getMoveCount() - 1;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		PecaXadrez p = (PecaXadrez)getBoard().peca(position);

		return p!=null && p.getCor() != cor; 
	}
	
}
