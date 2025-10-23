// realizado por Gonçalo Sousa n 30011627
public class Reserva {
    private int idPassageiro;
    private boolean bagagemExtra;
    private double preco;

    public Reserva(int idPassageiro, boolean bagagemExtra, double preco) {
        this.idPassageiro = idPassageiro;
        this.bagagemExtra = bagagemExtra;
        this.preco = preco;
    }

    public int getIdPassageiro() {
        return idPassageiro;
    }

    public void setIdPassageiro(int idPassageiro) {
        this.idPassageiro = idPassageiro;
    }

    public boolean isBagagemExtra() {
        return bagagemExtra;
    }

    public void setBagagemExtra(boolean bagagemExtra) {
        this.bagagemExtra = bagagemExtra;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
