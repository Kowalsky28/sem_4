/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> implements Runnable{

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes){
        
    }

    @Override
    public String call() throws Exception {
        return "";
    }
}
