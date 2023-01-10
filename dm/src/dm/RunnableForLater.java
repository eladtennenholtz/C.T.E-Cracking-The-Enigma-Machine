package dm;

import data.DataFromAgentToDm;

public class RunnableForLater implements Runnable {



    private DataFromAgentToDm data;


    public RunnableForLater(DataFromAgentToDm data){
        this.data=data;
    }

    @Override
    public void run() {


        System.out.println(data.getConfiguration());
        System.out.println(data.getNumberOfThread());
        System.out.println("elad!!!!!!!!!!!!!!");


    }
}
