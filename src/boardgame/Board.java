package boardgame;

public class Board {
	private int linhas;
	private int colunas;
	private Peca[][] pecas;

	public Board(int linhas, int colunas) {
		if( linhas <1 || colunas <1){
			throw new BoardException("Error criando board: linha ou coluna devem ser pelo menos 1");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas]; 
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
	public Peca peca(int linha, int coluna) {
		if(!posicaoExiste(linha, coluna)){
			throw new BoardException("Posição não existe no tabuleiro");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Position position) {
		if(!posicaoExiste(position)){
			throw new BoardException("Posição não existe no tabuleiro");
		}
		return pecas[position.getLinha()][position.getColuna()];
	}
	
	public void lugarPeca(Peca peca, Position position) {
		if(pecaExistente(position)) {
			throw new BoardException("Há uma peça nessa posição " + position);
		}
		pecas[position.getLinha()][position.getColuna()] = peca;
		peca.position = position;
	}
	
	public Peca removerPeca(Position position) {
		if(!posicaoExiste(position)) {
			throw new BoardException("Posição não existe no tabuleiro");
		}
		if(peca(position) == null) {
			return null;
		}
		Peca aux = peca(position);
		aux.position = null;
		pecas[position.getLinha()][position.getColuna()] = null;
		return aux;
	}
	
	private boolean posicaoExiste(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >=0 && coluna < colunas;
	}
	
	public boolean posicaoExiste(Position position) {
		return posicaoExiste(position.getLinha(), position.getColuna());
	}
	
	public boolean pecaExistente(Position position) {
		if(!posicaoExiste(position)){
			throw new BoardException("Posição não existe no tabuleiro");
		}
		return peca(position) != null;
	}
}
