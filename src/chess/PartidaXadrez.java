package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Peca;
import boardgame.Position;
import chess.pecas.Bispo;
import chess.pecas.Cavalo;
import chess.pecas.Peao;
import chess.pecas.Rainha;
import chess.pecas.Rei;
import chess.pecas.Torre;

public class PartidaXadrez {
	private int turn;
	private Cor currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulnerable;
	private PecaXadrez promoted;
	
	private List<Peca> piecesOnTheBoard = new ArrayList<>();
	private List<Peca> capturedPieces = new ArrayList<>();

	public PartidaXadrez() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Cor.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}

	public Cor getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public PecaXadrez getPromoted() {
		return promoted;
	}

	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] mat = new PecaXadrez[board.getLinhas()][board.getColunas()];
		for(int i= 0; i < board.getLinhas(); i++) {
			for(int j = 0; j<board.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) board.peca(i, j);
			}
		}
		return mat;
	}
	public boolean[][] possibleMoves (ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.peca(position).possibleMoves();
		
	}
	
	public PecaXadrez performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Peca capturaPeca = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturaPeca);
			throw new ChessException("Você não pode se colocar em xeque");
		}
		PecaXadrez movedPiece = (PecaXadrez)board.peca(target);

		// #specialmove promotion
		promoted = null;
		if(movedPiece instanceof Peao) {
			if((movedPiece.getCor() == Cor.WHITE && target.getLinha() == 0) ||(movedPiece.getCor() == Cor.BLACK && target.getLinha() == 7) ) {
				promoted = (PecaXadrez)board.peca(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;		
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}else {
			nextTurn();
		}
		
		//#specialmove en passant
		if(movedPiece instanceof Peao && (target.getLinha() == source.getLinha() - 2 || target.getLinha() == source.getLinha() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		
		return (PecaXadrez) capturaPeca;
	}
	
	public PecaXadrez replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("Não há peça para ser promovida");
		}
		
		if(!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("R")) {
			return promoted;
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Peca p = board.removerPeca(pos);
		piecesOnTheBoard.remove(p);
		
		PecaXadrez newPiece = newPiece(type, promoted.getCor());
		board.lugarPeca(newPiece, pos);
		piecesOnTheBoard.add(p);
		
		
		return newPiece;
	}
	
	private PecaXadrez newPiece(String type, Cor cor) {
		if(type.equals("B")) return new Bispo(board, cor);
		if(type.equals("C")) return new Cavalo(board, cor);
		if(type.equals("T")) return new Torre(board, cor);
		
		return new Rainha(board, cor);
	}

	private void validateTargetPosition(Position source,Position target) {
		if(!board.peca(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mexer para a posição escolhida");
		}
	}

	private Peca makeMove(Position source, Position target) {
		PecaXadrez p = (PecaXadrez)board.removerPeca(source);
		p.increaseMoveCount();
		Peca pecaCapturada = board.removerPeca(target);
		board.lugarPeca(p, target);
		
		if(pecaCapturada != null) {
			piecesOnTheBoard.remove(pecaCapturada);
			capturedPieces.add(pecaCapturada);
		}
		
		// #specialmove castling kingside rook
		if(p instanceof Rei && target.getColuna() == source.getColuna() + 2) {
			Position sourceT = new Position(source.getLinha(), source.getColuna() + 3);
			Position targetT = new Position(target.getLinha(), target.getColuna() + 2);
			PecaXadrez torre = (PecaXadrez)board.removerPeca(sourceT);
			board.lugarPeca(torre, targetT);
			torre.increaseMoveCount();
		}
		
		// #specialmove castling queenside rook
		if(p instanceof Rei && target.getColuna() == source.getColuna() - 2) {
			Position sourceT = new Position(source.getLinha(), source.getColuna() - 4);
			Position targetT = new Position(target.getLinha(), target.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)board.removerPeca(sourceT);
			board.lugarPeca(torre, targetT);
			torre.increaseMoveCount();
		}
		
		// #specialmove en passant 
		if(p instanceof Peao) {
			if(source.getColuna() != target.getColuna() && pecaCapturada == null) {
				Position peaoPosition;
				if(p.getCor() == Cor.WHITE) {
					peaoPosition = new Position(target.getLinha() + 1, target.getColuna());
				}
				else {
					peaoPosition = new Position(target.getLinha() - 1, target.getColuna());
				}
				pecaCapturada= board.removerPeca(peaoPosition);
				capturedPieces.add(pecaCapturada);
				piecesOnTheBoard.remove(pecaCapturada);
			}
		}
		
		return pecaCapturada;
	}
	
	private void undoMove(Position source, Position target, Peca capturedPiece) {
		PecaXadrez p =(PecaXadrez) board.removerPeca(target);
		p.decreaseMoveCount();
		board.lugarPeca(p, source);
		
		if(capturedPiece !=null) {
			board.lugarPeca(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// #specialmove castling kingside rook
		if(p instanceof Rei && target.getColuna() == source.getColuna() + 2) {
			Position sourceT = new Position(source.getLinha(), source.getColuna() + 3);
			Position targetT = new Position(target.getLinha(), target.getColuna() + 2);
			PecaXadrez torre = (PecaXadrez)board.removerPeca(targetT);
			board.lugarPeca(torre, sourceT);
			torre.decreaseMoveCount();
		}
		
		// #specialmove castling queenside rook
		if(p instanceof Rei && target.getColuna() == source.getColuna() - 2) {
			Position sourceT = new Position(source.getLinha(), source.getColuna() - 4);
			Position targetT = new Position(target.getLinha(), target.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)board.removerPeca(targetT);
			board.lugarPeca(torre, sourceT);
			torre.decreaseMoveCount();
		}
		
		// #specialmove en passant 
		if(p instanceof Peao) {
			if(source.getColuna() != target.getColuna() && capturedPiece == null) {
				PecaXadrez peao = (PecaXadrez)board.removerPeca(target);
				Position peaoPosition;
				if(p.getCor() == Cor.WHITE) {
					peaoPosition = new Position(3, target.getColuna());
				}
				else {
					peaoPosition = new Position(4, target.getColuna());
				}
				board.lugarPeca(peao, peaoPosition);
				capturedPiece = board.removerPeca(peaoPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.pecaExistente(position)) {
			throw new ChessException("Não existe peça na posição de origem");
		}
		
		if(currentPlayer != ((PecaXadrez)board.peca(position)).getCor()) {
			throw new ChessException("A peça escolhida não é sua");
		}
		
		if(!board.peca(position).isThereAnyPossibleMove()) {
			throw new ChessException("Não existe movimentos possíveis para a peça escolhida");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Cor.WHITE)? Cor.BLACK : Cor.WHITE;
	}
	
	private Cor opponent(Cor cor) {
		return 	(cor == Cor.WHITE)? Cor.BLACK : Cor.WHITE;

	}
	
	private PecaXadrez Rei(Cor cor) {
		List<Peca> list = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez)x).getCor()== cor).collect(Collectors.toList());
		for(Peca p: list) {
			if(p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("Não há um rei com essa cor no tabuleiro "+ cor);
	}
	
	private boolean testCheck(Cor cor) {
		Position ReiPosition = Rei(cor).getChessPosition().toPosition();
		List<Peca> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez)x).getCor()== opponent(cor)).collect(Collectors.toList());
		for(Peca p: opponentPieces) {
			boolean[][]mat = p.possibleMoves();
			if(mat[ReiPosition.getLinha()][ReiPosition.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Cor cor) {
		if(!testCheck(cor)) {
			return false;
		}
		List<Peca> list = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez)x).getCor()== cor).collect(Collectors.toList());
		for(Peca p: list) {
			boolean[][] mat = p.possibleMoves();
			for(int i =0; i< board.getLinhas(); i++) {
				for(int j = 0; j < board.getColunas(); j++) {
					if(mat[i][j]) {
						Position source = ((PecaXadrez)p).getChessPosition().toPosition();
						Position target = new Position( i, j);
						Peca capturedPiece = makeMove(source, target);
						boolean testeCheck = testCheck(cor);
						undoMove(source, target, capturedPiece);
						if(!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private void lugarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		board.lugarPeca(peca, new ChessPosition(coluna, linha).toPosition());
		piecesOnTheBoard.add(peca);
	}
	
	private void initialSetup() {
        lugarNovaPeca('a', 1, new Torre(board, Cor.WHITE));
        lugarNovaPeca('b', 1, new Cavalo(board, Cor.WHITE));
        lugarNovaPeca('c', 1, new Bispo(board, Cor.WHITE));
        lugarNovaPeca('d', 1, new Rainha(board, Cor.WHITE));
        lugarNovaPeca('e', 1, new Rei(board, Cor.WHITE, this));
        lugarNovaPeca('f', 1, new Bispo(board, Cor.WHITE));
        lugarNovaPeca('g', 1, new Cavalo(board, Cor.WHITE));
        lugarNovaPeca('h', 1, new Torre(board, Cor.WHITE));
        lugarNovaPeca('a', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('b', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('c', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('d', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('e', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('f', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('g', 2, new Peao(board, Cor.WHITE, this));
        lugarNovaPeca('h', 2, new Peao(board, Cor.WHITE, this));

        lugarNovaPeca('a', 8, new Torre(board, Cor.BLACK));
        lugarNovaPeca('b', 8, new Cavalo(board, Cor.BLACK));
        lugarNovaPeca('c', 8, new Bispo(board, Cor.BLACK));
        lugarNovaPeca('d', 8, new Rainha(board, Cor.BLACK));
        lugarNovaPeca('e', 8, new Rei(board, Cor.BLACK, this));
        lugarNovaPeca('f', 8, new Bispo(board, Cor.BLACK));
        lugarNovaPeca('g', 8, new Cavalo(board, Cor.BLACK));
        lugarNovaPeca('h', 8, new Torre(board, Cor.BLACK));
        lugarNovaPeca('a', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('b', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('c', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('d', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('e', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('f', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('g', 7, new Peao(board, Cor.BLACK, this));
        lugarNovaPeca('h', 7, new Peao(board, Cor.BLACK, this));

	}
	
}
