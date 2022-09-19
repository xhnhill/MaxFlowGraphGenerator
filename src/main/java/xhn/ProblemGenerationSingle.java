package xhn;

import java.io.IOException;

/**
 * @author Haining Xie
 */
public class ProblemGenerationSingle extends ProblemGeneration{
    public static void main(String[] args) throws IOException {
        int nv = Integer.parseInt(args[0]);
        int ne = Integer.parseInt(args[1]);
        int num = Integer.parseInt(args[2]);
        int dir_idx = Integer.parseInt(args[3]);
        generateSingle(nv,ne,num,dir_idx);
    }
}
