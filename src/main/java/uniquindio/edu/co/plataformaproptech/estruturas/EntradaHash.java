package uniquindio.edu.co.plataformaproptech.estruturas;

public class EntradaHash<K, V> {
    K clave;
    V valor;
    EntradaHash<K, V> siguiente;

    public EntradaHash(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
        this.siguiente = null;
    }
}