package parallelizetest.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;


public class FlowGraph extends JScrollPane {
    private Graph<Integer,Integer> graph;
    List<String> vertexlabel = new ArrayList<String>();

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<Integer,Integer> vv;
    DAGLayout<Integer,Integer> dagLayout;
    
    boolean showLabels;
    
    /**
     * create an instance of a simple graph with controls to
     * demo the zoom features.
     * 
     */
    public FlowGraph() {
       //List<String> edgelabel = new ArrayList<String>();
       graph = new DirectedSparseGraph<Integer,Integer>();
                		
       Integer[] v = new Integer[8];
       v = createVertices();
	   createEdges(v);
	   String[] label = new String[vertexlabel.size()];
	   for(int i=0;i < vertexlabel.size(); i++)
		   label[i] = vertexlabel.get(i).toString();
       MyVertexStringer myvs = new MyVertexStringer<Integer>(v,label);  
 
       VertexShapeSizeAspect<Integer,Integer> vssa = new VertexShapeSizeAspect<Integer,Integer>(graph);

       dagLayout = new DAGLayout<Integer,Integer>(graph,new Dimension(400,400));
       vv =  new VisualizationViewer<Integer,Integer>(dagLayout);

       vv.getRenderContext().setVertexLabelTransformer(myvs);
       vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.blue));
       vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.black));
       vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), Color.blue,  Color.orange));
       vv.getRenderContext().setVertexShapeTransformer(vssa);
        
        vv.getRenderContext().setEdgeShapeTransformer(new DAGLayout.BentLine<Integer,Integer>(dagLayout));
        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Integer>(vv.getPickedEdgeState(), Color.black, Color.green));

        vv.setVertexToolTipTransformer(new ToStringLabeller<Integer>());

        vv.setBackground(Color.white);
		vv.setDoubleBuffered(true);
		vv.revalidate();
		
		this.setViewportView(vv);
		this.revalidate();

    }
    
    
    /**
     * create some vertices
     * @param count how many to create
     * @return the Vertices in an array
     */
    private Integer[] createVertices() {
    	int count = 8;
        Integer[] v = new Integer[count];
        for (int i = 0; i < count; i++) {
        	v[i] = new Integer(i);
        	graph.addVertex(v[i]);
        	vertexlabel.add("s"+i);
        }
        return v;
    }

    /**
     * create edges for this demo graph
     * @param v an array of Vertices to connect
     */
    private void createEdges(Integer[] v) {
    	int count = 0;
    	
    	graph.addEdge(0,v[0],v[1]);
    	graph.addEdge(1,v[1],v[2]);
    	graph.addEdge(2,v[1],v[2]);
    	graph.addEdge(3,v[1],v[3]);
    	graph.addEdge(4,v[2],v[3]);
    	graph.addEdge(5,v[2],v[3]);
    	graph.addEdge(6,v[1],v[4]);
    	graph.addEdge(7,v[2],v[4]);
    	graph.addEdge(8,v[3],v[4]);
    	graph.addEdge(9,v[3],v[4]);
    	graph.addEdge(10,v[4],v[5]);
    	graph.addEdge(11,v[5],v[6]);
    	graph.addEdge(12,v[5],v[6]);
    	graph.addEdge(13,v[6],v[7]);
    	graph.addEdge(14,v[5],v[7]);

    }
    
    private class MyVertexStringer<V> implements Transformer<V,String> {

        Map<V,String> map = new HashMap<V,String>();
        
        public MyVertexStringer(V[] vertices,String[] labels) {
            for(int i=0; i<vertices.length; i++) {
                map.put(vertices[i], labels[i]);
            }
        }
        
        public String getLabel(V v) {
                return (String)map.get(v);
        }
        
		public String transform(V input) {
			return getLabel(input);
		}
		
	    private V findVertex(String vname,V[] v)
	    {
	    	int i;
	    	for(i=0; i < v.length; i++)
	    	{
	    		if(map.get(v[i]).equals(vname))
	    			return v[i];
	    	}
	    	return null;
	    }
    }

    private class VertexShapeSizeAspect<V,E>
    extends AbstractVertexShapeTransformer <V>
    implements Transformer<V,Shape>  {
        protected Graph<V,E> graph;
        public VertexShapeSizeAspect(Graph<V,E> graphIn)
        {
        	this.graph = graphIn;
            setSizeTransformer(new Transformer<V,Integer>() {

				public Integer transform(V v) {
					if((((Integer)v).intValue() == 0) || 
							(((Integer)v).intValue() == 7))
						return 50;
					else
						return 20;
				}});
            setAspectRatioTransformer(new Transformer<V,Float>() {

				public Float transform(V v) {
						return 1.0f;
				}});
        }
        public Shape transform(V v)
        {
        	switch(((Integer)v).intValue())
        	{
        	case 6:
        		{
        			return factory.getRectangle(v);
        		}
        	case 3:
        		{
        			return factory.getRegularStar(v, 5);
        		}
        	case 1:
        		{
        			return factory.getRegularPolygon(v, 3);
        		}
        	default:
        	{
        		return factory.getEllipse(v);
        	}
        	}
        }
    }
}