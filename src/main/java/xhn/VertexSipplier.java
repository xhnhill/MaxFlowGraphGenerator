package xhn;
import java.util.function.Supplier;

/**
 * @author Haining Xie
 */
public class VertexSipplier  implements Supplier<Integer> {
    VertexSipplier(){
        ct = 0;
    }
    private Integer ct;
    @Override
    public Integer get() {
        ct++;
        return ct;
    }
}
