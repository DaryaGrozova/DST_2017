
package server_rmi;

import java.io.IOException;

public interface ITask<T> {
    T execute() throws IOException;    
}
