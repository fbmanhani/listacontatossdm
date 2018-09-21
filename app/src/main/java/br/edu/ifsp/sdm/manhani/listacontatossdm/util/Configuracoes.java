package br.edu.ifsp.sdm.manhani.listacontatossdm.util;

public class Configuracoes {

    public static final int ARMAZENAMENTO_INTERNO = 0;
    public static final int ARMAZENAMENTO_EXTERNO = 1;
    public static final int BANCO_DE_DADOS = 2;

    private static final Configuracoes ourInstance = new Configuracoes();
    private int tipoArmazenamento;

    public static Configuracoes getInstance() {
        return ourInstance;
    }

    private Configuracoes() {
        setTipoArmazenamento(ARMAZENAMENTO_INTERNO);
    }


    public int getTipoArmazenamento() {
        return tipoArmazenamento;
    }

    public void setTipoArmazenamento(int tipoArmazenamento) {
        this.tipoArmazenamento = tipoArmazenamento;
    }
}
