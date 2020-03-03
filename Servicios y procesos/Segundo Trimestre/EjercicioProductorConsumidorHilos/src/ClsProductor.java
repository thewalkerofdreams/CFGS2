import java.lang.reflect.Array;
import java.util.Random;

public class ClsProductor implements Runnable {

    private MyWaitNotify myWaitNotify;

    public ClsProductor(MyWaitNotify myWaitNotify){
        this.myWaitNotify = myWaitNotify;
    }

    @Override
    public void run() {
        Random random = new Random();
        char letra;

        for(int i = 0; i < 1000; i++){
            letra = (char)(random.nextInt(26) +97);
            myWaitNotify.lanzar( letra );
            System.out.println( "Lanzado "+letra+" a la tuberia." );
        }
    }

    /*public void tryInsertCharacterToArray(){
        int index = indexToInsertCharacter();
        Random random = new Random();

        if(index != -1){
            array[index] = (char) (random.nextInt(26) +97);
        }
    }

    public int indexToInsertCharacter(){
        int index = -1;

        for(int i = 0; i < array.length && index == -1; i++){
            if(array[i] == ' '){
                index = i;
            }
        }

        return index;
    }*/
}