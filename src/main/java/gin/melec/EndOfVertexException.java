/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class EndOfVertexException extends Exception {

    /**
     * Creates a new instance of <code>EndOfVertexException</code> without
     * detail message.
     */
    public EndOfVertexException() {
        //TODO Something
    }

    /**
     * Constructs an instance of <code>EndOfVertexException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EndOfVertexException(String msg) {
        super(msg);
    }
}
