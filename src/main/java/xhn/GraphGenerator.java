package xhn;
import org.jgrapht.generate.netgen.MaximumFlowProblem;
import org.jgrapht.generate.netgen.NetworkGenerator;
import org.jgrapht.generate.netgen.NetworkGeneratorConfig;
import org.jgrapht.generate.netgen.NetworkGeneratorConfigBuilder;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.util.SupplierUtil;

/**
 * @author Haining Xie
 */
public class GraphGenerator {
    private NetworkGeneratorConfigBuilder configBuilder;
    private NetworkGenerator<Integer, DefaultWeightedEdge> networkGenerator;
    GraphGenerator(){
        this.configBuilder = new NetworkGeneratorConfigBuilder();
        this.configBuilder.setMaximumFlowProblemParams(4,5,20,1,1);
        this.networkGenerator = new NetworkGenerator<>(this.configBuilder.build());
    }
    GraphGenerator(int num_v,int num_e){
        this.configBuilder = new NetworkGeneratorConfigBuilder();
        this.configBuilder.setMaximumFlowProblemParams(num_v,num_e,20,1,20);
        this.networkGenerator = new NetworkGenerator<>(this.configBuilder.build());
    }
    public MaximumFlowProblem generate(){
        return this.networkGenerator.generateMaxFlowProblem(new DefaultDirectedGraph<>(new VertexSipplier(), SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER,true));
    }


}
