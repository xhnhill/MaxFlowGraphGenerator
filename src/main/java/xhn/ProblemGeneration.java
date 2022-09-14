package xhn;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.generate.netgen.MaximumFlowProblem;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.matrix.MatrixExporter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @author Haining Xie
 */
public class ProblemGeneration {
    public static void main(String[] args) throws IOException {
        boolean ifTest = false;
        //test();
        if(ifTest) return;
        int nv = Integer.parseInt(args[0]);
        int ne = Integer.parseInt(args[1]);
        int num = Integer.parseInt(args[2]);
        int dir_idx = Integer.parseInt(args[3]);
        generateAll(nv,ne,num,dir_idx);
    }
    private static void test() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("graph.txt"));
        int fn = sc.nextInt();
        int en = sc.nextInt();
        int an = sc.nextInt();
        int res = sc.nextInt();
        GraphGenerator gg = new GraphGenerator(10,20);
        MaximumFlowProblem maximumFlowProblem = gg.generate();
        Graph<Integer, DefaultWeightedEdge> g = maximumFlowProblem.getGraph();
        for(int i=1;i<=10;i++){
            g.removeVertex(i);
        }
        for(int i =0;i<en;i++){
            int s = sc.nextInt();
            int t = sc.nextInt();
            int wei = sc.nextInt();
            if(!g.containsVertex(s)) g.addVertex(s);
            if(!g.containsVertex(t)) g.addVertex(t);

            g.setEdgeWeight(g.addEdge(s,t),wei);

        }
        EdmondsKarpMFImpl<Integer, DefaultWeightedEdge> alg = new EdmondsKarpMFImpl<>(g);
        System.out.println(alg.getMaximumFlowValue(23,26));
    }

    private static void generateAll(int nv,int ne,int num_file,int dir_idx) throws IOException {
        Path path = Paths.get(""+dir_idx);
        generateInfoFile(nv,ne,num_file);
        if(!Files.exists(path)){
            Files.createDirectory(path);
        }


        for(int i =1;i<=num_file;i++){
            Path tp = Paths.get(""+dir_idx,""+i);
            GraphGenerator gg = new GraphGenerator(nv,ne);
            MaximumFlowProblem maximumFlowProblem = gg.generate();
            maximumFlowProblem.dumpCapacities();
            Graph<Integer, DefaultWeightedEdge> g = maximumFlowProblem.getGraph();
            int[][] mfs = new int[nv][nv];

            EdmondsKarpMFImpl<Integer, DefaultWeightedEdge> alg = new EdmondsKarpMFImpl<>(maximumFlowProblem.getGraph());
            int allMin =Integer.MAX_VALUE;
            for(int p = 1;p<=nv;p++){
                for(int q =1;q<=nv;q++){
                    if(p == q) continue;
                    mfs[p-1][q-1] = (int)alg.getMaximumFlowValue(p,q);
                    allMin =Math.min(mfs[p-1][q-1],allMin);
                }
            }
            convert(g);
            BufferedWriter w = new BufferedWriter(new FileWriter(tp.toString()));
            w.write(""+g.vertexSet().size()+" "+g.edgeSet().size()+" "+nv+" "+allMin+"\n");
            writeEdgePair(g,w);
            writeMatrix(mfs,w);
            w.write(""+allMin);
            w.flush();
        }

    }
    private static void writeEdgePair(Graph<Integer, DefaultWeightedEdge> g,Writer w) throws IOException {
        for(DefaultWeightedEdge e:g.edgeSet()){
            int sc = g.getEdgeSource(e);
            int ta = g.getEdgeTarget(e);
            int cap = (int)g.getEdgeWeight(e);
            w.write(""+sc+" "+ta+" "+cap+"\n");
        }
    }

    private static void  generateInfoFile(int nv,int ne,int num_file) throws IOException {
        String fn = ""+nv+"x"+ne;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fn));
        bufferedWriter.write(""+nv+" "+ne+" "+num_file);

    }
    private static void export(Graph<Integer, DefaultWeightedEdge> g,Writer w) throws IOException {
        int num = g.vertexSet().size();
        int[][] mx = new int[num][num];
        for(DefaultWeightedEdge edge:g.edgeSet()){
            mx[g.getEdgeSource(edge)-1][g.getEdgeTarget(edge)-1] = (int)g.getEdgeWeight(edge);
        }
        writeMatrix(mx,w);

    }
    private static void convert(Graph<Integer, DefaultWeightedEdge> g){
        ArrayList<int[]> l = new ArrayList<>();
        HashSet<String> hs = new HashSet<>();
        for(DefaultWeightedEdge edge:g.edgeSet()){
            int x = g.getEdgeTarget(edge);
            int y = g.getEdgeSource(edge);
            if(!hs.contains(""+y+x) && g.containsEdge(x,y)){
                int[] pt = new int[]{x,y};
                l.add(pt);
                hs.add(""+x+y);
            }
        }
        int len = l.size();
        int nidx = g.vertexSet().size();
        for(int i = 0;i<len;i++){
            double weight = g.getEdgeWeight(g.getEdge(l.get(i)[0],l.get(i)[1]));
            g.removeEdge(l.get(i)[0],l.get(i)[1]);
            nidx++;
            g.addVertex(nidx);
            g.addEdge(l.get(i)[0],nidx);
            g.setEdgeWeight(l.get(i)[0],nidx,weight);
            g.addEdge(nidx,l.get(i)[1]);
            g.setEdgeWeight(nidx,l.get(i)[1],weight);
        }
    }
    private static void writeMatrix(int[][] mx, Writer w) throws IOException {
        for(int i =0;i<mx.length;i++){
            for(int j =0;j<mx[0].length;j++){
                w.write(""+mx[i][j]+" ");
            }
            w.write("\n");
        }
    }
    private static void testCode() throws IOException {
        GraphGenerator graphGenerator = new GraphGenerator();
        MaximumFlowProblem maximumFlowProblem = graphGenerator.generate();
        System.out.println(maximumFlowProblem.isSingleSourceSingleSinkProblem());
        maximumFlowProblem.dumpCapacities();
        Graph<Integer, DefaultWeightedEdge> g = maximumFlowProblem.getGraph();

        EdmondsKarpMFImpl<Integer, DefaultWeightedEdge> alg = new EdmondsKarpMFImpl<>(maximumFlowProblem.getGraph());
        MaximumFlowAlgorithm.MaximumFlow flow = alg.getMaximumFlow(1,4);
        convert(g);
        double res = alg.getMaximumFlowValue(1,4);
        System.out.println(res);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("graph.txt"));
        export(g,bufferedWriter);
        bufferedWriter.flush();
    }

}
