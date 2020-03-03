public class Main {
    public static  void main (String[] args){
        MyWaitNotify myWaitNotify = new MyWaitNotify(8);
        ClsConsumidor consumidor = new ClsConsumidor(myWaitNotify);
        ClsProductor productor = new ClsProductor(myWaitNotify);

        Thread threadConsumidor = new Thread(consumidor);
        Thread threadProductor = new Thread(productor);

        threadConsumidor.start();
        threadProductor.start();
    }
}
