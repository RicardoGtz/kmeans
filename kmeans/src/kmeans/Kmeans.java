package kmeans;

import java.util.ArrayList;
import java.util.TreeSet;

public class Kmeans {
	ArrayList<Attribute> attributes;
	ArrayList<ArrayList<String>> instances;
	ArrayList<Cluster> clusters;
	int classifierAttribute;
	//Constructor de la clase
	Kmeans(ArrayList<Attribute> attributes,ArrayList<ArrayList<String>> instances,int classifierAttribute){
		this.attributes=attributes;
		this.instances=instances;
		this.classifierAttribute=classifierAttribute;
		this.clusters=new ArrayList<Cluster>();
	}
	//Construye los grupos
	public void buildClusters(int numClusters) {
		clusters.clear();
		TreeSet<Integer> id=new TreeSet<Integer>();
		while(clusters.size()!=numClusters) {			
			int index=(int)(Math.random()*(this.instances.size()-1));			
			if(id.add(index)) 				
				clusters.add(new Cluster(instances.get(index),classifierAttribute,2));			
		}
		id=null;
		fillClusters();
		updateClustersCentroid();
		
		
		
		for(int i=0;i<this.clusters.size();i++) 
			System.out.println(clusters.get(i).getInstances());
					
	}
	private void updateClustersCentroid() {
		for(Cluster value:this.clusters)
			value.updateCentroid();		
	}
	private void fillClusters() {
		for(ArrayList<String> value:this.instances) {
			double minDist=Double.MAX_VALUE; int minCluster=0;
			for(int i=0;i<this.clusters.size();i++) {
				double aux=clusters.get(i).getDistance(value);
				if(aux<minDist) {
					minDist=aux;
					minCluster=i;
				}
			}
			this.clusters.get(minCluster).addInstance(value);
		}		
	}
}
