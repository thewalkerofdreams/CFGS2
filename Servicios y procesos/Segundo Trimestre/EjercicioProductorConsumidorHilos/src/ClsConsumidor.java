import java.util.Random;

public class ClsConsumidor implements Runnable {

    //private char[] array;
    private MyWaitNotify myWaitNotify;

    public ClsConsumidor(MyWaitNotify myWaitNotify){
        this.myWaitNotify = myWaitNotify;
    }

    @Override
    public void run() {
        char letra;
        for(int i = 0; i < 1000; i++){
            letra = myWaitNotify.recoger();
            System.out.println( "Recogido el caracter "+letra );
        }
    }


    /*public void tryCatchCharacterToArray(){
        int index = indexToCatchCharacter();
        Random random = new Random();

        if(index != -1){
            array[index] = ' ';
        }
    }

    public int indexToCatchCharacter(){
        int index = -1;

        for(int i = 0; i < array.length && index == -1; i++){
            if(array[i] != ' '){
                index = i;
            }
        }

        return index;
    }*/
}
